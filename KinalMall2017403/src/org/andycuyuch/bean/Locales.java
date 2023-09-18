package org.andycuyuch.bean;

public class Locales {
    private int codigoLocal; 
    private double saldoFavor; 
    private double saldoContra; 
    private int mesesPendientes; 
    private boolean disponibilidad; 
    private double valorLocal; 
    private double valorAdministrativo; 
    
    public Locales(){
        
    }

    public Locales(int codigoLocal, double saldoFavor, double saldoContra, int mesesPendientes, boolean disponibilidad, double valorLocal, double valorAdministrativo) {
        this.codigoLocal = codigoLocal;
        this.saldoFavor = saldoFavor;
        this.saldoContra = saldoContra;
        this.mesesPendientes = mesesPendientes;
        this.disponibilidad = disponibilidad;
        this.valorLocal = valorLocal;
        this.valorAdministrativo = valorAdministrativo;
    }

    public int getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(int codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public double getSaldoFavor() {
        return saldoFavor;
    }

    public void setSaldoFavor(double saldoFavor) {
        this.saldoFavor = saldoFavor;
    }

    public double getSaldoContra() {
        return saldoContra;
    }

    public void setSaldoContra(double saldoContra) {
        this.saldoContra = saldoContra;
    }

    public int getMesesPendientes() {
        return mesesPendientes;
    }

    public void setMesesPendientes(int mesesPendientes) {
        this.mesesPendientes = mesesPendientes;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void IsDisponibilidad(boolean disponibilidad){
        this.disponibilidad = disponibilidad;
    }
    
    public double getValorLocal() {
        return valorLocal;
    }

    public void setValorLocal(double valorLocal) {
        this.valorLocal = valorLocal;
    }

    public double getValorAdministrativo() {
        return valorAdministrativo;
    }

    public void setValorAdministrativo(double valorAdministrativo) {
        this.valorAdministrativo = valorAdministrativo;
    }

    
    public String toString() {
       return getCodigoLocal() + " Q " + getValorLocal() + " Q " + isDisponibilidad();
    }
     
}


