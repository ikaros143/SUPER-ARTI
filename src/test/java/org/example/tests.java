package org.example;

import jakarta.annotation.Resource;
import love.forte.simbot.Identifies;
import love.forte.simbot.application.Application;
import love.forte.simbot.application.BotManagers;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.component.mirai.MiraiFriend;
import love.forte.simbot.component.mirai.MiraiGroup;
import love.forte.simbot.component.mirai.bot.MiraiBot;
import love.forte.simbot.component.mirai.bot.MiraiBotManager;
import org.example.untils.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class tests {

    public static void main(String[] args) throws Exception {

//        String redirectUrl ="https://api.r10086.com"+getRedirectUrl();
//        System.out.println(redirectUrl);
//        RestTemplate restTemplate=new RestTemplate();
//        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(redirectUrl, HttpMethod.GET, null, byte[].class);
//        byte[] body = responseEntity.getBody();
//        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\1.jpg"));
//        fileOutputStream.write(body);
//        fileOutputStream.close();
        RestTemplate restTemplate = new RestTemplate();


    }
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Test
    public  void aa(){
        redisTemplate.opsForValue().set("1","2");

    }

    @Autowired
    private Application application;
    @Test
    public void test1(){
        BotManagers botManagers = application.getBotManagers();
        for (BotManager<?> manager : botManagers) {
            if (manager instanceof MiraiBotManager miraiBotManager) {
                MiraiBot bot = miraiBotManager.get(Identifies.ID("3502568092"));
                // 拿到bot，怎么操作看你心情，比如往某个群发消息
                assert bot != null;
                MiraiFriend friend = bot.getFriend(Identifies.ID(1321604543));
                assert friend != null;
                friend.sendAsync("主动发送");
                MiraiGroup group = bot.getGroup(Identifies.ID(494050282));
                assert group != null;
                group.sendAsync("主动发送");
                break;
            }
        }
    }

    public static String getRedirectUrl() throws Exception {

        HttpURLConnection conn = (HttpURLConnection) new URL("https://api.r10086.com/img-api.php?zsy=原神")

                .openConnection();

        conn.setInstanceFollowRedirects(false);

        conn.setConnectTimeout(5000);

           //重定向乱码
        return new String(conn.getHeaderField("Location").getBytes("ISO-8859-1"), "UTF-8");

    }

}


