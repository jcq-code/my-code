package com.example.smartshop.mapper;

import com.example.smartshop.entity.Product;
import com.example.smartshop.entity.ProductQuery;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("<script>" +
            "SELECT p.*, c.name as cat_name FROM product p LEFT JOIN category c ON p.cat_id = c.id " +
            "<where>" +
            "<if test='catId != null'> AND p.cat_id = #{catId} </if>" +
            "<if test='keyword != null and keyword != \"\"'> AND p.name LIKE CONCAT('%', #{keyword}, '%') </if>" +
            "<if test='minPrice != null'> AND p.price &gt;= #{minPrice} </if>" +
            "<if test='maxPrice != null'> AND p.price &lt;= #{maxPrice} </if>" +
            "</where>" +
            "ORDER BY p.id DESC" +
            "</script>")
    List<Product> findByCondition(ProductQuery query);

    @Select("SELECT p.*, c.name as cat_name FROM product p LEFT JOIN category c ON p.cat_id = c.id WHERE p.id = #{id}")
    Product findById(Integer id);

    @Insert("INSERT INTO product(name, photo_url, price, descp, release_date, cat_id) " +
            "VALUES(#{name}, #{photoUrl}, #{price}, #{descp}, #{releaseDate}, #{catId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("UPDATE product SET name=#{name}, photo_url=#{photoUrl}, price=#{price}, descp=#{descp}, " +
            "release_date=#{releaseDate}, cat_id=#{catId} WHERE id=#{id}")
    int update(Product product);

    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteById(Integer id);
}
