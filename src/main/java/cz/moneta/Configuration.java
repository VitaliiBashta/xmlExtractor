package cz.moneta;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

class Configuration {
    private String outputFile;

    private String inputFile;
    private Map<String, List<String>> headers;
    private final Properties properties = new Properties();

    Configuration(String filename) throws IOException {

        try (InputStream resourceStream = Files.newInputStream(Paths.get(filename))) {
            properties.load(resourceStream);

            outputFile = properties.getProperty("output");
            inputFile = properties.getProperty("input");
            headers = getSheetsHeaders();
        }
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getInputFile() {
        return inputFile;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getXPath(String columnName) {
        String result = "";
        try {
            result = properties.getProperty(columnName.trim()).trim();
            return result;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, List<String>> getSheetsHeaders() {
        return properties.entrySet().stream()
                .filter(p -> p.getKey().toString().endsWith(".headers"))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString().split(".headers")[0],
                        entry -> Arrays.asList(entry.getValue().toString().split(","))));
    }
}
