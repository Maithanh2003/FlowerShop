package com.mai.flower_shop.service.order;

import com.mai.flower_shop.dto.OrderDto;
import com.mai.flower_shop.enums.OrderStatus;
import com.mai.flower_shop.exception.QuantityExceededException;
import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.*;
import com.mai.flower_shop.repository.CartRepository;
import com.mai.flower_shop.repository.OrderRepository;
import com.mai.flower_shop.repository.ProductRepository;
import com.mai.flower_shop.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getInventory() < item.getQuantity()) {
                throw new QuantityExceededException("Sản phẩm " + product.getName() + " không đủ số lượng trong kho");
            }
        }
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItem(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItem(Order order, Cart cart) {
        return cart.getItems()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(order, product, cartItem.getQuantity(), cartItem.getUnitPrice());
                }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {

        return orderRepository.findById(orderId).map(this::convertToDto)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No order found")
                );
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
