package cn.cheng.hot.news.recevier;

import cn.cheng.hot.news.handle.Handler;
import cn.cheng.hot.news.handle.HandlerFactory;
import cn.cheng.hot.news.sender.ProcessorSender;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import javax.annotation.Resource;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 15:06
 **/
@Log4j2
@EnableBinding(value = {ProcessorSender.class})
public class ProcessorReceiver {
    @Resource
    private HandlerFactory handlerFactory;

    @StreamListener(ProcessorSender.NEWS)
    public void receive(Object payload) {
        log.info("收到数据 : {}", payload.toString());
        String s = payload.toString();
        JSONObject jsonObject = JSON.parseObject(s);
        String cmd = jsonObject.getString("cmd");
        Handler product = handlerFactory.product(cmd);
        if (product != null) {
            product.handle(s);
        }
    }

}
