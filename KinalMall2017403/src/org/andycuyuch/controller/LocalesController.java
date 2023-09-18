package org.andycuyuch.controller;

import java.net.URL;/*M.A*/
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle; /*M.A*/
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; /*M.A*/
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.andycuyuch.bean.Locales;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;



public class LocalesController  implements Initializable{
    private enum operacionesLocal{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO} /*2.4 pára que todo inicie,nesesitamos un elemento por defecto (NINGUNO)*/
    private operacionesLocal tipoDeOperacion = operacionesLocal.NINGUNO;
    private Principal escenarioPrincipal; /*1.4 Agregamos del paquete system*/
    private ObservableList <Locales> listaLocales;
    
    /*Vamos a agregar todos los objetos que voy a inyectar de lado de javaFX al lado del controlador*/
    /*Agragamos los botones*/
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar; 
    @FXML private Button btnReporte; 
    
    /*las imagenes de los  botones van a cambiar cuando se den las opciones*/
    /* Agregamos los objetos tipo TextField */
    @FXML private TextField txtCodigoLocal; 
    @FXML private TextField txtSaldoFavor;
    @FXML private TextField txtSaldoContra; 
    @FXML private TextField txtMesesPendientes; 
    @FXML private TextField txtDisponibilidad; 
    @FXML private TextField txtValorLocal; 
    @FXML private TextField txtValorAdministrativo;
    
    /*Nesesitamos la parte de TableView, y las columnas tenemos que generar el objeto cocmpleto mas 
    cada una de las columnas por que cuando nosotros metamos los datos deben ser de forma indivuidual 
    por columna */
    @FXML private TableView tblLocales; 
    @FXML private TableColumn colCodigoLocal; 
    @FXML private TableColumn colSaldoFavor; 
    @FXML private TableColumn colSaldoContra; 
    @FXML private TableColumn colMesesPendientes; 
    @FXML private TableColumn colDisponibilidad; 
    @FXML private TableColumn colValorLocal;
    @FXML private TableColumn colValorAdministrativos; 
    
    
    /*Ahora como digimos que las imagenes van a cambiar ahora las colocamos */
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar; 
    @FXML private ImageView imgReporte; 
   

    
    @Override /*Metodos abstractos*/
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();
    }
    
    
    public void cargarDatos(){
      tblLocales.setItems(getLocales());
      colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Locales,Integer>("codigoLocal"));/**/
      colSaldoFavor.setCellValueFactory(new  PropertyValueFactory<Locales, Double>("saldoFavor"));
      colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, Double>("saldoContra"));
      colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales,Integer>("mesesPendientes"));
      colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("disponibilidad"));/**/
      colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorLocal"));
      colValorAdministrativos.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorAdministrativo"));
    }
   
   public ObservableList<Locales> getLocales(){
       ArrayList<Locales> local = new ArrayList<Locales>();
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales}");
           ResultSet resultado = procedimiento.executeQuery();
           while(resultado.next()){
               local.add(new Locales(resultado.getInt("codigoLocal"),
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
       
       
       return listaLocales = FXCollections.observableArrayList(local);
   }
      
   public void seleccionarElemento(){
       if(tblLocales.getSelectionModel().getSelectedItem() !=null){
       txtCodigoLocal.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getCodigoLocal()));
       txtSaldoFavor.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
       txtSaldoContra.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra()));
       txtMesesPendientes.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));
       txtDisponibilidad.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad()));
       txtValorLocal.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
       txtValorAdministrativo.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getValorAdministrativo()));
   }   else{
           txtCodigoLocal.clear();
           txtSaldoFavor.clear();
           txtSaldoContra.clear();
           txtMesesPendientes.clear();
           txtDisponibilidad.clear();
           txtValorLocal.clear();
           txtValorAdministrativo.clear();
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
                    tipoDeOperacion = operacionesLocal.GUARDAR;
               break;
           case GUARDAR:
               guardar();
                     desactivarControles();
                     limpiarControles();
                     btnNuevo.setText("Nuevo");
                     btnEliminar.setText("Eliminar");
                     btnReporte.setDisable(false);
                     btnEditar.setDisable(false);
                     imgNuevo.setImage(new Image("/org/andycuyuch/images/NuevoScene.png"));
                     imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png"));
                     tipoDeOperacion = operacionesLocal.NINGUNO;
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
               btnEditar.setDisable(false);
               btnReporte.setDisable(false);
               imgNuevo.setImage(new Image("/org/andycuyuch/images/NuevoScene.png"));
               imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png"));
               tipoDeOperacion = operacionesLocal.NINGUNO;
               break;
           default:
               if(tblLocales.getSelectionModel().getSelectedItem() != null){
                   int pregunta = JOptionPane.showConfirmDialog(null, " ¿Deseas eliminar este registro", "Eliminar Locales",
                           JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(pregunta == JOptionPane.YES_OPTION){
                       try{
                           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarLocales(?)}");
                           procedimiento.setInt(1,((Locales)tblLocales.getSelectionModel().getSelectedItem()).getCodigoLocal());
                           procedimiento.execute();
                           listaLocales.remove(tblLocales.getSelectionModel().getSelectedIndex());
                           limpiarControles();
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                   }
               }else{
                   JOptionPane.showMessageDialog(null, "Tiene que seleccionar un elemnto");
               }
       }
   }
   
   public void guardar(){
       if(txtSaldoFavor.getText().isEmpty() || txtSaldoContra.getText().isEmpty() || txtMesesPendientes.getText().isEmpty() ||
          txtDisponibilidad.getText().isEmpty() || txtValorAdministrativo.getText().isEmpty() || txtValorLocal.getText().isEmpty()){
           JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
       }else{
        Locales registro = new Locales();
        registro.setSaldoContra(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoFavor(Double.parseDouble(txtSaldoContra.getText()));
        registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        registro.setDisponibilidad(Boolean.parseBoolean(txtDisponibilidad.getText()));
        registro.setValorAdministrativo(Double.parseDouble(txtValorAdministrativo.getText()));
        registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarLocales(?,?,?,?,?,?)}");
            procedimiento.setDouble(1, registro.getSaldoFavor());
            procedimiento.setDouble(2, registro.getSaldoContra());
            procedimiento.setInt(3, registro.getMesesPendientes());
            procedimiento.setBoolean(4, registro.isDisponibilidad());
            procedimiento.setDouble(5, registro.getValorLocal());
            procedimiento.setDouble(6, registro.getValorAdministrativo());
            procedimiento.execute();
            listaLocales.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
   }  
}
   
   public void actualizar(){
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarLocales(?,?,?,?,?,?,?)}");
           Locales registro = ((Locales)tblLocales.getSelectionModel().getSelectedItem());
           registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
           registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
           registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
           registro.setDisponibilidad(Boolean.parseBoolean(txtDisponibilidad.getText()));
           registro.setValorAdministrativo(Double.parseDouble(txtValorAdministrativo.getText()));
           registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
           procedimiento.setInt(1, registro.getCodigoLocal());
           procedimiento.setDouble(2, registro.getSaldoFavor());
           procedimiento.setDouble(3, registro.getSaldoContra());
           procedimiento.setInt(4, registro.getMesesPendientes());
           procedimiento.setBoolean(5, registro.isDisponibilidad());
           procedimiento.setDouble(6, registro.getValorLocal());
           procedimiento.setDouble(7, registro.getValorAdministrativo());
           procedimiento.execute();
           listaLocales.add(registro);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   
    public void editar(){
       switch(tipoDeOperacion){
           case NINGUNO:
               if(tblLocales.getSelectionModel().getSelectedItem() != null){
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Cancelar");
                   imgEditar.setImage(new Image("/org/andycuyuch/images/Update.png"));
                   imgReporte.setImage(new Image("/org/andycuyuch/images/cancelar.png"));
                   btnNuevo.setDisable(true);
                   btnEliminar.setDisable(true);
                   activarControles();
                   tipoDeOperacion = operacionesLocal.ACTUALIZAR;
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
               tipoDeOperacion = operacionesLocal.NINGUNO;
               cargarDatos();
               break;
       }
   }
   
   /*Actualizar*/
   
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
                tipoDeOperacion = operacionesLocal.NINGUNO;
                break;
       }
   }
    
    public void activarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtMesesPendientes.setEditable(true);
        txtDisponibilidad.setEditable(true);
        txtValorLocal.setEditable(true);
        txtValorAdministrativo.setEditable(true);
    }
   
    public void desactivarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtMesesPendientes.setEditable(false);
        txtDisponibilidad.setEditable(false);
        txtValorLocal.setEditable(false);
        txtValorAdministrativo.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoLocal.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtMesesPendientes.clear();
        txtDisponibilidad.clear();
        txtValorLocal.clear();
        txtValorAdministrativo.clear();
    }
   
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaCuentasPorCobrar(){
        escenarioPrincipal.ventanaCuentasPorCobrar();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
