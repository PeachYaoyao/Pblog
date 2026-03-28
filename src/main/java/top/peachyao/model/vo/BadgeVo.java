package top.peachyao.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: GitHub徽标
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@NoArgsConstructor
@Data
public class BadgeVo {
    private String title;
    private String url;
    private String subject;
    private String value;
    private String color;
}
