package top.peachyao.entity;

import lombok.*;

/**
 * @Description: 关于我
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@NoArgsConstructor
@Data
public class About {
	private Long id;
	private String nameEn;
	private String nameZh;
	private String value;
}
