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
import org.andycuyuch.bean.Departamentos;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class DepartamentosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Departamentos> listaDepartamento;
    
    /*Botones*/
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar; 
    @FXML private Button btnEditar; 
    @FXML private Button btnReporte; 
    /*Contenidos*/
    @FXML private TextField txtCodigoDepartamento; 
    @FXML private TextField txtNombreDepartamento; 
    /*Tabla y Columna*/
    @FXML private TableView tblDepartamento; 
    @FXML private TableColumn colCodigoDepartamento;
    @FXML private TableColumn colNombreDepartamento;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblDepartamento.setItems(getDepartamento());
        colCodigoDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("codigoDepartamento"));
        colNombreDepartamento.setCellValueFactory(new  PropertyValueFactory<Departamentos, String>("nombreDepartamento"));
        
    }
    
    public ObservableList<Departamentos> getDepartamento(){
        ArrayList<Departamentos> lista = new ArrayList<Departamentos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDepartamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Departamentos(resultado.getInt("codigoDepartamento"),
                                          resultado.getString("nombreDepartamento")));
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        
        
        return listaDepartamento = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){ /**/
        if(tblDepartamento.getSelectionModel().getSelectedItem() != null){
         txtCodigoDepartamento.setText(String.valueOf(((Departamentos)tblDepartamento.getSelectionModel().getSelectedItem()).getCodigoDepartamento()));
         txtNombreDepartamento.setText(((Departamentos)tblDepartamento.getSelectionModel().getSelectedItem()).getNombreDepartamento());   
    }else{
        txtCodigoDepartamento.clear();
        txtNombreDepartamento.clear();
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
                    cargarDatos();
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
                    if(tblDepartamento.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null,  " ¿Quiere eliminar el registro?", "Eliminar Departamentos",
                               JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDepartamentos(?)}");
                            procedimiento.setInt(1, ((Departamentos)tblDepartamento.getSelectionModel().getSelectedItem()).getCodigoDepartamento());
                            procedimiento.execute();
                            listaDepartamento.remove(tblDepartamento.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                        JOptionPane.showMessageDialog(null,"Tiene que seleccionar un elemento");
                    }
            }
        }
    
    
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO: 
                    if(tblDepartamento.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        imgEditar.setImage(new Image("/org/andycuyuch/images/Update.png"));
                        imgReporte.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoDeOperacion = operaciones.ACTUALIZAR;
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
                tipoDeOperacion = operaciones.NINGUNO;
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
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
            }
        }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDepartamentos (?,?)}");
            Departamentos registro = ((Departamentos)tblDepartamento.getSelectionModel().getSelectedItem());
            registro.setNombreDepartamento(txtNombreDepartamento.getText());
            procedimiento.setInt(1, registro.getCodigoDepartamento());
            procedimiento.setString(2, registro.getNombreDepartamento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void guardar(){
        if(txtNombreDepartamento.getText().isEmpty()){
             JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");  
        }else{
        Departamentos registro = new Departamentos();
        registro.setNombreDepartamento(txtNombreDepartamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDepartamentos(?) }");
            procedimiento.setString(1, registro.getNombreDepartamento());
            procedimiento.execute();
            listaDepartamento.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
    
    
    public void desactivarControles(){
        txtCodigoDepartamento.setEditable(false);
        txtNombreDepartamento.setEditable(false);
    }
   
    public void activarControles(){
        txtCodigoDepartamento.setEditable(false);
        txtNombreDepartamento.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoDepartamento.clear();
        txtNombreDepartamento.clear();
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
