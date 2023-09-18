package org.andycuyuch.controller;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.andycuyuch.bean.Login;
import org.andycuyuch.bean.Usuario;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class LoginController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<Usuario> listaUsuario;
   
    @FXML private Button btnLogin;
    @FXML private TextField txtUsuarioMaster; 
    @FXML private TextField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }
    
    public ObservableList<Usuario> getUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuarios()}");
            ResultSet resultado = procedimiento.executeQuery(); 
            while(resultado.next()){
                lista.add(new Usuario( resultado.getInt("codigoUsuario"),
                                        resultado.getString("nombreUsuario"),
                                        resultado.getString("apellidoUsuario"),
                                        resultado.getString("usuarioLogin"),
                                        resultado.getString("contrasena")));
            }
        }catch(Exception e){
            e.printStackTrace();
        } 
        return listaUsuario = FXCollections.observableArrayList(lista);
    }
    
    @FXML 
    private void Login()throws NoSuchAlgorithmException{
        Login lo = new Login();
        int dos = 0;
        boolean bandera = false;
        lo.setUsuarioMaster(txtUsuarioMaster.getText());
        lo.setPasswordLogin(txtPassword.getText()); 
        while( dos < getUsuario().size()){
            String user = getUsuario().get(dos).getUsuarioLogin();
            String pass = getUsuario().get(dos).getContraseña();
            if(user.equals(lo.getUsuarioMaster()) && pass.equals(lo.getPasswordLogin())){
                /*Corregir*/
                JOptionPane.showMessageDialog(null, "Sesion Iniciada Correctamente\n" + getUsuario().get(dos).getNombreUsuario()
                +" "+ getUsuario().get(dos).getApellidoUsuario());
                escenarioPrincipal.menuPrincipal();
                dos = getUsuario().size();
                bandera = true;
            }
         dos++;
        }
         if(bandera == false){
                JOptionPane.showMessageDialog(null, "Usuario o contraseña Incorrecta");
        }
        
     }

  
    
   
    
    
    
    
    
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    
    
    
    
    
}
