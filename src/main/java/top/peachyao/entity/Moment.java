package top.peachyao.entity;

import lombok.*;

import java.util.Date;

/**
 * @Description: 博客动态
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
public class Moment {
	private Long id;
	private String content;//动态内容
	private Date createTime;//创建时间
	private Integer likes;//点赞数量
	private Boolean published;//是否公开
}
