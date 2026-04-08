package top.peachyao.model.dto;

import lombok.*;

/**
 * @Description: 访问日志备注
 * @author: PeachYao
 * @date: 2026-04-08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitLogRemarkDto {
	/**
	 * 访问内容
	 */
	private String content;

	/**
	 * 备注
	 */
	private String remark;
}
