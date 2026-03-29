package top.peachyao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.checkerframework.checker.units.qual.N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.constant.JwtConstants;
import top.peachyao.entity.Comment;
import top.peachyao.entity.User;
import top.peachyao.enums.CommentOpenStateEnum;
import top.peachyao.exception.NotFoundException;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.CommentMapper;
import top.peachyao.model.dto.CommentDto;
import top.peachyao.model.vo.PageCommentVo;
import top.peachyao.model.vo.PageResult;
import top.peachyao.service.CommentService;
import top.peachyao.service.UserService;
import top.peachyao.util.JwtUtils;
import top.peachyao.util.StringUtils;
import top.peachyao.util.comment.CommentUtils;

import java.util.*;

/**
 * @Description: 博客评论业务层实现
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentUtils commentUtils;
    @Autowired
    UserServiceImpl userService;

    @Override
    public Map<String, Object> comments(Integer page, Long blogId, Integer pageNum, Integer pageSize, String jwt) {
        CommentOpenStateEnum openState = commentUtils.judgeCommentState(page, blogId);
        switch (openState) {
            case NOT_FOUND:
                throw new NotFoundException("该博客不存在");
            case CLOSE:
                throw new NotFoundException("评论已关闭");
            case PASSWORD:
                if(JwtUtils.judgeTokenIsExist(jwt)) {
                    try {
                        String subject = JwtUtils.getTokenBody(jwt).getSubject();
                        if(subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
                            String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                            User admin = (User) userService.loadUserByUsername(username);
                            if(admin == null) {
                                throw new NotFoundException("博主身份Token已失效，请重新登录！");
                            }
                        } else {
                            Long tokenBlogId = Long.parseLong(subject);
                            if(!tokenBlogId.equals(blogId)) {
                                throw new NotFoundException("Token不匹配，请重新验证密码！");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new NotFoundException("Token已失效，请重新验证密码！");
                    }
                } else {
                    throw new NotFoundException("此文章受密码保护，请验证密码！");
                }
                break;
            default:
                break;
        }
        Integer allComment = countByPageAndIsPublished(page, blogId, null);
        Integer openComment = countByPageAndIsPublished(page, blogId, true);
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<PageCommentVo> pageInfo = new PageInfo<>(getPageCommentList(page, blogId, -1L));
        PageResult<PageCommentVo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
        Map<String, Object> map = new HashMap<>(8);
        map.put("allComment", allComment);
        map.put("closeComment", allComment - openComment);
        map.put("comments", pageResult);
        return map;
    }

    @Override
    public void postComment(CommentDto commentDto, HttpServletRequest request, String jwt) {
        if(StringUtils.isEmpty(commentDto.getContent()) || commentDto.getContent().length() > 250 || commentDto.getPage() == null || commentDto.getParentCommentId() == null) {
            throw new NotFoundException("参数有误");
        }
        boolean isVisitorComment = false;
        Comment parentComment = null;
        if(commentDto.getParentCommentId() != -1) {
            parentComment = getCommentById(commentDto.getParentCommentId());
            Integer page = parentComment.getPage();
            Long blogId = page == 0 ? parentComment.getBlog().getId() : null;
            commentDto.setPage(page);
            commentDto.setBlogId(blogId);
        } else if(commentDto.getPage() != 0) {
            commentDto.setBlogId(null);
        }
        CommentOpenStateEnum openState = commentUtils.judgeCommentState(commentDto.getPage(), commentDto.getBlogId());
        switch (openState) {
            case NOT_FOUND:
                throw new NotFoundException("该博客不存在");
            case CLOSE:
                throw new NotFoundException("评论已关闭");
            case PASSWORD:
                if (JwtUtils.judgeTokenIsExist(jwt)) {
                    String subject;
                    try {
                        subject = JwtUtils.getTokenBody(jwt).getSubject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new NotFoundException("Token已失效，请重新验证密码！");
                    }
                    if(subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
                        String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                        User admin = (User) userService.loadUserByUsername(username);
                        if(admin == null) {
                            throw new NotFoundException("博主身份Token已失效，请重新登录！");
                        }
                        commentUtils.setAdminComment(commentDto, request, admin);
                        isVisitorComment = false;
                    } else {
                        if(StringUtils.isEmpty(commentDto.getNickname(), commentDto.getEmail()) || commentDto.getNickname().length() > 15) {
                            throw new NotFoundException("参数有误");
                        }
                        Long tokenBlogId = Long.parseLong(subject);
                        if(!tokenBlogId.equals(commentDto.getBlogId())) {
                            throw new NotFoundException("Token不匹配，请重新验证密码！");
                        }
                        commentUtils.setVisitorComment(commentDto, request);
                    }
                } else {
                    throw new NotFoundException("此文章受密码保护，请验证密码！");
                }
                break;
            case OPEN:
                if(JwtUtils.judgeTokenIsExist(jwt)) {
                    String subject;
                    try {
                        subject = JwtUtils.getTokenBody(jwt).getSubject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new NotFoundException("Token已失效，请重新验证密码");
                    }
                    if(subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
                        String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
                        User admin = (User) userService.loadUserByUsername(username);
                        if(admin == null) {
                            throw new NotFoundException("博主身份Token已失效，请重新登录！");
                        }
                        commentUtils.setAdminComment(commentDto, request, admin);
                        isVisitorComment = false;
                    } else {
                        if(StringUtils.isEmpty(commentDto.getNickname(), commentDto.getEmail()) || commentDto.getNickname().length() > 15) {
                            throw new NotFoundException("参数有误");
                        }
                        commentUtils.setVisitorComment(commentDto, request);
                        isVisitorComment = true;
                    }
                } else {
                    if(StringUtils.isEmpty(commentDto.getNickname(), commentDto.getEmail()) || commentDto.getNickname().length() > 15) {
                        throw new NotFoundException("参数有误");
                    }
                    commentUtils.setVisitorComment(commentDto, request);
                    isVisitorComment = true;
                }
                break;
            default:
                break;
        }
        saveComment(commentDto);
        commentUtils.judgeSendNotify(commentDto, isVisitorComment, parentComment);
    }

    @Override
    public List<PageCommentVo> getPageCommentList(Integer page, Long blogId, Long parentCommentId) {
        List<PageCommentVo> comments = getPageCommentListByPageAndParentCommentId(page, blogId, parentCommentId);
        for (PageCommentVo comment : comments) {
            List<PageCommentVo> tmpComments = new ArrayList<>();
            getReplyComments(tmpComments, comment.getReplyComments());
            Comparator<PageCommentVo> comparator = Comparator.comparing(PageCommentVo::getCreateTime);
            tmpComments.sort(comparator);
            comment.setReplyComments(tmpComments);
        }
        return comments;
    }

    @Override
    public Comment getCommentById(Long id) {
        Comment comment = commentMapper.getCommentById(id);
        if(comment == null) {
            throw new PersistenceException("评论不存在");
        }
        return comment;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCommentsByBlogId(Long blogId) {
        commentMapper.deleteCommentsByBlogId(blogId);
    }

    @Override
    public int countByPageAndIsPublished(Integer page, Long blogId, Boolean isPublished) {
        return commentMapper.countByPageAndIsPublished(page, blogId, isPublished);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveComment(CommentDto comment) {
        if(commentMapper.saveComment(comment) != 1) {
            throw new PersistenceException("评论失败");
        }
    }

    private List<PageCommentVo> getPageCommentListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId) {
        List<PageCommentVo> comments = commentMapper.getPageCommentListByPageAndParentCommentId(page, blogId, parentCommentId);
        for(PageCommentVo comment : comments) {
            List<PageCommentVo> replyComments = getPageCommentListByPageAndParentCommentId(page, blogId, comment.getId());
            comment.setReplyComments(replyComments);
        }
        return comments;
    }
    private void getReplyComments(List<PageCommentVo> tmpComments, List<PageCommentVo> comments) {
        for(PageCommentVo comment : comments) {
            tmpComments.add(comment);
            getReplyComments(tmpComments, comment.getReplyComments());
        }
    }
}
