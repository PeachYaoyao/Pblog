package top.peachyao.service;


import top.peachyao.model.vo.FriendInfoVo;
import top.peachyao.model.vo.FriendVo;

import java.util.List;

public interface FriendService {
    List<FriendVo> getFriendVOList();
    void updateViewsByNickname(String nickname);
    FriendInfoVo getFriendInfo(boolean cache, boolean md);
}
