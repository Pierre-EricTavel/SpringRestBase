/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import java.lang.reflect.Field;
import java.util.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
    private List<Order> orders = new ArrayList<>();
    
    private List<Order>  getOrders(){
        if(orders.isEmpty()){
            Produit p1 = new Produit(0, "a", "aaaaaa", 12.5);
            Produit p2 = new Produit(1, "a1", "bbbbbbbb", 120.5);
            Produit p3 = new Produit(2, "a2", "ccccccc", 1.25);
            Produit p4 = new Produit(3, "a3", "cccccccccc", 125);

            List<Produit> lp1 = new ArrayList<>();
            lp1.add(p2);lp1.add(p1);   lp1.add(p4);
            List<Produit> lp2 = new ArrayList<>();
            lp2.add(p3);lp2.add(p2);

            Order o1 = new Order(0, new Date(2019, 4, 20), 0, lp1);
            Order o2 = new Order(1, new Date(2018,5,10), 0, lp2);
            orders.add(o2);
            orders.add(o1);
        }
        return orders;
    }
    
    

    @RequestMapping("/orders")
    public List<Order> getAllOrdersJSON(){
        return getOrders();
    }
    
    @RequestMapping("/orders/{no}")
    public Order getOrderJSONbyNo(@PathVariable int no){
        return getOrders().stream().filter(o->o.getNo()==no).findFirst().orElse(null);
    }
    @RequestMapping("/orders/{no}/produits")
    public List<Produit> getAllProductsForOrderJSONbyNo(@PathVariable int no){
        return getOrderJSONbyNo(no).getProduits();
    }
    @RequestMapping("/orders/{no}/produit/{id}")
    public Produit getProduitbyIdForOrderJSONbyNo(@PathVariable int no, @PathVariable int id){
        return getAllProductsForOrderJSONbyNo(no).stream().filter(p->p.getId()==id).findFirst().orElse(null);
    }
    @RequestMapping(value = "/orders/{no}", method = RequestMethod.DELETE)
    public Order deleteOrderJSONbyNo(@PathVariable int no){
        Order p =getOrderJSONbyNo(no);
        getOrders().remove(p);
        return p;
    }
    @RequestMapping(value = "/orders/{no}", method = RequestMethod.PUT)
    public Order updateOrderJSONbyNo(@PathVariable int no, Order modified){
        Order p =getOrderJSONbyNo(no);
        getOrders().remove(p);
        return p;
    }
    
    @RequestMapping(value = "/orders/{no}/patch", method = RequestMethod.POST)
    public Order patchOrderJSONbyNo(@PathVariable int no, @RequestBody PatchObject patchObject) 
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Order p =getOrderJSONbyNo(no);   
        Class<?> clazz = Order.class;
        List<String> pnames = patchObject.getPropertyNames();
        
        for (int i = 0; i < pnames.size(); i++) {
            Field field= clazz.getField(pnames.get(i));
            field.setAccessible(true);
            field.set(p, patchObject.getValues()[i]);
        }
        return p;
    }
}
class PatchObject{
    private List<String> propertyNames;
    private Object[] values;

    public PatchObject(String toto) {
        System.out.println(toto);
    }

    
    
    public List<String> getPropertyNames() {
        return propertyNames;
    }

    public void setFields(List<String> fields) {
        this.propertyNames = fields;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
    
    
    
}