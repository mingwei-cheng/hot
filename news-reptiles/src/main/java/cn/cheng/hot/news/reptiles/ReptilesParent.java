package cn.cheng.hot.news.reptiles;

import cn.cheng.hot.news.handle.SendHandler;
import cn.cheng.hot.news.pojo.CommonHotBody;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.util.Collections;
import java.util.List;

/**
 * @author Cheng Mingwei
 * @create 2020-08-27 16:35
 **/
public class ReptilesParent {

    final Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    private final SendHandler sendHandler;

    public ReptilesParent(SendHandler sendHandler) {
        this.sendHandler = sendHandler;
    }

    void putToList(List<CommonHotBody> commonHotBodyList, String text, String href, String hot, Spider spider) {
        ResultItems o = spider.get(href);
        //图片地址
        String img = null;
        String content = null;
        if (o != null) {
            content = o.get("content");
            img = o.get("img");
        }
        if (content != null) {
            commonHotBodyList.add(new CommonHotBody(content, href, text, hot, img));
        }
    }

    void putAndSend(List<CommonHotBody> list, String cmd) {
        Collections.reverse(list);
        JSONObject data = new JSONObject();
        data.put("cmd", cmd);
        data.put("data", list);
        sendHandler.consumer.accept(data);
    }
}
