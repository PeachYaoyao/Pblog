package top.peachyao.model.vo;

import lombok.*;

import java.util.List;

/**
 * @Description: 分页结果
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Data
public class PageResult<T> {
	private Integer totalPage;//总页数
	private List<T> list;//数据

	public PageResult(Integer totalPage, List<T> list) {
		this.totalPage = totalPage;
		this.list = list;
	}
}
