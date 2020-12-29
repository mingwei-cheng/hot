package cn.cheng.hot.news.handle;

import org.springframework.stereotype.Component;

/**
 * @author Cheng Mingwei
 * @create 2020-08-26 17:35
 **/
@Component
public class ZhiHuHandler extends Handler {
    @Override
    public void handle(String message) {
        webSocketHandler.sendToAll(message);
    }
}
