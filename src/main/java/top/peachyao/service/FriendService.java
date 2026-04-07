package top.peachyao.service;


import top.peachyao.entity.Friend;
import top.peachyao.model.dto.FriendDto;
import top.peachyao.model.vo.FriendInfoVo;
import top.peachyao.model.vo.FriendVo;

import java.util.List;

public interface FriendService {
    List<Friend> getFriendList();
    List<FriendVo> getFriendVOList();
    void updateFriendPublishedById(Long friendId, Boolean published);
    void saveFriend(Friend friend);
    void updateFriend(FriendDto friend);
    void deleteFriend(Long id);
    void updateViewsByNickname(String nickname);
    FriendInfoVo getFriendInfo(boolean cache, boolean md);
    void updateFriendInfoContent(String content);
    void updateFriendInfoCommentEnabled(Boolean commentEnabled);
}
