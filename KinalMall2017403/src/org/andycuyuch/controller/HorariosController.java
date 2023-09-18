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
import org.andycuyuch.bean.Horarios;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.system.Principal;

public class HorariosController implements Initializable {
    private Principal escenarioPrincipal; /* Paso: 1*/
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO}; /* Paso: 2*/
    private operaciones  tipoDeOperacion = operaciones.NINGUNO; /* Paso: 2*/
    private ObservableList <Horarios>listaHorarios;/* Paso: 2*/
    
    /* Paso 3*/
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar; 
    @FXML private Button btnEditar; 
    @FXML private Button btnReporte;
    
    /* Paso 3*/
    @FXML private TextField txtCodigoHorarios; 
    @FXML private TextField txtHorarioEntrada;
    @FXML private TextField txtHorarioSalida;
    @FXML private TextField txtLunes; 
    @FXML private TextField txtMartes; 
    @FXML private TextField txtMiercoles; 
    @FXML private TextField txtJueves; 
    @FXML private TextField txtViernes;
    
    /* Paso 3*/
    @FXML private TableColumn colCodigoHorarios;
    @FXML private TableColumn colHorarioEntrada;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes; 
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves; 
    @FXML private TableColumn colViernes; 
      
    /* Paso 3*/
    @FXML private TableView tblHorarios; 
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar; 
    @FXML private ImageView imgReporte; 
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos(); /* Paso 4*/
    }
    
    public void cargarDatos(){ /*Paso 4*/
        tblHorarios.setItems(getHorarios());
        colCodigoHorarios.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("codigoHorario"));
        colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioEntrada"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
    }
    
    public void seleccionarElemento(){ /* Paso: 5 */
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
        txtCodigoHorarios.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario()));
        txtHorarioEntrada.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada()));
        txtHorarioSalida.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida()));
        txtLunes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isLunes()));
        txtMartes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isMartes()));
        txtMiercoles.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles()));
        txtJueves.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isJueves()));
        txtViernes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isViernes()));
        }else{
             txtCodigoHorarios.clear();
             txtHorarioEntrada.clear();
             txtHorarioSalida.clear();
             txtLunes.clear();
             txtMartes.clear();
             txtMiercoles.clear();
             txtJueves.clear();
             txtViernes.clear();
        }
    }
      
    public ObservableList <Horarios> getHorarios(){ /* Paso: 4*/
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
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/andycuyuch/images/Update.png"));
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
        if(txtHorarioEntrada.getText().isEmpty() || txtHorarioSalida.getText().isEmpty() || txtLunes.getText().isEmpty() ||
                txtMartes.getText().isEmpty() || txtMiercoles.getText().isEmpty() || txtJueves.getText().isEmpty() || txtViernes.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
        }else{
        Horarios registro = new Horarios();
        registro.setHorarioEntrada(txtHorarioEntrada.getText());
        registro.setHorarioSalida(txtHorarioSalida.getText());
        registro.setLunes(Boolean.parseBoolean(txtLunes.getText()));
        registro.setMartes(Boolean.parseBoolean(txtMartes.getText()));
        registro.setMiercoles(Boolean.parseBoolean(txtMiercoles.getText()));
        registro.setJueves(Boolean.parseBoolean(txtJueves.getText()));
        registro.setViernes(Boolean.parseBoolean(txtViernes.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarHorarios(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioEntrada());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.isLunes());
            procedimiento.setBoolean(4, registro.isMartes());
            procedimiento.setBoolean(5, registro.isMiercoles());
            procedimiento.setBoolean(6, registro.isJueves());
            procedimiento.setBoolean(7, registro.isViernes());
            procedimiento.execute();
            listaHorarios.add(registro);
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/andycuyuch/images/NuevoScene.png"));
                imgEliminar.setImage(new Image("/org/andycuyuch/images/EliminarScene.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default: 
            if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                   int pregunta = JOptionPane.showConfirmDialog(null, " ¿Deseas eliminar este registro", "Eliminar Horarios",
                           JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(pregunta == JOptionPane.YES_OPTION){
                       try{
                           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarHorarios(?)}");
                           procedimiento.setInt(1,((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                           procedimiento.execute();
                           listaHorarios.remove(tblHorarios.getSelectionModel().getSelectedIndex());
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
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                  if(tblHorarios.getSelectionModel().getSelectedItem() != null){
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
         
         public void actualizar(){
             try{
                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_EditarHorarios(?,?,?,?,?,?,?,?)}");
                 Horarios registro = (Horarios)tblHorarios.getSelectionModel().getSelectedItem();
                 registro.setHorarioEntrada(txtHorarioEntrada.getText());
                 registro.setHorarioSalida(txtHorarioSalida.getText());
                 registro.setLunes(Boolean.parseBoolean(txtLunes.getText()));
                 registro.setMartes(Boolean.parseBoolean(txtMartes.getText()));
                 registro.setMiercoles(Boolean.parseBoolean(txtMiercoles.getText()));
                 registro.setJueves(Boolean.parseBoolean(txtJueves.getText()));
                 registro.setViernes(Boolean.parseBoolean(txtViernes.getText()));
                 
                 procedimiento.setInt(1,registro.getCodigoHorario());
                 procedimiento.setString(2, registro.getHorarioEntrada());
                 procedimiento.setString(3, registro.getHorarioSalida());
                 procedimiento.setBoolean(4, registro.isLunes());
                 procedimiento.setBoolean(5, registro.isMartes());
                 procedimiento.setBoolean(6, registro.isMiercoles());
                 procedimiento.setBoolean(7, registro.isJueves());
                 procedimiento.setBoolean(8, registro.isViernes());
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
    
    public void activarControles(){ /* Pase 6*/
        txtCodigoHorarios.setEditable(false);
        txtHorarioEntrada.setEditable(true);
        txtHorarioSalida.setEditable(true);
        txtLunes.setEditable(true);
        txtMartes.setEditable(true);
        txtMiercoles.setEditable(true);
        txtJueves.setEditable(true);
        txtViernes.setEditable(true);
    }
    
    public void desactivarControles(){ /* Pase 6*/
        txtCodigoHorarios.setEditable(false);
        txtHorarioEntrada.setEditable(false);
        txtHorarioSalida.setEditable(false);
        txtLunes.setEditable(false);
        txtMartes.setEditable(false);
        txtMiercoles.setEditable(false);
        txtJueves.setEditable(false);
        txtViernes.setEditable(false);
    }
    
    
   public void limpiarControles(){ /* Pase 6*/
       txtCodigoHorarios.clear();
       txtHorarioEntrada.clear();
       txtHorarioSalida.clear();
       txtLunes.clear();
       txtMartes.clear();
       txtMiercoles.clear();
       txtJueves.clear();
       txtViernes.clear();
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
