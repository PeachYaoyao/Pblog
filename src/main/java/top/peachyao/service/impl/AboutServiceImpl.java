package top.peachyao.service.impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.peachyao.constant.RedisKeyConstants;
import top.peachyao.entity.About;
import top.peachyao.exception.PersistenceException;
import top.peachyao.mapper.AboutMapper;
import top.peachyao.service.AboutService;
import top.peachyao.service.RedisService;
import top.peachyao.util.markdown.MarkdownUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 关于我页面业务层实现
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Service
public class AboutServiceImpl implements AboutService {
    @Autowired
    AboutMapper aboutMapper;
    @Autowired
    RedisService redisService;

    @Override
    public Map<String, String> getAboutInfo() {
        String redisKey = RedisKeyConstants.ABOUT_INFO_MAP;
        Map<String, String> aboutInfoMapFromRedis = redisService.getMapByValue(redisKey);
        if(aboutInfoMapFromRedis != null) {
            return aboutInfoMapFromRedis;
        }
        List<About> abouts = aboutMapper.getAbout();
        Map<String, String> aboutInfoMap = new HashMap<>(16);
        for(About about:abouts) {
            if("content".equals(about.getNameEn())) {
                about.setValue(MarkdownUtils.toHtmlWithExtensions(about.getValue()));
            }
            aboutInfoMap.put(about.getNameEn(), about.getValue());
        }
        redisService.saveMapToValue(redisKey, aboutInfoMap);
        return aboutInfoMap;
    }

    @Override
    public Map<String, String> getAboutSetting() {
        List<About> abouts = aboutMapper.getAbout();
        Map<String, String> map = new HashMap<>(16);
        for(About about:abouts) {
            map.put(about.getNameEn(), about.getValue());
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAbout(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        for(String key:keySet) {
            if(aboutMapper.updateAbout(key, map.get(key)) != 1){
                throw new PersistenceException("修改失败");
            }
        }
        redisService.deleteCacheByKey(RedisKeyConstants.ABOUT_INFO_MAP);
    }

    @Override
    public boolean getAboutCommentEnabled() {
        String commentEnabledString = aboutMapper.getAboutCommentEnabled();
        return Boolean.parseBoolean(commentEnabledString);
    }
}
