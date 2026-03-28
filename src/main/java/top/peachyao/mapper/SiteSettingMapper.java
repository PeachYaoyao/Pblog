package top.peachyao.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.peachyao.entity.SiteSetting;

import java.util.List;

@Mapper
public interface SiteSettingMapper {
    List<SiteSetting> getList();
    List<SiteSetting> getFriendInfo();
}
