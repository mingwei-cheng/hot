package cn.cheng.hot.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NewsReptilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsReptilesApplication.class, args);
    }

}
