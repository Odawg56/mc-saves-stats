module com.boxy.mcworldstats {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires tools.jackson.databind;
    requires java.net.http;
    requires org.slf4j;

    opens com.boxy.mcworldstats to javafx.fxml;
    exports com.boxy.mcworldstats;
    exports com.boxy.mcworldstats.controller;
    opens com.boxy.mcworldstats.controller to javafx.fxml;
}