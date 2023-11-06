package me.exam.ktwebfx.component

import me.exam.ktwebfx.error.CustomErrorCode
import me.exam.ktwebfx.error.CustomException
import org.springframework.stereotype.Component
import org.w3c.dom.NodeList
import org.w3c.dom.ls.DOMImplementationLS
import org.xml.sax.InputSource
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.lang.model.element.Element
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

@Component
class SoapComponent {
    private val soapCallUrl: String = "http://soap.me"

    fun soapCall(methodName: String, params: Map<String, String>): Element {
        val factory = DocumentBuilderFactory.newInstance()
        var documentBuilder: DocumentBuilder?
        return try {
            documentBuilder = factory.newDocumentBuilder()

            // create xml document
            val doc = documentBuilder.newDocument()
            val envelope = doc.createElement("s:Envelope")
            envelope.setAttribute("xmlns:s", "http://schemas.xmlsoap.org/soap/envelope/")
            val sBody = doc.createElement("s:Body")
            val methodTag = doc.createElement(methodName)

            // set node xmlns
            methodTag.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://tempuri.org/")

            // set xml document
            params.forEach { (key: String, value: String) ->
                if (value.isNotEmpty() && value != "") {
                    val methodParamTag = doc.createElement(key)
                    methodParamTag.appendChild(doc.createTextNode(value))
                    methodTag.appendChild(methodParamTag)
                }
            }
            sBody.appendChild(methodTag)
            envelope.appendChild(sBody)
            val domImplLS = doc.implementation as DOMImplementationLS
            val serializer = domImplLS.createLSSerializer()
            val lsOutput = domImplLS.createLSOutput()
            lsOutput.encoding = "UTF-8"
            val stringWriter: Writer = StringWriter()
            lsOutput.characterStream = stringWriter
            serializer.write(envelope, lsOutput)
            val requestBody = stringWriter.toString()

            // request
            val url = URL(soapCallUrl)
            val http = url.openConnection() as HttpURLConnection
            http.defaultUseCaches = false
            http.setDoInput(true)
            http.setDoOutput(true)
            http.setRequestMethod("POST")
            http.setRequestProperty("content-type", "text/xml;charset=UTF-8")
            http.setRequestProperty("SOAPAction", "http://tempuri.org/ICompanyObjectService/$methodName")
            val outStream = OutputStreamWriter(http.outputStream, "UTF-8")
            val writer = PrintWriter(outStream)
            writer.write(requestBody)
            writer.flush()

            // read response
            val sb = http.inputStream.bufferedReader(Charsets.UTF_8).use(BufferedReader::readText)

            // parse XML
            val `is` = InputSource(StringReader(sb))
            documentBuilder = factory.newDocumentBuilder()
            val document = documentBuilder.parse(`is`)

            // normalize XML
            document.documentElement.normalize()
            val xPathFactory = XPathFactory.newInstance()
            val xPath = xPathFactory.newXPath()
            val expr = xPath.compile("//" + methodName + "Result")
            val nodeList = expr.evaluate(document, XPathConstants.NODESET) as NodeList
            if (nodeList.length == 1) {
                nodeList.item(0) as Element
            } else {
                throw CustomException(CustomErrorCode.INTERNAL_SERVER_Custom_ERROR, "failed to soap call")
            }
        } catch (e: Exception) {
            throw CustomException(CustomErrorCode.INTERNAL_SERVER_Custom_ERROR, "failed to soap call", e)
        }
    }
}