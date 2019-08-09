/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import net.itta.springrest.async.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderAsyncRestController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("/ordersasync/{no}")
    public Order getOrderJSONbyNo(@PathVariable int no) throws InterruptedException, ExecutionException {
        CompletableFuture<Order> order =  asyncService.getOrder(no);
        return order.get();
    }

     @GetMapping("/twofirstordersasync")
    public List<Order> get2FirstOrderJSONbyNo() throws InterruptedException, ExecutionException {
        List<Order> lo  = new ArrayList<>();
        CompletableFuture<Order> order0 =  asyncService.getOrder(0);
        CompletableFuture<Order> order1 =  asyncService.getOrder(1);
        CompletableFuture.allOf(order0, order1).join();
        lo.add( order0.get());
        lo.add( order1.get());
        return lo;
    }

}
