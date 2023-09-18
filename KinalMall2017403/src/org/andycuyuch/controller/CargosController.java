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
import org.andycuyuch.bean.Cargos;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class CargosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operacion {NUEVO,ELIMNAR,GUARDAR,CANCELAR,ACTUALIZAR,NINGUNO}
    private operacion tipoDeOperacion = operacion.NINGUNO;
    private ObservableList<Cargos> listaCargos; 
    
    
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar; 
    @FXML private Button btnEditar; 
    @FXML private Button btnReporte; 
    
    @FXML private TableView tblVistaCargos;
    @FXML private TableColumn colCodigoCargos; 
    @FXML private TableColumn colNombreCargos;
    
    @FXML private TextField txtCodigoCargos; 
    @FXML private TextField txtNombreCargos; 
    
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar; 
    @FXML private ImageView imgReporte; 
     

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
    }
    
    public void cargarDatos(){
        tblVistaCargos.setItems(getCargos());
        colCodigoCargos.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("codigoCargo"));
        colNombreCargos.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombreCargo"));
    }
    
    public void seleccionarElemento(){
        if(tblVistaCargos.getSelectionModel().getSelectedItem() != null){
            txtCodigoCargos.setText(String.valueOf(((Cargos)tblVistaCargos.getSelectionModel().getSelectedItem()).getCodigoCargo()));
            txtNombreCargos.setText(String.valueOf(((Cargos)tblVistaCargos.getSelectionModel().getSelectedItem()).getNombreCargo()));
        }else{
            
        }
    }
    
    public ObservableList<Cargos> getCargos(){
        ArrayList<Cargos> lista = new ArrayList<Cargos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargos(resultado.getInt("codigoCargo"),
                                     resultado.getString("nombreCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaCargos = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guarnar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/andycuyuch/images/save.png"));
                imgEliminar.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
                tipoDeOperacion = operacion.GUARDAR; 
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
                tipoDeOperacion = operacion.NINGUNO; 
                cargarDatos();
                break; 
        }
    }
    
    public void guardar(){
        if(txtNombreCargos.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");    
        }else{
        Cargos registro = new Cargos();
        registro.setNombreCargo(txtNombreCargos.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargos(?)}");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.execute();
            listaCargos.add(registro);
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
                btnNuevo.setText("nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/andycuyuch/images/NuevoScene.png"));
                imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png"));
                tipoDeOperacion = operacion.NINGUNO; 
                break; 
            default: 
                if(tblVistaCargos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,  "¿Quiere eliminar el registro?", "Eliminar Cargos",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                        procedimiento.setInt(1, ((Cargos)tblVistaCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                        procedimiento.execute(); 
                        listaCargos.remove(tblVistaCargos.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else{
                    JOptionPane.showMessageDialog(null, "Tiene que seleccionar el elemento");
                }         
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO: 
                if(tblVistaCargos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/andycuyuch/images/Update.png"));
                    imgReporte.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operacion.ACTUALIZAR; 
                }else{
                    JOptionPane.showMessageDialog(null, "Tiene que seleccionar el elemento");
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
                    tipoDeOperacion = operacion.NINGUNO;
                    cargarDatos();
                    break;          
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
                    tipoDeOperacion = operacion.NINGUNO;
                    break;
            }
        }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCargos(?,?)}");
            Cargos  registro = ((Cargos)tblVistaCargos.getSelectionModel().getSelectedItem());
            registro.setNombreCargo(txtNombreCargos.getText());
            procedimiento.setInt(1, registro.getCodigoCargo());
            procedimiento.setString(2,registro.getNombreCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
    public void limpiarControles(){
        txtCodigoCargos.clear();
        txtNombreCargos.clear();
    }
    
    public void activarControles(){
        txtCodigoCargos.setEditable(false);
        txtNombreCargos.setEditable(true);
    }
    
    public void desactivarControles(){
        txtCodigoCargos.setEditable(false);
        txtNombreCargos.setEditable(false);
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
   
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
