package cn.cheng.hot.news.timer;

import cn.cheng.hot.news.config.Global;
import cn.cheng.hot.news.pojo.AgentIp;
import cn.cheng.hot.news.reptiles.BaiDuReptiles;
import cn.cheng.hot.news.reptiles.DouYinReptiles;
import cn.cheng.hot.news.reptiles.WeiBoReptiles;
import cn.cheng.hot.news.reptiles.ZhiHuReptiles;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cheng Mingwei
 * @create 2020-08-24 21:50
 **/
@EnableScheduling
@Component
public class ReptilesTimer {

    @Resource
    private WeiBoReptiles weiBoReptiles;
    @Resource
    private ZhiHuReptiles zhiHuReptiles;
    @Resource
    private BaiDuReptiles baiDuReptiles;
    @Resource
    private DouYinReptiles douYinReptiles;


    @Scheduled(fixedDelay = 60_000)
    public void weiBoHot() {
        Spider.create(weiBoReptiles)
                .addUrl("https://s.weibo.com/top/summary")
                .addPipeline(new ConsolePipeline())
                .run();
    }

    @Scheduled(fixedDelay = 60_000)
    public void baiDuHot() {
        Spider.create(baiDuReptiles)
                .addUrl("http://top.baidu.com/buzz?b=1&fr=topnews")
                .addPipeline(new ConsolePipeline())
                .run();
    }

    @Scheduled(fixedDelay = 60_000)
    public void zhiHuHot() {
        zhiHuReptiles.getHot();
    }

    @Scheduled(fixedDelay = 60_000)
    public void douYinHot() {
        douYinReptiles.getHot();
    }

    //    @Scheduled(fixedDelay = 60_000)
    public void getAgentIp() {
        Spider.create(new PageProcessor() {
            private final Site site = Site.me().setRetryTimes(3).setSleepTime(100);

            @Override
            public void process(Page page) {
                Element element = page.getHtml().getDocument().body();
                Elements table = element.getElementsByTag("table");
                Elements trs = table.get(0).getElementsByTag("tr");
                List<AgentIp> agentIps = new ArrayList<>();
                for (int i = 1; i < trs.size(); i++) {
                    Element tr = trs.get(i);
                    Elements td = tr.getElementsByTag("td");
                    String ip = td.get(0).text();
                    String port = td.get(1).text();
                    AgentIp agentIp = new AgentIp(ip, Integer.parseInt(port));
                    agentIps.add(agentIp);
                }
                Global.agentIps = agentIps;
            }

            @Override
            public Site getSite() {
                return site;
            }
        }).addUrl("https://ip.jiangxianli.com/?page=1&country=%E4%B8%AD%E5%9B%BD")
                .addPipeline(new ConsolePipeline())
                .run();
    }

}
