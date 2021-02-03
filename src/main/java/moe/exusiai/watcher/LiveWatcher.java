package moe.exusiai.watcher;

import com.alibaba.fastjson.JSONObject;
import moe.exusiai.Rosmontis;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LiveWatcher {
    public static Integer lastlivestatus = 0;
    public static void LiveWatcher() {
        String url = "https://api.bilibili.com/x/space/acc/info?mid=38829";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(body);
            Integer liveStatus = jsonObject.getJSONObject("data").getJSONObject("live_room").getInteger("liveStatus");
            if (liveStatus == 1 && lastlivestatus == 0) {
                String liveurl = jsonObject.getJSONObject("data").getJSONObject("live_room").getString("url");
                lastlivestatus = 1;
                Rosmontis.updateLive(liveurl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
