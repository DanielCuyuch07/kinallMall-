package org.andycuyuch.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
/*import javafx.scene.Parent;*/
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.andycuyuch.controller.AdministracionController;
import org.andycuyuch.controller.CargosController;
import org.andycuyuch.controller.ClienteController;
import org.andycuyuch.controller.CuentasPorCobrarController;
import org.andycuyuch.controller.CuentasPorPagarController;
import org.andycuyuch.controller.DepartamentosController;
import org.andycuyuch.controller.EmpleadosController;
import org.andycuyuch.controller.HorariosController;
import org.andycuyuch.controller.LocalesController;
import org.andycuyuch.controller.LoginController;
import org.andycuyuch.controller.MenuPrincipalController;
import org.andycuyuch.controller.ProgramadorController;
import org.andycuyuch.controller.ProveedoresController;
import org.andycuyuch.controller.TipoClienteController;
import org.andycuyuch.controller.UsuarioController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/andycuyuch/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override 
    public void start(Stage escenarioPrincipal) throws IOException {
  
        this.escenarioPrincipal = escenarioPrincipal; 
        this.escenarioPrincipal.setTitle("KinallMall 2017403 "); 
//        Parent root = FXMLLoader.load(getClass().getResource("/org/andycuyuch/view/ProgramadorView.fxml"));
//        Scene escena = new Scene(root); 
//        escenarioPrincipal.setScene(escena); 
        ventanaLogin();
        escenarioPrincipal.show(); /* lo muestra */ 
      }
    
    public void menuPrincipal(){
        try{
           MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",430,435);
           menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /* Programador*/
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml",725,534);
            programador.setEscenarioPrincipal(this);
           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*codigo*/
    public void ventanaAdministracion(){
     try{
         AdministracionController admin = (AdministracionController) cambiarEscena("AdministracionView.fxml",1077,541);
         admin.setEscenarioPrincipal(this);
     }catch(Exception e){
         e.printStackTrace();
     }
    }

    /*El metodo seria(ventanaLocales)  el controlador seria (LocalesController )
     se coloca un nombre(local), se castea, colocamos el Initializable (cambiarEscena)
     la escena se llama LocalesView.fxml y nos traemos el ancho y el alto
     Agregamos del paquete controler localController
     como nos puede dar un error se utiliza un try catch */
    public void ventanaLocales(){
        try{
            LocalesController local = (LocalesController)cambiarEscena("LocalesView.fxml",1137,535);
            local.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoCLiente(){
        try{
            TipoClienteController cliente = (TipoClienteController)cambiarEscena("TipoClienteView.fxml",1008,539);
            cliente.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDepartamentos(){
        try{
            DepartamentosController  departamento = (DepartamentosController)cambiarEscena("DepartamentosView.fxml",1124,557); 
           departamento.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void VentanaCliente(){
        try{
            ClienteController cliente = (ClienteController)cambiarEscena("ClienteView.fxml",1322,534);
            cliente.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
   
    public void ventanaProveedores(){
        try{
            ProveedoresController usuario = (ProveedoresController) cambiarEscena("ProveedoresView.fxml",1350,563);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e ){
            e.printStackTrace();
        }
    }
    
    public void VentanaCuentasPagar(){
        try{
            CuentasPorPagarController cuentas = (CuentasPorPagarController) cambiarEscena("CuentasPorPagarView.fxml",1339,541);
            cuentas.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaHorarios(){
        try{
           HorariosController hora = (HorariosController) cambiarEscena("HorariosView.fxml",1237,604);
           hora.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
      
    
    public void ventanaCargos(){
        try{
            CargosController ventana = (CargosController) cambiarEscena("CargosView.fxml",960,506); 
            ventana.setEscenarioPrincipal(this);
    }catch(Exception e){
            e.printStackTrace();
        }
    }
     
    public void ventanaEmpleados(){
        try{
           EmpleadosController empleado = (EmpleadosController) cambiarEscena("EmpleadosView.fxml",1471,574);
           empleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaCuentasPorCobrar(){
        try{
            CuentasPorCobrarController cuenta = (CuentasPorCobrarController) cambiarEscena("CuentasPorCobrarView.fxml",1467,543);
            cuenta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    public void ventanaUsuario(){
        try{
            UsuarioController personal = (UsuarioController) cambiarEscena("UsuarioView.fxml",981,570);
            personal.setEscenarioPrincipal(this); 
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaLogin(){
        try{
            LoginController inicio = (LoginController) cambiarEscena("LoginView.fxml",932,578);
            inicio.setEscenarioPrincipal(this);
        }catch(Exception e){
         e.printStackTrace();
        }
    }

    
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado; 
    }
    
}
