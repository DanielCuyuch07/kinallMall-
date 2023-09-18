package org.andycuyuch.controller;

/*Tiempo de Corrido por que lo ejecuta cuendo el proyecto se ejecuta*/
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat; /*Calendario */
import java.util.ArrayList;
import java.util.Locale; /*Calendario se agregar de java util locale*/
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.andycuyuch.bean.Administracion;
import org.andycuyuch.bean.CuentasPorPagar;
import org.andycuyuch.bean.Proveedores;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

/*Paso 1 crear el controlador,Clase atributos, Agregar ventana, hacer que se levante la vista*
/*Paso 2 inyectamos nuestros enumeradores.Agregar nuesto tipo de operacion donde queremos que comienze,
agregamos los observablesList que vamos a utilizar*/
/*Paso 3 Realizar el cargar datos con sus respectivos observablesList*/

public class CuentasPorPagarController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,CANCELAR,EDITAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedores> listaProveedores;
    
    private DatePicker fechaLimite;
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar; 
    @FXML private ImageView imgReporte; 
    
    @FXML private TextField txtCodigoCuentaPagar; 
    @FXML private TextField txtNumeroFactura; 
    @FXML private TextField txtEstadoPago; 
    @FXML private TextField txtValorNetoPago;
    @FXML private TableView tblCuentasPorPagar;
    
    @FXML private ComboBox cmbCodigoAdministracion;
    @FXML private ComboBox cmbCodigoProveedor;
    @FXML private GridPane grpFechaLimite;
    
    @FXML private TableColumn colCodigoCuentasPorPagar;
    @FXML private TableColumn colNumeroFactura; 
    @FXML private TableColumn colFechaLimitePago; 
    @FXML private TableColumn colEstadoPago; 
    @FXML private TableColumn colValorNetoPago;
    @FXML private TableColumn colCodigoAdministracion; 
    @FXML private TableColumn colCodigoProveedores;
/*-------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fechaLimite = new DatePicker(Locale.ENGLISH); /*Va estar establecido de forma ingles (hora en ingles)*/
        fechaLimite.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        /*Vamos establecer que nuestro calendario establesca el dia el cual se esta ejecutando */
        fechaLimite.getCalendarView().todayButtonTextProperty().set("Today");
        fechaLimite.getCalendarView().setShowWeeks(false);
        grpFechaLimite.add(fechaLimite, 5, 0);
        fechaLimite.getStylesheets().add("/org/andycuyuch/resourse/DatePicker.css"); /*Falsta Css*/
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblCuentasPorPagar.setItems(getCuentasPorPagar());
        colCodigoCuentasPorPagar.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar,Integer>("codigoCuentasPorPagar"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("numeroFactura"));
        colFechaLimitePago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar,String>("estadoPago"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar,Double>("valorNetoPago"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoProveedores.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodigoProveedor.setItems(getProveedores());
       
    }
    
    public void seleccionarElemento(){
        if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
        txtCodigoCuentaPagar.setText(String.valueOf(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorPagar()));
        txtNumeroFactura.setText(String.valueOf(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        fechaLimite.selectedDateProperty().set(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago());
        txtEstadoPago.setText(String.valueOf(String.valueOf(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago())));
        txtValorNetoPago.setText(String.valueOf(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
        cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        cmbCodigoProveedor.getSelectionModel().select(buscarProveedores(((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
     }else{
        txtCodigoCuentaPagar.clear();
        txtNumeroFactura.clear();
        txtEstadoPago.clear();
        txtValorNetoPago.clear();
        cmbCodigoAdministracion.getSelectionModel().clearSelection();
        cmbCodigoProveedor.getSelectionModel().clearSelection();
        fechaLimite.setPromptText("");
        } 
 }
    
/*------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*Para Obtener nuestros datos*/
    public ObservableList<CuentasPorPagar> getCuentasPorPagar(){ /*Obtener los datos y pasarlos a un arraylist*/
        ArrayList<CuentasPorPagar> lista = new ArrayList<CuentasPorPagar>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorPagar()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CuentasPorPagar(resultado.getInt("codigoCuentasPorPagar"),
                                              resultado.getString("numeroFactura"),
                                              resultado.getDate("fechaLimitePago"),
                                              resultado.getString("estadoPago"),
                                              resultado.getDouble("valorNetoPago"),
                                              resultado.getInt("codigoAdministracion"),
                                              resultado.getInt("codigoProveedor")));
            }     
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCuentasPorPagar = FXCollections.observableArrayList(lista);
    }
 /*-----------------------------------------ObservableList<Administracion>----------------------------------------------------------*/
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
    
 /*-------------------------------------ObservableList<Proveedores>----------------------------------------------------------*/
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
        }catch(Exception e ){
            e.printStackTrace();
        }
        return listaProveedores = FXCollections.observableArrayList(lista);
    }
 /*-------------------------------------------------------------------------------------------------------------------------------------------------------------*/
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
    
    public Proveedores buscarProveedores(int codigoProveedores){
        Proveedores resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedores(?)}");
            procedimiento.setInt(1, codigoProveedores);
            ResultSet registro = procedimiento.executeQuery(); 
            while(registro.next()){
                resultado = new Proveedores(registro.getInt("codigoProveedores"),
                                            registro.getString("NITProveedor"),
                                            registro.getString("servicioPrestado"),
                                            registro.getString("telefonoProveedor"),
                                            registro.getString("direccionProveedor"),
                                            registro.getDouble("saldoFavor"),
                                            registro.getDouble("saldoContra"),
                                            registro.getInt("administracion_codigoAdministracion"));
            }
        }catch(Exception e ){
            e.printStackTrace();
        }   
        return resultado;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                cmbCodigoAdministracion.setDisable(false);
                cmbCodigoProveedor.setDisable(false);
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
                imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;        
        }
    }
    
    public void guardar(){
        if(txtNumeroFactura.getText().isEmpty() || txtEstadoPago.getText().isEmpty() || txtValorNetoPago.getText().isEmpty() ||
                cmbCodigoAdministracion.getSelectionModel().isEmpty() || cmbCodigoProveedor.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
        }else{
        CuentasPorPagar registro = new CuentasPorPagar();
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(fechaLimite.getSelectedDate());
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedores());/*Todos estos datos tenemos que pasarlos a MYSQL*/
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentasPorPagar(?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNumeroFactura());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaLimitePago().getTime()));
            procedimiento.setString(3, registro.getEstadoPago());
            procedimiento.setDouble(4, registro.getValorNetoPago());
            procedimiento.setInt(5, registro.getCodigoAdministracion());
            procedimiento.setInt(6, registro.getCodigoProveedor());
            procedimiento.execute();
            listaCuentasPorPagar.add(registro);
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
                  if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
                      int respuesta = JOptionPane.showConfirmDialog(null, " ¿Quiere eliminar el registro?", "Eliminar  CuentasPorPagar",
                             JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if(respuesta == JOptionPane.YES_OPTION){
                          try{
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCuentasPorPagar(?)}");
                              procedimiento.setInt(1, ((CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorPagar());
                              procedimiento.execute();
                              listaCuentasPorPagar.remove(tblCuentasPorPagar.getSelectionModel().getSelectedIndex());
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
                if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() !=null){
                    cmbCodigoAdministracion.setDisable(true);
                    cmbCodigoProveedor.setDisable(true);
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
                cmbCodigoProveedor.setDisable(true);
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
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentasPorPagar(?,?,?,?,?)}");
                CuentasPorPagar registro = (CuentasPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem();
                registro.setNumeroFactura(txtNumeroFactura.getText());
                registro.setFechaLimitePago(fechaLimite.getSelectedDate());
                registro.setEstadoPago(txtEstadoPago.getText());
                registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
                procedimiento.setInt(1,registro.getCodigoCuentasPorPagar());
                procedimiento.setString(2, registro.getNumeroFactura());
                procedimiento.setDate(3,new java.sql.Date(registro.getFechaLimitePago().getTime()));
                procedimiento.setString(4, registro.getEstadoPago());
                procedimiento.setDouble(5, registro.getValorNetoPago());
                procedimiento.execute();
                listaCuentasPorPagar.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    
    public void reporte(){
           switch(tipoDeOperacion){
             case ACTUALIZAR: 
                 cmbCodigoAdministracion.setDisable(true);
                 cmbCodigoProveedor.setDisable(true);
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
        txtCodigoCuentaPagar.setEditable(false);
        txtNumeroFactura.setEditable(false);
        txtEstadoPago.setEditable(false);/**/
        txtValorNetoPago.setEditable(false);
        cmbCodigoAdministracion.setEditable(true);
        cmbCodigoProveedor.setEditable(true);
        fechaLimite.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoCuentaPagar.setDisable(false);
        txtNumeroFactura.setEditable(true);
        txtEstadoPago.setEditable(true);
        txtValorNetoPago.setEditable(true);
        cmbCodigoAdministracion.setEditable(false);
        cmbCodigoProveedor.setEditable(false);
        fechaLimite.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCuentaPagar.clear();
        txtNumeroFactura.clear();
        txtEstadoPago.clear();
        txtValorNetoPago.clear();
        cmbCodigoAdministracion.getSelectionModel().clearSelection();
        cmbCodigoProveedor.getSelectionModel().clearSelection();
        fechaLimite.setPromptText("");
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
    
    public void ventanaProveedores(){
      escenarioPrincipal.ventanaProveedores();
    }
    
}
