package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;


/**
 * @Description: 博客评论持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@Mapper
public interface CommentMapper {
    int deleteCommentsByBlogId(Long blogId);
}
