package cn.cheng.hot.news.recevier;

import cn.cheng.hot.news.sender.ProcessorSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author Cheng Mingwei
 * @create 2020-08-25 15:06
 **/
@EnableBinding(value = {ProcessorSender.class})
@Log4j2
public class ProcessorReceiver {

}
