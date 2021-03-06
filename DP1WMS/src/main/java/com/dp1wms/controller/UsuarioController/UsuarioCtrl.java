package com.dp1wms.controller.UsuarioController;

import com.dp1wms.controller.FxmlController;
import com.dp1wms.dao.RepositoryCargaMasiva;
import com.dp1wms.dao.RepositoryMantEmpleado;
import com.dp1wms.dao.RepositoryMantTipoEmpleado;
import com.dp1wms.dao.RepositoryMantUsuario;
import com.dp1wms.model.Empleado;
import com.dp1wms.model.TipoEmpleado;
import com.dp1wms.model.UsuarioModel.Usuario;
import com.dp1wms.model.UsuarioModel.UsuarioXEmpleado;
import com.dp1wms.view.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class UsuarioCtrl implements FxmlController {

    private final StageManager stageManager;

    private com.dp1wms.model.Usuario usuario;

    @FXML private TableView<UsuarioXEmpleado> e_table;
    @FXML private TableColumn<UsuarioXEmpleado, Integer> e_id;
    @FXML private TableColumn<UsuarioXEmpleado, String> e_user;
    @FXML private TableColumn<UsuarioXEmpleado, String> e_numDoc;
    @FXML private TableColumn<UsuarioXEmpleado, String> e_nombre;
    @FXML private TableColumn<UsuarioXEmpleado, String> e_apellido;
    @FXML private TableColumn<UsuarioXEmpleado, String> e_descripcion;
    @FXML private TableColumn<UsuarioXEmpleado, Boolean> e_activo;
    @FXML private TableColumn<UsuarioXEmpleado, String> e_mostrarActivo;

    @Autowired
    private RepositoryMantUsuario repositoryMantUsuario;
    @Autowired
    private RepositoryMantEmpleado repositoryMantEmpleado;
    @Autowired
    private RepositoryMantTipoEmpleado repositoryMantTipoEmpleado;
    @Autowired
    private RepositoryCargaMasiva repositoryCargaMasiva;

    @Autowired @Lazy
    public  UsuarioCtrl(StageManager stageManager){
        this.stageManager = stageManager;
    }

   public void cargaMasiva(ActionEvent event){
        this.repositoryCargaMasiva.storeProcedure_cargarUsuariosEmpleados();
        this._llenarGrilla();
    }

    //Los botones del mantenimiento de usuarios
    public void btnClickCrearUsuario(ActionEvent event){
        System.out.println("Agrear Usuario");
        if( (repositoryMantTipoEmpleado.selectAllTipoEmpleado() == null)
                || (repositoryMantTipoEmpleado.selectAllTipoEmpleado().size() == 0) ){
            mostrarErrorDialog("No hay ningun tipo de empleado",
                    "Ningun tipo de empleado registrado. Registrar al menos uno para continuar");
            return;
        }
        Parent root = null;
        FXMLLoader loader;
        Usuario auxUsuario = new Usuario();
        Empleado auxEmpleado = new Empleado();
        TipoEmpleado auxTipoEmpleado = new TipoEmpleado();
        //
        try {
            loader =new FXMLLoader(getClass().getResource("/fxml/UsuarioFxml/DatosUsuario.fxml"));
            root = (Parent) loader.load();
            UsuarioDatosController controller = loader.getController();
            //0 es crear
            controller.setV_parentController(this);
            controller._setData(auxUsuario,auxEmpleado,auxTipoEmpleado,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Window existingWindow = ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    public void btnClickModificarUsuario(ActionEvent event){
        System.out.println("Modificar Usuario");
        if(e_table.getSelectionModel().getSelectedItem() == null){
            mostrarErrorDialog("Error Usuario",
                    "No se selecciono ningun usuario.");
            return;
        }
        if( (repositoryMantTipoEmpleado.selectAllTipoEmpleado() == null)
                || (repositoryMantTipoEmpleado.selectAllTipoEmpleado().size() == 0) ){
            mostrarErrorDialog("No hay ningun tipo de empleado",
                    "Ningun tipo de empleado registrado. Registrar al menos uno para continuar");
            return;
        }
        UsuarioXEmpleado auxUsuarioXEmpleado = e_table.getSelectionModel().getSelectedItem();
        Usuario auxUsuario = repositoryMantUsuario.findUsuariobyId(auxUsuarioXEmpleado.getV_id_user());
        Empleado auxEmpleado = repositoryMantEmpleado.obtenerEmpleadoPorIdUsuario( auxUsuario.getV_id() );
        TipoEmpleado auxTipoEmpleado = repositoryMantTipoEmpleado.obtenerTipoEmpleadoPorIdTipo(auxEmpleado.getIdtipoempleado());
        Parent root = null;
        FXMLLoader loader;
        try {
            loader =new FXMLLoader(getClass().getResource("/fxml/UsuarioFxml/DatosUsuario.fxml"));
            root = (Parent) loader.load();
            UsuarioDatosController controller = loader.getController();
            //1 es modificar
            controller.setV_parentController(this);
            controller._setData(auxUsuario, auxEmpleado, auxTipoEmpleado,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Window existingWindow = ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    public void btnClickEliminarUsuario(ActionEvent event){
        System.out.println("Eliminar Usuario");
        if(e_table.getSelectionModel().getSelectedItem() == null) {
            mostrarErrorDialog("Error Usuario",
                    "No se selecciono ningun usuario.");
            return;
        }

        UsuarioXEmpleado auxUsuarioXEmpleado = e_table.getSelectionModel().getSelectedItem();
        Usuario auxUsuario = repositoryMantUsuario.findUsuariobyId(auxUsuarioXEmpleado.getV_id_user());

        if(!auxUsuarioXEmpleado.getV_activo()){
            mostrarErrorDialog("Error Usuario",
                    "El usuario ya se encuentra deshabilitado.");
            return;
        }

        repositoryMantEmpleado.deleteEmpleado(auxUsuario, repositoryMantEmpleado.obtenerEmpleadoPorIdUsuario(auxUsuario.getV_id()), this.usuario.getIdusuario() );
        //repositoryMantUsuario.deleteUsuario(auxUsuario);
        mostrarInfoDialog("Deshabilitar Usuario",
                "Se deshabilito el usuario " + auxUsuario.getV_nombre() + " con exito.");

        this._llenarGrilla();
    }

    public void btnClickActivarUsuario(ActionEvent event){
        System.out.println("Activar Usuario");
        if(e_table.getSelectionModel().getSelectedItem() == null){
            mostrarErrorDialog("Error Usuario",
                    "No se selecciono ningun usuario.");
            return;
        }
        UsuarioXEmpleado auxUsuarioXEmpleado = e_table.getSelectionModel().getSelectedItem();
        Usuario auxUsuario = repositoryMantUsuario.findUsuariobyId(auxUsuarioXEmpleado.getV_id_user());

        if(auxUsuarioXEmpleado.getV_activo()){
            mostrarErrorDialog("Error Usuario",
                    "El usuario ya se encuentra habilitado.");
            return;
        }

        repositoryMantEmpleado.activeEmpleado(auxUsuario, repositoryMantEmpleado.obtenerEmpleadoPorIdUsuario(auxUsuario.getV_id()), this.usuario.getIdusuario() );
        mostrarInfoDialog("Habilitar Usuario",
                "Se habilito el usuario " + auxUsuario.getV_nombre() + " con exito.");

        this._llenarGrilla();
    }

    @Override
    public void initialize() {
        e_id.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, Integer>("v_id_user"));
        e_user.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, String>("v_user"));
        e_numDoc.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, String>("v_numDoc"));
        e_nombre.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, String>("v_nombre"));
        e_apellido.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, String>("v_apellido"));
        e_descripcion.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, String>("v_descripcion"));
        e_activo.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, Boolean>("v_activo"));
        e_mostrarActivo.setCellValueFactory(new PropertyValueFactory<UsuarioXEmpleado, String>("v_mostrarActivo"));
        this._llenarGrilla();
    }

    private void _llenarGrilla(){
        e_table.getItems().clear();
        List<UsuarioXEmpleado> auxListaUsuarioXEmpleado = repositoryMantEmpleado.obtenerUsuarioXEmpleadoPorIdUsuario();
        this.e_table.getItems().addAll(auxListaUsuarioXEmpleado);
    }

    public void crearUsuarioDB(Usuario auxUsuario){
        repositoryMantUsuario.createUsuario(auxUsuario);
    }

    public void modificarUsuarioDB(Usuario auxUsuario, boolean auxModificarPassword){
        repositoryMantUsuario.updateUsuario(auxUsuario, auxModificarPassword);
    }

    public void crearEmpleadoDB(Usuario auxUsuario, Empleado auxEmpleado, TipoEmpleado auxTipoEmpelado){
        repositoryMantEmpleado.createEmpleado(auxUsuario, auxEmpleado, auxTipoEmpelado, this.usuario.getIdusuario());
    }
    public void modificarEmpleadoDB(Usuario auxUsuario, Empleado auxEmpleado, TipoEmpleado auxTipoEmpelado){
        repositoryMantEmpleado.updateEmpleado(auxUsuario, auxEmpleado, auxTipoEmpelado, this.usuario.getIdusuario());
    }

    public TipoEmpleado obtenerTipoEmpleadoPorDescripcion(String auxDescripcion){
        return repositoryMantTipoEmpleado.obtenerTipoEmpleadoPorDescripcion(auxDescripcion);
    }

    public List<TipoEmpleado> llenarGrillaTipoEmpleado(){
        return repositoryMantTipoEmpleado.selectAllTipoEmpleado();
    }

    public boolean existeUsuarioDB(String auxUserName){
        return repositoryMantUsuario.existeUsuario(auxUserName);
    }

    public boolean coincideUsuarioIdDB(String auxUserName, int auxId){
        return repositoryMantUsuario.coincideUsuarioId(auxUserName, auxId);
    }

    public void setUsuario(com.dp1wms.model.Usuario usuario){
        this.usuario = usuario;
    }

    public void mostrarErrorDialog(String auxTitulo, String auxTexto){
        this.stageManager.mostrarErrorDialog(auxTitulo, null,
                auxTexto);
    }

    public void mostrarInfoDialog(String auxTitulo, String auxTexto){
        this.stageManager.mostrarInfoDialog(auxTitulo, null,
                auxTexto);
    }

}