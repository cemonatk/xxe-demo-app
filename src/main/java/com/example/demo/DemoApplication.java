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

}
