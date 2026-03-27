package top.peachyao.service;

import top.peachyao.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getTagList();
    List<Tag> getTagListByBlogId(Long blogId);
    void saveTag(Tag tag);
    Tag getTagById(Long id);
    Tag getTagByName(String name);
}
