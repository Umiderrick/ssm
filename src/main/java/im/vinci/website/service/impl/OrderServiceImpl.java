package im.vinci.website.service.impl;

import im.vinci.core.metatype.Dto;
import im.vinci.website.persistence.OrderMapper;
import im.vinci.website.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by mlc on 2016/1/14.
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public List<Dto> doQueryOrders(Dto dto) throws Exception {
        return orderMapper.getOrderList(dto);
    }

    @Override
    public List<Dto> doQueryChangeLogByOrdid(Dto dto) throws Exception {
        return  orderMapper.getChangeLog(dto);
    }
    /*查询结果中的订单的操作日志*/
    @Override
    public List<Dto> doQueryChangeLogs(List<Dto> dtos) throws Exception {
        for (Dto dto:dtos) {
            dto.put("logList",doQueryChangeLogByOrdid(dto));
        }
        return dtos;
    }
    /*订单加日志返回*/
    @Override
    public List<Dto> doQueryOrdersWithLogs(Dto dto) throws Exception {
        return doQueryChangeLogs(doQueryOrders(dto));
    }


    @Override
    public int doUpdateOrdersByBatch(List<Dto> dto) throws Exception {
        for (Dto dto1:dto){
            int result = doUpdateOrderById(dto1);
            if(result < 1){
                throw new Exception("数据失效！");
            }
        }
        return 0;//设置0为默认返回值
        //return orderMapper.doUpdateOrder(dto);
    }

    @Override
    public int doUpdateOrder(Dto dto) throws Exception {
        doUpdateOrderById(dto);
        doUpdateOrderSpecByOrderId(dto);
        doUpdateInvoideByOrderId(dto);
        return 0;
    }

    @Override
    public int doUpdateOrderById(Dto dto) throws Exception {
        return orderMapper.doUpdateOrderById(dto);
    }

    @Override
    public int doUpdateOrderSpecByOrderId(Dto dto) throws Exception {
        return orderMapper.doUpdateOrderSpecByOrderId(dto);
    }

    @Override
    public int doUpdateInvoideByOrderId(Dto dto) throws Exception {
        return orderMapper.doUpdateInvoiceByOrderId(dto);
    }


}
