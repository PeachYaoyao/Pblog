package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.constant.RedisKeyConstants;
import top.peachyao.entity.Tag;
import top.peachyao.exception.NotFoundException;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.TagMapper;
import top.peachyao.service.RedisService;
import top.peachyao.service.TagService;

import java.util.List;


/**
 * @Description: 博客标签业务层实现
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagMapper tagMapper;
    @Autowired
    RedisService redisService;

    @Override
    public List<Tag> getTagList() {
        return tagMapper.getTagList();
    }

    @Override
    public List<Tag> getTagListByBlogId(Long blogId) {
        return tagMapper.getTagListByBlogId(blogId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveTag(Tag tag) {
        if(tagMapper.saveTag(tag) != 1){
            throw new PersistenceException("标签添加失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.TAG_CLOUD_LIST);
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag = tagMapper.getTagById(id);
        if(tag == null) {
            throw new NotFoundException("标签不存在");
        }
        return tag;
    }

    @Override
    public Tag getTagByName(String name) {
        return tagMapper.getTagByName(name);
    }
}
