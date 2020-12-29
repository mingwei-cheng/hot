package cn.cheng.hot.news.sender;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 15:46
 **/
public interface ProcessorSender {
    String NEWS = "news";

    @Input(NEWS)
    SubscribableChannel input();
}
