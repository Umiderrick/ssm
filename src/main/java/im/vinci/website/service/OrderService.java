package im.vinci.website.service;

import im.vinci.core.metatype.Dto;

import java.util.List;

/**
 * Created by mlc on 2016/1/14.
 */
public interface OrderService {
    //订单主页面查询功能
    List<Dto> doQueryOrders(Dto dto) throws Exception;

    List<Dto> doQueryChangeLogByOrdid(Dto dto) throws Exception;

    List<Dto> doQueryChangeLogs(List<Dto> dtos) throws Exception;

    List<Dto> doQueryOrdersWithLogs(Dto dto) throws Exception;


    int doUpdateOrdersByBatch(List<Dto> dto) throws Exception;

    int doUpdateOrder(Dto dto) throws Exception;

    int doUpdateOrderById(Dto dto) throws Exception;

    int doUpdateOrderSpecByOrderId(Dto dto) throws Exception;

    int doUpdateInvoideByOrderId(Dto dto) throws Exception;
}
