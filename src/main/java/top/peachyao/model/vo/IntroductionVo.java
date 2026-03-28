package top.peachyao.model.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 侧边栏资料卡
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
public class IntroductionVo {
	private String avatar;
	private String name;
	private String github;
	private String telegram;
	private String qq;
	private String bilibili;
	private String netease;
	private String email;

	private List<String> rollText = new ArrayList<>();
	private List<FavoriteVo> favorites = new ArrayList<>();

}
