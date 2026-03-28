package top.peachyao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.peachyao.handler.Result;
import top.peachyao.model.vo.FriendInfoVo;
import top.peachyao.model.vo.FriendVo;
import top.peachyao.service.FriendService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 友链
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@RestController
public class FriendController {
    @Autowired
    FriendService friendService;
    /**
     * 获取友链页面
     *
     * @return
     */
    @GetMapping("/friends")
    public Result friends() {
        List<FriendVo> friendList = friendService.getFriendVOList();
        FriendInfoVo friendInfo = friendService.getFriendInfo(true, true);
        Map<String, Object> map = new HashMap<>(4);
        map.put("friendList", friendList);
        map.put("friendInfo", friendInfo);
        return Result.ok("获取成功", map);
    }

    /**
     * 按昵称增加友链浏览次数
     *
     * @param nickname 友链昵称
     * @return
     */
    @PutMapping("/friend")
    public Result addViews(@RequestParam String nickname) {
        friendService.updateViewsByNickname(nickname);
        return Result.ok("操作成功");
    }
}
