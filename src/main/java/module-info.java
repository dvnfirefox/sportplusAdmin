module com.bot.adminfront {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bot.adminfront to javafx.fxml;
    exports com.bot.adminfront;
}