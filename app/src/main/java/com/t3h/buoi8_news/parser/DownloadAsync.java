package com.t3h.buoi8_news.parser;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadAsync extends AsyncTask<String, Integer, String> {


    private DownloadCallback callback;

    public DownloadAsync(DownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        //String... truyen vao 1 mang 1 chieu string
        String link = strings[0];
        String path = strings[1];
        try {
            File f = new File(path);
            f.getParentFile().mkdirs();
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            int total = connection.getContentLength();
            int totalSave = 0;
            byte[] b = new byte[1024];
            int count = in.read(b);
            while (count != -1) {
                totalSave += count;
                float percent = (float) totalSave / total;
                publishProgress((int) (percent* 100));
                out.write(b, 0, count);
                count = in.read(b);
            }

            in.close();
            out.close();
            return f.getPath();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.downloadFinish(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        callback.updatePercent(values[0]);
    }

    public interface DownloadCallback{
        void updatePercent(int percent);
        void downloadFinish(String path);
    }


    //    @Override
//    protected Void doInBackground(Void... voids) {
//        //ko duoc goi truc tiep vi doIn chay trong thread khac
//        publishProgress();
//        return null;
//        //tuong duong vs run trong thread
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//        //thuc hien cap nhap giao dien trong khi project thuc hien
//        //chay trong main thread
//    }
//
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        //khi chay xong goi vao day
//        super.onPostExecute(aVoid);
//
//    }


}



