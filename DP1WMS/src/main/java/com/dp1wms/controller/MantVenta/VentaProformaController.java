package com.dp1wms.controller.MantVenta;

import com.dp1wms.controller.FxmlController;
import com.dp1wms.controller.MainController;
import com.dp1wms.model.*;
import com.dp1wms.view.MainView;
import com.dp1wms.view.StageManager;
import com.dp1wms.view.VentasView;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class VentaProformaController implements FxmlController{

    @FXML private Label codigoLabel;
    @FXML private Label nombreLabel;
    @FXML private Label telefonoLabel;
    @FXML private Label emailLabel;
    @FXML private Label rucLabel;
    @FXML private Label direccionLabel;

    @FXML private TableView<DetalleEnvio> preEnvioTable;
    @FXML private TableColumn<DetalleEnvio, String> envCodigoTC;
    @FXML private TableColumn<DetalleEnvio, String> envProductoTC;
    @FXML private TableColumn<DetalleEnvio, Float> envPrecioTC;
    @FXML private TableColumn<DetalleEnvio, Integer> envCantidadTC;
    @FXML private TableColumn<DetalleEnvio, Float> envSubtotalTC;
    @FXML private TextField destinoPreEnvioField;
    @FXML private DatePicker fechaPreEnvioField;
    @FXML private TextField costoFletePreEnvioField;

    @FXML private Button guardarCambiosBtn;
    @FXML private Button agregarEnvioBtn;
    @FXML private Button limpiarDatosBtn;


    @FXML private TableView<Envio> enviosTable;
    @FXML private TableColumn<Envio, String> envioDestinoTC;
    @FXML private TableColumn<Envio, String> fechaEnvioTC;
    @FXML private TableColumn<Envio, Integer> envioCantProdTC;
    @FXML private TableColumn<Envio, Float> costoFleteTC;


    private Cliente cliente;
    private Proforma proforma;
    private Envio preEnvio;
    private ArrayList<Envio> envios;


    private final StageManager stageManager;
    private final MainController mainController;

    @Override
    public void initialize(){
        this.cliente = null;
        this.proforma = new Proforma();
        this.envios = new ArrayList<Envio>();
        this.limpiarPreEnvio();
        this.limpiarEnvios();
        this.guardarCambiosBtn.setDisable(true);
        this.fechaPreEnvioField.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            public String toString(LocalDate localDate) {
                if(localDate == null)
                    return "";
                else
                    return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString==null || dateString.trim().isEmpty()){
                    return null;
                } else {
                    return LocalDate.parse(dateString, dateTimeFormatter);
                }
            }
        });
    }

    @Autowired @Lazy
    public VentaProformaController(StageManager stageManager, MainController mainController){
        this.stageManager = stageManager;
        this.mainController = mainController;
    }

    @FXML
    private void mostrarBusquedaCliente(){
        this.stageManager.mostrarModal(MainView.BUSCAR_CLIENTE);
    }

    @FXML
    private void mostrarRegistrarCliente(){
        this.stageManager.mostrarModal(MainView.REGISTRAR_CLIENTE);
    }

    @FXML
    private void mostrarBusquedaProducto(){
        this.stageManager.mostrarModal(VentasView.VENTA_BUSCAR_PROD);
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
        this.codigoLabel.setText(String.valueOf(cliente.getIdCliente()));
        this.nombreLabel.setText(cliente.getRazonSocial());
        this.rucLabel.setText(cliente.getNumDoc());
        this.telefonoLabel.setText(cliente.getTelefono());
        this.emailLabel.setText(cliente.getEmail());
        this.direccionLabel.setText(cliente.getDireccion());
    }

    public void agregarProductoAPreEnvio(Producto producto, int cantidad){
        DetalleEnvio de = this.preEnvio.agregarProducto(producto, cantidad);
        if(de == null){
            this.limpiarTablePreEnvio();
            this.preEnvioTable.getItems().addAll(this.preEnvio.getDetalleEnvio());
        } else {
            this.preEnvioTable.getItems().add(de);
        }
        //recalcular descuentos
    }

    @FXML
    private void modificarCantidadDetallePreEnvio(){
        DetalleEnvio de = this.preEnvioTable.getSelectionModel().getSelectedItem();
        if (de == null){
            this.stageManager.mostrarErrorDialog("Error", null, "No ha seleccionado ningún item del envio");
        } else {
            Producto p = de.getProducto();
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Confirmación");
            dialog.setHeaderText("Producto: " + p.getNombreProducto());
            dialog.setContentText("Ingrese la cantidad:");
            ButtonType ok = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().setAll(ok, cancelar);
            Optional<String> result = dialog.showAndWait();
            int cantidad = 0;
            if(result.isPresent()){
                try{
                    cantidad = Integer.parseInt(result.get());
                } catch (NumberFormatException e){
                    cantidad = 0;
                }
            }
            if(cantidad > 0 ){
                de.setCantidad(cantidad);
            } else {//show error
                this.stageManager.mostrarErrorDialog("Error cantidad de producto",null,
                        "Debes ingreasr un valor válido");
            }
            this.limpiarTablePreEnvio();
            this.preEnvioTable.getItems().addAll(this.preEnvio.getDetalleEnvio());
        }
    }

    @FXML
    private void eliminarDetallePreEnvio(){
        DetalleEnvio de = this.preEnvioTable.getSelectionModel().getSelectedItem();
        int index = this.preEnvioTable.getSelectionModel().getSelectedIndex();
        if(de == null){
            this.stageManager.mostrarErrorDialog("Error", null, "No ha seleccionado ningún item del envio");
        } else {
            this.preEnvio.eliminarDetalleEnvio(de);
            this.preEnvioTable.getItems().remove(index);
        }
    }

    @FXML
    private void agregarEnvio(){
        String destino = this.destinoPreEnvioField.getText();
        LocalDate localDate = this.fechaPreEnvioField.getValue();
        String fechaEnvio = this.fechaPreEnvioField.getConverter().toString(localDate);
        String costoFleteStr = this.costoFletePreEnvioField.getText();
        float costoFlete = 0;
        try {
            costoFlete = Float.valueOf(costoFleteStr);
        } catch (Exception e){
            this.stageManager.mostrarErrorDialog("Error costo flete", null, "Debe ingresar un costo válido");
            return;
        }
        if(this.preEnvio.getDetalleEnvio().size() == 0){
            this.stageManager.mostrarErrorDialog("Error de productos", null, "Debe haber ingresado al menos un producto");
            return;
        }
        this.preEnvio.setDestino(destino);
        this.preEnvio.setFechaEnvio(fechaEnvio);
        this.preEnvio.setCostoFlete(costoFlete);
        this.envios.add(this.preEnvio);
        this.enviosTable.getItems().add(this.preEnvio);
        limpiarPreEnvio();
    }

    @FXML
    private void guardarCambiosEnvio(){
        float costoFlete = 0;
        try {
            costoFlete = Float.valueOf(this.costoFletePreEnvioField.getText());
        } catch (Exception e){
            this.stageManager.mostrarErrorDialog("Error costo flete", null, "Debe ingresar un costo válido");
            return;
        }

        this.guardarCambiosBtn.setDisable(true);
        this.agregarEnvioBtn.setDisable(false);
        this.limpiarDatosBtn.setDisable(false);

        this.preEnvio.setDestino(this.destinoPreEnvioField.getText());
        this.preEnvio.setCostoFlete(costoFlete);
        LocalDate localDate = this.fechaPreEnvioField.getValue();
        this.preEnvio.setFechaEnvio(this.fechaPreEnvioField.getConverter().toString(localDate));
        this.limpiarEnvios();
        this.llenarTableEnvios();
        this.limpiarPreEnvio();
    }

    @FXML
    private void editarEnvio(){
        Envio env = this.enviosTable.getSelectionModel().getSelectedItem();
        int index = this.enviosTable.getSelectionModel().getSelectedIndex();
        if(env == null){
            this.stageManager.mostrarErrorDialog("Error Envio", null, "Debe seleccionar un envio que desea editar");
        } else {
            limpiarPreEnvio();
            this.guardarCambiosBtn.setDisable(false);
            this.agregarEnvioBtn.setDisable(true);
            this.limpiarDatosBtn.setDisable(true);
            this.preEnvio = env;
            this.preEnvioTable.getItems().addAll(preEnvio.getDetalleEnvio());
            this.destinoPreEnvioField.setText(this.preEnvio.getDestino());
            this.costoFletePreEnvioField.setText(String.valueOf(this.preEnvio.getCostoFlete()));
            LocalDate localDate = this.fechaPreEnvioField.getConverter().fromString(this.preEnvio.getFechaEnvio());
            this.fechaPreEnvioField.setValue(localDate);
        }
    }

    @FXML
    private void eliminarEnvio(){
        Envio env = this.enviosTable.getSelectionModel().getSelectedItem();
        int index = this.enviosTable.getSelectionModel().getSelectedIndex();
        if(env == null){
            this.stageManager.mostrarErrorDialog("Error Envio", null, "Debe seleccionar un envio que desea eliminar");
        } else {
            this.envios.remove(env);
            this.enviosTable.getItems().remove(index);
        }
    }

    @FXML
    private void limpiarPreEnvio(){
        this.preEnvio = new Envio();
        this.limpiarTablePreEnvio();

    }

    private void limpiarTablePreEnvio(){
        this.preEnvioTable.getItems().clear();
        this.envCodigoTC.setCellValueFactory(value -> {
            return new SimpleStringProperty(value.getValue().getProducto().getCodigo());
        });
        this.envProductoTC.setCellValueFactory(value -> {
            return new SimpleStringProperty(value.getValue().getProducto().getNombreProducto());
        });
        this.envPrecioTC.setCellValueFactory(value -> {
            return new SimpleObjectProperty<Float>(value.getValue().getProducto().getPrecio());
        });
        this.envCantidadTC.setCellValueFactory(new PropertyValueFactory<DetalleEnvio, Integer>("cantidad"));
        this.envSubtotalTC.setCellValueFactory(value -> {
            return new SimpleObjectProperty<Float>(value.getValue().getCantidad() * value.getValue().getProducto().getPrecio());
        });
        this.destinoPreEnvioField.clear();
        this.costoFletePreEnvioField.clear();
        this.fechaPreEnvioField.setValue(null);
    }

    private void limpiarEnvios(){
        this.enviosTable.getItems().clear();
        this.envioDestinoTC.setCellValueFactory(new PropertyValueFactory<Envio, String>("destino"));
        this.fechaEnvioTC.setCellValueFactory(new PropertyValueFactory<Envio, String>("fechaEnvio"));
        this.envioCantProdTC.setCellValueFactory(value -> {
            return new SimpleObjectProperty<Integer>(value.getValue().getTotalProductos());
        });
        this.costoFleteTC.setCellValueFactory(new PropertyValueFactory<Envio, Float>("costoFlete"));
    }

    private void llenarTableEnvios(){
        this.enviosTable.getItems().addAll(envios);
    }
}
