package org.example.listener;


import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;

import love.forte.simbot.component.mirai.message.MiraiForwardMessage;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.definition.Friend;
import love.forte.simbot.event.*;
import love.forte.simbot.message.*;
import love.forte.simbot.message.Image;
import love.forte.simbot.resources.PathResource;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.message.data.ForwardMessage;
import org.example.mute.until.httpGets;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * 一个自定义的监听函数载体类，直接通过 {@link Component} 注册到Spring中即可。
 * 在此类中通过标记 {@link love.forte.simboot.annotation.Listener} 来开始编写监听函数。
 *
 * @author ForteScarlet
 */
@Component
public class MyListener {

    /**
     * 这个监听函数我们实现：
     * <p>好友发送一句: 你好
     * <p>就回复一句: 你也好
     * <p>
     * 此监听函数将会使用 <b>阻塞的</b> 代码风格。
     */
    @Listener
    @Filter("兽耳") // 简单的事件过滤可以直接通过 @Filter 来完成。其更多参数请参考此注解的各注释说明
    @ContentTrim   // @ContentTrim 会在 @Filter 进行文本匹配的时候，消除其可能存在的前后空字符（默认是不会的），
    // 这对于可能掺杂其他类型的消息元素时候很有用（例如At，或者表情）
    public void helloListener(FriendMessageEvent event) {
        // 因为要监听'好友消息'，因此此处参数为 FriendMessageEvent
        // 当此方法被触发的时候，就说明好友发送的消息就是 '你好' 了, 此处直接回复 '你也好'
        // 此处有多种方式。
        // 方式1: 获取此好友并直接发送
//        Messages messages = event.getMessageContent().getMessages();
        Friend friend = event.getFriend();
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        MessagesBuilder image;
        try {
            image = messagesBuilder.image(Resource.of
                    (new URL("https://iw233.cn/api.php?sort=cat")));
            friend.sendBlocking(image.build());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Listener
    @Filter("银发")
    @ContentTrim
    public void helloListener2(FriendMessageEvent event) {
        Friend friend = event.getFriend();
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        MessagesBuilder image;
        try {
            image = messagesBuilder.image(Resource.of
                    (new URL("https://iw233.cn/api.php?sort=yin")));
            friend.sendBlocking(image.build());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //621673939
    @Listener
    @Filter(value = "银发", targets = @Filter.Targets(groups = {"740994565","621673939"}))
    @ContentTrim
    public void group(GroupMessageEvent event) {
        event.getBot().delay(Duration.ofSeconds(2), () -> {
            MessagesBuilder messagesBuilder = new MessagesBuilder();
            MessagesBuilder image;
            try {
                image = messagesBuilder.image(Resource.of
                        (new URL("https://iw233.cn/api.php?sort=yin")));
                event.getGroup().sendAsync(image.build());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }
//
    @Listener
    @Filter(value = "兽耳", targets = @Filter.Targets(groups = {"740994565","621673939"}))
    @ContentTrim
    public void group2(GroupMessageEvent event) {
        ID authorId = event.getAuthor().getId();
        event.getBot().delay(Duration.ofSeconds(2), () -> {
            MessagesBuilder messagesBuilder = new MessagesBuilder();
            MessagesBuilder image;
            try {
                image = messagesBuilder.at(authorId).text("兽耳").image(Resource.of
                        (new URL("https://iw233.cn/api.php?sort=cat")));
                event.getGroup().sendBlocking(image.build());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }
    @Listener
    @Filter(value = "壁纸推荐", targets = @Filter.Targets(groups = {"740994565","621673939"}))
    @ContentTrim
    public void group4(GroupMessageEvent event) {
        ID authorId = event.getAuthor().getId();
            MessagesBuilder messagesBuilder = new MessagesBuilder();
            MessagesBuilder image;
            try {
                image = messagesBuilder.at(authorId).text("壁纸推荐").image(Resource.of
                        (new URL("https://iw233.cn/api.php?sort=top")));
                event.getGroup().sendAsync(image.build());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
    }

    @Listener
    @ContentTrim
    public void group3(FriendMessageEvent event) throws UnsupportedEncodingException {
        String plainText = event.getMessageContent().getPlainText();
        if (plainText.contains("涩图")){
//        群里基本发不出去所以改为私聊.......
//        Messages messages = Messages.toMessages(at, Text.of(" 涩涩！"));
        Friend friend = event.getFriend();
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        String s = "https://api.lolicon.app/setu/v2?tag=#&r18=1";
            String replace="";
//            URLEncoder.encode(tag, "UTF-8")
        if (plainText.length()>3){
             replace = s.replace("#", URLEncoder.encode(plainText.substring(3), "UTF-8"));
        }else {
             replace = s.replace("tag=#&", "");
        }
        System.out.println(replace);
        String getst = httpGets.getst(replace);
        try {
            Messages build = messagesBuilder.image(Resource.of
                    (new URL(getst))).build();
            friend.sendBlocking(build);
            friend.sendBlocking(getst);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    }

    //关键字精准匹配
//    private static boolean isContain(String source, String subItem){
//        String pattern = "\\b"+subItem+"\\b";
//        Pattern p= Pattern.compile(pattern);
//        Matcher m=p.matcher(source);
//        return m.find();
//    }



    @Listener //好友自动通过
    public void acc(RequestEvent event) {
        event.acceptBlocking();


    }


}
