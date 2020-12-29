package cn.cheng.hot.news.config;

import cn.cheng.hot.news.pojo.AgentIp;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 13:37
 **/
public class Global {

    /**
     * 代理ip库
     */
    public static List<AgentIp> agentIps = new ArrayList<>();

    public static HttpClientDownloader getHttpClientDownloader() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader
                .setProxyProvider(
                        new SimpleProxyProvider(Global.agentIps.stream()
                                .map(agentIp -> new Proxy(agentIp.getIp(), agentIp.getPort()))
                                .collect(Collectors.toList())));
        return httpClientDownloader;
    }


}
