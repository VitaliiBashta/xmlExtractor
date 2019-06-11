package cz.moneta;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class XmlResponses {

    private List<Document> documents;

    public XmlResponses loadFrom(String fileName) {
        try {
            documents = Files.lines(Path.of(fileName))
                    .map(XmlResponses::getDocument)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private static Document getDocument(String xml) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return document;
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
