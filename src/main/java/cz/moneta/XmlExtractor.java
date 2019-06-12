package cz.moneta;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XmlExtractor {
    private Configuration config;
    private final Map<String, List<List<String>>> data = new HashMap<>();

    public XmlExtractor(String filename) {
        try {
            init(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(String filename) throws IOException {
        config = new Configuration(filename);
        List<Document> documents = new XmlResponses(config.getInputFile()).getDocuments();

        Map<String, List<String>> headers = config.getHeaders();
        for (Map.Entry<String, List<String>> sheet : headers.entrySet()) {
            String sheetName = sheet.getKey();
            List<List<String>> entries = new ArrayList<>();
            entries.add(headers.get(sheetName));
            for (Document document : documents) {
                List<String> record = sheet.getValue()
                        .stream()
                        .map(col -> getValues(document, config.getXPath(col)))
                        .collect(Collectors.toList());


                entries.add(record);
            }
            data.put(sheetName, entries);
        }
    }

    public void saveToXls() {
        new ExcelWriter(config.getOutputFile()).saveToXlsFile(data);
    }

    private static String getValues(Document document, String xPathString) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes;
        List<String> values = new ArrayList<>();
        try {
            nodes = (NodeList) xpath.evaluate(xPathString, document, XPathConstants.NODESET);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    String item = nodes.item(i).getTextContent();
                    if (!item.isEmpty()) values.add(item);
                }
            }
        } catch (XPathExpressionException | NullPointerException e) {
            e.printStackTrace();
        }
        if (values.size() == 0) return "";
        if (values.size() == 1) return values.get(0);
        return values.toString();
    }
}
