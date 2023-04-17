package org.example.mute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;

import love.forte.simbot.ID;
import love.forte.simbot.Identifies;
import love.forte.simbot.action.DeleteSupport;
import love.forte.simbot.application.Application;
import love.forte.simbot.application.BotManagers;

import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.component.mirai.MiraiFriend;
import love.forte.simbot.component.mirai.MiraiGroup;
import love.forte.simbot.component.mirai.bot.MiraiBot;
import love.forte.simbot.component.mirai.bot.MiraiBotManager;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent;
import love.forte.simbot.component.mirai.event.MiraiGroupNudgeEvent;
import love.forte.simbot.component.mirai.message.MiraiAudio;
import love.forte.simbot.component.mirai.message.MiraiNudge;
import love.forte.simbot.component.mirai.message.MiraiSendOnlyAudio;
import love.forte.simbot.definition.*;
import love.forte.simbot.event.*;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.ByteArrayResource;
import love.forte.simbot.resources.PathResource;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.contact.AudioSupported;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.utils.ExternalResource;
import org.example.listener.image;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Component
public class muteall {
    //
    @Autowired
    private Application application;

    @Autowired
    private BotManagers botManagers;

    @Listener
    @Filter(value = "aaa", targets = @Filter.Targets(groups = "494050282"))
    public void bb(GroupMessageEvent event) throws MalformedURLException {
//        String plainText = event.getMessageContent().getPlainText();//获取监听群的消息
        MessagesBuilder messagesBuilder = new MessagesBuilder();
        MessagesBuilder image = messagesBuilder.at(ID.$(1321604543)).image(Resource.of
                (new URL("https://api.caonm.net/api/dm/index.php")));
        Messages build = image.build();
        event.getGroup().sendAsync(build);
    }


    @Listener
    @Filter("你好")
    @ContentTrim
    public void nudeg(FriendMessageEvent event) {
        Friend friend = event.getFriend();
//        friend.sendBlocking("你也好");
        Messages messages1 = Messages.toMessages(Text.of("爬"));

        friend.sendBlocking(messages1);
    }

    @Listener
    @Filter("踢他")
    @ContentTrim
    public void aas(GroupMessageEvent event, ContinuousSessionContext sessionContext) {
        var group = event.getGroup();
        var author = event.getAuthor();
        var groupId = group.getId();
        var authorId = author.getId();
        At firstOrNull = event.getMessageContent().getMessages().getFirstOrNull(At.Key);//获取at的那个人的信息
            ID id = firstOrNull.getTarget();//获取QQ
            GroupMember member = event.getGroup().getMember(id);
            String nickOrUsername = member.getNickOrUsername();
            event.getGroup().sendAsync("请输入是或否来确定是否踢" + nickOrUsername);
                while (true) {
                    ((DeleteSupport) member).deleteAsync();
                    var nextMessage = sessionContext.waitingForNextMessage(MiraiGroupMessageEvent.Key, Duration.ofMinutes(2), (context, events) -> {
                        // 判断下一个与当前事件的群、发送人相同的消息事件
                        return events.getGroup().getId().equals(groupId) && events.getAuthor().getId().equals(authorId);
                    });
                    var newNick = nextMessage.getPlainText().trim();
                    if (newNick.equals("是")) {
                        if (member instanceof DeleteSupport) {
                            ((DeleteSupport) member).deleteAsync();
                         event.getGroup().sendAsync("已删除!");
                        }
                    } else {
                        event.getGroup().sendAsync("已取消");
                    }
                }



    }

    @Scheduled(cron = "0 30 11 ? * *")
    public void goodafte() {
        BotManagers botManagers = application.getBotManagers();
        // 如果只有一种bot，也可以考虑使用 botManagers.get(0).all().get(0);
        // 这样的话注意处理异常情况
        Bot o = botManagers.get(0).all().get(0);
        Group group = o.getGroup(ID.$(740994565));
        PathResource resource = Resource.of(Paths.get("E:\\img\\吃饭.jpg"));
        Image<?> resourceImage = Image.of(resource);
        Messages messages = Messages.toMessages(Text.of("大家要记得按时吃饭哦~"), resourceImage);
        group.sendAsync(messages);

    }

    @Scheduled(cron = "0 30 8  * * ?")
    public void goodMorning1() {
        BotManagers botManagers = application.getBotManagers();
        // 如果只有一种bot，也可以考虑使用 botManagers.get(0).all().get(0);
        // 这样的话注意处理异常情况
        String s = "起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！不起床的都会被萝莉猫耳淫纹中等胸部情趣内衣扶她小魅魔上门榨精！起床！";
        Bot o = botManagers.get(0).all().get(0);
        Group group = o.getGroup(ID.$(740994565));
        PathResource resource = Resource.of(Paths.get("E:\\img\\atri\\起床.jpg"));
        Image<?> resourceImage = Image.of(resource);
        Messages messages = Messages.toMessages(Text.of(s), resourceImage);
        group.sendAsync(messages);

    }

    @Scheduled(cron = "0 20 17  * * ?")
    public void goodMorning2() {
        BotManagers botManagers = application.getBotManagers();
        Bot o = botManagers.get(0).all().get(0);
        Group group = o.getGroup(ID.$(740994565));
        PathResource resource = Resource.of(Paths.get("E:\\img\\atri\\睡觉.jpg"));
        Image<?> resourceImage = Image.of(resource);
        Messages messages = Messages.toMessages(Text.of("哦呀斯密"), resourceImage);
        group.sendAsync(messages);

    }

    @Listener
    @Filter(value = "亚托莉口我", targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void kowo(GroupMessageEvent event) {
        Random r = new Random();
        int i = r.nextInt(4);
        event.getGroup().sendAsync("?");
        event.getAuthor().muteAsync(i + 1, TimeUnit.MINUTES);
    }

    @Listener
    @Filter(value = "亚托莉亲亲", targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void kow2(GroupMessageEvent event) throws IOException {
        Random r = new Random();
        int i = r.nextInt(6);
        System.out.println(i);
        if (i == 1 || i == 0) {
            String a = "好…好吧，不要让别人看到哦……";
            event.getGroup().sendAsync(a);
        } else if (i == 2) {
            String a = "啾……";
            event.getGroup().sendAsync(a);
        } else if (i == 3) {
            String a = "火箭拳-------！！！";
            Path path = Paths.get("E:\\img\\atri\\铁拳.jpg");
            byte[] bytes = Files.readAllBytes(path);
            ByteArrayResource resource = Resource.of(bytes, path.toString());
            Image<?> image = Image.of(resource);
            Messages messages = Messages.toMessages(Text.of(a), image);
            event.getGroup().sendAsync(messages);
            event.getAuthor().muteAsync(1, TimeUnit.MINUTES);
        } else if (i == 4) {
            String a = "对机器人是犯法的哦~";
            event.getGroup().sendAsync(a);
        } else {
            String a = "呜哇";
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
//        return EventResult.truncate();
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

    @Listener//戳一戳会回复一个戳一戳
    @Filter(targets = @Filter.Targets(groups = {"740994565", "494050282"}))
    public void nudge1(MiraiGroupNudgeEvent event) {
        System.out.println(event.getAuthor());
        MiraiNudge nudge = new MiraiNudge(event.getAuthor().getId());
        event.getGroup().sendAsync(nudge);
    }


}