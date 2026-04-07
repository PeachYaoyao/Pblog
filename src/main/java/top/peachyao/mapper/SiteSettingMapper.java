package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.SiteSetting;

import java.util.List;

/**
 * @Description: 站点设置持久层接口
 * @Author: PeachYao
 * @Date: 2020-08-28
 */
@Mapper
public interface SiteSettingMapper {
    List<SiteSetting> getList();
    List<SiteSetting> getFriendInfo();
    int updateFriendInfoContent(String content);
    int updateFriendInfoCommentEnabled(Boolean commentEnabled);
}
