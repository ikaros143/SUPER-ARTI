package org.example.mute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kotlin.Unit;
import lombok.SneakyThrows;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;

import love.forte.simbot.ID;
import love.forte.simbot.Identifies;
import love.forte.simbot.application.Application;
import love.forte.simbot.application.Applications;
import love.forte.simbot.application.BotManagers;

import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;

import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.component.mirai.message.MiraiNudge;
import love.forte.simbot.component.mirai.message.MiraiSendOnlyForwardMessage;
import love.forte.simbot.core.application.Simple;
import love.forte.simbot.definition.*;
import love.forte.simbot.event.*;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.ByteArrayResource;
import love.forte.simbot.resources.PathResource;
import love.forte.simbot.resources.Resource;
import love.forte.simbot.utils.Lambdas;
import net.mamoe.mirai.message.data.ForwardMessage;
import org.example.listener.image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class muteall {
    //
    @Autowired
    private Application application;

    @Autowired
    private BotManagers botManagers;


    @Listener
    @Filter("你好")
    public void nudeg(FriendMessageEvent event) {
        Friend friend = event.getFriend();
        friend.sendBlocking("你也好");
    }

    @Scheduled(cron = "0 30 11 ? * *")
    public void goodafte() {
        BotManagers botManagers = application.getBotManagers();
        // 如果只有一种bot，也可以考虑使用 botManagers.get(0).all().get(0);
        // 这样的话注意处理异常情况
        Bot o = botManagers.get(0).all().get(0);
        Group group = o.getGroup(ID.$(740994565));
        PathResource resource = Resource.of(Paths.get("/mcl/img/atri/cf.jpg"));
        Image<?> resourceImage = Image.of(resource);
        Messages messages = Messages.toMessages( Text.of("大家要记得按时吃饭哦~"), resourceImage);
        group.sendBlocking(messages);
    }
    @Scheduled(cron = "0 30 7  * * ?")
    public void goodMorning1() {
        BotManagers botManagers = application.getBotManagers();
        // 如果只有一种bot，也可以考虑使用 botManagers.get(0).all().get(0);
        // 这样的话注意处理异常情况
        String s = "起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！";
        Bot o = botManagers.get(0).all().get(0);
        Group group = o.getGroup(ID.$(740994565));
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        MessagesBuilder image = null;
        try {
            image = messagesBuilder.image(Resource.of
                    (new URL("https://sex.nyan.xyz/api/v2/img?tag=魅魔")));
            group.sendAsync(image.build());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        Messages messages = Messages.toMessages( Text.of(s));
//        group.sendAsync(messages);

    }
    @Scheduled(cron = "0 59 23  * * ?")
    public void goodMorning2() {
        BotManagers botManagers = application.getBotManagers();
        Bot o = botManagers.get(0).all().get(0);
        Group group = o.getGroup(ID.$(740994565));
        PathResource resource = Resource.of(Paths.get("/mcl/img/atri/sj.jpg"));
        Image<?> resourceImage = Image.of(resource);
        Messages messages = Messages.toMessages(Text.of("大家要早点睡觉哦~"), resourceImage);
        group.sendAsync(messages);

    }

    @Listener
    @Filter(value = "亚托莉口我", targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void kowo(GroupMessageEvent event) {
        Random r = new Random();
        int i = r.nextInt(4);
        event.getAuthor().muteAsync(i + 1, TimeUnit.MINUTES);
    }

    @Listener
    @Filter(value = "亚托莉亲亲", targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void kow2(GroupMessageEvent event) {
        Random r = new Random();
        int i = r.nextInt(7);
        System.out.println(i);
        if (i == 1 || i == 0) {
            String a = "好…好吧，不要让别人看到哦……";
            event.getGroup().sendAsync(a);
        } else if (i == 2) {
            String a = "啾……";
            event.getGroup().sendAsync(a);
        } else if (i == 3) {
            String a = "呜哇~";
            event.getGroup().sendAsync(a);
        } else if (i == 4) {
            String a = "对机器人是犯法的哦~";
            event.getGroup().sendAsync(a);
        } else if(i==5){
            String a = "kimo...";
            event.getGroup().sendAsync(a);
            MiraiNudge nudge = new MiraiNudge(event.getAuthor().getId());
            event.getGroup().sendAsync(nudge);
        }else {
            String a = "mua~";
            event.getGroup().sendAsync(a);
        }
    }

    @Listener
    @Filter(targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void gtp(GroupMessageEvent event) {
        String plainText = event.getMessageContent().getPlainText();
        if (plainText.contains("亚托莉 ")) {
            String substring = plainText.substring(4);
            System.out.println(substring);
            String s = "http://api.caonm.net/api/ai/o.php";
            Map map = new HashMap();
            map.put("msg", substring);
            String s1 = image.doGet(s, map);
            if (s1.isEmpty()) {
                event.replyBlocking("服务器波动或者未查询到，请重试或者换个问题喵~");
                System.out.println(s1);
            } else {

                JSONObject jsonObject = JSON.parseObject(s1);
                if (jsonObject == null) {
                    event.replyBlocking("服务器波动或者未查询到，请重试或者换个问题喵~");
                } else {
                    String output = jsonObject.getString("Output").trim();
                    MessageReceipt messageReceipt = event.replyBlocking(output);
                }
            }
        }
    }

//    @Listener
//    @Filter("帮助")
//    public void help(FriendMessageEvent event) throws IOException {
//        String s = "群内发送 “”银发“、”兽耳“、”壁纸推荐“可回复相应的图片\n使" +
//                "用标签查询需输入标准的查询格式：例如：@ロボット2号 num:3,uid:129822,key:水着アズ" +
//                "author_uuid 输入uid即可，keyword 输入key即可，查询的标点符号都为英文符号，别用中文符号。" +
//                "发送 亚托莉亲亲、亚托莉口我；可触发事件" +
//                "需要提问格式为 亚托莉 xxxxx（亚托莉后面记得加上一个空格哦）" +
//                "\n向我发送“涩图”可能会有惊喜哦~";
//        Path path = Paths.get("E:\\img\\atri\\help.jpg");
//        byte[] bytes = Files.readAllBytes(path);
//        ByteArrayResource resource = Resource.of(bytes, path.toString());
//        Image<?> image = Image.of(resource);
//        ID id = event.getFriend().getId();
//        MiraiNudge miraiNudge = new MiraiNudge(id);
//        Messages messages = Messages.toMessages(Text.of(s), image, miraiNudge);
//        event.getFriend().sendBlocking(messages);
//    }


//    @Listener//戳一戳会回复一个戳一戳
//    @Filter(targets = @Filter.Targets(groups = {"740994565", "494050282"}))
//    public void nudge1(GroupMessageEvent event) {
////        System.out.println(event.getAuthor());
//        MiraiNudge nudge = new MiraiNudge(event.getAuthor().getId());
//        event.getGroup().sendAsync(nudge);
//    }

//    @Listener//戳一戳会回复一个戳一戳
//    public void nudge2(FriendMessageEvent event) {
//        MiraiNudge nudge = new MiraiNudge(event.getId());
//        event.getFriend().sendAsync(nudge);
//    }



}