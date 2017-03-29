package com.duy.pascal.frontend.info_application;

import android.content.Context;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.utils.UnexpectedElementException;
import com.duy.pascal.frontend.utils.XmlRead;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Duy on 29-Mar-17.
 */

public class InfoAppUtil {

    public static ArrayList<ItemInfo> readListInfo(Context context, String filePath, boolean fromAssets)
            throws IOException, UnexpectedElementException, SAXException, ParserConfigurationException {
        ArrayList<ItemInfo> result = new ArrayList<>();

        InputStream inputStream;
        if (fromAssets) {
            inputStream = context.getAssets().open(filePath);
        } else {
            inputStream = new FileInputStream(filePath);
        }
        Node root = XmlRead.getRootNode(inputStream);
        Node child = root.getFirstChild();
        while (child != null) {
            NamedNodeMap attr = child.getAttributes();
            Node name = attr.getNamedItem("name");
            Node link = attr.getNamedItem("link");
            Node image = attr.getNamedItem("image");

            String sName = name.getNodeValue();
            String sLink = link.getNodeValue();
            String imgPath = image.getNodeValue();
            result.add(new ItemInfo(sName, sLink, imgPath));
            child = root.getNextSibling();
        }
        return result;
    }

    public static ArrayList<ItemInfo> readListInfo(InputStream inputStream) {
        ArrayList<ItemInfo> result = new ArrayList<>();
        try {
            Node root;
            root = XmlRead.getRootNode(inputStream);
            DLog.d(root.getNodeName());
            Node child = root.getFirstChild();
            DLog.d("readListInfo: " + child.getNodeName());
            while (child != null) {
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    //Read the attributes
                    NamedNodeMap attributes = child.getAttributes();

                    Node nameNode = attributes.getNamedItem("name");
                    Node linkNode = attributes.getNamedItem("link");
                    Node imageNode = attributes.getNamedItem("image");

                    String sName = nameNode.getNodeValue();
                    String sLink = linkNode.getNodeValue();
                    String imgPath = imageNode.getNodeValue();
                    result.add(new ItemInfo(sName, sLink, imgPath));
                }
                child = root.getNextSibling();
            }
        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
        } catch (SAXException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } catch (UnexpectedElementException e) {
//            e.printStackTrace();
        }
        return result;
    }
}
