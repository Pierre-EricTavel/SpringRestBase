/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderOasRestController {

    private final List<OrderOAS> orders = new ArrayList<>();

    private List<OrderOAS> getOrders() {
        if (orders.isEmpty()) {
            new OrderRestController().getOrders()
                    .forEach(o -> orders.add(new OrderOAS(o)));
        }
        return orders;
    }

    @GetMapping("/ordersoas/{no}")
    public OrderOAS getOrderJSONbyNo(@PathVariable int no) {
        OrderOAS ooas = getOrders().stream().filter(o -> o.getNo() == no).findFirst().orElse(null);
        OrderRestController clb = ControllerLinkBuilder.methodOn(OrderRestController.class);
        List<Produit> pds = clb.getAllProductsForOrderJSONbyNo(no);
        ooas.add(ControllerLinkBuilder.linkTo(pds).withRel("produits"));
        return ooas;
    }

    @GetMapping("/ordersoas")
    public List<Link> getOrdersLinksJSON() {
        OrderOasRestController clb = ControllerLinkBuilder.methodOn(OrderOasRestController.class);
        List<Link> links = new ArrayList<>();
        getOrders().forEach(o -> {
            OrderOAS oas = clb.getOrderJSONbyNo(o.getNo());
            links.add(ControllerLinkBuilder
                    .linkTo(oas)
                    .withSelfRel()
                    .withMedia("application/json"));
        });
        return links;
    }

}
