package top.peachyao.service;

import top.peachyao.entity.Category;
import top.peachyao.handler.Result;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryList();
    List<Category> getCategoryNameList();
    void saveCategory(Category category);
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    void updateCategory(Category category);
    void deleteCategoryById(Long id);
    Result getResult(Category category, String type);
}
