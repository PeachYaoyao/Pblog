package top.peachyao.service;

import top.peachyao.model.vo.PageCommentVo;

import java.util.List;
import java.util.Map;

public interface CommentService {
    Map<String, Object> comments(Integer page, Long blogId, Integer pageNum, Integer pageSize, String jwt);
    List<PageCommentVo> getPageCommentList(Integer page, Long blogId, Long parentCommentId);
    void deleteCommentsByBlogId(Long blogId);
    int countByPageAndIsPublished(Integer page, Long blogId, Boolean isPublished);

}
