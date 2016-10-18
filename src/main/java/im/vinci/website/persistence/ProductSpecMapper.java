package im.vinci.website.persistence;

import im.vinci.website.domain.order.ProductSpec;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 实际商品mapper
 * Created by tim@vinci on 16/1/11.
 */
@Repository
public interface ProductSpecMapper {

    @Select("select * from product_spec where id=#{id}")
    ProductSpec getProductSpec(@Param("id")long id);
}
