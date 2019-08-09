/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import net.itta.springrest.webflux.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@RestController 
public class OrderReactRestController {

    @Autowired  
    OrdersRepository ordersRepository;

    @GetMapping("/ordersreactasync/{no}")
    public Callable<Order> getOrderJSONbyNo(@PathVariable int no) {
        return () -> {
            Order o = ordersRepository.findOrderByNo(no).block();
            return o;
            
        };
    }
 
    @GetMapping("/ordersreactasync") 
    public WebAsyncTask<List<Order>> getOrdersLinksJSON() {
             Callable<List<Order>> clo  = () -> ordersRepository.getOrders().toStream().collect(Collectors.toList());
             return new  WebAsyncTask<>(1000, clo);
    }
    
    @GetMapping("/ordersreactsync/{no}")
    public Order getOrderJSONSyncbyNo(@PathVariable int no) {
        return ordersRepository.findOrderByNo(no).block();
    }
    

    @GetMapping("/ordersreactsync")
    public List<Order> getOrdersLinksJSONSync() {
             return ordersRepository.getOrders().toStream().collect(Collectors.toList());

    }
    
     @GetMapping("/ordersreact2")
    public List<Order> getOrderJSONbyNo2()  {
          List<Order> lo  = new ArrayList<>();
          Scheduler s = Schedulers.parallel();
          Mono<Order> mo0= ordersRepository.findOrderByNo(1).doOnNext((v)-> System.out.println(Thread.currentThread().getName())) .subscribeOn(s );
          Mono<Order> mo1= ordersRepository.findOrderByNo(0).doOnNext((v)-> System.out.println(Thread.currentThread().getName())).subscribeOn( s);
          Flux.merge(mo0,mo1).doOnEach(t->lo.add(t.get())).blockLast();
        return lo;

    }

}
