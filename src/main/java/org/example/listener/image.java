package org.example.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;
import love.forte.simbot.resources.PathResource;
import love.forte.simbot.resources.Resource;
import org.apache.http.client.utils.URIBuilder;
import org.example.untils.RedisService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class image {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RedisService redisService;

    @Listener //模糊搜索图片
    @Filter(targets = @Filter.Targets(groups = {"740994565", "494050282"}, atBot = true))
    public synchronized EventResult group5(GroupMessageEvent event) {
        String plainText = event.getMessageContent().getPlainText();//获取群中发送的文本
        Map<String, String> map = map(plainText);//将文字转为map格式
      while (!map.isEmpty()){
          if (redisTemplate.hasKey("1")) { //判断是否还在cd中
              System.out.println(redisTemplate.hasKey("1") + "haskey");
              Long expire = redisTemplate.getExpire("1", TimeUnit.SECONDS);
              System.out.println(expire);
              event.replyBlocking("查询cd中，还有" + expire + "秒");
          } else {
              redisTemplate.opsForValue().set("1", 1, 15, TimeUnit.SECONDS);//一定时间内请求次数限制，5秒
              String b = "https://sex.nyan.xyz/api/v2/?";
              String urlstring = urlstring(plainText);
              String s = doGet(b+urlstring, null);//把链接和map参数发送请求
              JSONObject jsonObject = JSON.parseObject(s);
              JSONArray honors = jsonObject.getJSONArray("data");
              if (honors == null) {
                  event.getGroup().sendAsync("未查到喵~");
              } else {
                  for (int i = 0; i < honors.size(); i++) {
                      JSONObject honor = (JSONObject) honors.get(i);
                      String aa = honor.getString("url");
                      String author_uid = honor.getString("author_uid");
                      System.out.println(aa);
                      ResourceImage resourceImage = httpGet(aa, i);
                      event.replyBlocking(aa);
                      Messages messages = Messages.toMessages(Text.of(author_uid), resourceImage);
                      event.getGroup().sendBlocking(messages);
                  }
              }
          }
          break;
      }
      if (map.isEmpty()){
          return null;
      }else {
          return EventResult.truncate();
      }
    }
//返回拼接的字符串
    public  String urlstring(String str) {
//        String str = "num:2,UID:2,tag:夏天&山";
        String str2 = str.replaceAll(" ", "");
        String[] split = str2.split(",");
        Map<String,List<String>> map = new HashMap<>();
//        System.out.println(Arrays.toString(split)); //[num:2, UID:2, tag:夏天&山]
        for (int j=0;j<split.length;j++){
            String[] split1 = split[j].split(":");//[num,2]...[tag,夏天&山]
            String sm="";
            List<String> l = new ArrayList<>();
            for (int i=0;i<split1.length;i++){
                if (i==0){
                    if(split1[0].equals("uid")){
                        sm="author_uuid";
                    }else {
                        sm =split1[0];
                    }
//                    map.put(split1[0],l);
//                    System.out.println("sm"+i+sm);
                }else {
                    if (split1[i].contains("&")){
                        String[] split2 = split1[i].split("&");
//                        System.out.println(Arrays.toString(split2)); //[tag:夏天, 山]
                        for (String s3:split2){
                            l.add(s3);
                        }
                    } else {
                        l.add(split1[i]);
                    }
                }
            }
            map.put(sm,l);
//            System.out.println(map);
        }
        Set<String> strings = map.keySet();
        String b = "";String c="";
        for (String s : strings){
            List<String> list = map.get(s);
            for (String a : list){
                b =b+s+"="+a+"&";
            }
        }
//        System.out.println(b.substring(0,b.length()-1));
        return b.substring(0,b.length()-1);
    }


    @NotNull
    public ResourceImage httpGet(String url, int i) {
        RestTemplate restTemplate = new RestTemplate();
        JSONObject template = new JSONObject();
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange
                    (url, HttpMethod.GET, null, byte[].class);//根据链接获取图片本体
            byte[] body = responseEntity.getBody();
            FileOutputStream fileOutputStream = new FileOutputStream(new File("E:\\img\\" + i + ".png"));
            fileOutputStream.write(body);//保存到本地
            fileOutputStream.close();
            Path path = Paths.get("E:\\img\\" + i + ".png");//上传到服务器
            PathResource of = Resource.of(path);
            ResourceImage of1 = Image.of(of);
            return of1;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        } catch (IOException s) {
            s.printStackTrace();
        }
        Path path = Paths.get("E:\\img\\" + 1 + ".png");//上传到服务器
        PathResource of = Resource.of(path);
        ResourceImage of1 = Image.of(of);
        return of1;

    }

    public static Map<String, String> map(String s1) {
//        String str = "num:2,UID作者:2,tag:3";
        String str2 = s1.replaceAll(" ", "");
//        System.out.println(str2);
        String[] strings = str2.split("\\,");
        Map<String, String> map = new HashMap<>();
        // 循环加入map集合
        for (String string : strings) {
            // 截取一组字符串
            String[] ss = string.split(":");
            if (ss[0].equals("num")) {
                if (Integer.valueOf((ss[1])) >= 5) {
                    map.put(ss[0], String.valueOf(5));
                } else {
                    map.put(ss[0], String.valueOf((ss[1])));
                }
            } else if (ss[0].equals("uid")) {
                map.put("author_uuid", String.valueOf((ss[1])));
            } else if (ss[0].equals("key")) {
                map.put("keyword", String.valueOf((ss[1])));
            } else if (ss[0].equals("tag")) {
                map.put(ss[0], String.valueOf((ss[1])));
            }
        }
//        System.out.println("map" + map);
        return map;
    }

    public static String doGet(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            System.out.println(uri);

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
//                System.out.println("resultString1:"+resultString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("resultString2:" + resultString);
        return resultString;
    }


}
