package top.peachyao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.peachyao.handler.Result;
import top.peachyao.service.CommentService;

import java.util.Map;

/**
 * @Description: 评论
 * @Author: PeachYao
 * @Date: 2026-03-29
 */
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     * 根据页面分页查询评论列表
     *
     * @param page     页面分类（0普通文章，1关于我...）
     * @param blogId   如果page==0，需要博客id参数
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @param jwt      若文章受密码保护，需要获取访问Token
     * @return
     */
    @GetMapping("/comments")
    public Result comments(@RequestParam Integer page,
                           @RequestParam(defaultValue = "") Long blogId,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
        Map<String, Object> map = commentService.comments(page, blogId, pageNum, pageSize, jwt);
        return Result.ok("获取成功", map);
    }

    /**
     * 提交评论 又长又臭 能用就不改了:)
     * 单个ip，30秒内允许提交1次评论
     *
     * @param comment 评论DTO
     * @param request 获取ip
     * @param jwt     博主身份Token
     * @return
     */

}
