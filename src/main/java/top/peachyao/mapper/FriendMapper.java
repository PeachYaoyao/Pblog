package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.Friend;
import top.peachyao.model.dto.FriendDto;
import top.peachyao.model.vo.FriendVo;

import java.util.List;

/**
 * @Description: 友链持久层接口
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@Mapper
public interface FriendMapper {
    List<Friend> getFriendList();
    List<FriendVo> getFriendVOList();
    int updateFriendPublishedById(Long id, Boolean published);
    int saveFriend(Friend friend);
    int updateFriend(FriendDto friend);
    int deleteFriend(Long id);
    int updateViewsByNickname(String nickname);
}
