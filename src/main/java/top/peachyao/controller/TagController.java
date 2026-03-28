package top.peachyao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.peachyao.handler.Result;
import top.peachyao.model.vo.BlogInfoVo;
import top.peachyao.model.vo.PageResult;
import top.peachyao.service.BlogService;


/**
 * @Description: 标签
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@RestController
public class TagController {
    @Autowired
    BlogService blogService;
    /**
     * 根据标签name分页查询公开博客列表
     *
     * @param tagName 标签name
     * @param pageNum 页码
     * @return
     */
    @GetMapping("/tag")
    public Result tag(@RequestParam String tagName,
                      @RequestParam(defaultValue = "1") Integer pageNum) {
        PageResult<BlogInfoVo> pageResult = blogService.getBlogInfoListByTagNameAndIsPublished(tagName, pageNum);
        return Result.ok("请求成功", pageResult);
    }
}
