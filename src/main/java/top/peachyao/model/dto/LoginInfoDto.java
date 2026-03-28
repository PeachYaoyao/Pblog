package top.peachyao.model.dto;

import lombok.*;

/**
 * @Description: 登录账号密码
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
public class LoginInfoDto {
	private String username;
	private String password;
}
