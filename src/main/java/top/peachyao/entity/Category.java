package top.peachyao.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 博客分类
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Data
public class Category {
	private Long id;
	private String name;//分类名称
	private List<Blog> blogs = new ArrayList<>();//该分类下的博客文章
}
