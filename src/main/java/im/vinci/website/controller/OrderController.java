package im.vinci.website.controller;

import im.vinci.core.metatype.Dto;
import im.vinci.core.metatype.Dtos;
import im.vinci.core.util.BaseLogger;
import im.vinci.website.domain.ResultObject;
import im.vinci.website.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* Created by mlc on 2016/1/13.
        */
@Controller
@RequestMapping(value = "/order", produces = "application/json;charset=UTF-8")
public class OrderController extends BaseLogger{
    @Autowired
    OrderService orderService;
    @RequestMapping("/doList")
    @ResponseBody
    public ResultObject doGetOders(HttpServletRequest request) throws  Exception{
        Dto dto = Dtos.fromJson(request);
        List<Dto> dtos = orderService.doQueryOrdersWithLogs(dto);
        return new ResultObject(dtos);
    }

    @RequestMapping("/doUpdateOrdersBatch")
    @ResponseBody
    public ResultObject doUpdateBatch(HttpServletRequest request) throws Exception{
        List<Dto> params  = Dtos.newDtosList(request);
        orderService.doUpdateOrdersByBatch(params);
        return new ResultObject();
    }
    @RequestMapping("/doView")
    @ResponseBody
    public ResultObject doViewOrderById(HttpServletRequest request) throws Exception{
        Dto dto = Dtos.fromJson(request);
        if (!StringUtils.isNotEmpty(dto.getAsString("order_id"))) {
            throw new Exception("请提供订单id!");
        }
        return new ResultObject(orderService.doQueryOrders(dto));
    }

    @RequestMapping("/doUpdate")
    @ResponseBody
    public ResultObject doUpdateOrderById(HttpServletRequest request) throws Exception{
        Dto dto = Dtos.fromJson(request);
        int colums = orderService.doUpdateOrderById(dto);
        if(colums < 1){
            throw new Exception("数据失效");
        }
        orderService.doUpdateOrder(dto);
        return new ResultObject();
    }

}
