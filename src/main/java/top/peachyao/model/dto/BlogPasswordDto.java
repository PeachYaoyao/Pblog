package top.peachyao.model.dto;


import lombok.Data;

/**
 * @Description: 受保护文章密码DTO
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@Data
public class BlogPasswordDto {
    private Long blogId;
    private String password;
}
