package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.model.vo.FriendVo;

import java.util.List;

/**
 * @Description: 友链持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@Mapper
public interface FriendMapper {
    List<FriendVo> getFriendVOList();
    int updateViewsByNickname(String nickname);
}
