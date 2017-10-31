package com.dp1wms.controller.MantVenta;

import com.dp1wms.controller.FxmlController;
import com.dp1wms.model.*;
import com.dp1wms.util.DateParser;
import com.dp1wms.view.StageManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;


@Component
public class VentaInformacionEnvio implements FxmlController{


    private Envio envio;

    @FXML private TextField destinoTF;
    @FXML private DatePicker fechaEnvioDP;

    @FXML private TableView<DetallePedido> prodDisponiblesTable;
    @FXML private TableColumn<DetallePedido, String> codigoDispTC;
    @FXML private TableColumn<DetallePedido, String> nombreDispTC;
    @FXML private TableColumn<DetallePedido, Integer> disponibleTC;

    @FXML private TableView<DetalleEnvio> envioTable;
    @FXML private TableColumn<DetalleEnvio, String>  codigoEnvioTC;
    @FXML private TableColumn<DetalleEnvio, String>  nombreEnvioTC;
    @FXML private TableColumn<DetalleEnvio, Integer>  asignadoEnvioTC;

    @FXML private Label costoFleteLabel;

    @FXML private Button guardarBtn;
    @FXML private Button agregarBtn;

    private ArrayList<DetallePedido> productosDisponibles;
    private ArrayList<DetalleEnvio> detalleEnvios;


    private StageManager stageManager;
    private VentaPedido ventaPedido;



    @FXML
    private void agregarUnProducto(){
        DetallePedido dp = this.prodDisponiblesTable.getSelectionModel().getSelectedItem();
        if(dp == null){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "Debe seleccionar un producto que desea agregar");
        } else {
            //buscar producto si existe en detalle envios
            int cantidad = 0;
            Producto p = dp.getProducto();

            //ingresar cantidad
            TextInputDialog dialog = new TextInputDialog(String.valueOf(dp.getCantidad()));
            dialog.setTitle("Confirmación");
            dialog.setHeaderText("Producto: " + p.getNombreProducto());
            dialog.setContentText("Ingrese la cantidad (MAX. " + dp.getCantidad() + ") :");
            ButtonType ok = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().setAll(ok, cancelar);
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                try{
                    cantidad = Integer.parseInt(result.get());
                } catch (NumberFormatException e){
                    cantidad = 0;
                }
            }
            if(cantidad <= 0 || cantidad > dp.getCantidad()){
                this.stageManager.mostrarErrorDialog("Error cantidad de producto",null,
                        "Debes ingresar un valor válido");
                return;
            }

            DetalleEnvio de = null;
            for(DetalleEnvio deAux: this.detalleEnvios){
                if(deAux.getProducto().getIdProducto() == p.getIdProducto()){
                    de = deAux;
                    break;
                }
            }
            if(de == null){
                de = new DetalleEnvio();
                de.setProducto(p);
                de.setCantidad(cantidad);
                this.detalleEnvios.add(de);
            } else {
                de.setCantidad(de.getCantidad() + cantidad);
            }
            //disminuir cantidad
            dp.setCantidad(dp.getCantidad() - cantidad);
            this.llenarTablaDisponibles();
            this.llenarTablaAsignados();
        }
    }

    @FXML
    private void removerUnProducto(){
        DetalleEnvio de = this.envioTable.getSelectionModel().getSelectedItem();
        if(de == null){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "Debe seleccionar un producto del envio que desea remover");
        } else {
            Producto p = de.getProducto();
            int cantidad = 0;
            //ingresar cantidad
            TextInputDialog dialog = new TextInputDialog(String.valueOf(de.getCantidad()));
            dialog.setTitle("Confirmación");
            dialog.setHeaderText("Producto: " + p.getNombreProducto());
            dialog.setContentText("Ingrese la cantidad (MAX. " + de.getCantidad() + ") :");
            ButtonType ok = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().setAll(ok, cancelar);
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                try{
                    cantidad = Integer.parseInt(result.get());
                } catch (NumberFormatException e){
                    cantidad = 0;
                }
            }
            if(cantidad <= 0 || cantidad > de.getCantidad()){
                this.stageManager.mostrarErrorDialog("Error cantidad de producto",null,
                        "Debes ingresar un valor válido");
                return;
            }

            DetallePedido dp = null;
            for(DetallePedido dpAux: this.productosDisponibles){
                if(dpAux.getProducto().getIdProducto() == p.getIdProducto()){
                    dp = dpAux;
                    break;
                }
            }
            if(dp == null){
                dp = new DetallePedido();
                dp.setProducto(p);
                dp.setCantidad(cantidad);
                this.productosDisponibles.add(dp);
            } else {
                dp.setCantidad(dp.getCantidad() + cantidad);
            };
            de.setCantidad(de.getCantidad() - cantidad);
            this.detalleEnvios.removeIf(deAux-> deAux.getCantidad() == 0);
            this.llenarTablaDisponibles();
            this.llenarTablaAsignados();

        }
    }

    @FXML
    private void agregarEnvio(ActionEvent event){
        String destino = this.destinoTF.getText();
        LocalDate localdate = this.fechaEnvioDP.getValue();
        Timestamp fecha = DateParser.localdateToTimestamp(localdate);

        if(destino.isEmpty()){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "Debe agregar un destino");
            return;
        }
        if(fecha == null){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "Debe seleccionar una fecha valida");
            return;
        }
        if(this.detalleEnvios.size() == 0){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "El envio debe contener un producto al menos");
            return;
        }
        this.envio = new Envio();
        this.envio.setDestino(destino);
        this.envio.setFechaEnvio(fecha);
        this.envio.setCostoFlete(0);//TODO
        this.envio.setDetalleEnvio(this.detalleEnvios);

        this.stageManager.mostrarInfoDialog("Envio", null,
                "Se agregó un envío");
        this.cerrarVentana(event);
    }

    @FXML
    private void guardarEnvio(ActionEvent event){
        String destino = this.destinoTF.getText();
        LocalDate localdate = this.fechaEnvioDP.getValue();
        Timestamp fecha = DateParser.localdateToTimestamp(localdate);

        if(destino.isEmpty()){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "Debe agregar un destino");
            return;
        }
        if(fecha == null){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "Debe seleccionar una fecha valida");
            return;
        }
        if(this.detalleEnvios.size() == 0){
            this.stageManager.mostrarErrorDialog("Error Envio", null,
                    "El envio debe contener un producto al menos");
            return;
        }

        this.envio.setDestino(destino);
        this.envio.setFechaEnvio(fecha);
        this.envio.setCostoFlete(0);//TODO
        this.envio.setDetalleEnvio(this.detalleEnvios);
        this.stageManager.mostrarInfoDialog("Envio", null,
                "Se modificó un envío");
        this.cerrarVentana(event);
    }

    @FXML
    private void cerrarVentana(ActionEvent event){
        this.stageManager.cerrarVentana(event);
    }

    @Override
    public void initialize(){
        this.detalleEnvios = new ArrayList<>();
        this.limpiarTablaDisponibles();
        this.limpiarTablaAsignados();

        this.fechaEnvioDP.setConverter(DateParser.getConverter());

        //buttons
        this.agregarBtn.managedProperty().bind(agregarBtn.visibleProperty());
        this.guardarBtn.managedProperty().bind(guardarBtn.visibleProperty());

        if(this.envio == null){ //nuevo envio
            //buttons
            this.agregarBtn.setDisable(false);
            this.agregarBtn.setVisible(true);

            this.guardarBtn.setDisable(true);
            this.guardarBtn.setVisible(false);

            //fecha
            this.fechaEnvioDP.setValue(DateParser.currentDateLocalDate());
        } else {
            //buttons
            this.agregarBtn.setDisable(true);
            this.agregarBtn.setVisible(false);

            this.guardarBtn.setDisable(false);
            this.guardarBtn.setVisible(true);

            //init campos
            this.destinoTF.setText(this.envio.getDestino());
            this.fechaEnvioDP.setValue(DateParser.timestampToLocaldate(this.envio.getFechaEnvio()));

            //init tabla envio
            for(DetalleEnvio de: this.envio.getDetalleEnvio()){
                this.detalleEnvios.add(new DetalleEnvio(de));
            }
            this.envioTable.getItems().addAll(this.detalleEnvios);

            //init costo flete
            this.costoFleteLabel.setText(String.valueOf(this.envio.getCostoFlete()));
        }

        this.initProductosDisponibles();
    }

    private void initProductosDisponibles(){
        this.productosDisponibles = new ArrayList<DetallePedido>();

        Pedido pedido = this.ventaPedido.getPedido();

        ArrayList<Envio> envios = this.ventaPedido.getEnvios();

        //crear nuevos detalles proforma
        for(DetallePedido dp: pedido.getDetalles()){
            DetallePedido dpNew = new DetallePedido();
            int cantidad = dp.getCantidad();
            Producto producto = dp.getProducto();

            for(Envio e: envios){
                //buscar el producto
                for(DetalleEnvio de: e.getDetalleEnvio()){
                    if(de.getProducto().getIdProducto() == producto.getIdProducto()){
                        cantidad -= de.getCantidad();
                        break;
                    }
                }
            }

            dpNew.setProducto(dp.getProducto());
            dpNew.setCantidad(cantidad);
            this.productosDisponibles.add(dpNew);
        }

        //remove detalles con cantidad 0
        this.prodDisponiblesTable.getItems().addAll(this.productosDisponibles);
    }

    private void limpiarTablaDisponibles(){
        this.prodDisponiblesTable.getItems().clear();
        this.codigoDispTC.setCellValueFactory(value->{
            return new SimpleStringProperty(value.getValue().getProducto().getCodigo());
        });
        this.nombreDispTC.setCellValueFactory(value->{
            return new SimpleStringProperty(value.getValue().getProducto().getNombreProducto());
        });
        this.disponibleTC.setCellValueFactory(value->{
            return new SimpleObjectProperty<Integer>(value.getValue().getCantidad());
        });
    }

    private void limpiarTablaAsignados(){
        this.envioTable.getItems().clear();
        this.codigoEnvioTC.setCellValueFactory(value->{
            return new SimpleStringProperty(value.getValue().getProducto().getCodigo());
        });
        this.nombreEnvioTC.setCellValueFactory(value->{
            return new SimpleStringProperty(value.getValue().getProducto().getNombreProducto());
        });
        this.asignadoEnvioTC.setCellValueFactory(value->{
            return new SimpleObjectProperty<Integer>(value.getValue().getCantidad());
        });
    }

    private void llenarTablaDisponibles(){
        this.limpiarTablaDisponibles();
        this.prodDisponiblesTable.getItems().addAll(this.productosDisponibles);
    }

    private void llenarTablaAsignados(){
        this.limpiarTablaAsignados();
        this.envioTable.getItems().addAll(this.detalleEnvios);
    }

    @Autowired @Lazy
    public VentaInformacionEnvio(StageManager stageManager, VentaPedido ventaPedido){
        this.stageManager = stageManager;
        this.ventaPedido = ventaPedido;
    }

    public Envio getEnvio(){
        return  this.envio;
    }

    public void setEnvio(Envio envio){
        this.envio = envio;
    }
}
