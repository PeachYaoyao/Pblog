package top.peachyao.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 博客浏览量
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@NoArgsConstructor
@Data
public class BlogViewDto {
    private Long id;
    private Integer views;
}
