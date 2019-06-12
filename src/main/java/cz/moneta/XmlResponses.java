package cz.moneta;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class XmlResponses {

    private List<Document> documents;

    XmlResponses(String fileName) {
        try {
            documents = Files.lines(Path.of(fileName))
                    .map(XmlResponses::getDocument)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Document getDocument(String xml) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return document;
    }

    List<Document> getDocuments() {
        return documents;
    }
}
