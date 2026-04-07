package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.constant.RedisKeyConstants;
import top.peachyao.entity.Friend;
import top.peachyao.entity.SiteSetting;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.FriendMapper;
import top.peachyao.mapper.SiteSettingMapper;
import top.peachyao.model.dto.FriendDto;
import top.peachyao.model.vo.FriendInfoVo;
import top.peachyao.model.vo.FriendVo;
import top.peachyao.service.FriendService;
import top.peachyao.service.RedisService;
import top.peachyao.util.markdown.MarkdownUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description: 友链业务层实现
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    FriendMapper friendMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    SiteSettingMapper siteSettingMapper;

    @Override
    public List<Friend> getFriendList() {
        return friendMapper.getFriendList();
    }

    @Override
    public List<FriendVo> getFriendVOList() {
        return friendMapper.getFriendVOList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFriendPublishedById(Long friendId, Boolean published) {
        if(friendMapper.updateFriendPublishedById(friendId, published) != 1) {
            throw new PersistenceException("操作失败");
        }
    }

    @Override
    public void saveFriend(Friend friend) {
        friend.setViews(0);
        friend.setCreateTime(new Date());
        if(friendMapper.saveFriend(friend) != 1) {
            throw new PersistenceException("添加失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFriend(FriendDto friend) {
        if(friendMapper.updateFriend(friend) != 1) {
            throw new PersistenceException("修改失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFriend(Long id) {
        if(friendMapper.deleteFriend(id) != 1) {
            throw new PersistenceException("删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateViewsByNickname(String nickname) {
        if(friendMapper.updateViewsByNickname(nickname) != 1) {
            throw new PersistenceException("操作失败");
        }
    }

    @Override
    public FriendInfoVo getFriendInfo(boolean cache, boolean md) {
        String redisKey = RedisKeyConstants.FRIEND_INFO_MAP;
        if(cache) {
            FriendInfoVo friendInfoFromRedis = redisService.getObjectByValue(redisKey, FriendInfoVo.class);
            if(friendInfoFromRedis != null) {
                return friendInfoFromRedis;
            }
        }
        List<SiteSetting> siteSettings = siteSettingMapper.getFriendInfo();
        FriendInfoVo friendInfo = new FriendInfoVo();
        for(SiteSetting siteSetting : siteSettings) {
            if ("friendContent".equals(siteSetting.getNameEn())) {
                if (md) {
                    friendInfo.setContent(MarkdownUtils.toHtmlWithExtensions(siteSetting.getValue()));
                } else {
                    friendInfo.setContent(siteSetting.getValue());
                }
            } else if ("friendCommentEnabled".equals(siteSetting.getNameEn())) {
                if ("1".equals(siteSetting.getValue())) {
                    friendInfo.setCommentEnabled(true);
                } else {
                    friendInfo.setCommentEnabled(false);
                }
            }
        }
        if (cache && md) {
            redisService.saveObjectToValue(redisKey, friendInfo);
        }
        return friendInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFriendInfoContent(String content) {
        if(siteSettingMapper.updateFriendInfoContent(content) != 1) {
            throw new PersistenceException("修改失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.FRIEND_INFO_MAP);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFriendInfoCommentEnabled(Boolean commentEnabled) {
        if(siteSettingMapper.updateFriendInfoCommentEnabled(commentEnabled) != 1) {
            throw new PersistenceException("修改失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.FRIEND_INFO_MAP);
    }
}
