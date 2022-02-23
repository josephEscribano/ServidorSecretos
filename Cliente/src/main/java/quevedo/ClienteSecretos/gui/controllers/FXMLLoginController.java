package quevedo.ClienteSecretos.gui.controllers;

import io.vavr.control.Either;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import quevedo.ClienteSecretos.service.ServiceUsuario;
import quevedo.ClienteSecretos.utils.Constantes;
import quevedo.Common.modelos.Usuario;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class FXMLLoginController {
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ServiceUsuario serviceUsuario;

    private FXMLPrincipalController principal;
    @FXML
    private TextField tfPass;
    @FXML
    private TextField tfUser;

    @Inject
    public FXMLLoginController(ServiceUsuario serviceUsuario) {
        this.serviceUsuario = serviceUsuario;

    }

    public void setPrincipal(FXMLPrincipalController fxmlPrincipalController) {
        principal = fxmlPrincipalController;
    }

    public void doLogin() {

    }

    public void registro() {

        if (!tfPass.getText().isBlank() && !tfUser.getText().isBlank()){
            Either<String,String> result = serviceUsuario.registro(Usuario
                    .builder()
                    .userName(tfUser.getText())
                    .pass(tfPass.getText())
                    .build());
            if (result.isRight()){
                alert.setContentText(result.get());
            }else{
                alert.setContentText(result.getLeft());
            }
        }else{
            alert.setContentText(Constantes.AVISO_CAMPOS);
        }

        alert.showAndWait();


    }
}
