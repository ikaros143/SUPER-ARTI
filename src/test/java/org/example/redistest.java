package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.messages;
import org.example.untils.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@TestComponent
public class redistest {
 @Autowired
 private static RedisService redisService;

    public static void main(String[] args) {
//        String s1 = " num:2,uid:2,tag:3";
//        String s2="num:1,tag:萝莉|哈哈&白色&泳装";
//        String str2 = s2.replaceAll(" ", "");
//        String[] strings = str2.split("\\,");
////        System.out.println("1"+Arrays.toString(strings));
//        Map<String, List<String>> map = new HashMap<>();
//        // 循环加入map集合
//        for (String string : strings) {
//            List<String> list = new ArrayList<>();
//            // 截取一组字符串
//            String[] ss = string.split(":");
////            System.out.println("2"+Arrays.toString(ss));
//            if (ss[0].equals("num")) {
//                if (Integer.valueOf((ss[1])) >= 5) {
//                    list.add("5");
//                    map.put(ss[0], list);
//                }else {
//                    list.add(ss[1]);
//                    map.put(ss[0],list);
//                }
//            } else if (ss[0].equals("uid")) {
//                list.add(ss[1]);
//                map.put("author_uuid", list);
//            } else if (ss[0].equals("key")) {
//                list.add(ss[1]);
//                map.put("keyword", list);
//            } else if (ss[0].equals("tag")) {
//                String[] ss1 = string.split("&");
//                for (int i =0;i<ss1.length;i++){
//                    if (i==0){
//                        String substring = ss1[i].substring(4);
//                        list.add(substring);
//                    }else {
//                        list.add(ss1[i]);
//                    }
//                }
//                map.put(ss[0], list);
//            }
//        }
//        System.out.println("tag"+map);
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add(map);
//        System.out.println(jsonArray.toJSONString());
        long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);

    }
//https://sex.nyan.xyz/img-original/img/2023/01/18/02/59/25/104606654_p0.jpg
    public static byte[] bij(String url){
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange
                    (url, HttpMethod.GET, null, byte[].class);//根据链接获取图片本体
            byte[] body = responseEntity.getBody();
            return body;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("404");
        }

return null;


    }



}
