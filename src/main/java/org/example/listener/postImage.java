package org.example.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;


import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.component.mirai.message.MiraiSendOnlyForwardMessage;
import love.forte.simbot.event.FriendMessageEvent;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//"740994565",
@Component
public class postImage {
    @Autowired
    private image reimage;

    @Listener
    @Filter(targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void postimg(GroupMessageEvent event) {
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
//               MessagesBuilder builder = new MessagesBuilder();
//               Messages append = null;
                for (int i = 0; i < data1.size(); i++) {
                    StringBuilder sb = new StringBuilder();
                    JSONObject honor = (JSONObject) data1.get(i);
                    JSONObject urls = honor.getJSONObject("urls");
                    String original = urls.getString("original");
                    String uid = honor.getString("uid");
                    String author = honor.getString("author");
                    ResourceImage resourceImage = reimage.httpGet(original, i);
                    System.out.println(original);
                    Messages messages = Messages.toMessages(Text.of(author), resourceImage);
                    event.getGroup().sendBlocking(messages);
                    event.replyBlocking(original);
                }
            }
        }
    }

    @Listener
    public void postimg1(FriendMessageEvent event) {
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
//               MessagesBuilder builder = new MessagesBuilder();
//               Messages append = null;

                for (int i = 0; i < data1.size(); i++) {
                    StringBuilder sb = new StringBuilder();
                    JSONObject honor = (JSONObject) data1.get(i);
                    JSONObject urls = honor.getJSONObject("urls");
                    String original = urls.getString("original");
                    String uid = honor.getString("uid");
                    String author = honor.getString("author");
                    ResourceImage resourceImage = reimage.httpGet(original, i);
                    System.out.println(original);
                    Messages messages = Messages.toMessages(Text.of(author), resourceImage);
                    event.getFriend().sendBlocking(messages);
                    event.getFriend().sendAsync(original);

                }
            }
        }
    }

    public static void main(String[] args) {
        String data = getData("tag:明日方舟|原神&白丝|黑丝,num:3,");
        System.out.println(data);
        String s = doPostJson("https://api.lolicon.app/setu/v2", data);
        System.out.println(s);
    }
    public static String getData(String s2) {
//         String s2="num:3,tag:明日方舟|原神&白丝|黑丝";
        String str2 = s2.replaceAll(" ", "");
        String[] strings = str2.split("\\,");
        Map<String, List<String>> map = new HashMap<>();
        // 循环加入map集合
        for (String string : strings) {
            List<String> list = new ArrayList<>();
            // 截取一组字符串
            String[] ss = string.split(":");
            if (ss[0].equals("num")) {
                if (Integer.valueOf((ss[1])) >= 5) {
                    list.add("1");
                    map.put(ss[0], list);
                } else {
                    list.add(ss[1]);
                    map.put(ss[0], list);
                }
            } else if (ss[0].equals("uid")) {
                list.add(ss[1]);
                map.put("uid", list);
            } else if (ss[0].equals("r18")) {
                list.add(ss[1]);
                map.put("r18", list);
            } else if (ss[0].equals("key")) {
                list.add(ss[1]);
                map.put("keyword", list);
            } else if (ss[0].equals("tag")) {
                String[] ss1 = string.split("&");
                for (int i = 0; i < ss1.length; i++) {
                    if (i == 0) {
                        String substring = ss1[i].substring(4);
                        list.add(substring);
                    } else {
                        list.add(ss1[i]);
                    }
                }
                map.put(ss[0], list);
            }
        }
        String str = JSON.toJSONString(map);
        System.out.println(str);


        return str;
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

//
//    public void fasong(MiraiGroupMessageEvent event,Messages messages){
//        MiraiForwardMessageBuilder builder = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);
//        builder.add(event.getBot(), messages);
//
//// 在 Java 中，基本所有的API都叫 'add'。包括在Kotlin中被成为 'says' 的那些。
//        MiraiSendOnlyForwardMessage message = builder.build();
//        event.getGroup().sendAsync(message);
//    }

}
