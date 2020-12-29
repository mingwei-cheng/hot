package cn.cheng.hot.news.handle;

import cn.cheng.hot.news.pojo.CommonHotBody;
import cn.cheng.hot.news.sender.ProcessorSender;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Cheng Mingwei
 * @create 2020-08-24 21:30
 **/
@Component
@Log4j2
public class SendHandler {
    public Consumer<JSONObject> consumer;
    private final ProcessorSender sender;

    @Resource
    private RedisTemplate<String, Object> redis;

    public SendHandler(ProcessorSender sender) {
        this.sender = sender;
    }

    /**
     * 初始化消费者
     */
    @PostConstruct
    public void init() {
        consumer = (jsonObject) -> {
            JSONArray list = jsonObject.getJSONArray("data");
            for (Object listObj : list) {
                CommonHotBody commonHotBody = (CommonHotBody) listObj;
                JSONObject re = new JSONObject();
                re.put("cmd", jsonObject.getString("cmd"));
                re.put("data", commonHotBody);
                Boolean aBoolean = redis.opsForZSet().add("hot:" + jsonObject.getString("cmd"), commonHotBody.getTitle(), -Long.parseLong(commonHotBody.getHot()));
                redis.expire("hot:" + jsonObject.getString("cmd"), 1, TimeUnit.DAYS);
                if (aBoolean != null && aBoolean) {
                    String cmd = (String) redis.opsForHash().get(commonHotBody.getTitle(),"hot_" + jsonObject.getString("cmd"));
                    JSONObject oldData = JSON.parseObject(cmd);
                    if (oldData == null || (StringUtils.isBlank(oldData.getString("img")) || StringUtils.isBlank(oldData.getString("content")) || StringUtils.isBlank(oldData.getString("title")))) {
                        aBoolean = redis.opsForHash().putIfAbsent("hot_" + jsonObject.getString("cmd"), commonHotBody.getTitle(), re.toJSONString());
                        redis.expire("hot_" + jsonObject.getString("cmd"), 1, TimeUnit.DAYS);
                        if (aBoolean) {
                            sender.output().send(MessageBuilder.withPayload(re).build());
                            log.info("发送数据：{}", re);
                        }
                    }
                }
            }
        };
    }
}

