package org.example;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import net.mamoe.mirai.internal.deps.okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.dao.messages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//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
public class testgpt {
    public static void main(String[] args) throws IOException {
        tets2();
    }

    public static  void a() throws IOException {
        String a ="https://api.r10086.com/img-api.php?zsy=原神";
        HttpURLConnection conn = (HttpURLConnection) new URL(a)
                .openConnection();
        conn.setInstanceFollowRedirects(false);//禁止重定向
        conn.setConnectTimeout(5000);

        Map<String, List<String>> headerFields = conn.getHeaderFields();
        System.out.println(headerFields);
    }
    private static final String API_ENDPOINT = "https://api.openai-proxy.com/v1/chat/completions";
    private static final String API_KEY = "Bearer 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";

    public static String chat(String txt) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", "gpt-3.5-turbo");
        List<Map<String, String>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, String>() {{
            put("role", "user");
            put("content", txt);
        }});
        paramMap.put("messages", dataList);
        JSONObject message = null;
        try {
            String body = HttpRequest.post(API_ENDPOINT)
                    .header("Authorization", API_KEY)
                    .header("Content-Type", "application/json")
                    .body(JSON.toJSONString(paramMap))
                    .execute()
                    .body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject result = choices.get(0, JSONObject.class, Boolean.TRUE);
            message = result.getJSONObject("message");
        } catch (HttpException e) {
            return "出现了异常";
        } catch (ConvertException e) {
            return "出现了异常";
        }
        return message.getStr("content");
    }

    public static void balce() throws IOException {
        String url = "https://api.openai-proxy.com/pro/balance?apiKey=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";
        CloseableHttpClient aDefault = HttpClients.createDefault();//创建http请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse execute = aDefault.execute(httpGet);
        HttpEntity entity = execute.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        System.out.println(result);
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        com.alibaba.fastjson.JSONObject data = jsonObject.getJSONObject("data");
        String total = data.getString("total");
        String balance = data.getString("balance");
        String used = data.getString("used");

        System.out.println(total + "," + balance + "," + used);
    }


    public static void tets() throws IOException {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        JSONArray messages = new JSONArray();
        messages.put(new messages("你是谁", "user").toJSON());
        jsonObject.put("temperature", 0.9);
        jsonObject.put("model", "gpt-3.5-turbo");
        jsonObject.put("frequency_penalty", 0.8);
        jsonObject.put("presence_penalty", 0.8);
        jsonObject.put("messages", messages);
        // create a StringEntity with the JSON object as content
        HttpEntity entity1 = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
        String s = jsonObject.toString();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpUriRequest request = RequestBuilder.post("https://api.openai-proxy.com/v1/chat/completions")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", API_KEY)
                .setEntity(entity1)
                .build();
        CloseableHttpResponse response = httpClient.execute(request);
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        } finally {
            response.close();
        }
    }

    public static void tets2() throws IOException {//画图
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        JSONArray messages = new JSONArray();
//        messages.put(new messages("你是谁", "user").toJSON());
        jsonObject.put("prompt", "车水马龙");
        jsonObject.put("size", "512x512");
//        jsonObject.put("model","DALL·E 2");
        // create a StringEntity with the JSON object as content
        HttpEntity entity1 = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
        String s = jsonObject.toString();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpUriRequest request = RequestBuilder.post("https://api.openai-proxy.com/v1/images/generations")
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", API_KEY)
                .setEntity(entity1)
                .build();
        CloseableHttpResponse response = httpClient.execute(request);
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        } finally {
            response.close();
        }
    }

    public static void test3() throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
//            jsonObject.put("prompt", "你好");
        jsonObject.set("max_tokens", 200);
        jsonObject.set("n", 1);
        jsonObject.set("temperature", 0.5);
        jsonObject.set("context", "你好");

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());

        Request request = new Request.Builder()
                .url("https://api.openai-proxy.com/v1/completions")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", API_KEY)
                .build();

        Response response = client.newCall(request).execute();
        String jsonResponse = response.body().string();
        System.out.println(jsonResponse);
        List<String> completions = new ArrayList<>();
        com.alibaba.fastjson.JSONObject jsonObjectResponse = JSON.parseObject(jsonResponse);
        com.alibaba.fastjson.JSONArray jsonArray = jsonObjectResponse.getJSONArray("choices");

        for (int i = 0; i < jsonArray.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonObjectChoice = jsonArray.getJSONObject(i);
            completions.add(jsonObjectChoice.getString("text"));
        }

        String newContext = jsonObjectResponse.getJSONObject("conversation").getString("id");
        // 保存新的对话ID以便下一次请求使用
        // ...

        System.out.println(completions);

    }

}