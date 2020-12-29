package cn.cheng.hot.news.sender;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 15:46
 **/
@Component
public interface ProcessorSender {
    String NEWS = "news";

    @Output(NEWS)
    MessageChannel output();
}
