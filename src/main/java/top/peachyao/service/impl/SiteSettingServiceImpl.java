package top.peachyao.service.impl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.peachyao.constant.RedisKeyConstants;
import top.peachyao.constant.SiteSettingConstants;
import top.peachyao.entity.SiteSetting;
import top.peachyao.mapper.SiteSettingMapper;
import top.peachyao.model.vo.BadgeVo;
import top.peachyao.model.vo.CopyrightVo;
import top.peachyao.model.vo.FavoriteVo;
import top.peachyao.model.vo.IntroductionVo;
import top.peachyao.service.RedisService;
import top.peachyao.service.SiteSettingService;
import top.peachyao.util.JacksonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 站点设置业务层实现
 * @Author: PeachYao
 * @Date: 2026-03-28
 */
@Service
public class SiteSettingServiceImpl implements SiteSettingService {
    @Autowired
    RedisService redisService;
    @Autowired
    SiteSettingMapper siteSettingMapper;
    private static final Pattern PATTERN = Pattern.compile("\"(.*?)\"");

    @Override
    public Map<String, Object> getSiteInfo() {
        String redisKey = RedisKeyConstants.SITE_INFO_MAP;
        Map<String, Object> siteInfoMapFromRedis = redisService.getMapByValue(redisKey);
        if(siteInfoMapFromRedis != null) {
            return siteInfoMapFromRedis;
        }
        List<SiteSetting> siteSettings = siteSettingMapper.getList();
        Map<String, Object> siteInfo = new HashMap<>(2);
        List<BadgeVo> badges = new ArrayList<>();
        IntroductionVo introduction = new IntroductionVo();
        List<FavoriteVo> favorites = new ArrayList<>();
        List<String> rollTexts = new ArrayList<>();
        for(SiteSetting s : siteSettings) {
            switch (s.getType()) {
                case 1:
                    if(SiteSettingConstants.COPYRIGHT.equals(s.getNameEn())) {
                        CopyrightVo copyright = JacksonUtils.readValue(s.getValue(), CopyrightVo.class);
                        siteInfo.put(s.getNameEn(), copyright);
                    } else {
                        siteInfo.put(s.getNameEn(), s.getValue());
                    }
                    break;
                case 2:
                    switch (s.getNameEn()) {
                        case SiteSettingConstants.AVATAR:
                            introduction.setAvatar(s.getValue());
                            break;
                        case SiteSettingConstants.NAME:
                            introduction.setName(s.getValue());
                            break;
                        case SiteSettingConstants.GITHUB:
                            introduction.setGithub(s.getValue());
                            break;
                        case SiteSettingConstants.TELEGRAM:
                            introduction.setTelegram(s.getValue());
                            break;
                        case SiteSettingConstants.QQ:
                            introduction.setQq(s.getValue());
                            break;
                        case SiteSettingConstants.BILIBILI:
                            introduction.setBilibili(s.getValue());
                            break;
                        case SiteSettingConstants.NETEASE:
                            introduction.setNetease(s.getValue());
                            break;
                        case SiteSettingConstants.EMAIL:
                            introduction.setEmail(s.getValue());
                            break;
                        case SiteSettingConstants.FAVORITE:
                            FavoriteVo favorite = JacksonUtils.readValue(s.getValue(), FavoriteVo.class);
                            favorites.add(favorite);
                            break;
                        case SiteSettingConstants.ROLL_TEXT:
                            Matcher matcher = PATTERN.matcher(s.getValue());
                            while(matcher.find()) {
                                rollTexts.add(matcher.group(1));
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:
                    BadgeVo badge = JacksonUtils.readValue(s.getValue(), BadgeVo.class);
                    badges.add(badge);
                    break;
                default:
                    break;
            }
        }
        introduction.setFavorites(favorites);
        introduction.setRollText(rollTexts);
        Map<String, Object> map = new HashMap<>(8);
        map.put("introduction", introduction);
        map.put("siteInfo", siteInfo);
        map.put("badges", badges);
        redisService.saveMapToValue(redisKey, map);
        return map;
    }
}
