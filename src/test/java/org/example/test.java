package org.example;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import love.forte.simbot.message.MessagesBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.net.URI;
import java.util.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//2、切换渠道为ANDROID_PHONE、ANDROID_PAD、ANDROID_WATCH、MACOS、IPAD

public class test {


    public static void main(String[] args) throws  JSONException {
        // 构建请求头
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Api-Key", "bbrad7alnxxgqkm3");
//        headers.add("Api-Secret", "132160");
        String s = "{\"code\":\"00000\",\"plugin\":\"Chat\",\"data\":[{\"content\":\"主人不怕触电\\u2026\",\"typed\":1,\"remark\":\"\"}],\"message\":\"请求成功\"}";
        JSONObject body = new JSONObject();
        com.alibaba.fastjson.JSONObject jsonObject =  JSON.parseObject(s);
// 发送的内容
        body.put("content", "亲亲我");
//// 消息类型，1：私聊，2：群聊
//        body.put("type", 1);
//        body.put("from", "ikaros");
//        body.put("fromName", "ikaros");
//        HttpEntity<String> formEntity = new HttpEntity<String>(body.toString(), headers);
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        JSONObject jsonObject = restTemplate.postForEntity("https://api.mlyai.com/reply", formEntity, JSONObject.class).getBody();
//        System.out.println(jsonObject.toJSONString());
//        String s = JSON.toJSONString(jsonObject);
        String code = jsonObject.getString("code");
        if (code.equals("C1001")){
            System.out.println("次数已用完");
        }else if (code.equals("00000")){
            JSONArray honors = jsonObject.getJSONArray("data");
//            System.out.println(honors.toJSONString());
            for (int i=0;i<honors.size();i++){
                Object honor = honors.get(i);
                JSONObject jsonObject2 = (JSONObject) JSON.toJSON(honor);
                String typed = jsonObject2.getString("typed");
                if (typed.equals(1)){
                    String content = jsonObject2.getString("content");
                    System.out.println(content);
                } else if (typed.equals(2)){
                    //图片发送
                    //https://files.molicloud.com/
                }else if (typed.equals(4)){
                    //音频
                }
                System.out.println(jsonObject2.toJSONString());


            }
        }      else {
            //异常
        }

    }

    public static void httpGet(String url, int i) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject template = new JSONObject();
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange
                    (url, HttpMethod.GET, null, byte[].class);
            byte[] body = responseEntity.getBody();
            MessagesBuilder builder = new MessagesBuilder();
            FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\img\\" + i + ".png"));
            fileOutputStream.write(body);
            fileOutputStream.close();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            System.out.println("404 not fund");
        }
    }

//    public static String sendGet(String url) throws IOException {
//        CloseableHttpClient httpClient;
//        HttpGet httpGet;
//        String CONTENT_TYPE = "Content-Type";
//        httpClient = HttpClients.createDefault();
//        httpGet = new HttpGet(url);
//        CloseableHttpResponse response = httpClient.execute(httpGet);
//        String resp;
//        try {
//            HttpEntity entity = response.getEntity();
//            resp = EntityUtils.toString(entity, "utf-8");
//            EntityUtils.consume(entity);
//        } finally {
//            response.close();
//        }
//        return resp;
//    }

    public static String doGet(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            System.out.println(builder);
            URI uri = builder.build();
//            System.out.println(s);
//             创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

//             执行请求
            response = httpClient.execute(httpGet);
//             判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "resultString";
    }

    public static void map(String s1) {
        String str = "num:2,uid:2,tag:啊|哈";
        //map形式
        //线切割
        String[] strs = str.split(",");
        //对数组循环遍历
        Map<String, String> map = new HashMap<>();
        for (String s : strs) {
            String[] ss = s.split(":");
            if (ss[0].equals("num")) {
                if (Integer.valueOf((ss[1])) >= 5) {
                    map.put(ss[0], String.valueOf(5));
                }
            } else if (ss[0].equals("uid")) {
                map.put("author_uuid", String.valueOf((ss[1])));
            } else if (ss[0].equals("uid")) {
                map.put("key", String.valueOf((ss[1])));
            } else if (ss[0].equals("tag")) {
                map.put(ss[0], String.valueOf((ss[1])));
            }

        }
        System.out.println(map);
//        return map;
    }


    /**
     * 发送post 请求
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return
     */
    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            System.out.println(response);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

}
