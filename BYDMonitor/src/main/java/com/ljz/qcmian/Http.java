package com.ljz.qcmian;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Http {

    private static int mMaxContentLength = 100 * 1024;

    public static String sendGet(String url, String param) {
        String result = "";
        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader bfr = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(5 * 1000);
            connection.connect();

            StringBuilder sb = new StringBuilder();
            in = connection.getInputStream();
            if (in != null) {
                inr = new InputStreamReader(in, "utf-8");
                bfr = new BufferedReader(inr);
                String line;
                while ((line = bfr.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bfr != null) {
                    bfr.close();
                }
                if (inr != null) {
                    inr.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    public static String sendPost(String url, String param) {
        OutputStream out = null;
        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader bfr = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5 * 1000);


            out = conn.getOutputStream();
            if (out != null) {
                out.write(param.getBytes("utf-8"));
                out.flush();
            }

            StringBuilder sb = new StringBuilder();
            in = conn.getInputStream();
            if (in != null) {
                inr = new InputStreamReader(in, "utf-8");
                bfr = new BufferedReader(inr);
                String line;
                while ((line = bfr.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
            result = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (bfr != null) {
                    bfr.close();
                }
                if (inr != null) {
                    inr.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static int fitLength(int len) {
        if (len > mMaxContentLength) {
            len = mMaxContentLength;
        }
        return len;
    }
}
