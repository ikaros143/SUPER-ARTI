package org.example.mute.until;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;


public class httpGets {


    public static String getst(String urlStr){
        String jsonResponse ="";
        String originalUrl="";
        try {
            // 定义请求的 URL
//            String urlStr = "https://api.lolicon.app/setu/v2?r18=1";  // 替换为实际的 API 地址
            // 创建 URL 对象
            URL url = new URL(urlStr);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为 GET
            connection.setRequestMethod("GET");
            // 获取响应代码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 获取输入流，读取响应数据
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                // 关闭输入流
                in.close();
                // 输出响应数据
                 jsonResponse = response.toString();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    JSONObject dataObject = dataArray.getJSONObject(0);
                    JSONObject urlsObject = dataObject.getJSONObject("urls");
                     originalUrl = urlsObject.getString("original");
//                    System.out.println("原始链接: " + originalUrl);
                } else {
//                    System.out.println("未找到链接数据。");
                }
                System.out.println("JSON 响应: " + jsonResponse);
            } else {
                System.out.println("GET 请求失败，响应代码: " + responseCode);
            }
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originalUrl;

    }
}
