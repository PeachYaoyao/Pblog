package top.peachyao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.handler.Result;
import top.peachyao.model.dto.BlogPasswordDto;
import top.peachyao.model.vo.BlogDetailVo;
import top.peachyao.model.vo.BlogInfoVo;
import top.peachyao.model.vo.PageResult;
import top.peachyao.model.vo.SearchBlogVo;
import top.peachyao.service.BlogService;
import top.peachyao.util.JwtUtils;
import top.peachyao.util.StringUtils;

import java.util.List;

/**
 * @Description: 博客相关
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@RestController
public class BlogController {
    @Autowired
    BlogService blogService;

    /**
     * 按置顶、创建时间排序 分页查询博客简要信息列表
     *
     * @param pageNum 页码
     * @return
     */
    @GetMapping("/blogs")
    public Result getblogs(@RequestParam(defaultValue = "1") Integer pageNum) {
        PageResult<BlogInfoVo> pageResult = blogService.getBlogInfoListByIsPublished(pageNum);
        return Result.ok("请求成功", pageResult);
    }

    /**
     * 按id获取公开博客详情
     *
     * @param id  博客id
     * @param jwt 密码保护文章的访问Token
     * @return
     */
    @GetMapping("/blog")
    public Result getBlog(@RequestParam Long id,
                          @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
        BlogDetailVo blogDetailVo = blogService.getBlogByIdAndIsPublished(id, jwt);
        return Result.ok("获取成功", blogDetailVo);
    }

    /**
     * 校验受保护文章密码是否正确，正确则返回jwt
     *
     * @param blogPassword 博客id、密码
     * @return
     */
    @PostMapping("/checkBlogPassword")
    public Result checkBlogPassword(@RequestBody BlogPasswordDto blogPassword) {
        String password = blogService.getBlogPassword(blogPassword.getBlogId());
        if (password.equals(blogPassword.getPassword())) {
            //生成有效时间一个月的Token
            String jwt = JwtUtils.generateToken(blogPassword.getPassword().toString(), 1000 * 3600 * 24 * 30L);
            return Result.ok("密码正确", jwt);
        } else {
            return Result.create(403, "密码错误");
        }
    }
    /**
     * 按关键字根据文章内容搜索公开且无密码保护的博客文章
     *
     * @param query 关键字字符串
     * @return
     */
    @GetMapping("/searchBlog")
    public Result searchBlog(@RequestParam String query) {
        //校验关键字字符串合法性
        if(StringUtils.isEmpty(query) || StringUtils.hasSpecialChar(query) || query.trim().length() > 20) {
            return Result.error("参数错误");
        }
        List<SearchBlogVo> searchBlogs = blogService.getSearchBlogListByQueryAndIsPublished(query.trim());
        return Result.ok("获取成功", searchBlogs);
    }
}
