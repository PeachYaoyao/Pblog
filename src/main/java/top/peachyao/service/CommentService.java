package top.peachyao.service;

import jakarta.servlet.http.HttpServletRequest;
import top.peachyao.entity.Comment;
import top.peachyao.model.dto.CommentDto;
import top.peachyao.model.vo.PageCommentVo;

import java.util.List;
import java.util.Map;

public interface CommentService {
    Map<String, Object> comments(Integer page, Long blogId, Integer pageNum, Integer pageSize, String jwt);
    void postComment(CommentDto commentDto, HttpServletRequest request, String jwt);

    List<PageCommentVo> getPageCommentList(Integer page, Long blogId, Long parentCommentId);
    Comment getCommentById(Long id);
    void deleteCommentsByBlogId(Long blogId);
    int countByPageAndIsPublished(Integer page, Long blogId, Boolean isPublished);
    void saveComment(CommentDto comment);
}
