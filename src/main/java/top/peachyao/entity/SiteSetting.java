package top.peachyao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @Description: 站点设置
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteSetting {
	private Long id;
	private String nameEn;
	private String nameZh;
	private String value;
	private Integer type;
}
