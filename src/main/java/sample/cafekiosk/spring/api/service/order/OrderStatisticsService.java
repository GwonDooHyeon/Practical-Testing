package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * 주문 통계 서비스
 */
@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    // 해당 날짜에 일일 매출 통계를 이메일로 보낸다.
    public boolean sendOrderStatisticsMail(LocalDate orderDate, String toEmail) {
        // 해당 일자에 해당하는 주문을 가져와서
        List<Order> orders = orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        // 총 매출 합계를 계산 하고
        int totalPrice = orders.stream().mapToInt(Order::getTotalPrice).sum();

        // 메일 전송
        boolean result = mailService.sendMail("no-reply@cafekiosk.com",
                toEmail,
                "[매출통계] %s".formatted(orderDate),
                "총 매출 합계는 %s원입니다.".formatted(totalPrice)
        );

        if (!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }

        return true;
    }
}
