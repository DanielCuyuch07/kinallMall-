package org.andycuyuch.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.andycuyuch.bean.Usuario;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class UsuarioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
/* private ObservableList<Usuario> listaUsuario; */
    
    
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar; 
    
    @FXML private TextField txtNombreUsuario; 
    @FXML private TextField txtApellidoUsuario; 
    @FXML private TextField txtUsuario; 
    @FXML private TextField txtPassword; 
    
    @FXML private ImageView imgNuevo; 
 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    
    
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO: 
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/andycuyuch/images/save.png")); 
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR: 
                guardar();
                desactivarContoles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/andycuyuch/images/TipoCliente.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;   
        }
    }
    
    public void guardar(){
        if(txtNombreUsuario.getText().isEmpty() || txtUsuario.getText().isEmpty() || txtApellidoUsuario.getText().isEmpty()||
                txtPassword.getText().isEmpty()){
           JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
    }else{
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContraseña(txtPassword.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContraseña());
            procedimiento.execute();
            ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
    public void activarControles(){
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        
    }
    
    
    public void desactivarContoles(){
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
    }
    
    public void limpiarControles(){
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
