package com.example.smartshop.service;

import com.example.smartshop.entity.Category;
import com.example.smartshop.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "category")
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY = "category:list";

    @SuppressWarnings("unchecked")
    public List<Category> findAll() {
        try {
            List<Category> list = (List<Category>) redisTemplate.opsForValue().get(REDIS_KEY);
            if (list != null) {
                System.out.println("[Redis缓存命中] 获取分类列表");
                return list;
            }
        } catch (Exception e) {
            System.out.println("[Redis不可用] 直接查询数据库");
        }
        List<Category> list = categoryMapper.findAll();
        try {
            redisTemplate.opsForValue().set(REDIS_KEY, list, 30, TimeUnit.MINUTES);
        } catch (Exception ignored) {}
        return list;
    }

    @Cacheable(key = "#id")
    public Category findById(Integer id) {
        System.out.println("[声明式缓存] 查询分类ID=" + id);
        return categoryMapper.findById(id);
    }

    @CacheEvict(allEntries = true)
    public int save(Category category) {
        try { redisTemplate.delete(REDIS_KEY); } catch (Exception ignored) {}
        if (category.getId() == null) {
            return categoryMapper.insert(category);
        }
        return categoryMapper.update(category);
    }

    @CacheEvict(allEntries = true)
    public int deleteById(Integer id) {
        try { redisTemplate.delete(REDIS_KEY); } catch (Exception ignored) {}
        return categoryMapper.deleteById(id);
    }
}
