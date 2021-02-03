package moe.exusiai.watcher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import moe.exusiai.Rosmontis;
import moe.exusiai.utils.Config;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;

public class DynamicWatcher {
    public static void DynamicWatcher() {
        String url = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?&host_uid=38829&offset_dynamic_id=0&need_top=0";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();

            JSONObject json = JSONObject.parseObject(body);
            JSONArray cards = json.getJSONObject("data").getJSONArray("cards");
            String dynamicid = cards.getJSONObject(0).getJSONObject("desc").getString("dynamic_id_str");
//            System.out.println(Config.lastdynamicid);
//            System.out.println(dynamicid);
            if (!dynamicid.equals(Config.lastdynamicid)) {
                Config.lastdynamicid = dynamicid;
                Config.saveConfig();
                Integer dynamictype = cards.getJSONObject(0).getJSONObject("desc").getInteger("type");
                //转发型动态
                if (dynamictype == 1) {
                    String dynamiccontent = JSONObject.parseObject(cards.getJSONObject(0).getString("card")).getJSONObject("item").getString("content");
                    Long origindynamicid = cards.getJSONObject(0).getJSONObject("desc").getLong("orig_dy_id_str");
                    dynamiccontent += "\n 原动态: https://t.bilibili.com/" + String.valueOf(origindynamicid);
                    Rosmontis.updateDynamic(dynamiccontent, dynamicid);
                }
                //纯动态，包含图片
                else if (dynamictype == 2) {
                    String dynamiccontent = JSONObject.parseObject(cards.getJSONObject(0).getString("card")).getJSONObject("item").getString("description");
                    JSONArray dynamicpicturearray = JSONObject.parseObject(cards.getJSONObject(0).getString("card")).getJSONObject("item").getJSONArray("pictures");
                    for (int i = 0; i < dynamicpicturearray.size(); i++) {
                        String imageurl = dynamicpicturearray.getJSONObject(i).getString("img_src");
                        System.out.println(imageurl);
                        OkHttpClient imageclient = new OkHttpClient();
                        Request imagerequest = new Request.Builder()
                                .url(imageurl)
                                .build();
                        try {
                            Response imageresponse = imageclient.newCall(imagerequest).execute();
                            File imagefilefolder = new File("./" + dynamicid + "/");
                            imagefilefolder.mkdirs();
                            File imagefile = new File("./" + dynamicid + "/" + i + imageurl.substring(imageurl.length() -4, imageurl.length()));
                            FileOutputStream fileOutputStream = new FileOutputStream(imagefile);
                            fileOutputStream.write(imageresponse.body().bytes());
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    Rosmontis.updateDynamic(dynamiccontent,dynamicid);
                }
                //视频投稿型动态
                else if (dynamictype == 8) {
                    String dynamiccontent = JSONObject.parseObject(cards.getJSONObject(0).getString("card")).getString("dynamic");
                    String imageurl = JSONObject.parseObject(cards.getJSONObject(0).getString("card")).getString("pic");
                    String link = JSONObject.parseObject(cards.getJSONObject(0).getString("card")).getString("short_link");

                    OkHttpClient imageclient = new OkHttpClient();
                    Request imagerequest = new Request.Builder()
                            .url(imageurl)
                            .build();
                    try {
                        Response imageresponse = imageclient.newCall(imagerequest).execute();
                        File imagefilefolder = new File("./" + dynamicid + "/");
                        imagefilefolder.mkdirs();
                        File imagefile = new File("./" + dynamicid + "/1" + imageurl.substring(imageurl.length() -4, imageurl.length()));
                        FileOutputStream fileOutputStream = new FileOutputStream(imagefile);
                        fileOutputStream.write(imageresponse.body().bytes());
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Rosmontis.updateDynamic(dynamiccontent + "\n" + link, dynamicid);
                }

                //String dynamiccontent = JSONObject.parseObject(cards.getJSONObject(0).getString("card")).getJSONObject("item").getString("description");
                //System.out.println(dynamiccontent);
            }
            //return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
