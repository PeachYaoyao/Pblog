package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.peachyao.model.vo.BlogInfoVo;
import top.peachyao.model.vo.PageResult;
import top.peachyao.service.RedisService;
import top.peachyao.util.JacksonUtils;

import java.util.Map;

/**
 * @Description: 读写Redis相关操作
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public PageResult<BlogInfoVo> getBlogInfoPageResultByHash(String hash, Integer pageNum) {
        if(redisTemplate.opsForHash().hasKey(hash, pageNum)) {
            Object redisResult = redisTemplate.opsForHash().get(hash, pageNum);
            PageResult<BlogInfoVo> pageResult = JacksonUtils.convertValue(redisResult, PageResult.class);
            return pageResult;
        } else {
            return null;
        }
    }

    @Override
    public void saveKVToHash(String hash, Object key, Object value) {
        redisTemplate.opsForHash().put(hash, key, value);
    }

    @Override
    public void saveMapToHash(String hash, Map map) {
        redisTemplate.opsForHash().putAll(hash, map);
    }

    @Override
    public Object getValueByHashKey(String hash, Object key) {
        return redisTemplate.opsForHash().get(hash, key);
    }

    @Override
    public void incrementByHashKey(String hash, Object key, int increment) {
        if (increment < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        redisTemplate.opsForHash().increment(hash, key, increment);
    }

    @Override
    public void deleteByHashKey(String hash, Object key) {
        redisTemplate.opsForHash().delete(hash, key);
    }

    @Override
    public <T> Map<String, T> getMapByValue(String key) {
        Map<String ,T> redisResult = (Map<String, T>) redisTemplate.opsForValue().get(key);
        return redisResult;
    }

    @Override
    public <T> void saveMapToValue(String key, Map<String, T> map) {
        redisTemplate.opsForValue().set(key, map);
    }

    @Override
    public void deleteCacheByKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
