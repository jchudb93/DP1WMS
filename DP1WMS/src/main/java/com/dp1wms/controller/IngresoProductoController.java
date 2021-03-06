package com.dp1wms.controller;

import com.dp1wms.view.MainView;
import com.dp1wms.dao.RepositoryMantMov;
import com.dp1wms.model.Lote;
import com.dp1wms.model.Producto;
import com.dp1wms.model.TipoMovimiento;
import com.dp1wms.view.FxmlView;
import com.dp1wms.view.StageManager;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.List;
@Component
public class IngresoProductoController implements FxmlController {

    @FXML
    private AnchorPane ingresoProductoAnchorePane;
    @FXML
    private TextField txb_nombreProd;
    @FXML
    private TextField txb_stock;
    @FXML
    private TextField txb_cantidad;
    @FXML
    private DatePicker dp_fecha;
    @FXML
    private ComboBox<TipoMovimiento> cb_motivo;
    @FXML
    private TextArea txa_observaciones;
    @FXML
    private Button btn_registrar;

    private List<TipoMovimiento> listaTiposMovimientos;

    @Autowired
    private RepositoryMantMov repositoryMantMov;

    private Producto productoEscogido=null;

    private Lote loteEscogido=null;

    private  MainController mainController;


    private final StageManager stageManager;

    @Autowired    @Lazy
    public IngresoProductoController(StageManager stageManager, MainController mainController){
        this.stageManager = stageManager;
        this.mainController = mainController;
    }

    public void buscarProducto(ActionEvent event){
        System.out.println("Buscar Producto");
        this.stageManager.mostrarModal(MainView.BUSQUEDA_PRODUCTO_LOTE);
    }


    public void actualizarDataProducto(Producto producto){
        this.txb_nombreProd.setText(producto.getNombreProducto());
        this.txb_stock.setText(""+producto.getStock());
        this.productoEscogido = producto;

    }

    public void  actualizarDataLote(Lote lote){
        this.txb_nombreProd.setText(lote.getNombreProducto());
        this.txb_stock.setText(""+lote.getStockParcial());
        this.loteEscogido = lote;
    }

    public void cancelarRegistro(ActionEvent event){
        ingresoProductoAnchorePane.getScene().getWindow().hide();
    }

    public void registrarIngresoProducto(ActionEvent event){
        TipoMovimiento tipoMovimiento = cb_motivo.getValue();
        if( tipoMovimiento != null){
            //obtener id motivo
            int idMotivoMovimiento;
            if((this.loteEscogido != null)) {
                System.out.println("idTipoMoviviento" + cb_motivo.getValue().getIdTipoMovimiento());

                if (txb_nombreProd.getText().equalsIgnoreCase("")) {
                    this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                            "Debe ingresar un producto. Seleccione el boton buscar");
                    return;
                }


                int totalProductos = 1;
                //repositoryMantMov.registrarMovimiento(totalProductos,this.txa_observaciones.toString(),this.dp_fecha.toString(),tipoMovimiento.getIdTipoMovimiento(),loteEscogido.getIdProducto(),loteEscogido.getIdLote(),Integer.parseInt(this.txb_cantidad.toString()));

                if (this.dp_fecha.getValue() == null) {
                    this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                            "Debe ingresar una fecha para el ingreso/salida de producto");
                    return;
                }

                Timestamp fecha = null;
                try {
                    fecha = this.obtenerFecha(this.dp_fecha.getValue().toString());
                } catch (ParseException e) {
                    //e.printStackTrace();
                    //System.out.println("Error al ingresar la fecha");
                    this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                            "Debe ingresar un formato de fecha valido");
                    return;
                }

                if (this.txb_cantidad.getText().equalsIgnoreCase("")) {
                    this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                            "Debe ingresar una cantidad de ingreso/salida producto");
                    return;
                }

                int cantidad;
                try {
                    cantidad = Integer.parseInt(this.txb_cantidad.getText());
                } catch (Exception e) {
                    //System.out.println("Error al ingresar la cantidad descrita");
                    this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                            "Debe ingresar una cantidad valida");
                    return;
                }

                int idMovimientoEscogido = tipoMovimiento.getIdTipoMovimiento();
                int idMovmientoDespacho = 2;
                int idMovimientoRobo = 5;
                int idMovimientoPerdida = 6;
                int idMovimientoSalida = 8;

                if((idMovimientoEscogido == idMovmientoDespacho) || (idMovimientoEscogido == idMovimientoRobo) || (idMovimientoEscogido == idMovimientoPerdida) || (idMovimientoEscogido == idMovimientoSalida)){
                    System.out.println("Stock Parcial: "+this.loteEscogido.getStockParcial() + " Cantidad Movimiento: "+cantidad);
                    if(cantidad > this.loteEscogido.getStockParcial()){
                        this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                                "No puede retirar mas del stock que posee ese lote");
                        return;
                    }
                }

                Long idEmpleadoAuditado = mainController.getEmpleado().getIdempleado();
                repositoryMantMov.registrarMovimiento(totalProductos,this.txa_observaciones.getText(),fecha,tipoMovimiento.getIdTipoMovimiento(),loteEscogido.getIdProducto(),loteEscogido.getIdLote(),cantidad,idEmpleadoAuditado,-1);
                this.stageManager.cerrarVentana(event);
            }else{
                this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                        "Debe ingresar un producto. Seleccione el boton buscar");
                return;
            }
        }else{
            this.stageManager.mostrarErrorDialog("Error Ingreso/Salida Producto", null,
                    "Debe ingresar un movito del ingreso o salida del producto");
            return;
            //System.out.println("Motivo Movimiento Vacio");
        }
        //ingresoProductoAnchorePane.getScene().getWindow().hide();
    }

    private Timestamp obtenerFecha(String fecha) throws ParseException {
        if(fecha != null)
            return convertirFecha(fecha);
        else
            return null;

    }

    private Timestamp convertirFecha(String fecha) throws ParseException  {

        Date utiltime = null;
        utiltime = ISO8601Utils.parse(fecha, new ParsePosition(0));
        return new Timestamp(utiltime.getTime());

    }


    private void configurarComboBox(){

        Callback<ListView<TipoMovimiento>, ListCell<TipoMovimiento>> factory = lv -> new ListCell<TipoMovimiento>() {

            @Override
            protected void updateItem(TipoMovimiento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getDescripcion());
            }

        };

        cb_motivo.setCellFactory(factory);
        cb_motivo.setButtonCell(factory.call(null));
    }

    @Override
    public void initialize() {
        this.txb_nombreProd.setDisable(true);
        this.txb_stock.setDisable(true);
        this.listaTiposMovimientos = repositoryMantMov.obtenerTiposMovimiento();

        this.configurarComboBox();

        for(TipoMovimiento tipoMovimiento : this.listaTiposMovimientos){
            int idMovimientoUbicacion = 3;
            int idMovimientoReubicacion = 4;
            int idMovimientoDevolucon = 10;
            if((tipoMovimiento.getIdTipoMovimiento() == idMovimientoUbicacion) || (tipoMovimiento.getIdTipoMovimiento() == idMovimientoReubicacion) || (tipoMovimiento.getIdTipoMovimiento() == idMovimientoDevolucon)){
                System.out.println("Movimiento no es de ingreso o salida, no se considera en el combo box");
            }else{
                cb_motivo.getItems().add(tipoMovimiento);
            }
        }
    }
}
