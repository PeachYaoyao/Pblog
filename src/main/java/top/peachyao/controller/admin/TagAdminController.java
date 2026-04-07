package top.peachyao.controller.admin;

import cn.hutool.db.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.entity.Tag;
import top.peachyao.exception.PersistenceException;
import top.peachyao.handler.Result;
import top.peachyao.service.BlogService;
import top.peachyao.service.TagService;

/**
 * @Description: 博客标签后台管理
 * @Author: PeachYao
 * @Date: 2026-04-07
 */
@RestController
@RequestMapping("/admin")
public class TagAdminController {
    @Autowired
    TagService tagService;
    @Autowired
    BlogService blogService;

    /**
     * 获取博客标签列表
     *
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return
     */
    @GetMapping("/tags")
    public Result tags(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        String orderBy = "id desc";
        PageHelper.startPage(pageNum, pageSize, orderBy);
        PageInfo<Tag> pageInfo = new PageInfo<>(tagService.getTagList());
        return Result.ok("请求成功",pageInfo);
    }

    /**
     * 添加新标签
     *
     * @param tag 标签实体
     * @return
     */
    @PostMapping("/tag")
    public Result saveTag(@RequestBody Tag tag) {
        return tagService.getResult(tag, "save");
    }

    /**
     * 修改标签
     *
     * @param tag 标签实体
     * @return
     */
    @PutMapping("/tag")
    public Result updateTag(@RequestBody Tag tag) {
        return tagService.getResult(tag, "update");
    }

    /**
     * 按id删除标签
     *
     * @param id 标签id
     * @return
     */
    @DeleteMapping("/tag")
    public Result delete(@RequestParam Long id) {
        int num = blogService.countBlogByTagId(id);
        if(num != 0) {
            throw new PersistenceException("已有博客与此标签关联，不可删除");
        }
        tagService.deleteTagById(id);
        return Result.ok("删除成功");
    }
}
