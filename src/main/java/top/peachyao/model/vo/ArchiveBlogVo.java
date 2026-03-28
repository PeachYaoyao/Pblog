package top.peachyao.model.vo;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 归档页面博客简要信息
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
public class ArchiveBlogVo {
    private Long id;
    private String title;
    private String day;
    private String password;
    private Boolean privacy;
}
