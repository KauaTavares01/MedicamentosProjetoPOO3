module org.pacientesys {
    requires javafx.controls;
    requires javafx.fxml;

    // O FXML precisa abrir o pacote do controller para reflexão:
    opens org.pacientesys.controller to javafx.fxml;

    // A TableView acessa getters do modelo via reflexão:
    opens org.pacientesys.model to javafx.base;

    // Exporte o pacote base se precisar usar Main fora do módulo:
    exports org.pacientesys;
}
