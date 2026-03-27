package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.About;

import java.util.List;

/**
 * @Description: 关于我持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Mapper
public interface AboutMapper {
    List<About> getAbout();
    int updateAbout(String nameEn, String value);
}
