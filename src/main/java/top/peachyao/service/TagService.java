package top.peachyao.service;

import top.peachyao.entity.Tag;
import top.peachyao.handler.Result;

import java.util.List;

public interface TagService {
    List<Tag> getTagList();
    List<Tag> getTagListNotId();
    List<Tag> getTagListByBlogId(Long blogId);
    void saveTag(Tag tag);
    Tag getTagById(Long id);
    Tag getTagByName(String name);
    Result getResult(Tag tag, String type);
    void deleteTagById(Long id);
    void updateTag(Tag tag);
}
