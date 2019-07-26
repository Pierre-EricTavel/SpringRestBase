/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest;

import java.util.Date;
import java.util.List;


public class Order {
    
    private int no;
    private Date date;
    private int idClient;
    private List<Produit> produits;

    public Order(int no, Date date, int idClient, List<Produit> produits) {
        this.no = no;
        this.date = date;
        this.idClient = idClient;
        this.produits = produits;
    }

    @Override
    public String toString() {
        return "Order{" + "no=" + no + ", date=" + date + ", idClient=" + idClient + ", produits=" + produits + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.no;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.no != other.no) {
            return false;
        }
        return true;
    }
    
    
    public Order(){
        
    }
    

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
    
}
