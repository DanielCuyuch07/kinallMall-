package org.andycuyuch.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.andycuyuch.bean.Administracion;
import org.andycuyuch.db.Conexion;
import org.andycuyuch.report.GenerarReporte;
import org.andycuyuch.system.Principal;

public class AdministracionController implements Initializable{
    private enum operaciones{Nuevo, GUARDAR, ELIMINAR, ACTUALIZAR,CANCELAR,NINGUNO}; /*Opcionde del switch*/
    private operaciones tipoDeOperacion = operaciones.NINGUNO; /*El valor de operaciones es ninguno (checar)*/
        private Principal escenarioPrincipal;
        private  ObservableList<Administracion> listaAdministracion;
    
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar; 
    @FXML private Button btnEditar; 
    @FXML private Button btnReporte;
    
    @FXML private TextField txtCodigoAdministracion; 
    @FXML private TextField txtDireccion; 
    @FXML private TextField txtTelefono;
    
    @FXML private TableView tblAdministracion;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TableColumn colDireccion; 
    @FXML private TableColumn colTelefono; 
    
    
    @FXML private ImageView imgNuevo; 
    @FXML private ImageView imgEliminar; 
    @FXML private ImageView imgEditar; 
    @FXML private ImageView imgReporte;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
    }
    
    public void cargarDatos(){
      tblAdministracion.setItems(getAdministracion());
      colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
      colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String>("direccion"));
      colTelefono.setCellValueFactory(new  PropertyValueFactory<Administracion, String>("telefono"));
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
    
    /*mod*/
    public void seleccionarElemento(){
        if(tblAdministracion.getSelectionModel().getSelectedItem() != null){
        txtCodigoAdministracion.setText(String.valueOf(((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        txtDireccion.setText(((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefono.setText(((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono());
    }else{
           txtCodigoAdministracion.clear();
           txtDireccion.clear();
           txtTelefono.clear();
        }
    }
    /*nuevo viene del boton Nuevo*/
        public void nuevo(){
            switch(tipoDeOperacion){ /*en caso de que switch sa de tipoDeoperacion*/
                case NINGUNO: /*en caso que sea ninguno*/
                    activarControles(); /* lo que quiero hacer es activar los controles (Para que pueda escribit)*/
                    btnNuevo.setText("Guardar"); /*le damos un boton al usuario para que pueda guardar los datos*/
                    btnEliminar.setText("Cancelar"); /*En caso de que se haya confundido */
                    btnEditar.setDisable(true); /*cuando las dos de arriba se utilizaen estas de (Desactivan)*/
                    btnReporte.setDisable(true); /*desactivan*/
                    imgNuevo.setImage( new Image("/org/andycuyuch/images/save.png")); /*a la hora de presionar nuevo automaticamente fracias al edit nos colocara guardar y la imagen*/
                    imgEliminar.setImage(new Image("/org/andycuyuch/images/cancelar.png")); /*de igual manera despues de darle click a nuevo mi opcion de nuevo cambia */
                    tipoDeOperacion = operaciones.GUARDAR;/*cambiamos el tipo de oparacion (ahora queresmos Guardar (queremos hacer una opcion de guardar))*/
                    break;
                case GUARDAR: 
                    guardar();
                    desactivarControles(); // desactivasmos controladores //
                    limpiarControles(); // Limpiamos controles //
                    btnNuevo.setText("Nuevo");/*lo volvemos a su estado original */
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
   
        /*Cada vez que yo quiera guardar debe ser un instancia de administracion*/
         public void guardar(){
             if(txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty()){
                 JOptionPane.showMessageDialog(null, "No puede acceder si no coloca sus datos");
             }else{
             Administracion registro = new Administracion();
             registro.setDireccion(txtDireccion.getText());/*El codigo se genera automaticamente*/
             registro.setTelefono(txtTelefono.getText());
             try{
                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarAdministracion(?,?)}");
                 procedimiento.setString(1, registro.getDireccion());
                 procedimiento.setString(2, registro.getTelefono());
                 procedimiento.execute(); /* Ejecute el Procedimiento */
                 listaAdministracion.add(registro);
             }catch(Exception e){
                 e.printStackTrace();
             }
           }
         }
         
         /*Tarea*/
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
                     if(tblAdministracion.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, " ¿Quiere eliminar el registro?", "Eliminar Tipo cliente",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarAdministracion(?)}");
                                procedimiento.setInt(1, ((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
                                procedimiento.execute();
                                listaAdministracion.remove(tblAdministracion.getSelectionModel().getSelectedIndex());
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
                     if(tblAdministracion.getSelectionModel().getSelectedItem() != null){
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
                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_EditarAdministracion (?,?,?)}");
                 Administracion registro = (Administracion)tblAdministracion.getSelectionModel().getSelectedItem();
                 registro.setDireccion(txtDireccion.getText());
                 registro.setTelefono(txtTelefono.getText());
                 procedimiento.setInt(1, registro.getCodigoAdministracion());
                 procedimiento.setString(2, registro.getDireccion());
                 procedimiento.setString(3, registro.getTelefono());
                 procedimiento.execute();
             }catch(Exception e){
                 e.printStackTrace();
             }
         }
    
         public void Reporte(){
             switch(tipoDeOperacion){
                 case NINGUNO: 
                     imprimirReporte();
                     break;     
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
         
         public void imprimirReporte(){
             Map parametros = new HashMap();
             parametros.put("codigoAdministracion", null);
             GenerarReporte.mostrarReporte("ReporteAdministracion.jasper", "Reporte de Administracion", parametros);
         }
              
     /*SetEditable =  si lo puedo o no lo puedo editar 
        cuando es desactivar controles no lo puedo EDITAR */ 
        public void desactivarControles(){
            txtCodigoAdministracion.setEditable(false);
            txtDireccion.setEditable(false);
            txtTelefono.setEditable(false);
        }
        
      /*SetEditable =  ahora solo utilizaremos false en el codigoAdministracion 
            por que es auto incrementable */
        public void activarControles(){
            txtCodigoAdministracion.setEditable(false);
            txtDireccion.setEditable(true); /*si lo podemos edutar*/
            txtTelefono.setEditable(true); /* si lo podemos editar*/
        }
        
     /* Cuando yo grabe  con el clear dejare vacios los textos 
        otra forma seria con el setText("") se deja vacio*/
        public void limpiarControles(){
             txtCodigoAdministracion.clear();
             txtDireccion.clear();
             txtTelefono.clear();
        }
        
        
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaLogin(){
       escenarioPrincipal.ventanaLogin();
    }
        
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    

}
