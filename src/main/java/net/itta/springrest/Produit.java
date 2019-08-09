
package net.itta.springrest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.ResourceSupport;



public class Produit{
    private int id;
    private String nom;
    private String marque;
    private double prix;



    public int getId() {
        return id;
    }

    public Produit() {
    }

    
    public Produit(int id, String nom, String marque, double prix) {
        this.id = id;
        this.nom = nom;
        this.marque = marque;
        this.prix = prix;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
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
        final Produit other = (Produit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
    
    
    
    
}
