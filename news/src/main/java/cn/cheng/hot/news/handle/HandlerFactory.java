package cn.cheng.hot.news.handle;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 16:41
 **/
@Component
public class HandlerFactory {


    @Resource
    private WeiBoHandler weiBoHandler;
    @Resource
    private ZhiHuHandler zhiHuHandler;

    public Handler product(String cmd) {
        switch (cmd) {
            case "WeiBoHot":
                return weiBoHandler;
            case "ZhiHuHot":
                return zhiHuHandler;
            default:
                return null;
        }
    }

}
