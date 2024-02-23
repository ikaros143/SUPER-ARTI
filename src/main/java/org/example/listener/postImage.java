package org.example.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;

import love.forte.simbot.event.FriendMessageEvent;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;

import love.forte.simbot.resources.Resource;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//"740994565",
@Component
public class postImage {

    @Listener
    @Filter(targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void postimg(GroupMessageEvent event) throws MalformedURLException {
        String s = event.getMessageContent().getPlainText();//获取发送的信息
        if (s.contains("来张图")) {
            String substring = s.substring(4);
//            System.out.println("发送的消息" + substring);
            String data = getData(substring);
            String img = doPostJson("https://api.lolicon.app/setu/v2", data);
            JSONObject jsonObject = JSON.parseObject(img);
            JSONArray data1 = jsonObject.getJSONArray("data");
            System.out.println(data1.size());
            if (data1.size() == 0) {
                event.getGroup().sendBlocking("未查到喵");
            } else {
                for (Object o : data1) {
                    JSONObject honor = (JSONObject) o;
                    JSONObject urls = honor.getJSONObject("urls");
                    String original = urls.getString("original");
                    String author = honor.getString("author");
                    Messages messages = Messages.toMessages(Text.of(author), Image.of(Resource.of(new URL(original))));
                    event.getGroup().sendBlocking(messages);
                    event.replyBlocking(original);
                }
            }
        }
    }

    @Listener
    public void postimg1(FriendMessageEvent event) throws MalformedURLException {
        String s = event.getMessageContent().getPlainText();//获取发送的信息
        if (s.contains("来张图")) {
            String substring = s.substring(4);
            System.out.println("发送的消息" + substring);
            String data = getData(substring);
            String img = doPostJson("https://api.lolicon.app/setu/v2", data);
            JSONObject jsonObject = JSON.parseObject(img);
            JSONArray data1 = jsonObject.getJSONArray("data");
            System.out.println(data1.size());
            if (data1.size() == 0) {
                event.getFriend().sendBlocking("未查到喵");
            } else {
                for (Object o : data1) {
                    JSONObject honor = (JSONObject) o;
                    JSONObject urls = honor.getJSONObject("urls");
                    String original = urls.getString("original");
                    String author = honor.getString("author");
                    System.out.println(original);
                    Messages messages = Messages.toMessages(Text.of(author), Image.of(Resource.of(new URL(original))));
                    event.getFriend().sendBlocking(messages);
                    event.getFriend().sendAsync(original);

                }
            }
        }
    }

    public static String getData(String s2) {
        String str2 = s2.replaceAll(" ", "");
        String[] strings = str2.split("\\,");
        Map<String, List<String>> map = new HashMap<>();

        for (String string : strings) {
            String[] keyValue = string.split(":");
            String key = keyValue[0];
            String value = keyValue[1];
            List<String> list = new ArrayList<>();

            switch (key) {
                case "num":
                    list.add(Integer.parseInt(value) >= 5 ? "1" : value);
                    break;
                case "uid":
                case "r18":
                case "key":
                    list.add(value);
                    break;
                case "tag":
                    String[] tagValues = value.split("&");
                    for (int i = 0; i < tagValues.length; i++) {
                        if (i == 0) {
                            list.add(tagValues[i].substring(4));
                        } else {
                            list.add(tagValues[i]);
                        }
                    }
                    break;
            }
            map.put(key.equals("tag") ? "tag" : key, list);
        }

        String jsonStr = JSON.toJSONString(map);
        System.out.println(jsonStr);
        return jsonStr;
    }
    /**
     * 发送post 请求
     *
     * @param url  请求地址
     * @param json 请求参数
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
