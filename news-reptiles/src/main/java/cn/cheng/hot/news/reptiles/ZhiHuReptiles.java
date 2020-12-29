package cn.cheng.hot.news.reptiles;

import cn.cheng.hot.news.handle.SendHandler;
import cn.cheng.hot.news.pojo.CommonHotBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cheng Mingwei
 * @create 2020-08-26 16:42
 **/
@Component
public class ZhiHuReptiles extends ReptilesParent {

    public ZhiHuReptiles(SendHandler sendHandler) {
        super(sendHandler);
    }

    public void getHot() {
        RestTemplate restTemplate = new RestTemplate();
        String re = restTemplate.getForObject("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=20&desktop=true", String.class);
        JSONObject jsonObject = JSONObject.parseObject(re);
        JSONArray dataList = jsonObject.getJSONArray("data");
        List<CommonHotBody> commonHotBodyList = new ArrayList<>();

        for (Object dataObj : dataList) {
            JSONObject data = JSONObject.parseObject(dataObj.toString());
            JSONObject target = data.getJSONObject("target");
            String title = target.getString("title");
            String url = "https://zhihu.com/question/" + target.getString("id");
            String content = target.getString("excerpt");
            String hot = data.getString("detail_text");
            String[] s = hot.split(" ");
            if (hot.contains("万")) {
                hot = s[0] + "0000";
            } else {
                hot = s[0];
            }
            JSONArray children = data.getJSONArray("children");
            String img = children.getJSONObject(0).getString("thumbnail");
            commonHotBodyList.add(new CommonHotBody(content, url, title, hot, img));

        }
        putAndSend(commonHotBodyList, "ZhiHuHot");
    }

}
