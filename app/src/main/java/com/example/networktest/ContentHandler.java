package com.example.networktest;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandler extends DefaultHandler {
    private static final String TAG = "54";
    String nodeName;
    StringBuilder id;
    StringBuilder name;
    StringBuilder version;
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startDocument() throws SAXException {
        id=new StringBuilder();
        name=new StringBuilder();
        version=new StringBuilder();
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName=localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("app".equals(localName)){
            Log.d(TAG, "id is "+id.toString().trim());
            Log.d(TAG, "name is "+name.toString().trim());
            Log.d(TAG, "version is "+version.toString().trim());
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName){
            case "id":
                id.append(ch,start,length);
                break;
            case "name":
                name.append(ch,start,length);
                break;
            case "version":
                version.append(ch,start,length);
                break;
            default:
                break;
        }
    }
}
