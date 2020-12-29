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
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Cheng Mingwei
 * @create 2020-08-24 18:48
 **/
@Component
@Log4j2
public class WeiBoReptiles extends ReptilesParent implements PageProcessor {

    public WeiBoReptiles(SendHandler sendHandler) {
        super(sendHandler);
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Elements data = html.getDocument().getElementsByClass("data");
        Element table = data.get(0);
        Elements tbody = table.getElementsByTag("tbody");
        Elements children = tbody.get(0).children();
        List<CommonHotBody> commonHotBodyList = new ArrayList<>();
        for (int i = 1; i < Math.min(children.size(), 10); i++) {
            Element tr = children.get(i);
            Elements td03 = tr.getElementsByClass("td-03");
            if (td03 != null && td03.size() > 0) {
                String flag = td03.get(0).getElementsByTag("i").text();
                if ("荐".equals(flag)) {
                    continue;
                }
            }
            Elements td = tr.getElementsByClass("td-02");
            Element tdChild = td.get(0);
            Elements a = tdChild.getElementsByTag("a");
            Elements span = tdChild.getElementsByTag("span");
            String href = "https://s.weibo.com" + a.get(0).attr("href");
            String text = a.get(0).text();
            String hot = span.get(0).text();
            log.info("地址：{}\t信息：{}\t热度：{}", href, text, hot);
            Spider spider = new Spider(new PageProcessor() {
                @Override
                public void process(Page page) {
                    Element body = page.getHtml().getDocument().body();
                    Elements spic = body.getElementsByClass("spic");
                    if (spic != null && spic.size() > 0) {
                        String img = spic.get(0).attr("src");
                        page.putField("img", img == null ? null : (img.contains("http") ? img : ("https:" + img)));
                    } else {
                        spic = body.getElementsByClass("pic");
                        if (spic.size() > 0) {
                            String img = spic.get(0).getElementsByTag("img").get(0).attr("src");
                            page.putField("img", img == null ? null : (img.contains("http") ? img : ("https:" + img)));
                        }
                    }
                    Elements div = body.getElementsByClass("card card-topic-lead s-pg16");
                    if (div != null && div.size() > 0) {
                        Elements p = div.get(0).getElementsByTag("p");
                        String content = p.get(0).text();
                        page.putField("content", content);
                    }
                }

                @Override
                public Site getSite() {
                    return site.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
                }
            });
            putToList(commonHotBodyList, text, href, hot, spider);
        }

        putAndSend(commonHotBodyList, "WeiBoHot");
    }


}
