package org.andycuyuch.bean;

public class Proveedores {
   private int codigoProveedores;
   private String NITProveedor;
   private String servicioPrestado;
   private String telefonoProveedor;
   private String direccionProveedor;
   private double saldoFavor;
   private double saldoContra;
   private int administracion_codigoAdministracion;
   
   public Proveedores(){
       
   }

    public Proveedores(int codigoProveedores, String NITProveedor, String servicioPrestado, String telefonoProveedor, String direccionProveedor, double saldoFavor, double saldoContra, int administracion_codigoAdministracion) {
        this.codigoProveedores = codigoProveedores;
        this.NITProveedor = NITProveedor;
        this.servicioPrestado = servicioPrestado;
        this.telefonoProveedor = telefonoProveedor;
        this.direccionProveedor = direccionProveedor;
        this.saldoFavor = saldoFavor;
        this.saldoContra = saldoContra;
        this.administracion_codigoAdministracion = administracion_codigoAdministracion;
    }

    public int getCodigoProveedores() {
        return codigoProveedores;
    }

    public void setCodigoProveedores(int codigoProveedores) {
        this.codigoProveedores = codigoProveedores;
    }

    public String getNITProveedor() {
        return NITProveedor;
    }

    public void setNITProveedor(String NITProveedor) {
        this.NITProveedor = NITProveedor;
    }

    public String getServicioPrestado() {
        return servicioPrestado;
    }

    public void setServicioPrestado(String servicioPrestado) {
        this.servicioPrestado = servicioPrestado;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
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

    public int getAdministracion_codigoAdministracion() {
        return administracion_codigoAdministracion;
    }

    public void setAdministracion_codigoAdministracion(int administracion_codigoAdministracion) {
        this.administracion_codigoAdministracion = administracion_codigoAdministracion;
    }

    @Override
    public String toString() {
        return getCodigoProveedores() +" NIT: " + NITProveedor +","+ " Telefono: " + telefonoProveedor + "," +" CodigoAdministracion: " + administracion_codigoAdministracion + "";
    }

    
    
   
}
