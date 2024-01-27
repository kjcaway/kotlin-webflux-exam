package me.exam.ktwebfx

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag("arch")
class ArchTest {
    @Test
    fun `Service는 Controller에서만 접근 가능하다`() {
        val classes = ClassFileImporter().importPackages("me.exam.ktwebfx.api")
        val rule: ArchRule = classes()
                .that().haveNameMatching(".*Controller")
                .should().onlyHaveDependentClassesThat().resideInAPackage("..service..")
        rule.check(classes)
    }

    @Test
    fun `반대로 Service에서 Controller를 접근 불가하다`() {
        val classes = ClassFileImporter().importPackages("me.exam.ktwebfx.api")
        val rule: ArchRule = noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat().haveNameMatching(".*Controller")
        rule.check(classes)
    }
}