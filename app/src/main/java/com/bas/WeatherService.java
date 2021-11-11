package com.bas;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * Created by Lucio on 2021/11/2.
 */
public class WeatherService {

    public interface Callback {

        void onSuccess(String temp);

        void onFail(Throwable e);
    }

    /**
     * 获取响应流
     *
     * @param connection
     * @return
     * @throws IOException
     */
    private InputStream getResponseStream(URLConnection connection) throws IOException {
        String encoding = connection.getHeaderField("Content-Encoding");
        //是否
        boolean gzipped = encoding != null && encoding.toLowerCase().contains("gzip");

        InputStream inputStream = connection.getInputStream();
        if (gzipped)//gzip流
            inputStream = new GZIPInputStream(inputStream);
        return inputStream;
    }

    public void requestData(Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.weather.com.cn/adat/sk/101010100.html");
                    //得到connection对象。
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    connection.setRequestMethod("GET");
//                    connection.setRequestProperty("Accept","*/*");
//                    connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
//                    connection.setRequestProperty("Connection","keep-alive");
                    //连接
                    connection.connect();
                    //得到响应码
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        //响应流
                        InputStream responseStream = getResponseStream(connection);
                        String result = readStream(responseStream, "UTF-8");
                        JSONObject jObj = new JSONObject(result);
                        JSONObject weatherBean = jObj.getJSONObject("weatherinfo");
                        String temp = weatherBean.getString("temp");
                        callback.onSuccess(temp);
                    } else {
                        callback.onFail(new SocketException("请求失败：code=" + responseCode));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFail(e);
                }
            }
        }).start();
    }

    /**
     * stream 转 string
     * @param inputStream
     * @param charsetName
     * @return
     * @throws IOException
     */
    private String readStream(InputStream inputStream, String charsetName) throws IOException {
        BufferedReader responseReader = null;

        try {
            responseReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
            StringBuffer sb = new StringBuffer();
            String readLine = null;
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            return sb.toString();
        } finally {
            if (responseReader != null) {
                responseReader.close();
            }
        }
    }

}
