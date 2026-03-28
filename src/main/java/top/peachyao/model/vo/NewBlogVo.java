package top.peachyao.model.vo;

import lombok.*;

/**
 * @Description: 最新推荐博客
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
public class NewBlogVo {
	private Long id;
	private String title;
	private String password;
	private Boolean privacy;
}
