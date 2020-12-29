package cn.cheng.hot.news.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Cheng Mingwei
 * @create 2020-08-26 14:52
 **/
@Log4j2
@Component
public class WSConnectHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private WebSocketHandler webSocketHandler;

    @Async
    public void handle(WebSocketSession session) {
        log.info("新连接 : {}", session.getRemoteAddress());
        Set<String> keys = redisTemplate.keys("hot:*");
        List<String> list = new ArrayList<>();
        for (String key : keys) {
            Set<Object> set = redisTemplate.opsForZSet().range(key, 0, 4);
            for (Object o : set) {
                Object re = redisTemplate.opsForHash().get(key.replace(":", "_"), o);
                if (re != null) {
                    list.add(re.toString());
                }
            }
        }
        for (String re : list) {
            webSocketHandler.sendOne(session.getId(), re);
        }


    }

}
