/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
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
    
    @RequestMapping(value = "/orders/{no}", method = RequestMethod.PATCH)
    public Order patchOrderJSONbyNo(@PathVariable int no, @RequestBody PatchObject patchObject) 
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Order p =getOrderJSONbyNo(no);   
        String[] pnames = patchObject.getPropertyNames();
        Object[] values= patchObject.getValues();
        BeanWrapper paccessor= PropertyAccessorFactory.forBeanPropertyAccess(p);
        
        for (int i = 0; i < pnames.length; i++) {           
            paccessor.setPropertyValue(pnames[i], values[i]);
        }
        return p;
    }
    
    @RequestMapping(value = "/orders/{no}/produit/{id}", method = RequestMethod.PATCH)
    public Produit patchProductJSONbyNo(@PathVariable int no, 
            @PathVariable int id,
            @RequestBody PatchObject patchObject) 
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Produit pdt= getAllProductsForOrderJSONbyNo(no).stream().filter(p->p.getId()==id).findFirst().orElse(null);   
        String[] pnames = patchObject.getPropertyNames();
        Object[] values= patchObject.getValues();
        BeanWrapper paccessor= PropertyAccessorFactory.forBeanPropertyAccess(pdt);
        
        for (int i = 0; i < pnames.length; i++) {           
            paccessor.setPropertyValue(pnames[i], values[i]);
        }
        return pdt;
    }
}
class PatchObject{
    private String[] propertyNames;
    private HashMap<String,Object>[] values;

    public PatchObject(String[] propertyNames, HashMap[] values) {
        this.propertyNames = propertyNames;
        this.values = values;
    }

    public PatchObject() {
    }

    public String[] getPropertyNames() {
        return propertyNames;
    }

    public Object[] getValues() {
        return Stream.of(values).flatMap(m->m.entrySet().stream()).map(n->n.getValue()).toArray();
    }

    public void setValues(HashMap[] values) {
        this.values = values;
    }

    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }
    
    
    
}