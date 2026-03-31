package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.User;

/**
 * @Description: 用户持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-27
 */
@Mapper
public interface UserMapper {
    User findByUsername(String username);
    int updateUserByUsername(String username, User user);
}
