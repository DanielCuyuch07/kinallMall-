
package org.andycuyuch.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.andycuyuch.system.Principal;

public class ProgramadorController implements Initializable{
    
    private Principal escenarioPrincipal;
    @FXML Button btnProgramador;
    @FXML Button btnAdministracion; 
    @FXML Label lblNombres; 
    @FXML Label lblApellidos; 
    @FXML Label lblTitulo; 

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    private void opciones(ActionEvent event){
        if(event.getSource() == btnProgramador){
            lblNombres.setText("Andy Daniel");
            lblApellidos.setText("Cuyuch Chitay");
            lblTitulo.setText("Informatico");
        }else if(event.getSource() == btnAdministracion){
            lblNombres.setText("Fundación");
            lblApellidos.setText("Kinal");
            lblTitulo.setText("2021");
        }
    }
   

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
  
}
