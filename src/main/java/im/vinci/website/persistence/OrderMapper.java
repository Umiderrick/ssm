package im.vinci.website.persistence;

import im.vinci.core.metatype.Dto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by mlc on 2016/1/14.
 */
@Repository
public interface OrderMapper {
    List<Dto> getOrderList(Dto dto) throws Exception;

    List<Dto> getChangeLog(Dto dto) throws Exception;

    int doUpdateOrderById(Dto dto) throws Exception;

    int doUpdateInvoiceByOrderId(Dto dto) throws  Exception;

    int doUpdateOrderSpecByOrderId(Dto dto) throws Exception;
}
