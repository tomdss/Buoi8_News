package com.t3h.buoi8_news.parser;

import com.t3h.buoi8_news.Constances;
import com.t3h.buoi8_news.model.News;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XMLParser extends DefaultHandler {
    private ArrayList<News> arr = new ArrayList<>();
    private News item;
    private StringBuilder builder;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(qName== Constances.ITEM){
            item = new News();
        }
        builder = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch,start,length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(item==null)return;

        switch (qName){
            case Constances.TITLE:
                item.setTitle(builder.toString());
                break;

            case Constances.LINK:
                item.setLink(builder.toString());
                break;
            case Constances.PUB_DATE:
                item.setPubDate(builder.toString());
                break;
            case Constances.ITEM:
                arr.add(item);
                break;
                

        }

    }

}
