import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java.sourceCompatibility = JavaVersion.VERSION_17

val projectGroup: String by project
val applicationVersion: String by project

group = projectGroup
version = applicationVersion

repositories {
    mavenCentral()
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

//dependencyManagement {
//    val springCloudDependenciesVersion: String by project
//    imports {
//        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudDependenciesVersion")
//    }
//}

dependencies {
    val kotlinVersion: String by project
    val springBootVersion: String by project
    val springCloudAwsVersion: String by project

    /* Kotlin */
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")

    /* Springboot */
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-log4j2:$springBootVersion")

    /* AWS */
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:$springCloudAwsVersion"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")

    /* Blockhound */
    implementation("io.projectreactor.tools:blockhound:1.0.8.RELEASE")

    implementation("com.lmax:disruptor:3.3.6")

    /* Test */
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("io.mockk:mockk:1.10.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")

    /* Test ArchUnit */
    testImplementation("com.tngtech.archunit:archunit-junit5:1.0.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    enabled = true
    useJUnitPlatform {
        includeTags("api", "util", "arch")
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}
