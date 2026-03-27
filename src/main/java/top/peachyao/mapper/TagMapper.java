package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.Tag;

import java.util.List;


/**
 * @Description: 博客标签持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@Mapper
public interface TagMapper {
    List<Tag> getTagList();
    List<Tag> getTagListByBlogId(Long blogId);
    int saveTag(Tag tag);
    Tag getTagById(Long id);
    Tag getTagByName(String name);
}
