/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author rvallez
 */
public class Departament {
    
    private String codi;
    private String nom;
    private String localitat;

    public Departament() {
    }
    
    public Departament(String codi, String nom, String localitat) {
        this.codi = codi;
        this.nom = nom;
        this.localitat = localitat;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalitat() {
        return localitat;
    }

    public void setLocalitat(String localitat) {
        this.localitat = localitat;
    }

    @Override
    public String toString() {
        return "Departament{" + "codi=" + codi + ", nom=" + nom + ", localitat=" + localitat + '}';
    }
    
    
}
