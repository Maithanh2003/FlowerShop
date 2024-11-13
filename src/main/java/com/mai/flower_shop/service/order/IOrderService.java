package com.mai.flower_shop.service.order;

import com.mai.flower_shop.dto.OrderDto;
import com.mai.flower_shop.model.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
