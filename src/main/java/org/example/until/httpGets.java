package org.example.until;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class httpGets {

    public static JSONObject getParamMap(String url, Map<String, String> paramMap) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            for(Map.Entry<String,String> entry:paramMap.entrySet()){
                pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
            CloseableHttpResponse response;
            URIBuilder builder = new URIBuilder(url).setParameters(pairs);
            // 执行get请求.
            HttpGet httpGet = new HttpGet(builder.build());
            response = httpClient.execute(httpGet);
            if(response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String jsonString= EntityUtils.toString(entity);
                JSONObject jsonObject=new JSONObject(Boolean.parseBoolean(jsonString));
                System.out.println("get请求数据成功！\n"+jsonObject);
                return jsonObject;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
