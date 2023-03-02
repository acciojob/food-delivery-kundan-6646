package com.driver.service.impl;

import com.driver.io.entity.OrderEntity;
import com.driver.io.repository.OrderRepository;
import com.driver.model.request.OrderDetailsRequestModel;
import com.driver.model.response.OperationStatusModel;
import com.driver.model.response.OrderDetailsResponse;
import com.driver.model.response.RequestOperationName;
import com.driver.model.response.RequestOperationStatus;
import com.driver.service.OrderService;
import com.driver.shared.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(order.getOrderId());
        orderEntity.setUserId(order.getUserId());
        orderEntity.setCost(order.getCost());
        orderEntity.setStatus(order.isStatus());
        orderEntity.setItems(order.getItems());
        orderRepository.save(orderEntity);

        order.setId(orderRepository.findByOrderId(order.getOrderId()).getId());
        return order;
    }

    @Override
    public OrderDto getOrderById(String orderId) throws Exception {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderEntity.getId());
        orderDto.setOrderId(orderEntity.getOrderId());
        orderDto.setUserId(orderEntity.getUserId());
        orderDto.setStatus(orderEntity.isStatus());
        orderDto.setCost(orderEntity.getCost());
        orderDto.setItems(orderEntity.getItems());
        return orderDto;
    }

    @Override
    public OrderDto updateOrderDetails(String orderId, OrderDto order) throws Exception {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        orderEntity.setUserId(order.getUserId());
        orderEntity.setCost(order.getCost());
        orderEntity.setItems(order.getItems());
        orderRepository.save(orderEntity);

        order.setId(orderEntity.getId());
        order.setOrderId(orderEntity.getOrderId());
        order.setStatus(orderEntity.isStatus());

        return order;
    }

    @Override
    public void deleteOrder(String orderId) throws Exception {
        long id = orderRepository.findByOrderId(orderId).getId();
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDto> getOrders() {
        List<OrderEntity> list = (List<OrderEntity>) orderRepository.findAll();
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(OrderEntity o : list){
            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(o.getOrderId());
            orderDto.setId(o.getId());
            orderDto.setUserId(o.getUserId());
            orderDto.setStatus(o.isStatus());
            orderDto.setItems(o.getItems());
            orderDto.setCost(o.getCost());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    public OrderDetailsResponse createOrder(OrderDetailsRequestModel order){

        OrderDto orderDto = new OrderDto();
        orderDto.setItems(order.getItems());
        orderDto.setCost(order.getCost());
        orderDto.setUserId(order.getUserId());

        OrderDto finalOrderDto = createOrder(orderDto);

        return getOrderResponse(finalOrderDto);
    }

    public OrderDetailsResponse getOrder(String id) throws Exception {
        OrderDto orderDto = getOrderById(id);

        return getOrderResponse(orderDto);
    }

    public OrderDetailsResponse updateOrder(String id, OrderDetailsRequestModel order) throws Exception {

        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(order.getUserId());
        orderDto.setCost(order.getCost());
        orderDto.setItems(order.getItems());

        OrderDto finalOrderDto = updateOrderDetails(id,orderDto);

        return getOrderResponse(orderDto);
    }

    public OperationStatusModel delete_Order(String id){
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.DELETE.toString());
        try{
            deleteOrder(id);
        } catch (Exception e){
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.toString());
            return operationStatusModel;
        }
        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.toString());
        return operationStatusModel;
    }

    public List<OrderDetailsResponse> get_Orders(){
        List<OrderDto> orderDtoList = getOrders();
        List<OrderDetailsResponse> orderDetailsResponseList = new ArrayList<>();
        for(OrderDto o : orderDtoList){
             orderDetailsResponseList.add(getOrderResponse(o));
        }
        return orderDetailsResponseList;
    }

    private OrderDetailsResponse getOrderResponse(OrderDto order) {
        OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
        orderDetailsResponse.setOrderId(order.getOrderId());
        orderDetailsResponse.setCost(order.getCost());
        orderDetailsResponse.setItems(order.getItems());
        orderDetailsResponse.setStatus(order.isStatus());
        orderDetailsResponse.setUserId(order.getUserId());

        return orderDetailsResponse;
    }
}