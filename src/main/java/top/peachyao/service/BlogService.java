package top.peachyao.service;

import top.peachyao.entity.Blog;
import top.peachyao.handler.Result;
import top.peachyao.model.dto.BlogDto;
import top.peachyao.model.dto.BlogVisibilityDto;
import top.peachyao.model.vo.*;

import java.util.List;
import java.util.Map;


public interface BlogService {
    List<Blog> getListByTitleAndCategoryId(String title, Integer categoryId);
    List<SearchBlogVo> getSearchBlogListByQueryAndIsPublished(String query);
    List<NewBlogVo> getNewBlogListByIsPublished();
    PageResult<BlogInfoVo> getBlogInfoListByIsPublished(Integer pageNum);
    PageResult<BlogInfoVo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName, Integer pageNum);
    PageResult<BlogInfoVo> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum);
    Map<String, Object> getArchiveBlogAndCountByIsPublished();
    List<RandomBlogVo> getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend();
    void deleteBlogById(Long id);
    void deleteBlogTagByBlogId(Long blogId);
    void saveBlog(BlogDto blogDto);
    void saveBlogTag(Long blogId, Long tagId);
    void updateBlogRecommendById(Long blogId, Boolean recommend);
    void updateBlogVisibilityById(Long blogId, BlogVisibilityDto blogVisibility);
    void updateBlogTopById(Long blogId, Boolean top);
    Blog getBlogById(Long id);
    BlogDetailVo getBlogByIdAndIsPublished(Long id, String jwt);
    String getBlogPassword(Long blogId);
    void updateBlog(BlogDto blogDto);
    Result getResult(BlogDto blogDto, String type);
}
