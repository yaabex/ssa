package ist.spln.config;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlParser {


    private String subtitleLocation;
    private String scriptLocation;

    public void parse(String filePath) {
        try {
            File xml = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("subLocation");
            subtitleLocation = nodeList.item(0).getTextContent();

            nodeList = doc.getElementsByTagName("scriptLocation");
            scriptLocation = nodeList.item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSubtitleLocation() {
        return subtitleLocation;
    }

    public String getScriptLocation() {
        return scriptLocation;
    }
}
