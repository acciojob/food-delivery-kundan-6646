package com.driver.service.impl;

import com.driver.model.request.OrderDetailsRequestModel;
import com.driver.model.response.OperationStatusModel;
import com.driver.model.response.OrderDetailsResponse;
import com.driver.service.OrderService;
import com.driver.shared.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl {

    @Autowired
    OrderService orderService;
    private OrderDetailsResponse getOrderResponse(OrderDto order) {
        OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
        orderDetailsResponse.setOrderId(order.getOrderId());
        orderDetailsResponse.setCost(order.getCost());
        orderDetailsResponse.setItems(order.getItems());
        orderDetailsResponse.setStatus(order.isStatus());
        orderDetailsResponse.setUserId(order.getUserId());

        return orderDetailsResponse;
    }

    public OrderDetailsResponse getOrderById(String id) throws Exception{
        OrderDto orderDto = orderService.getOrderById(id);
        return getOrderResponse(orderDto);
    }

    public OrderDetailsResponse createOrder(OrderDetailsRequestModel order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setCost(order.getCost());
        orderDto.setUserId(order.getUserId());
        orderDto.setItems(order.getItems());

        orderDto = orderService.createOrder(orderDto);

        return getOrderResponse(orderDto);
    }

    public OrderDetailsResponse updateOrderDetails(String id, OrderDetailsRequestModel order) throws Exception{
        OrderDto orderDto = orderService.getOrderById(id);
        orderDto.setItems(order.getItems());
        orderDto.setCost(order.getCost());
        orderDto.setUserId(order.getUserId());

        orderDto = orderService.updateOrderDetails(id, orderDto);

        return getOrderResponse(orderDto);
    }

    public OperationStatusModel deleteOrder(String id) throws Exception {
        orderService.deleteOrder(id);
        return new OperationStatusModel();
    }

    public List<OrderDetailsResponse> getOrders() {
        List<OrderDto> orderDtoList = orderService.getOrders();
        List<OrderDetailsResponse> orderDetailsResponses = new ArrayList<>();
        for (OrderDto orderDto: orderDtoList) {
            orderDetailsResponses.add(getOrderResponse(orderDto));
        }

        return orderDetailsResponses;
    }
}