package org.example.listener;

import love.forte.simboot.annotation.ContentTrim;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.action.DeleteSupport;

import love.forte.simbot.component.mirai.message.MiraiQuoteReply;
import love.forte.simbot.definition.*;
import love.forte.simbot.event.*;
import love.forte.simbot.message.*;
import net.mamoe.mirai.message.data.MessageSource;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class group {

    @Listener  //新人加群
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

//    @Listener
//    @Filter(value = "给我退群喂", targets = @Filter.Targets(atBot = true))//tuiqun
//    @ContentTrim
//    public void delete(GroupMessageEvent event, ContinuousSessionContext context) {
//        Group group = event.getGroup();
//        event.replyBlocking("了解");
//        MiraiGroup group1 = (MiraiGroup) group;
//        group1.deleteBlocking();
//    }

//    @Listener
//    @Filter(value = "你给我退群", targets = @Filter.Targets(atBot = true))
//    @ContentTrim
//    public EventResult listen(GroupMessageEvent groupMessageEvent, ContinuousSessionContext sessionContext) {
//        var group = groupMessageEvent.getGroup();
//        var author = groupMessageEvent.getAuthor();
//        var groupId = group.getId();
//        var authorId = author.getId();
////        var at = new At(authorId);
////        System.out.println(authorId.equals(ID.$(1321604543)));
//        if (!authorId.equals(ID.$(1321604543))){
//            groupMessageEvent.replyBlocking("除了主人谁也不能让咱退群哦~");
//            groupMessageEvent.getGroup().sendAsync("杂鱼~");
//            author.muteBlocking(1,TimeUnit.MINUTES);
//            return EventResult.truncate();
//        }
//        group.sendBlocking(Messages.toMessages(Text.of(" 主人不要我了吗？")));
//        while (true) {
//            var nextMessage = sessionContext.waitingForNextMessage(MiraiGroupMessageEvent.Key, Duration.ofMinutes(2), (context, event) -> {
//                // 判断下一个与当前事件的群、发送人相同的消息事件
//                return event.getGroup().getId().equals(groupId) && event.getAuthor().getId().equals(authorId);
//            });
//            var newNick = nextMessage.getPlainText().trim();
//            System.out.println(newNick);
//            if (newNick.equals("是的")) {
//                group.getBot()
//                        .delay(Duration.ofSeconds(3), () -> {
//                            // 延迟 「3s」, 然后发送消息.
//                            group.sendBlocking("......");
//                        }).delay(5000, () -> {
//                            // 再延迟「3000ms」, 输出日志
//                            group.sendBlocking("请在1分钟内输入确认");
//                            author.muteAsync(5, TimeUnit.HOURS);
//                        }).delay(5000,()->{
//                            group.sendBlocking("我才不要退群呢，笨蛋！");
//                        });
//                break;
//            } else {
//                group.sendBlocking("笨蛋！不要吓我啊...");
//                break;
//            }
//        }
//        return EventResult.truncate();
//    }

    @Listener
    @Filter(value = "撤回", targets = @Filter.Targets(atBot = true))
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
    @Filter("禁言ta")
    @ContentTrim
    public void mute(GroupMessageEvent event){
        At firstOrNull = event.getMessageContent().getMessages().getFirstOrNull(At.Key);//获取艾特的那个人
        ID id = firstOrNull.getTarget();
        if (String.valueOf(id).equals("1321604543")){
            event.getAuthor().muteAsync(30,TimeUnit.SECONDS);
            event.getGroup().sendAsync("不可以哦");
            return ;
        }
        GroupMember member = event.getGroup().getMember(id);
        member.muteAsync(10,TimeUnit.MINUTES);
        event.getGroup().sendAsync("了解!");
    }
    @Listener
    @Filter("解除禁言")
    @ContentTrim
    public void unmute(GroupMessageEvent event){
        At firstOrNull = event.getMessageContent().getMessages().getFirstOrNull(At.Key);
        ID id = firstOrNull.getTarget();
        GroupMember member = event.getGroup().getMember(id);
        member.unmuteBlocking();
    }
    @Listener
    @Filter("踢他")
    @ContentTrim
   public void delGroup (GroupMessageEvent event){
       At firstOrNull = event.getMessageContent().getMessages().getFirstOrNull(At.Key);
       ID id = firstOrNull.getTarget();
       GroupMember member = event.getGroup().getMember(id);
        if (member instanceof DeleteSupport){
            ((DeleteSupport) member).deleteBlocking();
        }
   }

}
