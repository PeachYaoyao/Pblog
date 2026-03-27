package top.peachyao.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.checkerframework.common.util.report.qual.ReportCreation;
import org.springframework.stereotype.Repository;
import top.peachyao.entity.Blog;
import top.peachyao.model.dto.BlogDto;
import top.peachyao.model.dto.BlogViewDto;
import top.peachyao.model.dto.BlogVisibilityDto;
import top.peachyao.model.vo.BlogDetailVo;
import top.peachyao.model.vo.BlogInfoVo;
import top.peachyao.model.vo.SearchBlogVo;

import java.util.List;

/**
 * @Description: 博客文章持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Mapper
public interface BlogMapper {
    List<Blog> getListByTitleAndCategoryId(String title, Integer categoryId);
    List<SearchBlogVo> getSearchBlogListByQueryAndIsPublished(String query);
    List<BlogInfoVo> getBlogInfoListByIsPublished();
    List<BlogViewDto> getBlogViewsList();
    int deleteBlogById(Long id);
    int deleteBlogTagByBlogId(Long blogId);
    int saveBlog(BlogDto blogDto);
    int updateBlogVisibilityById(Long blogId, BlogVisibilityDto blogVisibilityDto);
    int saveBlogTag(Long blogId, Long tagId);
    int updateBlogRecommendById(Long blogId, Boolean recommend);
    int updateBlogTopById(Long blogId, Boolean top);
    Blog getBlogById(Long id);
    BlogDetailVo getBlogByIdAndIsPublished(Long id);
    String getBlogPassword(Long blogId);
    int updateBlog(BlogDto blogDto);
}
