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
import org.andycuyuch.bean.Cliente;
import org.andycuyuch.bean.Locales;
import org.andycuyuch.bean.TipoCliente;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class ClienteController implements Initializable {
     private Principal escenarioPrincipal;
     private enum operaciones{GUARDAR,NUEVO,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
     private operaciones tipoDeOperacion = operaciones.NINGUNO;
     
     private ObservableList<Cliente> listaCliente;
     private ObservableList<Administracion> listaAdministracion;
     private ObservableList<Locales> listaLocales; 
     private ObservableList<TipoCliente> listaTipoCliente;
     
     @FXML private Button btnNuevo; 
     @FXML private Button btnEliminar;
     @FXML private Button btnEditar;
     @FXML private Button btnReporte;
     
     @FXML private TextField txtCodigoCliente; 
     @FXML private TextField txtNombreCliente; 
     @FXML private TextField txtApellidosCliente;
     @FXML private TextField txtTelefonoCliente;
     @FXML private TextField txtDireccionCliente;
     @FXML private TextField txtEmail;
     
     @FXML private ComboBox cmbCodigoLocal;
     @FXML private ComboBox cmbCodigoAdministracion;
     @FXML private ComboBox cmbCodigoTipoCliente;

     @FXML private TableView tblCliente;
     @FXML private TableColumn colCodigoCliente; 
     @FXML private TableColumn colNombreCliente;
     @FXML private TableColumn colApellidoCliente; 
     @FXML private TableColumn colTelefonoCliente; 
     @FXML private TableColumn colDireccionCliente; 
     @FXML private TableColumn colEmail;
     @FXML private TableColumn colCodigoLocal;  
     @FXML private TableColumn colCodigoAdministracion; 
     @FXML private TableColumn colCodigoTipoCliente;
     
     @FXML private ImageView imgNuevo; 
     @FXML private ImageView imgEliminar;
     @FXML private ImageView imgEditar; 
     @FXML private ImageView imgReporte;
      
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCliente.setItems(getCliente());
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Cliente,Integer>("codigoCliente"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Cliente,String>("nombreCliente"));
        colApellidoCliente.setCellValueFactory(new PropertyValueFactory<Cliente,String>("apellidoCliente"));
        colTelefonoCliente.setCellValueFactory(new PropertyValueFactory<Cliente,String>("telefonoCliente"));
        colDireccionCliente.setCellValueFactory(new PropertyValueFactory<Cliente,String>("direccionCliente"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Cliente,String>("email"));
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("codigoLocal"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<TipoCliente,Integer>("codigoTipoCliente"));
        cmbCodigoLocal.setItems(getLocales());
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodigoTipoCliente.setItems(getTipoCliente());
    }
    
    public void seleccionarElemento(){
        if(tblCliente.getSelectionModel().getSelectedItem() != null){
        txtCodigoCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txtNombreCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getNombreCliente()));
        txtApellidosCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getApellidoCliente()));
        txtTelefonoCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getTelefonoCliente()));
        txtDireccionCliente.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getDireccionCliente()));
        txtEmail.setText(String.valueOf(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getEmail()));
        cmbCodigoLocal.getSelectionModel().select(buscarLocales(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoLocal()));
        cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        cmbCodigoTipoCliente.getSelectionModel().select(buscarTipoCliente(((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));  
        }else{
            
        }
    }
    
    public ObservableList<Cliente> getCliente(){
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Cliente(resultado.getInt("codigoCliente"),
                                      resultado.getString("nombreCliente"),
                                      resultado.getString("apellidoCliente"),
                                      resultado.getString("TelefonoCliente"),
                                      resultado.getString("direccionCliente"),
                                      resultado.getString("email"),
                                      resultado.getInt("codigoLocal"),
                                      resultado.getInt("codigoAdministracion"),
                                      resultado.getInt("codigoTipoCliente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaCliente =FXCollections.observableArrayList(lista);
    }
   

    public ObservableList<Locales> getLocales(){
        ArrayList<Locales> lista = new ArrayList<Locales>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Locales(resultado.getInt("codigoLocal"),
                                      resultado.getDouble("saldoFavor"),
                                      resultado.getDouble("saldoContra"),
                                      resultado.getInt("mesesPendientes"),
                                      resultado.getBoolean("disponibilidad"),
                                      resultado.getDouble("valorLocal"),
                                      resultado.getDouble("valorAdministracion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        return listaLocales = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Administracion> getAdministracion(){
        ArrayList<Administracion> lista = new ArrayList<Administracion>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}"); 
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Administracion(resultado.getInt("codigoAdministracion"),
                                                resultado.getString("direccion"),
                                                resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
    }
        
        
        return listaAdministracion = FXCollections.observableArrayList(lista);
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
    
     public Locales buscarLocales(int codigoLocales){
         Locales resultado = null;
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarLocales(?)}");
             procedimiento.setInt(1, codigoLocales);
             ResultSet registro = procedimiento.executeQuery();
             while(registro.next()){
                 resultado = new Locales(registro.getInt("codigoLocal"),
                                        registro.getDouble("saldoFavor"),
                                        registro.getDouble("saldoContra"),
                                        registro.getInt("mesesPendientes"),
                                        registro.getBoolean("disponibilidad"),
                                        registro.getDouble("valorLocal"),
                                        registro.getDouble("valorAdministracion"));
                 
             }
     }catch(Exception e){
             e.printStackTrace();
         }
            
         return resultado;
     }
     
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
     
     public TipoCliente buscarTipoCliente(int codigoTipoCliente){
         TipoCliente resultado = null; 
         try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call SP_BuscarTipoCliente(?)}");
         procedimiento.setInt(1, codigoTipoCliente);
         ResultSet registro = procedimiento.executeQuery();
         while(registro.next()){
             resultado = new TipoCliente(registro.getInt("codigoTipoCliente"),
                                         registro.getString("descripcion"));
                                        
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
                    cmbCodigoLocal.setDisable(false);
                    cmbCodigoTipoCliente.setDisable(false);
                    activarControles();
                    limpiarControles();
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
                imgEliminar.setImage(new Image("org/andycuyuch/images/EliminarScene.png"));
                tipoDeOperacion = operaciones.NINGUNO; 
                cargarDatos();
                break;
         }
     }
     
     public void guardar(){
         if(txtNombreCliente.getText().isEmpty() || cmbCodigoLocal.getSelectionModel().isEmpty() || cmbCodigoAdministracion.getSelectionModel().isEmpty() ||
                 cmbCodigoTipoCliente.getSelectionModel().isEmpty()){         
         }else{
         Cliente registro = new Cliente();
         registro.setNombreCliente(txtNombreCliente.getText());
         registro.setApellidoCliente(txtApellidosCliente.getText());
         registro.setTelefonoCliente(txtTelefonoCliente.getText());
         registro.setDireccionCliente(txtDireccionCliente.getText());
         registro.setEmail(txtEmail.getText());
         registro.setCodigoLocal(((Locales)cmbCodigoLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
         registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
         registro.setCodigoTipoCliente(((TipoCliente)cmbCodigoTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente());
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarClientes(?,?,?,?,?,?,?,?)}");
             procedimiento.setString(1, registro.getNombreCliente());
             procedimiento.setString(2, registro.getApellidoCliente());
             procedimiento.setString(3, registro.getTelefonoCliente());
             procedimiento.setString(4, registro.getDireccionCliente());
             procedimiento.setString(5, registro.getEmail());
             procedimiento.setInt(6, registro.getCodigoLocal());
             procedimiento.setInt(7, registro.getCodigoAdministracion());
             procedimiento.setInt(8, registro.getCodigoTipoCliente());
             procedimiento.execute();
             listaCliente.add(registro);
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
                 if(tblCliente.getSelectionModel().getSelectedItem() != null){
                     int respuesta = JOptionPane.showConfirmDialog(null, " ¿Quiere eliminar el registro?", "Eliminar  cliente",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if(respuesta == JOptionPane.YES_OPTION){
                         try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarClientes(?)}");
                             procedimiento.setInt(1, ((Cliente)tblCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
                             procedimiento.execute();
                             listaCliente.remove(tblCliente.getSelectionModel().getSelectedIndex());
                             limpiarControles();
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                     }
                 }else {
                      JOptionPane.showMessageDialog(null, "Tiene que seleccionar un elemento");
                 }
         }
     }
     
     
   public void editar(){
       switch(tipoDeOperacion){
            case NINGUNO:
                     if(tblCliente.getSelectionModel().getSelectedItem() != null){
                         cmbCodigoAdministracion.setDisable(true);
                         cmbCodigoLocal.setDisable(true);
                         cmbCodigoTipoCliente.setDisable(true);
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
                     limpiarControles();
                     desactivarControles();
                     tipoDeOperacion = operaciones.NINGUNO;
                     cargarDatos();
                     break;
             }
         }
     
     public void actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_EditarClientes(?,?,?,?,?)}");
             Cliente registro = (Cliente)tblCliente.getSelectionModel().getSelectedItem();
             registro.setNombreCliente(txtNombreCliente.getText());
             registro.setApellidoCliente(txtApellidosCliente.getText());
             registro.setDireccionCliente(txtTelefonoCliente.getText());
             registro.setDireccionCliente(txtDireccionCliente.getText());
             registro.setEmail(txtEmail.getText());
             procedimiento.setInt(1, registro.getCodigoCliente());
             procedimiento.setString(2, registro.getNombreCliente());
             procedimiento.setString(3, registro.getApellidoCliente());
             procedimiento.setString(4, registro.getTelefonoCliente());
             procedimiento.setString(4, registro.getDireccionCliente());
             procedimiento.setString(5, registro.getEmail());
             procedimiento.execute();
             listaCliente.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
         
     
     public void reporte(){
         switch(tipoDeOperacion){
             case ACTUALIZAR: 
                 cmbCodigoAdministracion.setDisable(true);
                 cmbCodigoLocal.setDisable(true);
                 cmbCodigoTipoCliente.setDisable(true);
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
     
    public void desactivarControles(){
       txtCodigoCliente.setEditable(false);
       txtNombreCliente.setEditable(false);
       txtApellidosCliente.setEditable(false);
       txtTelefonoCliente.setEditable(false);
       txtDireccionCliente.setEditable(false);
       txtEmail.setEditable(false);
       cmbCodigoLocal.setEditable(true);
       cmbCodigoAdministracion.setEditable(true);/**/
       cmbCodigoTipoCliente.setEditable(true);
    }
    
    public void activarControles(){
       txtCodigoCliente.setEditable(false);
       txtNombreCliente.setEditable(true);
       txtApellidosCliente.setEditable(true);
       txtTelefonoCliente.setEditable(true);
       txtDireccionCliente.setEditable(true);
       txtEmail.setEditable(true);
       cmbCodigoLocal.setEditable(false);
       cmbCodigoAdministracion.setEditable(false);
       cmbCodigoTipoCliente.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCliente.clear();
        txtNombreCliente.clear();
        txtApellidosCliente.clear();
        txtTelefonoCliente.clear();
        txtDireccionCliente.clear();
        txtEmail.clear();
        cmbCodigoLocal.getSelectionModel().clearSelection();
        cmbCodigoAdministracion.getSelectionModel().clearSelection();
        cmbCodigoTipoCliente.getSelectionModel().clearSelection();
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
     
    public void ventanaTipoCliente(){
      escenarioPrincipal.ventanaTipoCLiente();
    }
    
}
