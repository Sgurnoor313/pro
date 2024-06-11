module cherryrockstudios.listo {

    requires javafx.controls;
    requires javafx.fxml;

    opens cherryrockstudios.listo to javafx.fxml;
    opens cherryrockstudios.listo.unit to javafx.base;
    exports cherryrockstudios.listo;
}