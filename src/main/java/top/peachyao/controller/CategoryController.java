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
 * @Description: 分类
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@RestController
public class CategoryController {
    @Autowired
    BlogService blogService;
    /**
     * 根据分类name分页查询公开博客列表
     *
     * @param categoryName 分类name
     * @param pageNum      页码
     * @return
     */
    @GetMapping("/category")
    public Result category(@RequestParam String categoryName,
                           @RequestParam(defaultValue = "1") Integer pageNum) {
        PageResult<BlogInfoVo> pageResult = blogService.getBlogInfoListByCategoryNameAndIsPublished(categoryName, pageNum);
        return Result.ok("请求成功", pageResult);
    }
}
