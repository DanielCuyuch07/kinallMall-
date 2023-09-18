package org.andycuyuch.controller;

import java.net.URL;
import java.sql.Date;
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
import org.andycuyuch.bean.CuentasPorCobrar;
import org.andycuyuch.bean.Locales;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class CuentasPorCobrarController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ACTUALIZAR,CANCELAR,ELIMINAR,NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Administracion> listaAdministracion; 
    private ObservableList<Cliente> listaCliente; 
    private ObservableList<Locales> listaLocales;
    private ObservableList<CuentasPorCobrar> listaCuentasPorCobrar;
    
    
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private TextField txtCodigoCobrar;
    @FXML private TextField txtCodigoFactura; 
    @FXML private TextField txtAnio;
    @FXML private TextField txtMes; 
    @FXML private TextField txtValorNetoPago;
    @FXML private TextField txtEstadoPago; 
    
    
    @FXML private ComboBox cmbCodigoAdministracion; 
    @FXML private ComboBox cmbCodiCliente; 
    @FXML private ComboBox cmbCodigoLocal; 
    
    @FXML private TableView tblCuentasPorCobrar; 
    @FXML private TableColumn colCodigoCuentasPorCobrar; 
    @FXML private TableColumn colCodigoFactura; 
    @FXML private TableColumn colAnio; 
    @FXML private TableColumn colMes; 
    @FXML private TableColumn colValorNetoPago; 
    @FXML private TableColumn colEstadoPago;
    @FXML private TableColumn colCodigoAdministracion; 
    @FXML private TableColumn colCodigoCliente; 
    @FXML private TableColumn colCodigoLocal; 
    
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar; 
    @FXML private ImageView imgReporte; 
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCuentasPorCobrar.setItems(getCuentasPorCobrar());
        colCodigoCuentasPorCobrar.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoCuentasPorCobrar"));
        colCodigoFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("codigoFactura")); 
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("mes"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Double>("valorNetoPago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("estadoPago"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoAdministracion"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoCliente"));
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoLocal"));
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodiCliente.setItems(getCliente());
        cmbCodigoLocal.setItems(getLocales());      
    }
    
    public void seleccionarElemento(){
        if(tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null){ 
          txtCodigoCobrar.setText(String.valueOf(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorCobrar())); 
           txtCodigoFactura.setText(String.valueOf(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoFactura()));
           txtAnio.setText(String.valueOf(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getAnio()));
           txtValorNetoPago.setText(String.valueOf(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
           txtEstadoPago.setText(String.valueOf(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago()));
           txtMes.setText(String.valueOf(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getMes()));
           cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
           cmbCodiCliente.getSelectionModel().select(buscarClientes(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCliente()));
           cmbCodigoLocal.getSelectionModel().select(buscarLocales(((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoLocal()));
         }else{
            
        }
    }


    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar(){
        ArrayList<CuentasPorCobrar> lista = new ArrayList<CuentasPorCobrar>();
        try{
            PreparedStatement  procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorCobrar()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CuentasPorCobrar(resultado.getInt("codigoCuentasPorCobrar"),
                                               resultado.getString("codigoFactura"),
                                               resultado.getString("anio"),
                                               resultado.getInt("mes"),
                                               resultado.getDouble("valorNetoPago"),
                                               resultado.getString("estadoPago"),
                                               resultado.getInt("codigoAdministracion"),
                                               resultado.getInt("codigoCliente"),
                                               resultado.getInt("codigoLocal")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCuentasPorCobrar = FXCollections.observableArrayList(lista);
    }
           
    /*--------------------------------------------------Administracion----------------------------------------*/
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
    /*--------------------------------------------------Buscar Administracion------------------------------------*/
    public Administracion buscarAdministracion(int codigoAdministracion ){
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
 /*-----------------------------------------------------------------------------------------------------------------*/  
    
/*-------------------------------------------------------------Locales------------------------------------------------*/
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
/*-------------------------------------------------------Buscar Locales-----------------------------------------------------*/   
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
 /*----------------------------------------------------------------------------------------------------------------------------*/
    
 /*------------------------------------------------------Buscar Cliente--------------------------------------------------------*/
    
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
    
    public Cliente buscarClientes(int codigoCliente){
        Cliente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarClientes(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cliente(registro.getInt("codigoCliente"),
                                        registro.getString("nombreCliente"),
                                        registro.getString("apellidoCliente"),
                                        registro.getString("telefonoCliente"),
                                        registro.getString("direccionCliente"),
                                        registro.getString("email"),
                                        registro.getInt("codigoLocal"),
                                        registro.getInt("codigoAdministracion"),
                                        registro.getInt("codigoTipoCliente"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
/*--------------------------------------------------------------------------------------------------------------------------------*/    
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
              cmbCodigoAdministracion.setDisable(false);
              cmbCodiCliente.setDisable(false);
              cmbCodigoLocal.setDisable(false);
              activarControles();
              limpiarControles(); 
              btnNuevo.setText("Guardar");
              btnEliminar.setText("Cancelar");
              btnEditar.setDisable(true);
              btnReporte.setDisable(true);
              imgNuevo.setImage(new Image("/org/andycuyuch/images/save.png"));
              imgEliminar.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
              tipoDeOperaciones = operaciones.GUARDAR;
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
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break; 
        }
    }
    
    public void guardar(){
        if(txtCodigoFactura.getText().isEmpty() || txtAnio.getText().isEmpty()
           || txtMes.getText().isEmpty() || txtValorNetoPago.getText().isEmpty() || txtEstadoPago.getText().isEmpty()
               ||cmbCodigoAdministracion.getSelectionModel().isEmpty() || cmbCodiCliente.getSelectionModel().isEmpty() ||
                     cmbCodigoLocal.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
        }else{
            CuentasPorCobrar registro = new CuentasPorCobrar(); 
            registro.setCodigoFactura(txtCodigoFactura.getText());
            registro.setAnio(txtAnio.getText());
            registro.setMes(Integer.parseInt(txtMes.getText()));
            registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
            registro.setEstadoPago(txtEstadoPago.getText());
            registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
            registro.setCodigoCliente(((Cliente)cmbCodiCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setCodigoLocal(((Locales)cmbCodigoLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentasPorCobrar(?,?,?,?,?,?,?,?)}");
                procedimiento.setString(1, registro.getCodigoFactura());
                procedimiento.setString(2, registro.getAnio());
                procedimiento.setInt(3, registro.getMes());
                procedimiento.setDouble(4, registro.getValorNetoPago());
                procedimiento.setString(5, registro.getEstadoPago());
                procedimiento.setInt(6, registro.getCodigoAdministracion());
                procedimiento.setInt(7, registro.getCodigoCliente());
                procedimiento.setInt(8, registro.getCodigoLocal());
                procedimiento.execute();
                listaCuentasPorCobrar.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
}
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/andycuyuch/images/NuevoScene.png"));
                imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png"));
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, " ¿Quiere eliminar el registro?", "Eliminar  CuentasPorCobrar",
                          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCuentasPorCobrar(?)}");
                            procedimiento.setInt(1, ((CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorCobrar());
                            procedimiento.execute();
                            listaCuentasPorCobrar.remove(tblCuentasPorCobrar.getSelectionModel().getSelectedIndex());
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
        switch(tipoDeOperaciones){
            case NINGUNO: 
                if(tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null){
                   cmbCodigoAdministracion.setDisable(true);
                   cmbCodiCliente.setDisable(true);
                   cmbCodigoLocal.setDisable(true);
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Cancelar");
                   imgEditar.setImage(new Image("/org/andycuyuch/images/Update.png"));
                   imgReporte.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
                   btnNuevo.setDisable(true);
                   btnEliminar.setDisable(true);
                   activarControles();
                   tipoDeOperaciones = operaciones.ACTUALIZAR;  
                }else{
                   JOptionPane.showMessageDialog(null, "Tiene que seleccionar el elemento");  
                }
                break;
            case ACTUALIZAR:
                actualizar();
                cmbCodigoAdministracion.setDisable(true);
                cmbCodiCliente.setDisable(true);
                cmbCodigoLocal.setDisable(true);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/andycuyuch/images/Edit.png"));
                imgReporte.setImage(new Image("/org/andycuyuch/images/Report.png"));
                btnEliminar.setDisable(false);
                btnNuevo.setDisable(false);
                limpiarControles();
                desactivarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }       
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentasPorCobrar(?,?,?,?,?,?)}");
            CuentasPorCobrar registro = (CuentasPorCobrar)tblCuentasPorCobrar.getSelectionModel().getSelectedItem();
            registro.setCodigoFactura(txtCodigoFactura.getText());
            registro.setAnio(txtAnio.getText());
            registro.setMes(Integer.parseInt(txtMes.getText()));
            registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
            registro.setEstadoPago(txtEstadoPago.getText());
            procedimiento.setInt(1, registro.getCodigoCuentasPorCobrar());
            procedimiento.setString(2, registro.getCodigoFactura());
            procedimiento.setString(3, registro.getAnio());
            procedimiento.setInt(4, registro.getMes());
            procedimiento.setDouble(5, registro.getValorNetoPago()); 
            procedimiento.setString(6, registro.getEstadoPago());
            procedimiento.execute();
            listaCuentasPorCobrar.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
   public void reporte(){
        switch(tipoDeOperaciones){
             case ACTUALIZAR: 
                 cmbCodigoAdministracion.setDisable(true);
                 cmbCodiCliente.setDisable(true);
                 cmbCodigoLocal.setDisable(true);
                 desactivarControles();
                 limpiarControles();
                 btnEditar.setText("Editar");
                 btnReporte.setText("Reporte");
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false);
                 imgEditar.setImage(new Image("/org/andycuyuch/images/Edit.png"));
                 imgReporte.setImage(new Image("/org/andycuyuch/images/Report.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 break;  
         }
     }
    
    
    
    
    
    
    public void activarControles(){
        txtCodigoCobrar.setEditable(false);
        txtCodigoFactura.setEditable(true);
        txtAnio.setEditable(true);
        txtMes.setEditable(true);
        txtValorNetoPago.setEditable(true);
        txtEstadoPago.setEditable(true);
        cmbCodigoAdministracion.setEditable(false);
        cmbCodiCliente.setEditable(false);
        cmbCodigoLocal.setEditable(false);
    }
    
    public void desactivarControles(){
        txtCodigoCobrar.setEditable(false);
        txtCodigoFactura.setEditable(false);
        txtAnio.setEditable(false);
        txtMes.setEditable(false);
        txtValorNetoPago.setEditable(false);
        txtEstadoPago.setEditable(false);
        cmbCodigoAdministracion.setEditable(true);
        cmbCodiCliente.setEditable(true);
        cmbCodigoLocal.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoCobrar.clear();
        txtCodigoFactura.clear();
        txtAnio.clear();
        txtMes.clear();
        txtValorNetoPago.clear();
        txtEstadoPago.clear();
        cmbCodigoAdministracion.getSelectionModel().clearSelection();
        cmbCodiCliente.getSelectionModel().clearSelection();
        cmbCodigoLocal.getSelectionModel().clearSelection();
    }
    
    public void ventanaLocales(){
        escenarioPrincipal.ventanaLocales();
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
