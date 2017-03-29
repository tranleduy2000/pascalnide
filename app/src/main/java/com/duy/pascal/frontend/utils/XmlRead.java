package com.duy.pascal.frontend.utils;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlRead {
    /**
     * Reads the categories node from an input stream of a .bpc file and returns it
     */
    public static Node getRootNode(InputStream is) throws ParserConfigurationException, SAXException, IOException, UnexpectedElementException {
        //Read document
        Document document;

        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw e;
        }

        document.getDocumentElement().normalize();

        //Get Nodes from document
        Node childRoot = document.getFirstChild();
        String name = childRoot.getNodeName();
        return childRoot;
    }
}
