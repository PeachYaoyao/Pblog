package top.peachyao.service;

import top.peachyao.model.vo.BlogInfoVo;
import top.peachyao.model.vo.PageResult;

import java.util.Map;

public interface RedisService {
    PageResult<BlogInfoVo> getBlogInfoPageResultByHash(String hash, Integer pageNum);
    void saveKVToHash(String hash, Object key, Object value);
    void saveMapToHash(String hash, Map map);
    Object getValueByHashKey(String hash, Object key);
    void incrementByHashKey(String hash, Object key, int increment);
    void deleteByHashKey(String hash, Object key);
    <T> Map<String, T> getMapByValue(String key);
    <T> void saveMapToValue(String key, Map<String, T> map);
    void deleteCacheByKey(String key);
    boolean hasKey(String key);
}
