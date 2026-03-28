package top.peachyao.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.peachyao.entity.Category;
import top.peachyao.entity.Tag;
import top.peachyao.handler.Result;
import top.peachyao.model.vo.NewBlogVo;
import top.peachyao.model.vo.RandomBlogVo;
import top.peachyao.service.BlogService;
import top.peachyao.service.CategoryService;
import top.peachyao.service.SiteSettingService;
import top.peachyao.service.TagService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 站点相关
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@RestController
public class IndexController {
    @Autowired
    SiteSettingService siteSettingService;
    @Autowired
    BlogService blogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TagService tagService;

    /**
     * 获取站点配置信息、最新推荐博客、分类列表、标签云、随机博客
     *
     * @return
     */
    @GetMapping("/site")
    public Result site() {
        Map<String, Object> map = siteSettingService.getSiteInfo();
        List<NewBlogVo> newBlogList = blogService.getNewBlogListByIsPublished();
        List<Category> categoryList = categoryService.getCategoryNameList();
        List<Tag> tagList = tagService.getTagListNotId();
        List<RandomBlogVo> randomBlogList = blogService.getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend();
        map.put("newBlogList", newBlogList);
        map.put("categoryList", categoryList);
        map.put("tagList", tagList);
        map.put("randomBlogList", randomBlogList);
        return Result.ok("请求成功", map);
    }
}
