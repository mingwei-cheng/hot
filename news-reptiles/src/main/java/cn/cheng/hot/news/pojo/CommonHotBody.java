package cn.cheng.hot.news.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cheng Mingwei
 * @create 2020-08-24 21:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonHotBody {

    private String content;
    private String url;
    private String title;
    private String hot;
    private String img;

}
