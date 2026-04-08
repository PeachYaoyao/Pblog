package top.peachyao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.peachyao.model.vo.BlogInfoVo;
import top.peachyao.model.vo.PageResult;
import top.peachyao.service.RedisService;
import top.peachyao.util.JacksonUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public <T> List<T> getListByValue(String key) {
        List<T> redisResult = (List<T>) redisTemplate.opsForValue().get(key);
        return redisResult;
    }

    @Override
    public <T> void saveListToValue(String key, List<T> list) {
        redisTemplate.opsForValue().set(key, list);
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
    public <T> T getObjectByValue(String key, Class t) {
        Object redisResult = redisTemplate.opsForValue().get(key);
        T object = (T) JacksonUtils.convertValue(redisResult, t);
        return object;
    }

    @Override
    public void incrementByKey(String key, int increment) {
        if(increment < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        redisTemplate.opsForValue().increment(key, increment);
    }

    @Override
    public void saveObjectToValue(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }

    @Override
    public void saveValueToSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public boolean hasValueInSet(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public void deleteCacheByKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void expire(String key, long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }
}
