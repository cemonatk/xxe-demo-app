package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "works";
    }

    @PostMapping(value = "/xml", consumes = MediaType.APPLICATION_XML_VALUE)
    public String parseXml(@RequestBody String xmlData) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
        dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", true);
        dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
        dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new ByteArrayInputStream(xmlData.getBytes()));
        return doc.getDocumentElement().getTextContent(); 
    }

    @PostMapping(value = "/deserialize", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String deserializeObject(@RequestBody String base64Data) {
        try {
            byte[] data = Base64.getDecoder().decode(base64Data);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            return obj.toString();
        } catch (ClassNotFoundException e) {
            return "Deserialization failed: ClassNotFoundException - " + e.getMessage();
        } catch (Exception e) {
            return "Deserialization failed: " + e.toString();
        }
    }

    @GetMapping("/serialize")
    public String serializeObject() throws Exception {
        ExampleObject obj = new ExampleObject("Example data", 123);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    public static class ExampleObject implements java.io.Serializable {
        private String data;
        private int number;

        public ExampleObject(String data, int number) {
            this.data = data;
            this.number = number;
        }

        @Override
        public String toString() {
            return "ExampleObject[data=" + data + ", number=" + number + "]";
        }
    }
}