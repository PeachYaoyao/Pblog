package top.peachyao.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 关键字搜索博客
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@NoArgsConstructor
@Data
public class SearchBlogVo {
    private Long id;
    private String title;
    private String content;
}
