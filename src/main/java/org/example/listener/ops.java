package org.example.listener;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.ByteArrayResource;
import love.forte.simbot.resources.Resource;
import org.example.until.until;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ops {
    public static String[] code = {"op", "烧鸡", "原神"};

    @Listener
    @Filter(targets = @Filter.Targets(groups = {"740994565","494050282"}))
    public void group(@RequestParam(required = false,defaultValue = "0",value ="date") GroupMessageEvent event) {
        String plainText = event.getMessageContent().getPlainText();
        if (plainText.contains("亚托莉")){

        }else {
            for (String c : code) {
                if (plainText.contains(c)) {
                    Random rand = new Random();
                    int i = rand.nextInt(50);
                    if (i == 25) {
                        ID authorId = event.getAuthor().getId();
                        At at = new At(authorId);
                        Messages messages = Messages.toMessages(at, Text.of(" 骂原神是吧？"));
                        event.getGroup().sendBlocking(messages);
                        event.getAuthor().muteAsync(3, TimeUnit.MINUTES);
                    } else {
                        ReceivedMessageContent messageContent = event.getMessageContent();
                        ID authorId = event.getAuthor().getId();
                        At at = new At(authorId);
                        Messages messages = Messages.toMessages(at, Text.of(" 原神怎么你了？"));
                        event.getBot().delay(Duration.ofSeconds(2),()->{
                            event.getGroup().sendBlocking(messages);
                        });

                    }
                    break;
                }
            }
        }
    }

    //关键字匹配
    private static boolean isContain(String source, String subItem) {
        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    @NotNull
    private static MessagesBuilder getSeTu(String s) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(s)
                .openConnection();
        conn.setInstanceFollowRedirects(false);//禁止重定向
        conn.setConnectTimeout(5000);
        String headerField = conn.getHeaderField(1);
        System.out.println(headerField);
        //重定向乱码
        String location = new String(conn.getHeaderField("location").getBytes("ISO-8859-1"), "UTF-8");

        String redirectUrl = "https://api.r10086.com" + location;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(redirectUrl, HttpMethod.GET, null, byte[].class);
        byte[] body = responseEntity.getBody();
        FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\img\\op\\2.webp"));
        fileOutputStream.write(body);
        fileOutputStream.close();
        until.webpToPng("E:\\img\\op\\2.webp", "E:\\img\\op\\3.png");//调用格式转换工具
        Path path = Paths.get("E:\\img\\op\\3.png");
        byte[] bytes = Files.readAllBytes(path);
        ByteArrayResource resource = Resource.of(bytes, path.toString());
        Image<?> image = Image.of(resource);
        MessagesBuilder builder = new MessagesBuilder();
        MessagesBuilder append = builder.append(image);

        return append;
    }
}
