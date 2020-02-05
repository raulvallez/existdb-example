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
public class Empleat {
    
    private String codi;
    private String dep;
    private String cap;
    private String cognom;
    private String ofici;
    private int salari;
    private String dataAlta;
    private int comissio;
    
    public Empleat() {
    }

    public Empleat(String codi, String dep, String cap, String cognom, String ofici, String dataAlta, int salari, int comissio) {
        this.codi = codi;
        this.dep = dep;
        this.cap = cap;
        this.cognom = cognom;
        this.ofici = ofici;
        this.salari = salari;
        this.dataAlta = dataAlta;
        this.comissio = comissio;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public String getOfici() {
        return ofici;
    }

    public void setOfici(String ofici) {
        this.ofici = ofici;
    }

    public int getSalari() {
        return salari;
    }

    public void setSalari(int salari) {
        this.salari = salari;
    }

    public String getDataAlta() {
        return dataAlta;
    }

    public void setDataAlta(String dataAlta) {
        this.dataAlta = dataAlta;
    }

    public int getComissio() {
        return comissio;
    }

    public void setComissio(int comissio) {
        this.comissio = comissio;
    }

    @Override
    public String toString() {
        return "Empleat{" + "codi=" + codi + ", dep=" + dep + ", cap=" + cap + ", cognom=" + cognom + ", ofici=" + ofici + ", salari=" + salari + ", dataAlta=" + dataAlta + ", comissio=" + comissio + '}';
    }

}
