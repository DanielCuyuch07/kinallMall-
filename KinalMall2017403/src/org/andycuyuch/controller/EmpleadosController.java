package org.andycuyuch.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.andycuyuch.bean.Cargos;
import org.andycuyuch.bean.Departamentos;
import org.andycuyuch.bean.Empleados;
import org.andycuyuch.bean.Horarios;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class EmpleadosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,CANCELAR,ACTUALIZAR,ELIMINAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Empleados> listaEmpleados; 
    private ObservableList<Departamentos> listaDepartamentos; 
    private ObservableList<Horarios> listaHorarios; 
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Cargos> listaCargos; 
   
    private DatePicker fechaContrato; 
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar; 
    @FXML private Button btnEditar; 
    @FXML private Button btnReporte; 
    
    @FXML private TableView tblVistaEmpleados; 
    @FXML private TableColumn colCodigoEmpleado; 
    @FXML private TableColumn colNombreEmpleado; 
    @FXML private TableColumn colApellidoEmpleado;
    @FXML private TableColumn colCorreoElectronico; 
    @FXML private TableColumn colTelefonoEmpleado; 
    @FXML private TableColumn colSueldo; 
    @FXML private TableColumn colFechaContrato; 
    @FXML private TableColumn colCodigoDepartamento; 
    @FXML private TableColumn colCodigoHorarios; 
    @FXML private TableColumn colCodigoAdministracion; 
    @FXML private TableColumn colCodigoCargos;
      
    @FXML private ComboBox cmbCodigoDepartamentos; 
    @FXML private ComboBox cmbCodigoHorarios; 
    @FXML private ComboBox cmbCodigoAdministracion; 
    @FXML private ComboBox cmbCodigoCargos; 
    @FXML private GridPane grpFechaDeContrato; 
    
    @FXML private TextField txtCodigoEmpleado; 
    @FXML private TextField txtNombreEmpleado; 
    @FXML private TextField txtApellidoEmpleado; 
    @FXML private TextField txtCorreoElectronico; 
    @FXML private TextField txtTelefonoEmpleado; 
    @FXML private TextField txtSueldo; 
    
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar; 
    @FXML private ImageView imgReporte; 
    
     
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fechaContrato = new DatePicker(Locale.ENGLISH);
        /*Va estar establecido de forma ingles (hora en ingles)*/
        fechaContrato.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        /*Vamos establecer que nuestro calendario establesca el dia el cual se esta ejecutando */
        fechaContrato.getCalendarView().todayButtonTextProperty().set("Today");
        fechaContrato.getCalendarView().setShowWeeks(false);
        grpFechaDeContrato.add(fechaContrato, 3, 1);
        fechaContrato.getStylesheets().add("/org/andycuyuch/resourse/DatePicker.css"); 
        /*Falsta Css*/
        cargarDatos();
       //desactivarControles();
    }
    
    public void seleccionarElemento(){
        if(tblVistaEmpleados.getSelectionModel().getSelectedItem() != null){
        txtCodigoEmpleado.setText(String.valueOf(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados()));
        txtNombreEmpleado.setText(String.valueOf(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado()));
        txtApellidoEmpleado.setText(String.valueOf(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado()));
        txtCorreoElectronico.setText(String.valueOf(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getCorreoElectronico()));
        txtTelefonoEmpleado.setText(String.valueOf(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getTelefonoEmpleado()));
        fechaContrato.selectedDateProperty().set(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getFechaContrato());
        txtSueldo.setText(String.valueOf(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
        cmbCodigoDepartamentos.getSelectionModel().select(buscarDepartamentos(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getCodigoDepartamentos()));
        cmbCodigoHorarios.getSelectionModel().select(buscarHorarios(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getCodigoHorario()));
        cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        cmbCodigoCargos.getSelectionModel().select(buscarCargos(((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargo())); 
        }else{
            
        }
    }
        
    public void cargarDatos(){
        tblVistaEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleados"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colCorreoElectronico.setCellValueFactory(new PropertyValueFactory<Empleados, String>("correoElectronico"));
        colTelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefonoEmpleado"));
        colFechaContrato.setCellValueFactory(new PropertyValueFactory<Empleados, Date>("fechaContrato"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colCodigoDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("codigoDepartamentos"));
        colCodigoHorarios.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("codigoHorario"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoCargos.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("codigoCargo"));
        cmbCodigoDepartamentos.setItems(getDepartamentos());
        cmbCodigoHorarios.setItems(getHorarios());
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodigoCargos.setItems(getCargos());        
    }
   
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery(); 
            while(resultado.next()){
                lista.add(new Empleados(resultado.getInt("codigoEmpleados"),
                                        resultado.getString("nombreEmpleado"),
                                        resultado.getString("apellidoEmpleado"),
                                        resultado.getString("correoElectronico"),
                                        resultado.getString("telefonoEmpleado"),
                                        resultado.getDate("fechaContrato"),
                                        resultado.getDouble("sueldo"),
                                        resultado.getInt("codigoDepartamentos"),
                                        resultado.getInt("codigoCargo"),
                                        resultado.getInt("codigoHorario"),
                                        resultado.getInt("codigoAdministracion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
   
        return  listaEmpleados = FXCollections.observableArrayList(lista);
    }
     
 /*---------------------------------------------------------------Departamentos-------------------------------------------*/
    public ObservableList<Departamentos> getDepartamentos(){
        ArrayList<Departamentos> lista = new ArrayList<Departamentos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDepartamentos()}");
            ResultSet resultado = procedimiento.executeQuery(); 
            while(resultado.next()){
                lista.add(new Departamentos(resultado.getInt("codigoDepartamento"),
                                            resultado.getString("nombreDepartamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDepartamentos = FXCollections.observableArrayList(lista); 
    }
    
    public Departamentos buscarDepartamentos(int codigoDepartamentos){
        Departamentos resultado = null; 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDepartamentos(?)}");
            procedimiento.setInt(1, codigoDepartamentos);
            ResultSet registro = procedimiento.executeQuery(); 
            while(registro.next()){
                resultado = new Departamentos(registro.getInt("codigoDepartamento"),
                                              registro.getString("nombreDepartamento"));
            }
        }catch(Exception e){
           e.printStackTrace();
        }   
        return resultado; 
    }
  
/*----------------------------------------------------------------HORARIOS-------------------------------------------------*/  
    public ObservableList<Horarios> getHorarios(){
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarHorarios()}");
           ResultSet resultado = procedimiento.executeQuery(); 
           while(resultado.next()){
               lista.add(new Horarios(resultado.getInt("codigoHorario"),
                                      resultado.getString("horarioEntrada"),
                                      resultado.getString("horarioSalida"),
                                      resultado.getBoolean("lunes"),
                                      resultado.getBoolean("martes"),
                                      resultado.getBoolean("miercoles"),
                                      resultado.getBoolean("jueves"),
                                      resultado.getBoolean("viernes")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }              
        return listaHorarios = FXCollections.observableArrayList(lista); 
    }
    
    public Horarios buscarHorarios(int codigoHorario){
        Horarios resultado = null; 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarHorarios(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horarios(registro.getInt("codigoHorario"),
                                         registro.getString("horarioEntrada"),
                                         registro.getString("horarioSalida"),
                                         registro.getBoolean("lunes"),
                                         registro.getBoolean("martes"),
                                         registro.getBoolean("miercoles"),
                                         registro.getBoolean("jueves"),
                                         registro.getBoolean("viernes"));
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
       
        return resultado; 
    }
     
 /*-------------------------------------------------------Administracion------------------------------------------------------*/
    
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
    
 /*----------------------------------------------------Cargos---------------------------------------------*/ 
    
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
    
    public Cargos buscarCargos (int codigoCargo){
        Cargos resultado = null; 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCargos(?)}");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargos(registro.getInt("codigoCargo"),
                                       registro.getString("nombreCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado; 
    }
    
/*----------------------------------------------------Botones--------------------------------------------*/
       
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO: 
               cmbCodigoDepartamentos.setDisable(false);
               cmbCodigoHorarios.setDisable(false);
               cmbCodigoAdministracion.setDisable(false);
               cmbCodigoCargos.setDisable(false);
               activarControles();
               limpiarControles();
               btnNuevo.setText("Guardar");
               btnEliminar.setText("cancelar");
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
                if(tblVistaEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, " ¿Quiere eliminar el registro?", "Eliminar  CuentasPorPagar",
                          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                            procedimiento.setInt(1, ((Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados());
                            procedimiento.execute();
                            listaEmpleados.remove(tblVistaEmpleados.getSelectionModel().getSelectedIndex());
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
                if(tblVistaEmpleados.getSelectionModel().getSelectedItem() != null){
                   cmbCodigoDepartamentos.setDisable(true);
                  cmbCodigoHorarios.setDisable(true);
                  cmbCodigoAdministracion.setDisable(true);
                  cmbCodigoCargos.setDisable(true);
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Cancelar");
                   btnNuevo.setDisable(true);
                   btnEliminar.setDisable(true);
                   imgEditar.setImage(new Image("/org/andycuyuch/images/Update.png"));
                   imgReporte.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
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
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/andycuyuch/images/Edit.png"));
                imgReporte.setImage(new Image("/org/andycuyuch/images/Report.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO; 
                cargarDatos(); 
                break;
        }
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleados(?,?,?,?,?,?,?)}");
            Empleados registro = (Empleados)tblVistaEmpleados.getSelectionModel().getSelectedItem();
            registro.setNombreEmpleado(txtNombreEmpleado.getText());
            registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
            registro.setCorreoElectronico(txtCorreoElectronico.getText());
            registro.setTelefonoEmpleado(txtTelefonoEmpleado.getText());
            registro.setFechaContrato(fechaContrato.getSelectedDate());
            registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
            procedimiento.setInt(1, registro.getCodigoEmpleados());
            procedimiento.setString(2, registro.getNombreEmpleado());
            procedimiento.setString(3, registro.getApellidoEmpleado());
            procedimiento.setString(4, registro.getCorreoElectronico());
            procedimiento.setString(5, registro.getTelefonoEmpleado());
            procedimiento.setDate(6,new java.sql.Date(registro.getFechaContrato().getTime()));
            procedimiento.setDouble(7, registro.getSueldo());
            procedimiento.execute(); 
            listaEmpleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

   public void reporte(){
           switch(tipoDeOperacion){
             case ACTUALIZAR: 
                 cmbCodigoDepartamentos.setDisable(true);
                 cmbCodigoHorarios.setDisable(true);
                 cmbCodigoAdministracion.setDisable(true);
                 cmbCodigoCargos.setDisable(true);
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
    
    
    
    public void guardar(){
        if(txtNombreEmpleado.getText().isEmpty() || txtApellidoEmpleado.getText().isEmpty() || txtCorreoElectronico.getText().isEmpty() ||
                txtTelefonoEmpleado.getText().isEmpty() || txtSueldo.getText().isEmpty() || cmbCodigoDepartamentos.getSelectionModel().isEmpty()
                  || cmbCodigoCargos.getSelectionModel().isEmpty() || cmbCodigoHorarios.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos"); 
        }else{
       Empleados registro = new Empleados();
       registro.setNombreEmpleado(txtNombreEmpleado.getText());
       registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
       registro.setCorreoElectronico(txtCorreoElectronico.getText());
       registro.setTelefonoEmpleado(txtTelefonoEmpleado.getText());
       registro.setFechaContrato(fechaContrato.getSelectedDate());
       registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
       registro.setCodigoDepartamentos(((Departamentos)cmbCodigoDepartamentos.getSelectionModel().getSelectedItem()).getCodigoDepartamento());
       registro.setCodigoCargo(((Cargos)cmbCodigoCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
       registro.setCodigoHorario(((Horarios)cmbCodigoHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
       registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?,?,?,?)}");
           procedimiento.setString(1, registro.getNombreEmpleado());
           procedimiento.setString(2, registro.getApellidoEmpleado());
           procedimiento.setString(3, registro.getCorreoElectronico());
           procedimiento.setString(4, registro.getTelefonoEmpleado());
           procedimiento.setDate(5, new java.sql.Date(registro.getFechaContrato().getTime()));
           procedimiento.setDouble(6, registro.getSueldo());
           procedimiento.setInt(7, registro.getCodigoDepartamentos());
           procedimiento.setInt(8, registro.getCodigoCargo());
           procedimiento.setInt(9, registro.getCodigoHorario());
           procedimiento.setInt(10, registro.getCodigoAdministracion());
           procedimiento.execute();
           listaEmpleados.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
    }
}
    
 /*--------------------------------------------------------------Medotos--------------------------------------------
    */   
    public void desactivarControles(){
        txtCodigoEmpleado.setDisable(false);
        txtNombreEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtCorreoElectronico.setEditable(false);
        txtTelefonoEmpleado.setEditable(false);
        fechaContrato.setDisable(true);
        txtSueldo.setEditable(false);
        cmbCodigoDepartamentos.setEditable(true);
        cmbCodigoCargos.setEditable(true);
        cmbCodigoHorarios.setEditable(true);
        cmbCodigoAdministracion.setEditable(true);
    }
    
    
    
    public void activarControles(){
       txtCodigoEmpleado.setEditable(false);
       txtNombreEmpleado.setEditable(true);
       txtApellidoEmpleado.setEditable(true);
       txtCorreoElectronico.setEditable(true);
       txtTelefonoEmpleado.setEditable(true);
       fechaContrato.setDisable(false);
       txtSueldo.setEditable(true);
       cmbCodigoDepartamentos.setEditable(false);
       cmbCodigoCargos.setEditable(false);
       cmbCodigoHorarios.setEditable(false);
       cmbCodigoAdministracion.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtCorreoElectronico.clear();
        txtTelefonoEmpleado.clear();
        fechaContrato.setPromptText("");
        txtSueldo.clear();
        cmbCodigoDepartamentos.getSelectionModel().clearSelection();
        cmbCodigoCargos.getSelectionModel().clearSelection();
        cmbCodigoHorarios.getSelectionModel().clearSelection();
        cmbCodigoAdministracion.getSelectionModel().clearSelection();
    }
    
    public void menuPrincipal(){
       escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaCargos(){
       escenarioPrincipal.ventanaCargos();
    }
    
    public void ventanaDepartamentos(){
       escenarioPrincipal.ventanaDepartamentos();
    }
     
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
