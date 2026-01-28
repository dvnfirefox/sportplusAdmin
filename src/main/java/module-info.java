module com.bot.adminfront {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // JSON
    requires com.fasterxml.jackson.databind;

    // HTTP client
    requires java.net.http;

    // Jakarta Servlet API (for HttpServletRequest, Cookie, etc.)
    requires jakarta.servlet;
    requires java.desktop;

    // Open packages for FXML
    opens com.bot.adminfront to javafx.fxml;
    opens com.bot.adminfront.controller to javafx.fxml;
    opens com.bot.adminfront.controller.utilisateur to javafx.fxml;
    opens com.bot.adminfront.controller.officiel to javafx.fxml;
    opens com.bot.adminfront.controller.tournois to javafx.fxml;


    // Export packages
    exports com.bot.adminfront;
    exports com.bot.adminfront.controller;
}