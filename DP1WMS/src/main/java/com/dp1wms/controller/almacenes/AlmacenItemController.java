package com.dp1wms.controller.almacenes;

import com.dp1wms.controller.FxmlController;
import com.dp1wms.model.Almacen;
import com.dp1wms.spring.config.SpringFXMLLoader;
import com.dp1wms.view.AlmacenView;
import com.dp1wms.view.StageManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class AlmacenItemController extends GridPane {

    @FXML private Label lbNombre;
    @FXML private Label lbDireccion;
    @FXML private Label lbLargoValue;
    @FXML private Label lbAnchoValue;
    @FXML private Label lbSuperficieValue;

    private Almacen almacen;
    private MantenimientoAlmacenesController mantenimientoAlmacenesController;

    public AlmacenItemController(MantenimientoAlmacenesController mantenimientoAlmacenesController, Almacen almacen){
        super();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AlmacenFxml/AlmacenListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.mantenimientoAlmacenesController = mantenimientoAlmacenesController;
        this.almacen = almacen;
    }

    @FXML
    public void onClick(MouseEvent event){
        if (this.almacen != null) {
            mantenimientoAlmacenesController.editarAlmacen(almacen);
        }
    }

    public void setAlmacen(Almacen almacen){
        lbNombre.setText(almacen.getNombre());
        lbDireccion.setText(almacen.getDireccion());
        lbLargoValue.setText(String.valueOf(almacen.getLargo()) + " m");
        lbAnchoValue.setText(String.valueOf(almacen.getAncho()) + " m");
        lbSuperficieValue.setText(String.valueOf(almacen.getLargo()*almacen.getAncho()));

        this.almacen = almacen;
    }

    public Almacen getAlmacen(){
        return this.almacen;
    }
}
