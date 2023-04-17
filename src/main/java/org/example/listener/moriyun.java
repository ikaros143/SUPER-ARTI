package org.example.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.message.MiraiSendOnlyAudio;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Image;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.Messages;
import love.forte.simbot.resources.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class moriyun {
    @Value("${moliyun.ApiKey}")
    private String ApiKey;

    @Value("${moliyun.ApiSecret}")
    private String ApiSecret;

    @Listener(priority = 1000,async = true)
    @Filter(targets = @Filter.Targets(groups = {"740994565", "494050282"}, atBot = true))
    @ContentTrim
    public EventResult moliyun(GroupMessageEvent event) throws MalformedURLException {
        String plainText = event.getMessageContent().getPlainText().trim();
        ID id = event.getAuthor().getId();
        String nickOrUsername = event.getAuthor().getNickOrUsername();
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Api-Key", ApiKey);
        headers.add("Api-Secret", ApiSecret);
        JSONObject body = new JSONObject();
// 发送的内容
        body.put("content", plainText);
// 消息类型，1：私聊，2：群聊
        body.put("type", 2);
        body.put("from", String.valueOf(id));
        body.put("fromName", nickOrUsername);
        body.put("to", String.valueOf(id));
//        body.put("toName",nickOrUsername);
        HttpEntity<String> formEntity = new HttpEntity<String>(body.toString(), headers);
        TestRestTemplate restTemplate = new TestRestTemplate();
        JSONObject jsonObject = restTemplate.postForEntity("https://api.mlyai.com/reply", formEntity, JSONObject.class).getBody();
        System.out.println(jsonObject.toJSONString());
        String code = jsonObject.getString("code");
        if (code.equals("C1001")) {
            event.getGroup().sendBlocking("能量耗尽了，明天在来吧~");
        } else if (code.equals("00000")) {
            JSONArray honors = jsonObject.getJSONArray("data");
            for (int i = 0; i < honors.size(); i++) {
                Object honor = honors.get(i);
                JSONObject jsonObject2 = (JSONObject) JSON.toJSON(honor);
                System.out.println(jsonObject2.toJSONString());
                String typed = jsonObject2.getString("typed");
                String content = jsonObject2.getString("content");
                System.out.println(content);
                switch (typed) {
                    case "1":
                        event.getGroup().sendBlocking(content);
                        break;
                    case "2":
                        Message message = Messages.toMessages(Image.of(Resource.of
                                (new URL("https://files.molicloud.com/" + content))));
                        event.getGroup().sendBlocking(message);
                        break;
//                    case "4":
//                        event.replyBlocking("正在努力更新中");
                }
                    String reg3 = "(?<=id=)[\\s\\S]*(?=&auto)";
                    Pattern p3 = Pattern.compile(reg3);
                    Matcher m3 = p3.matcher(content);
                    if(m3.find()){
                        Message n = new MiraiSendOnlyAudio(Resource.of(new URL("http://music.163.com/song/media/outer/url?id="+m3.group()+".mp3")));
                        event.getGroup().sendAsync(n);
                        System.out.println(m3.group());
                    }

            }
        } else {
            //异常
            event.getGroup().sendBlocking("出现了未知的问题...");
        }
        return EventResult.truncate();

    }
}
