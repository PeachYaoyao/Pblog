package top.peachyao.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.entity.Blog;
import top.peachyao.entity.Category;
import top.peachyao.entity.Tag;
import top.peachyao.handler.Result;
import top.peachyao.model.dto.BlogDto;
import top.peachyao.model.dto.BlogVisibilityDto;
import top.peachyao.service.BlogService;
import top.peachyao.service.CategoryService;
import top.peachyao.service.CommentService;
import top.peachyao.service.TagService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 博客文章后台管理
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@RestController
@RequestMapping("/admin")
public class BlogAdminController {
    @Autowired
    BlogService blogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CommentService commentService;
    @Autowired
    TagService tagService;

    /**
     * 获取博客文章列表
     *
     * @param title      按标题模糊查询
     * @param categoryId 按分类id查询
     * @param pageNum    页码
     * @param pageSize   每页个数
     * @return
     */
    @GetMapping("/blogs")
    public Result blogs(@RequestParam(defaultValue = "") String title,
                        @RequestParam(defaultValue = "") Integer categoryId,
                        @RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "10") Integer pageSize) {
        String orderBy = "create_time desc";
        PageHelper.startPage(pageNum, pageSize, orderBy);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogService.getListByTitleAndCategoryId(title, categoryId));
        List<Category> categories = categoryService.getCategoryList();
        Map<String, Object> map = new HashMap<>(4);
        map.put("blogs", pageInfo);
        map.put("categories", categories);
        return Result.ok("请求成功",map);
    }

    /**
     * 删除博客文章、删除博客文章下的所有评论、同时维护 blog_tag 表
     *
     * @param id 文章id
     * @return
     */
    @DeleteMapping("/blog")
    public Result delect(@RequestParam Long id) {
        blogService.deleteBlogTagByBlogId(id);
        blogService.deleteBlogById(id);
        commentService.deleteCommentsByBlogId(id);
        return Result.ok("删除成功");
    }

    /**
     * 获取分类列表和标签列表
     *
     * @return
     */
    @GetMapping("/categoryAndTag")
    public Result categoryAndTag() {
        List<Category> categories = categoryService.getCategoryList();
        List<Tag> tags = tagService.getTagList();
        Map<String, Object> map = new HashMap<>(4);
        map.put("categories", categories);
        map.put("tags", tags);
        return Result.ok("请求成功", map);
    }

    /**
     * 更新博客置顶状态
     *
     * @param id  博客id
     * @param top 是否置顶
     * @return
     */
    @PutMapping("/blog/top")
    public Result updateTop(@RequestParam Long id,
                            @RequestParam Boolean top) {
        blogService.updateBlogTopById(id, top);
        return Result.ok("操作成功");
    }

    /**
     * 更新博客推荐状态
     *
     * @param id        博客id
     * @param recommend 是否推荐
     * @return
     */
    @PutMapping("/blog/recommend")
    public Result updateRecommend(@RequestParam Long id,
                                  @RequestParam Boolean recommend) {
        blogService.updateBlogRecommendById(id, recommend);
        return Result.ok("操作成功");
    }

    /**
     * 更新博客可见性状态
     *
     * @param id             博客id
     * @param blogVisibility 博客可见性DTO
     * @return
     */
    @PutMapping("blog/{id}/visibility")
    public Result updateVisibility(@RequestParam Long id,
                                   @RequestParam BlogVisibilityDto blogVisibility) {
        blogService.updateBlogVisibilityById(id, blogVisibility);
        return Result.ok("操作成功");
    }

    /**
     * 按id获取博客详情
     *
     * @param id 博客id
     * @return
     */
    @GetMapping("/blog")
    public Result getBlogById(@RequestParam Long id) {
        Blog blog = blogService.getBlogById(id);
        return Result.ok("获取成功", blog);
    }

    /**
     * 保存草稿或发布新文章
     *
     * @param blogDto 博客文章DTO
     * @return
     */
    @PostMapping("/blog")
    public Result saveBlog(@RequestBody BlogDto blogDto) {
        return blogService.getResult(blogDto, "save");
    }

    /**
     * 更新博客
     *
     * @param blogDto 博客文章DTO
     * @return
     */
    @PutMapping("/blog")
    public Result updateBlog(@RequestBody BlogDto blogDto) {
        return blogService.getResult(blogDto, "update");
    }
}
