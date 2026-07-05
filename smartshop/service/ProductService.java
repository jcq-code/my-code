package com.example.smartshop.service;

import com.example.smartshop.entity.Product;
import com.example.smartshop.entity.ProductQuery;
import com.example.smartshop.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "product")
public class ProductService {
    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_KEY_LIST = "product:list";

    public PageInfo<Product> findByCondition(ProductQuery query, int pageNum, int pageSize) {
        String redisKey = REDIS_KEY_LIST + ":" + pageNum + ":" + pageSize + ":" +
                (query.getCatId() == null ? "0" : query.getCatId()) + ":" +
                (query.getKeyword() == null ? "" : query.getKeyword()) + ":" +
                (query.getMinPrice() == null ? "0" : query.getMinPrice()) + ":" +
                (query.getMaxPrice() == null ? "0" : query.getMaxPrice());

        try {
            @SuppressWarnings("unchecked")
            PageInfo<Product> pageInfo = (PageInfo<Product>) redisTemplate.opsForValue().get(redisKey);
            if (pageInfo != null) {
                System.out.println("[Redis缓存命中] 获取商品列表 page=" + pageNum);
                return pageInfo;
            }
        } catch (Exception e) {
            System.out.println("[Redis不可用] 直接查询数据库");
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.findByCondition(query);
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        try {
            redisTemplate.opsForValue().set(redisKey, pageInfo, 10, TimeUnit.MINUTES);
        } catch (Exception ignored) {}
        return pageInfo;
    }

    @Cacheable(key = "#id")
    public Product findById(Integer id) {
        System.out.println("[声明式缓存] 查询商品ID=" + id);
        return productMapper.findById(id);
    }

    @CachePut(key = "#product.id")
    public Product save(Product product) {
        clearProductListCache();
        if (product.getId() == null) {
            productMapper.insert(product);
        } else {
            productMapper.update(product);
        }
        return productMapper.findById(product.getId());
    }

    @CacheEvict(key = "#id")
    public int deleteById(Integer id) {
        clearProductListCache();
        return productMapper.deleteById(id);
    }

    private void clearProductListCache() {
        try {
            var keys = redisTemplate.keys(REDIS_KEY_LIST + ":*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                System.out.println("[Redis缓存清理] 清除商品列表缓存");
            }
        } catch (Exception ignored) {}
    }
}
