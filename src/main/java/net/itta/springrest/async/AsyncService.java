/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest.async;

import java.util.concurrent.CompletableFuture;
import net.itta.springrest.Order;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AsyncService {
    @Autowired
    private RestTemplate restTemplate;
    
    @Async("asyncExecutor")
    public CompletableFuture<Order> getOrder(int no) throws InterruptedException{
        System.out.println("..............Thread="+Thread.currentThread().getName());
        Order response
                = restTemplate.getForObject("http://localhost:8084/ittaspringrest/orders/"+no, Order.class);
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(response);
        
    }
    
}
