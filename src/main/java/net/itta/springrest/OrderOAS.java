/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import java.util.Date;
import org.springframework.hateoas.ResourceSupport;


public class OrderOAS extends ResourceSupport{
    private final Order order;

    public OrderOAS(Order order) {
        this.order = order;
    }

     public int getNo() {
        return order.getNo();
    }

    public void setNo(int no) {
        order.setNo(no);
    }

    public Date getDate() {
        return  order.getDate();
    }

    public void setDate(Date date) {
       order.setDate(date);
    }

    public int getIdClient() {
        return order.getIdClient();
    }

    public void setIdClient(int idClient) {
        order.setIdClient(idClient);
    }

}
