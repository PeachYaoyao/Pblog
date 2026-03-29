package top.peachyao.model.vo;

import lombok.*;

/**
 * @Description: 评论管理页面按博客title查询评论
 * @Author: PeachYao
 * @Date: 2026-03-29
 */
@NoArgsConstructor
@Data
public class BlogIdAndTitleVo {
	private Long id;
	private String title;
}
