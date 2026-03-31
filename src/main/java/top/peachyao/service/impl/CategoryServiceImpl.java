package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.constant.RedisKeyConstants;
import top.peachyao.entity.Category;
import top.peachyao.exception.NotFoundException;
import top.peachyao.exception.PersistenceException;
import top.peachyao.handler.Result;
import top.peachyao.mapper.CategoryMapper;
import top.peachyao.service.CategoryService;
import top.peachyao.service.RedisService;
import top.peachyao.util.StringUtils;

import java.util.List;

/**
 * @Description: 博客分类业务层实现
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    RedisService redisService;

    @Override
    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    @Override
    public List<Category> getCategoryNameList() {
        String redisKey = RedisKeyConstants.CATEGORY_NAME_LIST;
        List<Category> categoryListFromRedis = redisService.getListByValue(redisKey);
        if(categoryListFromRedis != null)
            return categoryListFromRedis;
        List<Category> categoryList = categoryMapper.getCategoryNameList();
        redisService.saveListToValue(redisKey, categoryList);
        return categoryList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveCategory(Category category) {
        if(categoryMapper.saveCategory(category) != 1) {
            throw new PersistenceException("分类添加失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.CATEGORY_NAME_LIST);
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryMapper.getCategoryById(id);
        if(category == null) {
            throw new NotFoundException("分类不存在");
        }
        return category;
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryMapper.getCategoryByName(name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCategory(Category category) {
        if(categoryMapper.updateCategory(category) != 1) {
            throw new NotFoundException("分类更新失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.CATEGORY_NAME_LIST);
        redisService.deleteCacheByKey(RedisKeyConstants.HOME_BLOG_INFO_LIST);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCategoryById(Long id) {
        if(categoryMapper.deleteCategoryById(id) != 1) {
            throw new PersistenceException("删除分类失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.CATEGORY_NAME_LIST);
    }

    @Override
    public Result getResult(Category category, String type) {
        if(StringUtils.isEmpty(category.getName())) {
            return Result.error("分类名不能为空");
        }
        Category category1 = getCategoryByName(category.getName());
        if(category1 != null && !category1.getId().equals(category.getId())) {
            return Result.error("分类已存在");
        }
        if("save".equals(type)) {
            saveCategory(category);
            return Result.ok("分类添加成功");
        } else {
            updateCategory(category);
            return Result.ok("分类更新成功");
        }
    }
}
