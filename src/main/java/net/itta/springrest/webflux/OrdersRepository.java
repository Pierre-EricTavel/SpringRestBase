
package net.itta.springrest.webflux;

import net.itta.springrest.Order;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OrdersRepository {
    Mono<Order> findOrderByNo(int no);
    Flux<Order> getOrders();
}
