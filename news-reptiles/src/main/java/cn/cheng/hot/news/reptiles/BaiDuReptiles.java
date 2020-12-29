package cn.cheng.hot.news.reptiles;

import cn.cheng.hot.news.handle.SendHandler;
import cn.cheng.hot.news.pojo.CommonHotBody;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cheng Mingwei
 * @create 2020-08-27 9:52
 **/
@Component
@Log4j2
public class BaiDuReptiles extends ReptilesParent implements PageProcessor {

    public BaiDuReptiles(SendHandler sendHandler) {
        super(sendHandler);
    }

    @Override
    public void process(Page page) {
        Element body = page.getHtml().getDocument().body();
        Element table = body.getElementsByTag("table").get(0);
        Elements trs = table.getElementsByTag("tr");
        List<CommonHotBody> commonHotBodyList = new ArrayList<>();
        for (int i = 1; i < trs.size(); i = i + 2) {
            Element element = trs.get(i);
            Element keyword = element.getElementsByClass("keyword").get(0);
            Element a = keyword.getElementsByTag("a").get(0);
            String text = a.text();
            String href = a.attr("href");
            Element last = element.getElementsByClass("last").get(0);
            Element span = last.getElementsByTag("span").get(0);
            String hot = span.text();

            Spider spider = new Spider(new PageProcessor() {
                @Override
                public void process(Page page) {
                    Elements contentElements = page.getHtml().getDocument().body().getElementsByClass("op-timeliness-abs");
                    if (contentElements != null && contentElements.size() > 0) {
                        Element contentElement = contentElements.get(0);
                        String content = contentElement.text();
                        page.putField("content", content);
                    }

                    Elements imgElements = page.getHtml().getDocument().body().getElementsByClass("c-img op-timeliness-img");
                    if (imgElements != null && imgElements.size() > 0) {
                        Element imgElement = imgElements.get(0);
                        String img = imgElement.attr("src");
                        page.putField("img", img);
                    }
                }

                @Override
                public Site getSite() {
                    return site;
                }
            });
            putToList(commonHotBodyList, text, href, hot, spider);
        }
        putAndSend(commonHotBodyList, "BaiDuHot");
    }

    @Override
    public Site getSite() {
        return site;
    }
}
