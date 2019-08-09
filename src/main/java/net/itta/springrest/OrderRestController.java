/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    private List<Order> orders = new ArrayList<>();

    List<Order> getOrders() {
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

    
    
    List<Order> filter ( List<Order> orders, String orderby,int limit, int skip ){
        Stream<Order> so =null;
        if(orderby!=null && !orderby.isEmpty()){
            String[] fields =orderby.split(",");
            Function<Order, Integer> name=null;
            for (String field : fields) {
                String[] ob=field.split(":");
                switch(ob[0]){
                    case "IdClient":
                        name = Order::getIdClient;
                        break;
                    case "no":
                        name = Order::getNo;
                        break;
                    }       
                    if(ob[1].equals("desc"))
                        so=orders.stream().sorted(Comparator.comparing(name).reversed()).skip(skip).limit(limit);
                    else
                        so=orders.stream().sorted(Comparator.comparing(name)).skip(skip).limit(limit); 
            }
            return so.collect(Collectors.toList());
        }
        else
            so=orders.stream();
       return so.skip(skip).limit(limit).collect(Collectors.toList());
    
    }

    @GetMapping("/orders")
    public List<Order> getAllOrdersJSON(
            @RequestParam(required = false) String orderby,
            @RequestParam(defaultValue = "50",required = false) int limit, 
            @RequestParam(defaultValue = "0",required = false) int skip ) {
        
        return filter(getOrders(), orderby, limit,  skip);
    }

    @GetMapping("/orders/{no}")
    public Order getOrderJSONbyNo(@PathVariable int no) {
        return getOrders().stream().filter(o -> o.getNo() == no).findFirst().orElse(null);
    }
   
    @GetMapping("/orders/{no}/produits")
    public List<Produit> getAllProductsForOrderJSONbyNo(@PathVariable int no) {
        return getOrderJSONbyNo(no).getProduits();
    }

    @GetMapping("/orders/{no}/produit/{id}")
    public Produit getProduitbyIdForOrderJSONbyNo(@PathVariable int no, @PathVariable int id) {
        return getAllProductsForOrderJSONbyNo(no).stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @DeleteMapping(value = "/orders/{no}")
    public Order deleteOrderJSONbyNo(@PathVariable int no) {
        Order p = getOrderJSONbyNo(no);
        getOrders().remove(p);
        return p;
    }
 @DeleteMapping("/orders/{no}/produit/{id}")
    public boolean deleteProduitbyIdForOrderJSONbyNo(@PathVariable int no, @PathVariable int id) {
        Order o = getOrderJSONbyNo(no);
        return o.getProduits().removeIf(p->p.getId()==id);

    }
    @PutMapping(value = "/orders/{no}")
    public Order updateOrderJSONbyNo(@PathVariable int no,  @RequestBody Order modified) {
        Order o = getOrderJSONbyNo(no);
        BeanUtils.copyProperties(modified, o, "no");
        return o;
    }
    

    
    @PostMapping(value = "/orders")
    public boolean addOrderJSON( @RequestBody Order added) {
        if(!getOrders().stream().anyMatch(o->o.getNo()==added.getNo())){
            return getOrders().add(added);
        }
        return false;  
    }
    
    @PostMapping(value = "/orders/{no}")
    public Boolean addProduitInOrderJSON(@PathVariable int no, @RequestBody Produit added) {
        Order o = getOrderJSONbyNo(no);
        if(o.getProduits().stream().allMatch(p->p.getId() !=added.getId())){
            return o.getProduits().add(added);
        }
        return false;  
    }
    
     @PutMapping(value = "/orders/{no}/produit/{id}")
    public Produit updateProduitForOrderJSONbyNo(@PathVariable int no, @PathVariable int id , @RequestBody Produit modified) {
       Produit p = getProduitbyIdForOrderJSONbyNo(no, id);
       BeanUtils.copyProperties(modified, p, "id");
       return p;
    }
    
    @PatchMapping(value = "/orders/{no}")
    public Order updateOrderJSONbyNo(@PathVariable int no, @RequestBody HashMap[] properties)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Order o = getOrderJSONbyNo(no);
        applyProperties(properties, o);
        return o;
    }

    @PatchMapping(value = "/orders/{no}/produit/{id}")
    public Produit updateProduitJSONbyNo(@PathVariable int no,
            @PathVariable int id,  @RequestBody HashMap[] properties)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Produit pdt = getProduitbyIdForOrderJSONbyNo(no, id);
        applyProperties(properties, pdt);
        return pdt;
    }

    void applyProperties(HashMap<String, Object>[] properties, Object object) throws BeansException {
        String[] pnames = Stream.of(properties).flatMap(v -> v.entrySet().stream()).map(s -> s.getKey()).toArray(String[]::new);
        Object[] pvalues = Stream.of(properties).flatMap(v -> v.entrySet().stream()).map(s -> s.getValue()).toArray();
        BeanWrapper paccessor = PropertyAccessorFactory.forBeanPropertyAccess(object);
        for (int i = 0; i < pnames.length; i++) {
            paccessor.setPropertyValue(pnames[i], pvalues[i]);
        }
    }
    
   
    
}
