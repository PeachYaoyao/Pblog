package top.peachyao.model.vo;

import lombok.*;

/**
 * @Description: 友链VO
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
public class FriendVo {
	private String nickname;//昵称
	private String description;//描述
	private String website;//站点
	private String avatar;//头像
}
