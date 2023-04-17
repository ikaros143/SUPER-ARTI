package org.example.listener;

import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.MiraiGroup;
import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent;
import love.forte.simbot.component.mirai.message.MiraiQuoteReply;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.event.*;
import love.forte.simbot.message.At;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.Text;
import net.mamoe.mirai.message.data.MessageSource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class group {

    @Listener  //新人加群1
    public void IncreaseEvent(GroupMemberIncreaseEvent event) {
        ID id = event.getAfter().getId();
        At at = new At(id);
//        System.out.println(event.getAfter().getId());
        Messages messages = Messages.toMessages(Text.of("欢迎加入银趴"), at);
        event.getGroup().sendAsync(messages);
    }

    @Listener//有人退群
    public void DecreaseEvent(GroupMemberDecreaseEvent event) {
        GroupMember before = event.getBefore();
        String nickOrUsername = before.getNickOrUsername();
        event.getGroup().sendBlocking(nickOrUsername + "离开了银趴");
    }

    @Listener
    @Filter(value = "给我退群", targets = @Filter.Targets(atBot = true))//tuiqun
    @ContentTrim
    public void delete(GroupMessageEvent event, ContinuousSessionContext context) {
        Group group = event.getGroup();
        event.replyBlocking("了解");
        MiraiGroup group1 = (MiraiGroup) group;
        group1.deleteBlocking();
    }

    @Listener
    @Filter(value = "你给我退群", targets = @Filter.Targets(atBot = true))
    @ContentTrim
    public EventResult listen(GroupMessageEvent groupMessageEvent, ContinuousSessionContext sessionContext) {
        var group = groupMessageEvent.getGroup();
        var author = groupMessageEvent.getAuthor();
        var groupId = group.getId();
        var authorId = author.getId();

//        var at = new At(authorId);
//        System.out.println(authorId.equals(ID.$(1321604543)));
        if (!authorId.equals(ID.$(1321604543))){
            groupMessageEvent.replyBlocking("除了主人谁也不能让咱退群哦~");
            groupMessageEvent.getGroup().sendAsync("杂鱼~");
            author.muteBlocking(1,TimeUnit.MINUTES);
            return EventResult.truncate();
        }
        group.sendBlocking(Messages.toMessages(Text.of(" 主人不要我了吗？")));
        while (true) {
         var nextMessage = sessionContext.waitingForNextMessage(MiraiGroupMessageEvent.Key, Duration.ofMinutes(2), (context, event) -> {
             // 判断下一个与当前事件的群、发送人相同的消息事件
             return event.getGroup().getId().equals(groupId) && event.getAuthor().getId().equals(authorId);
         });
         var newNick = nextMessage.getPlainText().trim();
//            System.out.println(newNick);
         if (newNick.equals("是的")) {
             group.getBot()
                     .delay(Duration.ofSeconds(3), () -> {
                         // 延迟 「3s」, 然后发送消息.
                      group.sendBlocking("......");
                     }).delay(5000, () -> {
                         // 再延迟「3000ms」, 输出日志
                      group.sendBlocking("请在1分钟内输入确认");
                      author.muteAsync(5, TimeUnit.HOURS);
                     }).delay(5000,()->{
                         group.sendBlocking("我才不要退群呢，笨蛋！");
                     });
             break;
         } else {
             group.sendBlocking("笨蛋！不要吓我啊...");
             break;
         }
        }
        return EventResult.truncate();
    }

    @Listener
    @Filter(value = "撤回喵", targets = @Filter.Targets(atBot = true))
    @ContentTrim
    public void deletequny(GroupMessageEvent event) {
        // 得到 '消息引用'
            MiraiQuoteReply firstOrNull = event.getMessageContent().getMessages().getFirstOrNull(MiraiQuoteReply.Key);
        if (firstOrNull == null) {
            event.getGroup().sendBlocking("？");
            return;
        }
        MessageSource source = firstOrNull.getOriginalMiraiMessage().getSource();
        MessageSource.recall(source);
    }
    @Listener
    @Filter("禁言他")
    @ContentTrim
    public void mute(GroupMessageEvent event){
        At firstOrNull = event.getMessageContent().getMessages().getFirstOrNull(At.Key);//获取艾特的那个人
        ID id = firstOrNull.getTarget();
        GroupMember member = event.getGroup().getMember(id);
        member.muteAsync(10,TimeUnit.MINUTES);
        event.getGroup().sendAsync("了解!");
    }
    @Filter("睡觉套餐")
    @Listener
    public void speel(GroupMessageEvent event, ContinuousSessionContext sessionContext){
        event.getGroup().sendAsync("主人想睡多久？亚托莉帮你哦~");
        var group = event.getGroup();
        var author = event.getAuthor();
        var groupId = group.getId();
        var authorId = author.getId();
        while (true) {
            var nextMessage = sessionContext.waitingForNextMessage(MiraiGroupMessageEvent.Key, Duration.ofMinutes(1), (context, events) -> {
                // 判断下一个与当前事件的群、发送人相同的消息事件
                return events.getGroup().getId().equals(groupId) && events.getAuthor().getId().equals(authorId);
            });
            var newNick = nextMessage.getPlainText().trim();
            Integer result = getString(newNick);
            if(result==0){
                group.sendAsync("请好好的说出自己想睡多久哦~");
            }else if (result>10){
                group.sendAsync("猪都睡不了那么久");
            }else {
                author.muteAsync(result,TimeUnit.HOURS);
                break;
            }

        }
    }

    @NotNull //提取字符串中数字
    private static Integer getString(String newNick) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(newNick);
        String result = m.replaceAll("").trim();
        if (result.isEmpty()){
            return 0;
        }
        return Integer.valueOf(result);
    }

}
