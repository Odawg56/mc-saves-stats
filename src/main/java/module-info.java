module com.boxy.mcworldstats {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.boxy.mcworldstats to javafx.fxml;
    exports com.boxy.mcworldstats;
}