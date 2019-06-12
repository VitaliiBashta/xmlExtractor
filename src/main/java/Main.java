import cz.moneta.XmlExtractor;

class Main {
    public static void main(String[] args) {
        String conf = args.length == 1 ? args[0] : "config.properties";
        XmlExtractor xmlExtractor = new XmlExtractor(conf);
        xmlExtractor.saveToXls();
    }
}