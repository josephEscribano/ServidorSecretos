package quevedo.ClienteSecretos.gui.controllers;

import io.vavr.control.Either;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import quevedo.ClienteSecretos.gui.utils.ConstantesGui;
import quevedo.ClienteSecretos.service.ServiceSecreto;
import quevedo.ClienteSecretos.service.ServiceUsuario;
import quevedo.Common.modelos.Secreto;
import quevedo.Common.modelos.UsuarioCertificado;

import javax.inject.Inject;
import java.util.List;

public class FXMLEncriptarController {

    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ServiceSecreto serviceSecretos;
    private final ServiceUsuario serviceUsuario;
    private FXMLPrincipalController principal;
    @FXML
    private ComboBox<UsuarioCertificado> cbCompartir;

    @FXML
    private ListView<Secreto> lvSecretos;

    @FXML
    private TextField tfSecreto;

    @Inject
    public FXMLEncriptarController(ServiceSecreto serviceSecretos, ServiceUsuario serviceUsuario) {
        this.serviceSecretos = serviceSecretos;
        this.serviceUsuario = serviceUsuario;
    }

    public void setPrincipal(FXMLPrincipalController fxmlPrincipalController) {
        principal = fxmlPrincipalController;
    }

    public void load() {


    }

    public void guardarSecreto() {




    }

    public void mostrarSecreto() {

    }

    public void compartirSecreto() {


    }

}
