package cn.cheng.hot.news.handle;

import org.springframework.stereotype.Component;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 16:44
 **/
@Component
public class WeiBoHandler extends Handler {


    @Override
    public void handle(String message) {
        webSocketHandler.sendToAll(message);
    }
}
