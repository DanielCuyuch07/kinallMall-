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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.andycuyuch.bean.Administracion;
import org.andycuyuch.bean.Proveedores;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class ProveedoresController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,CANCELAR,ACTUALIZAR,EDITAR,ELIMINAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<Administracion> listaAdministracion;
    
    /*---------------------Inyeccion en los bototes--------------------------*/
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar; 
    @FXML private Button btnReporte; 
    
    
    @FXML private TextField txtCodigoProveedores;
    @FXML private TextField txtNITProveedor; 
    @FXML private TextField txtServicioPrestado; 
    @FXML private TextField txtTelefonoProveedor; 
    @FXML private TextField txtDireccionProveedor; 
    @FXML private TextField txtSaldoFavor; 
    @FXML private TextField txtSaldoContra; 
    
    @FXML private TableView tblProveedores;
    @FXML private ComboBox cmbCodigoAdministracion;
    
    @FXML private TableColumn colCodigoProveedores;
    @FXML private TableColumn colNITProveedor; 
    @FXML private TableColumn colServicioPrestado;
    @FXML private TableColumn colTelefonoProveedor;
    @FXML private TableColumn colDireccionProveedor;
    @FXML private TableColumn colSaldoFavor;
    @FXML private TableColumn colSaldoContra;
    @FXML private TableColumn colCodigoAdministracion;
    
    
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
     
    public void cargarDatos(){
        tblProveedores.setItems(getProveedores());
        colCodigoProveedores.setCellValueFactory(new PropertyValueFactory<Proveedores,Integer>("codigoProveedores"));
        colNITProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores,String>("NITProveedor"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colTelefonoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefonoProveedor"));
        colDireccionProveedor.setCellValueFactory(new PropertyValueFactory<Proveedores,String>("direccionProveedor"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, Double>("saldoContra"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, Double>("saldoContra"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("administracion_codigoAdministracion"));
        cmbCodigoAdministracion.setItems(getAdministracion());  
    }
    
    public void seleccionarElemento(){
        if(tblProveedores.getSelectionModel().getSelectedItem() != null){
        txtCodigoProveedores.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedores()));
        txtNITProveedor.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNITProveedor()));
        txtServicioPrestado.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado()));
        txtTelefonoProveedor.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getTelefonoProveedor()));
        txtDireccionProveedor.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor()));
        txtSaldoFavor.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
        txtSaldoContra.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
        cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getAdministracion_codigoAdministracion()));
        }else{
              txtCodigoProveedores.clear();
              txtNITProveedor.clear();
              txtServicioPrestado.clear();
              txtTelefonoProveedor.clear();
              txtDireccionProveedor.clear();
              txtSaldoFavor.clear();
              txtSaldoContra.clear();
        }
    }
     
    /*-----------------------------------------------------ObservableList  de Proveedores--------------------------------------*/
    public ObservableList<Proveedores> getProveedores(){   
        ArrayList<Proveedores> lista = new ArrayList<Proveedores>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
                ResultSet resultado = procedimiento.executeQuery();
                 while(resultado.next()){
                        lista.add(new Proveedores(resultado.getInt("codigoProveedores"),
                                      resultado.getString("NITProveedor"),
                                      resultado.getString("servicioPrestado"),
                                      resultado.getString("telefonoProveedor"),
                                      resultado.getString("direccionProveedor"),
                                      resultado.getDouble("saldoFavor"),
                                      resultado.getDouble("saldoContra"),
                                      resultado.getInt("administracion_codigoAdministracion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         
        return listaProveedores = FXCollections.observableArrayList(lista);
    }
        
    /*-----------------------------------------------------ObservableList  de Administracion--------------------------------------*/
    public  ObservableList <Administracion> getAdministracion(){
        ArrayList<Administracion> lista = new ArrayList<Administracion>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Administracion(resultado.getInt("CodigoAdministracion"),
                                             resultado.getString("direccion"),
                                             resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }   
        return listaAdministracion = FXCollections.observableArrayList(lista);
    }
     
    /*------------------------------------------------------Buscar Administracion--------------------------------------------------*/
    public Administracion buscarAdministracion(int codigoAdministracion){
        Administracion resultado = null;
        try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
              procedimiento.setInt(1, codigoAdministracion);
              ResultSet registro = procedimiento.executeQuery();
              while(registro.next()){
                  resultado = new Administracion(registro.getInt("codigoAdministracion"),
                                                 registro.getString("direccion"),
                                                 registro.getString("telefono"));
              }
        }catch(Exception e){
            e.printStackTrace();
        }

        return resultado;
    }
    
    public void nuevo(){
       switch(tipoDeOperacion){        
           case NINGUNO:
               cmbCodigoAdministracion.setDisable(false);
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
               guardar(); /* Primero activara un metod (guardar su funcion es guardat los datos)*/
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
        if(txtNITProveedor.getText().isEmpty() || txtServicioPrestado.getText().isEmpty() || txtTelefonoProveedor.getText().isEmpty() ||
        txtDireccionProveedor.getText().isEmpty() || txtSaldoFavor.getText().isEmpty() || txtSaldoContra.getText().isEmpty()    
                || cmbCodigoAdministracion.getSelectionModel().isEmpty()){
                JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
    }else{   
        Proveedores registro = new Proveedores();
        registro.setNITProveedor(txtNITProveedor.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefonoProveedor(txtTelefonoProveedor.getText());
        registro.setDireccionProveedor(txtDireccionProveedor.getText());
        registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
        registro.setAdministracion_codigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProveedores(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNITProveedor());
            procedimiento.setString(2, registro.getServicioPrestado());
            procedimiento.setString(3, registro.getTelefonoProveedor());
            procedimiento.setString(4, registro.getDireccionProveedor());
            procedimiento.setDouble(5, registro.getSaldoFavor());
            procedimiento.setDouble(6, registro.getSaldoContra());
            procedimiento.setInt(7, registro.getAdministracion_codigoAdministracion());
            procedimiento.execute();
            listaProveedores.add(registro);
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
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, " ¿Quiere eliminar el registro?", "Eliminar Tipo cliente",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProveedores(?)}");
                        procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getAdministracion_codigoAdministracion());
                        procedimiento.execute();
                        listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedIndex());
                        limpiarControles();      
                    }catch(Exception e){
                      e.printStackTrace(); 
                    }
                   }
                }else{
                        JOptionPane.showMessageDialog(null, "Tiene que seleccion un elemento");
                }
        }
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProveedores.getSelectionModel().getSelectedItem() !=null){
                    cmbCodigoAdministracion.setDisable(true);
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
                cmbCodigoAdministracion.setDisable(true);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProveedores(?,?,?,?,?,?,?)}");
            Proveedores registro = (Proveedores)tblProveedores.getSelectionModel().getSelectedItem();
            registro.setNITProveedor(txtNITProveedor.getText());
            registro.setServicioPrestado(txtServicioPrestado.getText());
            registro.setTelefonoProveedor(txtTelefonoProveedor.getText());
            registro.setDireccionProveedor(txtDireccionProveedor.getText());
            registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
            registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
            procedimiento.setInt(1, registro.getCodigoProveedores());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getServicioPrestado());
            procedimiento.setString(4, registro.getTelefonoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setDouble(6, registro.getSaldoFavor());
            procedimiento.setDouble(7, registro.getSaldoContra());
            procedimiento.execute();
            listaProveedores.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void Reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                cmbCodigoAdministracion.setDisable(true);
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
    
    
    /*Activar y dessactivar Controles */
    public void activarControles(){
       txtCodigoProveedores.setEditable(false);
       txtNITProveedor.setEditable(true);
       txtServicioPrestado.setEditable(true);
       txtTelefonoProveedor.setEditable(true);
       txtDireccionProveedor.setEditable(true);
       txtSaldoFavor.setEditable(true);
       txtSaldoContra.setEditable(true);
       cmbCodigoAdministracion.setEditable(false);
    }
    
    public void desactivarControles(){
        txtCodigoProveedores.setEditable(false);
        txtNITProveedor.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtTelefonoProveedor.setEditable(false);
        txtDireccionProveedor.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        cmbCodigoAdministracion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProveedores.clear();
        txtNITProveedor.clear();
        txtServicioPrestado.clear();
        txtTelefonoProveedor.clear();
        txtDireccionProveedor.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
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
    
    public void ventanaCuentasPagar(){
      escenarioPrincipal.VentanaCuentasPagar();
    }
    
    
}
