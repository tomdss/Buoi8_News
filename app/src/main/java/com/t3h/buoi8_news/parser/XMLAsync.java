package com.t3h.buoi8_news.parser;

import android.net.Uri;
import android.os.AsyncTask;

import com.t3h.buoi8_news.model.News;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLAsync extends AsyncTask<String,Void, ArrayList<News>> {

    private ParserXMLCallback callback;

    public XMLAsync(ParserXMLCallback callback) {
        this.callback = callback;
    }

    @Override
    protected ArrayList<News> doInBackground(String... strings) {
        String api = "https://news.google.de/news/feeds?pz=1&cf=vi_vn&ned=vi_vn&hl=vi_vn&q=";
        String keySearch = strings[0];
        keySearch = Uri.encode(keySearch);//dung de encode cac keysearch co dau cach
        api = api+keySearch;
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLParser xmlParser = new XMLParser();
            parser.parse(api,xmlParser);
            return xmlParser.getArr();

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<News> news) {
        super.onPostExecute(news);
        callback.onParserFinish(news);
    }

    public interface ParserXMLCallback{

        void onParserFinish(ArrayList<News> arr);

    }

}
