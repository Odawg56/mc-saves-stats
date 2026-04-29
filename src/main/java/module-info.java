module com.boxy.mcworldstats {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires tools.jackson.databind;
    requires java.net.http;

    opens com.boxy.mcworldstats to javafx.fxml;
    exports com.boxy.mcworldstats;
}