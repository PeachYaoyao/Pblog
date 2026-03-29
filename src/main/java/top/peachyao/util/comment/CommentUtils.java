package top.peachyao.util.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import top.peachyao.enums.CommentOpenStateEnum;
import top.peachyao.constant.PageConstants;
import top.peachyao.model.vo.FriendInfoVo;
import top.peachyao.service.AboutService;
import top.peachyao.service.BlogService;
import top.peachyao.service.FriendService;
import top.peachyao.util.StringUtils;

/**
 * @Description:评论工具类
 * @author: PeachYao
 * @date: 2026-03-29
 */
@Component
@DependsOn("springContextUtils")
public class CommentUtils {
    @Autowired
    BlogService blogService;
    @Autowired
    AboutService aboutService;
    @Autowired
    FriendService friendService;

    /**
     * 查询对应页面评论是否开启
     *
     * @param page   页面分类（0普通文章，1关于我，2友链）
     * @param blogId 如果page==0，需要博客id参数，校验文章是否公开状态
     * @return CommentOpenStateEnum
     */
    public CommentOpenStateEnum judgeCommentState(Integer page, Long blogId) {
        switch (page) {
            case PageConstants.BLOG:
                Boolean commentEnabled = blogService.getCommentEnabledByBlogId(blogId);
                Boolean published = blogService.getPublishedByBlogId(blogId);
                if(commentEnabled == null || published == null) {
                    return CommentOpenStateEnum.NOT_FOUND;
                } else if(!published) {
                    return CommentOpenStateEnum.NOT_FOUND;
                } else if(!commentEnabled) {
                    return CommentOpenStateEnum.CLOSE;
                }
                String password = blogService.getBlogPassword(blogId);
                if(!StringUtils.isEmpty(password)) {
                    return CommentOpenStateEnum.PASSWORD;
                }
                break;
            case PageConstants.ABOUT:
                if(!aboutService.getAboutCommentEnabled()) {
                    return CommentOpenStateEnum.CLOSE;
                }
                break;
            case PageConstants.FRIEND:
                FriendInfoVo friendInfoVo = friendService.getFriendInfo(true, false);
                if(!friendInfoVo.getCommentEnabled()) {
                    return CommentOpenStateEnum.CLOSE;
                }
                break;
            default:
                break;
        }
        return CommentOpenStateEnum.OPEN;
    }
}
