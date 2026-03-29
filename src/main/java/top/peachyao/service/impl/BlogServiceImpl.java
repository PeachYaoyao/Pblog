package top.peachyao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.constant.JwtConstants;
import top.peachyao.constant.RedisKeyConstants;
import top.peachyao.entity.Blog;
import top.peachyao.entity.Category;
import top.peachyao.entity.Tag;
import top.peachyao.entity.User;
import top.peachyao.exception.NotFoundException;
import top.peachyao.exception.PersistenceException;
import top.peachyao.handler.Result;
import top.peachyao.mapper.BlogMapper;
import top.peachyao.model.dto.BlogDto;
import top.peachyao.model.dto.BlogViewDto;
import top.peachyao.model.dto.BlogVisibilityDto;
import top.peachyao.model.vo.*;
import top.peachyao.service.BlogService;
import top.peachyao.service.CategoryService;
import top.peachyao.service.RedisService;
import top.peachyao.service.TagService;
import top.peachyao.util.JacksonUtils;
import top.peachyao.util.JwtUtils;
import top.peachyao.util.StringUtils;
import top.peachyao.util.markdown.MarkdownUtils;

import java.util.*;


/**
 * @Description: 博客文章业务层实现
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    TagService tagService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CategoryService categoryService;
    //随机博客显示5条
    private static final int randomBlogLimitNum = 5;
    //最新推荐博客显示3条
    private static final int newBlogPageSize = 3;
    //每页显示5条博客简介
    private static final int pageSize = 5;
    //博客简介列表排序方式
    private static final String orderBy = "is_top desc, create_time desc";
    //私密博客提示
    private static final String PRIVATE_BLOG_DESCRIPTION = "此文章受密码保护！";

    /**
     * 项目启动时，保存所有博客的浏览量到Redis
     */
    @PostConstruct
    private void saveBlogViewsToRedis() {
        String redisKey = RedisKeyConstants.BLOG_VIEWS_MAP;
        if(!redisService.hasKey(redisKey)){
            List<BlogViewDto> blogViewList = blogMapper.getBlogViewsList();
            Map<Long, Integer> blogViewsMap = new HashMap<>(128);
            for(BlogViewDto blogViewDto:blogViewList) {
                blogViewsMap.put(blogViewDto.getId(), blogViewDto.getViews());
            }
            redisService.saveMapToHash(redisKey,blogViewsMap);
        }
    }

    private void setBlogViewsFromRedisToPageResult(PageResult<BlogInfoVo> pageResult) {
        String redisKey = RedisKeyConstants.BLOG_VIEWS_MAP;
        List<BlogInfoVo> BlogInfoVoList = pageResult.getList();
        for (int i = 0; i < BlogInfoVoList.size(); i++) {
            BlogInfoVo BlogInfoVo = JacksonUtils.convertValue(BlogInfoVoList.get(i), BlogInfoVo.class);
            Long blogId = BlogInfoVo.getId();
            int view = (int) redisService.getValueByHashKey(RedisKeyConstants.BLOG_VIEWS_MAP, blogId);
            BlogInfoVo.setViews(view);
            BlogInfoVoList.set(i, BlogInfoVo);
        }
    }

    private List<BlogInfoVo> processBlogInfosPassword(List<BlogInfoVo> BlogInfoVoList) {
        for(BlogInfoVo BlogInfoVo:BlogInfoVoList) {
            if(!"".equals(BlogInfoVo.getPassword())) {
                BlogInfoVo.setPrivacy(true);
                BlogInfoVo.setPassword("");
                BlogInfoVo.setDescription(PRIVATE_BLOG_DESCRIPTION);
            } else {
                BlogInfoVo.setPrivacy(false);
                BlogInfoVo.setDescription(MarkdownUtils.toHtmlWithExtensions(BlogInfoVo.getDescription()));
            }
            BlogInfoVo.setTags(tagService.getTagListByBlogId(BlogInfoVo.getId()));
        }
        return BlogInfoVoList;
    }

    @Override
    public List<Blog> getListByTitleAndCategoryId(String title, Integer categoryId) {
        return blogMapper.getListByTitleAndCategoryId(title, categoryId);
    }

    @Override
    public List<SearchBlogVo> getSearchBlogListByQueryAndIsPublished(String query) {
        List<SearchBlogVo> searchBlogList = blogMapper.getSearchBlogListByQueryAndIsPublished(query);
        query = query.toUpperCase();
        for (SearchBlogVo searchBlog : searchBlogList) {
            String content = searchBlog.getContent().toUpperCase();
            int contentLength = content.length();
            int index = content.indexOf(query) - 10;
            index = Math.max(index, 0);
            int end = index + 21;//以关键字字符串为中心返回21个字
            end = Math.min(end, contentLength - 1);
            searchBlog.setContent(searchBlog.getContent().substring(index, end));
        }
        return searchBlogList;
    }

    @Override
    public List<NewBlogVo> getNewBlogListByIsPublished() {
        String redisKey = RedisKeyConstants.NEW_BLOG_LIST;
        List<NewBlogVo> newBlogListFromRedis = redisService.getListByValue(redisKey);
        if (newBlogListFromRedis != null) {
            return newBlogListFromRedis;
        }
        PageHelper.startPage(1, newBlogPageSize);
        List<NewBlogVo> newBlogList = blogMapper.getNewBlogListByIsPublished();
        for(NewBlogVo newBlog : newBlogList) {
            if(!"".equals(newBlog.getPassword())) {
                newBlog.setPrivacy(true);
                newBlog.setPassword("");
            } else {
                newBlog.setPrivacy(false);
            }
        }
        redisService.saveListToValue(redisKey, newBlogList);
        return newBlogList;
    }

    @Override
    public PageResult<BlogInfoVo> getBlogInfoListByIsPublished(Integer pageNum) {
        String redisKey = RedisKeyConstants.HOME_BLOG_INFO_LIST;
        PageResult<BlogInfoVo> pageResultFromRedis = redisService.getBlogInfoPageResultByHash(redisKey, pageNum);
        if(pageResultFromRedis != null) {
            setBlogViewsFromRedisToPageResult(pageResultFromRedis);
            return pageResultFromRedis;
        }
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<BlogInfoVo> BlogInfoVoList = processBlogInfosPassword(blogMapper.getBlogInfoListByIsPublished());
        PageInfo<BlogInfoVo> pageInfo = new PageInfo<>(BlogInfoVoList);
        PageResult<BlogInfoVo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
        setBlogViewsFromRedisToPageResult(pageResult);
        redisService.saveKVToHash(redisKey, pageNum, pageResult);
        return pageResult;
    }

    @Override
    public PageResult<BlogInfoVo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<BlogInfoVo> blogInfos = processBlogInfosPassword(blogMapper.getBlogInfoListByCategoryNameAndIsPublished(categoryName));
        PageInfo<BlogInfoVo> pageInfo = new PageInfo<>(blogInfos);
        PageResult<BlogInfoVo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
        setBlogViewsFromRedisToPageResult(pageResult);
        return pageResult;
    }

    @Override
    public PageResult<BlogInfoVo> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, orderBy);
        List<BlogInfoVo> blogInfos = processBlogInfosPassword(blogMapper.getBlogInfoListByTagNameAndIsPublished(tagName));
        PageInfo<BlogInfoVo> pageInfo = new PageInfo<>(blogInfos);
        PageResult<BlogInfoVo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
        setBlogViewsFromRedisToPageResult(pageResult);
        return pageResult;
    }

    @Override
    public Map<String, Object> getArchiveBlogAndCountByIsPublished() {
        String redisKey = RedisKeyConstants.ARCHIVE_BLOG_MAP;
        Map<String, Object> mapFromRedis = redisService.getMapByValue(redisKey);
        if(mapFromRedis != null) {
            return mapFromRedis;
        }
        List<String> groupYearMonth = blogMapper.getGroupYearMonthByIsPublished();
        Map<String, List<ArchiveBlogVo>> archiveBlogMap = new LinkedHashMap<>();
        for(String s : groupYearMonth) {
            List<ArchiveBlogVo> archiveBlogs = blogMapper.getArchiveBlogListByYearMonthAndIsPublished(s);
            for(ArchiveBlogVo archiveBlog : archiveBlogs) {
                if("".equals(archiveBlog.getPassword())) {
                    archiveBlog.setPrivacy(true);
                    archiveBlog.setPassword("");
                } else {
                    archiveBlog.setPrivacy(false);
                }
            }
            archiveBlogMap.put(s, archiveBlogs);
        }
        Integer count = blogMapper.countBlogByIsPublished();
        Map<String, Object> map = new HashMap<>(4);
        map.put("blogMap", archiveBlogMap);
        map.put("count", count);
        redisService.saveMapToValue(redisKey, map);
        return map;
    }

    @Override
    public List<RandomBlogVo> getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend() {
        List<RandomBlogVo> randomBlogs = blogMapper.getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend(randomBlogLimitNum);
        for(RandomBlogVo randomBlog : randomBlogs) {
            if(!"".equals(randomBlog.getPassword())) {
                randomBlog.setPrivacy(true);
                randomBlog.setPassword("");
            } else {
                randomBlog.setPrivacy(false);
            }
        }
        return randomBlogs;
    }

    @Override
    public void deleteBlogById(Long id) {
        if(blogMapper.deleteBlogById(id) != 1) {
            throw new NotFoundException("该博客不存在");
        }
        deleteBlogRedisCache();
        redisService.deleteByHashKey(RedisKeyConstants.BLOG_VIEWS_MAP, id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBlogTagByBlogId(Long blogId) {
        if(blogMapper.deleteBlogTagByBlogId(blogId) == 0) {
            throw new PersistenceException("维护博客标签关联表失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBlog(BlogDto blogDto) {
        if(blogMapper.saveBlog(blogDto) != 1) {
            throw new PersistenceException("添加博客失败");
        }
        redisService.saveKVToHash(RedisKeyConstants.BLOG_VIEWS_MAP, blogDto.getId(), 0);
        deleteBlogRedisCache();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBlogTag(Long blogId, Long tagId) {
        if(blogMapper.saveBlogTag(blogId, blogId) != 1) {
            throw new PersistenceException("维护博客标签关联表失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBlogRecommendById(Long blogId, Boolean recommend) {
        if(blogMapper.updateBlogRecommendById(blogId, recommend) != 1) {
            throw new PersistenceException("操作失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBlogVisibilityById(Long id, BlogVisibilityDto blogVisibilityDto) {
        if(blogMapper.updateBlogVisibilityById(id, blogVisibilityDto) != 1) {
            throw new PersistenceException("操作失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.HOME_BLOG_INFO_LIST);
        redisService.deleteCacheByKey(RedisKeyConstants.NEW_BLOG_LIST);
        redisService.deleteCacheByKey(RedisKeyConstants.ARCHIVE_BLOG_MAP);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBlogTopById(Long blogId, Boolean top) {
        if(blogMapper.updateBlogTopById(blogId, top) != 1) {
            throw new PersistenceException("操作失败");
        }
        deleteBlogRedisCache();
    }

    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogMapper.getBlogById(id);
        if(blog == null) {
            throw new NotFoundException("博客不存在");
        }
        int view = (int) redisService.getValueByHashKey(RedisKeyConstants.BLOG_VIEWS_MAP, blog.getId());
        blog.setViews(view);
        return blog;
    }

    @Override
    public BlogDetailVo getBlogByIdAndIsPublished(Long id, String jwt) {
        BlogDetailVo blogDetailVo = blogMapper.getBlogByIdAndIsPublished(id);
        if(blogDetailVo == null) {
            throw new NotFoundException("博客不存在");
        }
        blogDetailVo.setContent(MarkdownUtils.toHtmlWithExtensions(blogDetailVo.getContent()));
        int view = (int) redisService.getValueByHashKey(RedisKeyConstants.BLOG_VIEWS_MAP, blogDetailVo.getId());
        blogDetailVo.setViews(view);
        if (!"".equals(blogDetailVo.getPassword())) {
            if(JwtUtils.judgeTokenIsExist(jwt)) {
                String subject;
                try {
                    subject = JwtUtils.getTokenBody(jwt).getSubject();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NotFoundException("Token已失效，请重新验证密码！");
                }
                if(subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
                    String username = subject.replace(JwtConstants.ADMIN_PREFIX,"");
                    User admin = (User) userService.loadUserByUsername(username);
                    if(admin == null) {
                        throw new NotFoundException("博主身份Token已失效，请重新登录！");
                    } else {
                        Long tokenBlogId = Long.parseLong(subject);
                        if(!tokenBlogId.equals(id)) {
                            throw new NotFoundException("Token不匹配，请重新验证密码！");
                        }
                    }
                }
            } else {
                throw new NotFoundException("此文章受密码保护，请验证密码！");
            }
            blogDetailVo.setPassword("");
        }
        redisService.incrementByHashKey(RedisKeyConstants.BLOG_VIEWS_MAP, id, 1);
        return blogDetailVo;
    }

    @Override
    public String getBlogPassword(Long blogId) {
        return blogMapper.getBlogPassword(blogId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBlog(BlogDto blogDto) {
        if(blogMapper.updateBlog(blogDto) != 1) {
            throw new PersistenceException("更新博客失败");
        }
        deleteBlogRedisCache();
        redisService.saveKVToHash(RedisKeyConstants.BLOG_VIEWS_MAP, blogDto.getId(), blogDto.getViews());
    }

    @Override
    public Boolean getCommentEnabledByBlogId(Long blogId) {
        return blogMapper.getCommentEnabledByBlogId(blogId);
    }

    @Override
    public Boolean getPublishedByBlogId(Long blogId) {
        return blogMapper.getPublishedByBlogId(blogId);
    }

    /**
     * 执行博客添加或更新操作：校验参数是否合法，添加分类、标签，维护博客标签关联表
     *
     * @param blogDto 博客文章DTO
     * @param type 添加或更新
     * @return
     */
    @Override
    public Result getResult(BlogDto blogDto, String type) {
        //验证普通字段
        if(StringUtils.isEmpty(blogDto.getTitle(), blogDto.getFirstPicture(), blogDto.getContent(), blogDto.getDescription()) || blogDto.getWords() == null || blogDto.getWords() < 0) {
            return Result.error("参数错误");
        }
        //处理分类
        Object cate = blogDto.getCate();
        if(cate == null) {
            return Result.error("分类不能为空");
        }
        if(cate instanceof Integer) {//选择了已存在的分类
            Category category = categoryService.getCategoryById(((Integer) cate).longValue());
            blogDto.setCategory(category);
        } else if(cate instanceof String) {//添加新分类
            Category category = categoryService.getCategoryByName((String) cate);
            if(category != null) {
                return Result.error("不可添加已存在的分类");
            }
            Category c = new Category();
            c.setName((String) cate);
            categoryService.saveCategory(c);
            blogDto.setCategory(c);
        } else {
            return Result.error("分类不正确");
        }
        //处理标签
        List<Object> tagList = blogDto.getTagList();
        List<Tag> tags = new ArrayList<>();
        for(Object tag : tagList){
            if(tag instanceof Integer) {//选择了已存在的标签
                Tag t = tagService.getTagById(((Integer) tag).longValue());
                tags.add(t);
            } else if(tag instanceof String) {//添加新标签
                Tag t = tagService.getTagByName((String) tag);
                if(t != null) {
                    return Result.error("不可添加已存在的标签");
                }
                Tag tag1 = new Tag();
                tag1.setName((String) tag);
                tagService.saveTag(tag1);
                tags.add(tag1);
            } else {
                return Result.error("标签不正确");
            }
        }
        Date date = new Date();
        if(blogDto.getReadTime() == null || blogDto.getReadTime() < 0) {
            blogDto.setReadTime((int) Math.round(blogDto.getWords() / 200.0));//粗略计算阅读时长
        }
        if (blogDto.getViews() == null || blogDto.getViews() < 0) {
            blogDto.setViews(0);
        }
        if ("save".equals(type)) {
            blogDto.setCreateTime(date);
            blogDto.setUpdateTime(date);
            User user = new User();
            user.setId(1L);//个人博客默认只有一个作者
            blogDto.setUser(user);
            saveBlog(blogDto);
            //关联博客和标签(维护 blog_tag 表)
            for(Tag tag : tags) {
                saveBlogTag(blogDto.getId(), tag.getId());
            }
            return Result.ok("添加成功");
        } else {
            blogDto.setUpdateTime(date);
            updateBlog(blogDto);
            //关联博客和标签(维护 blog_tag 表)
            deleteBlogTagByBlogId(blogDto.getId());
            for(Tag tag : tags) {
                saveBlogTag(blogDto.getId(), tag.getId());
            }
            return Result.ok("更新成功");
        }
    }

    /**
     * 删除首页缓存、最新推荐缓存、归档页面缓存、博客浏览量缓存
     */
    private void deleteBlogRedisCache() {
        redisService.deleteCacheByKey(RedisKeyConstants.HOME_BLOG_INFO_LIST);
        redisService.deleteCacheByKey(RedisKeyConstants.NEW_BLOG_LIST);
        redisService.deleteCacheByKey(RedisKeyConstants.ARCHIVE_BLOG_MAP);
    }
}
