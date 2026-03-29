package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.model.vo.PageCommentVo;

import java.util.List;


/**
 * @Description: 博客评论持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@Mapper
public interface CommentMapper {
    List<PageCommentVo> getPageCommentListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId);
    int deleteCommentsByBlogId(Long blogId);
    int countByPageAndIsPublished(Integer page, Long blogId, Boolean isPublished);
}
