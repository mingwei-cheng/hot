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
public class DouYinReptiles extends ReptilesParent {

    public DouYinReptiles(SendHandler sendHandler) {
        super(sendHandler);
    }

    public void getHot() {
        RestTemplate restTemplate = new RestTemplate();
        String re = restTemplate.getForObject("https://www.iesdouyin.com/web/api/v2/hotsearch/billboard/aweme/", String.class);
        JSONObject jsonObject = JSONObject.parseObject(re);
        JSONArray dataList = jsonObject.getJSONArray("aweme_list");
        List<CommonHotBody> commonHotBodyList = new ArrayList<>();

        for (Object dataObj : dataList) {
            JSONObject data = JSONObject.parseObject(dataObj.toString());
            JSONObject info = data.getJSONObject("aweme_info");

            String title = info.getString("desc");

            String url = info.getString("share_url");
            String hot = data.getString("hot_value");
            String content = "热度：" + hot;
            String img = info.getJSONObject("video").getJSONObject("cover").getJSONArray("url_list").getString(0);

            commonHotBodyList.add(new CommonHotBody(content, url, title, hot, img));

        }
        putAndSend(commonHotBodyList, "DouYinHot");
    }

}
