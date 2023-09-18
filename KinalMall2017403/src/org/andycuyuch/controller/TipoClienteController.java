package org.andycuyuch.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.andycuyuch.bean.TipoCliente;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class TipoClienteController implements Initializable  {
     private enum operaciones {NUEVO,CANCELAR,GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO}
     private operaciones tipoDeOperacion = operaciones.NINGUNO; 
     private Principal escenarioPrincipal;
     private ObservableList<TipoCliente> listaTipoCliente; 

     /*Inyectamos*/
     @FXML private Button btnNuevo; 
     @FXML private Button btnEliminar; 
     @FXML private Button btnEditar; 
     @FXML private Button btnReporte; 
     
     /*Agregamos tablas y columnas*/
     @FXML private TableView tblTipoCliente; 
     @FXML private TableColumn colCodigoTipoCliente; 
     @FXML private TableColumn colDescripcion; 
     
     /*Agregamos los TextField*/
     @FXML private TextField txtCodigoTipoCliente; 
     @FXML private TextField txtDescripcion; 
     
     /*Agregamos las imagenes*/
     @FXML private ImageView imgNuevo;
     @FXML private ImageView imgEliminar; 
     @FXML private ImageView imgEditar; 
     @FXML private ImageView imgReporte; 
          
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();    
    }
    
     public void cargarDatos(){
      tblTipoCliente.setItems(getTipoCliente());
      colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("codigoTipoCliente"));
      colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("descripcion"));
    }
    
    public ObservableList<TipoCliente> getTipoCliente(){
        ArrayList<TipoCliente> cliente = new ArrayList<TipoCliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoCliente()}"); 
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                cliente.add(new TipoCliente(resultado.getInt("codigoTipoCliente"),
                                                resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
    }
       
        return listaTipoCliente = FXCollections.observableArrayList(cliente);
    }

    public void seleccionElemento(){
        if(tblTipoCliente.getSelectionModel().getSelectedItem() != null){
        txtCodigoTipoCliente.setText(String.valueOf(((TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
        txtDescripcion.setText(((TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
    }else{
            txtCodigoTipoCliente.clear();
            txtDescripcion.clear();
        }
}
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO: 
               activarControles();
               btnNuevo.setText("Guardar");
               btnEliminar.setText("Cancelar");
               btnEditar.setDisable(true);
               btnReporte.setDisable(true);
               imgNuevo.setImage(new Image("/org/andycuyuch/images/save.png"));
               imgEliminar.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
               tipoDeOperacion = operaciones.GUARDAR;
               break; 
            case GUARDAR: 
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/andycuyuch/images/NuevoScene.png"));
                imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png")); 
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
  
    public void guardar(){ 
        if(txtDescripcion.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
        }else{
        TipoCliente registro = new TipoCliente();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoCliente(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoCliente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/andycuyuch/images/NuevoScene.png"));
                imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png"));
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break; 
            default:
                if(tblTipoCliente.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, " ¿Quiere eliminar el registro?", "Eliminar TipoCliente",
                           JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION){
                   try{
                       PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoCliente(?)}");
                       procedimiento.setInt(1, ((TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente());
                       procedimiento.execute();
                       listaTipoCliente.remove(tblTipoCliente.getSelectionModel().getSelectedIndex());
                       limpiarControles();
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                       
                }
                }else{
                    JOptionPane.showMessageDialog(null, "Tiene que seleccionar un elemento");
                }
        }
    }
   
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoCliente.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/andycuyuch/images/Update.png"));
                    imgReporte.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "tiene que seleccionar el elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/andycuyuch/images/Edit.png"));
                imgReporte.setImage(new Image("/org/andycuyuch/images/Report.png"));
                btnEliminar.setDisable(false);
                btnNuevo.setDisable(false);
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;         
        }
    }
    
        public void actualizar(){
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_EditarTipoCliente(?,?)}");
                TipoCliente registro = (TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem();
                registro.setDescripcion(txtDescripcion.getText());
                procedimiento.setInt(1, registro.getCodigoTipoCliente());
                procedimiento.setString(2, registro.getDescripcion());
                procedimiento.execute(); 
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void reporte(){
            switch(tipoDeOperacion){
                case ACTUALIZAR: 
                    desactivarControles();
                    limpiarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEditar.setImage(new Image("/org/andycuyuch/images/Edit.png"));
                    imgReporte.setImage(new Image("/org/andycuyuch/images/Report.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
            }
        }
       
    public void activarControles(){
        txtCodigoTipoCliente.setEditable(false);
        txtDescripcion.setEditable(true);
    }
    
    public void desactivarControles(){
        txtCodigoTipoCliente.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoTipoCliente.clear();
        txtDescripcion.clear();
    }
    
    
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaCliente(){
        escenarioPrincipal.VentanaCliente();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }   
}
