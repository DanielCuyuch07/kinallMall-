package org.andycuyuch.controller;

/**/
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.andycuyuch.system.Principal;

public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
           
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    public void ventanaAdministracion(){
        escenarioPrincipal.ventanaAdministracion();
    }
  /* Ya que tenemos el metodo el menu principal controller debe conocer el metodo
     la vista debe saber quien es el controlador (ScenenBuilder)*/
    public void ventanaLocales(){
        escenarioPrincipal.ventanaLocales();
    }
    
    public void ventanaTipoCliente(){
        escenarioPrincipal.ventanaTipoCLiente();
    }
    
    public void ventanaDepartamentos(){
        escenarioPrincipal.ventanaDepartamentos();
    }
    
    public void ventanaCliente(){
        escenarioPrincipal.VentanaCliente();
    }
    
    public void ventanaProveedores(){
        escenarioPrincipal.ventanaProveedores();
    }
    
    public void ventanaCuentasPagar(){
        escenarioPrincipal.VentanaCuentasPagar();
    }
    public void ventanaHorarios(){
        escenarioPrincipal.ventanaHorarios();
    }
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }
    
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    
    public void ventanaCuentasPorCobrar(){
        escenarioPrincipal.ventanaCuentasPorCobrar();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    
}
