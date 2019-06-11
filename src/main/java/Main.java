import cz.moneta.ExcelWriter;
import cz.moneta.XmlResponses;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static Properties properties = new Properties();
    private static Map<String, List<String>> headers = new HashMap<>();
    private static final Map<String, List<List<String>>> data = new HashMap<>();

    public static void main(String[] args) throws IOException {
        loadProperties();
        String outputFile = properties.getProperty("output");
        String inputFile = properties.getProperty("input");

        for (String sheet : List.of(properties.getProperty("sheets").split(","))) {
            headers.put(sheet, List.of(properties.getProperty(sheet + ".headers").split(",")));
        }


        List<Document> documents = new XmlResponses().loadFrom(inputFile).getDocuments();
        for (Map.Entry<String, List<String>> sheet : headers.entrySet()) {

            List<List<String>> entries = new ArrayList<>();
            entries.add(headers.get(sheet.getKey()));
            for (Document document : documents) {
                List<String> record = new ArrayList<>();
                for (String header : sheet.getValue()) {
                    String values = getValues(document, header);
                    record.add(values);
                }
                entries.add(record);
            }
            data.put(sheet.getKey(), entries);
        }
        data.forEach((k, v) -> System.out.println("Sheet:" + k + "\t" + v));
        ExcelWriter.saveToXlsFile(outputFile, data);
    }


    private static String getValues(Document document, String header) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes;
        List<String> values = new ArrayList<>();
        try {
            String xPathString = properties.getProperty(header);
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

    private static void loadProperties() throws IOException {
        try (InputStream resourceStream = Files.newInputStream(Paths.get("main.properties"))) {
            properties.load(resourceStream);
        }
    }
}