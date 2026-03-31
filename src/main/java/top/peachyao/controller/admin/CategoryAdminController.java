package top.peachyao.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.peachyao.entity.Category;
import top.peachyao.handler.Result;
import top.peachyao.service.BlogService;
import top.peachyao.service.CategoryService;

/**
 * @Description: 博客分类后台管理
 * @Author: PeachYao
 * @Date: 2026-03-31
 */
@RestController
@RequestMapping("/admin")
public class CategoryAdminController {
    @Autowired
    BlogService blogService;
    @Autowired
    CategoryService categoryService;

    /**
     * 获取博客分类列表
     *
     * @param pageNum  页码
     * @param pageSize 每页个数
     * @return
     */
    @GetMapping("/category")
    public Result categories(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        String orderBy = "id desc";
        PageHelper.startPage(pageNum, pageSize, orderBy);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryService.getCategoryList());
        return Result.ok("请求成功", pageInfo);
    }

    /**
     * 添加新分类
     *
     * @param category 分类实体
     * @return
     */
    @PostMapping("/category")
    public Result saveCategory(@RequestBody Category category) {
        return categoryService.getResult(category, "save");
    }

    /**
     * 修改分类名称
     *
     * @param category 分类实体
     * @return
     */
    @PutMapping("/category")
    public Result updateCategory(@RequestBody Category category) {
        return categoryService.getResult(category, "update");
    }

    /**
     * 按id删除分类
     *
     * @param id 分类id
     * @return
     */
    @DeleteMapping("/category")
    public Result deleteCategory(@RequestParam Long id) {
        if(blogService.countBlogByCategoryId(id) != 0) {
            return Result.error("已有博客与此分类关联，不可删除！");
        }
        categoryService.deleteCategoryById(id);
        return Result.ok("删除成功");
    }
}
