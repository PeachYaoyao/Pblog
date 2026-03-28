package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.Category;

import java.util.List;

/**
 * @Description: 博客分类持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@Mapper
public interface CategoryMapper {
    List<Category> getCategoryList();
    List<Category> getCategoryNameList();
    int saveCategory(Category category);
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
}
