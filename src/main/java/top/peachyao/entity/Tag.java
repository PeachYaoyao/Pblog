package top.peachyao.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 博客标签
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Data
public class Tag {
	private Long id;
	private String name;//标签名称
	private String color;//标签颜色(与Semantic UI提供的颜色对应，可选)
	private List<Blog> blogs = new ArrayList<>();//该标签下的博客文章
}
