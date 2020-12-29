package cn.cheng.hot.news.handle;

import cn.cheng.hot.news.websocket.WebSocketHandler;

import javax.annotation.Resource;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 16:42
 **/
public abstract class Handler {
    @Resource
    protected WebSocketHandler webSocketHandler;

    public abstract void handle(String message);

}
