//package org.example.listener;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import love.forte.simboot.annotation.Filter;
//import love.forte.simboot.annotation.Listener;
//import love.forte.simbot.ID;
//import love.forte.simbot.definition.GroupMember;
//import love.forte.simbot.event.GroupMessageEvent;
//import love.forte.simbot.message.Messages;
//import love.forte.simbot.message.MessagesBuilder;
//import love.forte.simbot.message.Text;
//import love.forte.simbot.resources.Resource;
//import love.forte.simbot.utils.item.Items;
//import org.example.untils.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.*;
//
//@Component
//public class ququnyou {
//    @Autowired
//    private RedisService redisService;
////后去除了自己外所有群成员,放进集合，存进redis，随机取出一个和另外一个一起放进另一个集合中，存在时间为12点之前，删掉原先集合中的这两个。配对完成~
////随机娶一个群员，被娶的可以换老婆，第一个娶的人变成单身狗，每人只能娶一次，
//    @Listener
//    @Filter("列表")
//    public void listener(GroupMessageEvent event) throws MalformedURLException {
//        Items<GroupMember> members = event.getGroup().getMembers();
//        List<GroupMember> groupMembers = members.collectToList();//获取群成员列表
//        int size = groupMembers.size();
//        Map<String,String> map = new HashMap();
//        String username = event.getBot().getUsername();
//        String avatar1 = event.getBot().getAvatar();
//        map.put("avatar",avatar1);//头像，
//        map.put("name",username);//昵称  //本人存进去
//        redisService.lpush("列表",map);
//        for (int i=0;i<size;i++){
//            String avatar = groupMembers.get(i).getAvatar();
//            String nickOrUsername = groupMembers.get(i).getNickOrUsername();
//            map.put("avatar",avatar);//头像，
//            map.put("name",nickOrUsername);//昵称
//           redisService.lpush("列表",map);//所有成员存进去
//        }
//    }
//    //随机娶一个
//    public Integer ran(long size){
//        Random random = new Random();
//        int i = random.nextInt((int) size);
//        return i;
//    }
//
//    @Listener
//    @Filter("jiehun")
//    public void kekonn(GroupMessageEvent event) throws MalformedURLException {
//
////        Map<String, String> bij = bij(nickOrUsername1);
////
////        if (bij==null){
////            Integer ran = ran( redisService.llen("列表"));//根据群员数量随机取一个下标值
////            Object usermap = redisService.lindex("列表", ran);//获取一个成员信息，就娶你了！
////            ququnyou(event, usermap);
////        }else {
////          ququnyou(event,bij);
////        }
//
//    }
//
//    private void ququnyou(GroupMessageEvent event,  Object usermap) throws MalformedURLException {
//        ID authorId = event.getAuthor().getId();//发消息人id
//        String nickOrUsername1 = event.getAuthor().getNickOrUsername();//发消息昵称
//        Map<String, Object> mapinfor = new ObjectMapper().convertValue(usermap, Map.class);//转为map
//        MessagesBuilder messagesBuilder = new MessagesBuilder();
//        MessagesBuilder image = messagesBuilder.at(authorId).text("今天你的老婆是").image(Resource.of
//                (new URL((String) mapinfor.get("avatar")))).text(mapinfor.get("name")+"哒！");
//        redisService.lrem("列表", usermap);//删除被娶的人，不会在被抽到
//        Map<String,String> map1 = new HashMap();
//        String yihun1 = nickOrUsername1;
//        String yihun2 = (String) mapinfor.get("name");
//        map1.put(yihun1,yihun2);
//        redisService.lpush("已婚列表",map1);//存进已婚列表
//        event.getGroup().sendAsync(image.build());
//    }
//
//    public Map<String, String> bij(String username){
//        List<Object> lieb = redisService.lrange("列表", 0, -1);
//        Map<String, String> mapinfor = new HashMap<>();
//        for (Object o:lieb) {
//            mapinfor = new ObjectMapper().convertValue(o, Map.class);//转为map
//            if (username.equals(mapinfor.get("name"))){
//                return mapinfor;
//            }
//        }
//        return null;
//    }
//
//    @Listener
//    @Filter("已婚列表")
//    public void yihun(GroupMessageEvent event){
//        Map<String, String> mapinfor = new HashMap<>();
//        List<Object> yihun = redisService.lrange("已婚列表", 0, -1);
//        List<String> list = new ArrayList<>();
//        for (Object o:yihun) {
//         mapinfor = new ObjectMapper().convertValue(o, Map.class);//转为map
//            Iterator<String> iterator = mapinfor.keySet().iterator();
//                String next = iterator.next();//key
//                String s = mapinfor.get(next);//value
//                list.add(next+"<==>"+s);
//        }
//        System.out.println(list);
//        Messages messages = Messages.toMessages(Text.of("老婆列表:"+list.toString()));
//        event.getGroup().sendAsync(messages);
//    }
//}
