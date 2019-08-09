/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest.webflux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.itta.springrest.Order;
import net.itta.springrest.Produit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class OrdersRepositoryImpl implements OrdersRepository{

        private final List<Order> orders = new ArrayList<>();


    public List<Order> getOrdersDao() {
        if (orders.isEmpty()) {
            Produit p1 = new Produit(0, "a", "aaaaaa", 12.5);
            Produit p2 = new Produit(1, "a1", "bbbbbbbb", 120.5);
            Produit p3 = new Produit(2, "a2", "ccccccc", 1.25);
            Produit p4 = new Produit(3, "a3", "cccccccccc", 125);

            List<Produit> lp1 = new ArrayList<>();
            lp1.add(p2);
            lp1.add(p1);
            lp1.add(p4);
            List<Produit> lp2 = new ArrayList<>();
            lp2.add(p3);
            lp2.add(p2);

            Order o1 = new Order(0, new Date(2019, 4, 20), 1, lp1);
            Order o2 = new Order(1, new Date(2018, 5, 10), 0, lp2);
            orders.add(o2);
            orders.add(o1);
        }
        return orders;
    }

    

        @Override
    public Mono<Order> findOrderByNo(int no) {
        return getOrders().filter(o->o.getNo()==no).singleOrEmpty();
    }

    @Override 
    public Flux<Order> getOrders() {
        return Flux.fromIterable(getOrdersDao());
    }

    
}
