package top.peachyao.model.dto;

import lombok.*;

/**
 * @Description: UserAgent解析DTO
 * @Author: PeachYao
 * @Date: 2026-04-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAgentDTO {
	private String os;
	private String browser;
}
