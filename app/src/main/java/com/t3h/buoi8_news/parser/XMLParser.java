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
        if(qName.equals(Constances.ITEM)){
            item = new News();
        }

        if(qName.equals(Constances.IMAGE)){
            String img = attributes.getValue("url");
            item.setImage(img);
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

            case Constances.DESC:
                descGoogleNews();
                break;

            case Constances.PUB_DATE:
                item.setPubDate(builder.toString());
                break;
            case Constances.ITEM:
                arr.add(item);
                break;
        }

    }


    private void descGoogleNews() {

        String s = "<p>";
        String value = builder.toString();

        int i = value.indexOf(s);

        if(i>=0){
            String value1;
            int index = value.indexOf(s) + s.length();
            value1 = value.substring(index);
            String desc = value1.substring(0, value1.indexOf("</p>"));
//        item.setDesc("123");
            item.setDesc(desc);
        }else {
            item.setDesc("Description");
        }


    }


    public ArrayList<News> getArr() {
        return arr;
    }

//    public interface FaceItemListener{
////        void onClick(int position);
////        void onLongClick(int position);
////
////    }
}
