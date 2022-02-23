package quevedo.ClienteSecretos.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.extern.log4j.Log4j2;
import quevedo.Common.modelos.UsuarioCertificado;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class FXMLPrincipalController implements Initializable {
    private final FXMLLoader loaderEncriptacion;
    private final FXMLLoader loaderLogin;
    @FXML
    private MenuBar menu;

    @FXML
    private BorderPane root;

    private UsuarioCertificado usuarioLogin;
    private AnchorPane pantallaEncriptacion;
    private FXMLEncriptarController fxmlEncriptarController;
    private AnchorPane pantallaLogin;
    private FXMLLoginController fxmlLoginController;

    @Inject
    public FXMLPrincipalController(FXMLLoader loaderEncriptacion, FXMLLoader loaderLogin) {
        this.loaderEncriptacion = loaderEncriptacion;
        this.loaderLogin = loaderLogin;
    }

    public UsuarioCertificado getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(UsuarioCertificado usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public void preloadEncriptacion() {
        try {
            if (pantallaEncriptacion == null) {
                pantallaEncriptacion = loaderEncriptacion.load(getClass().getResourceAsStream("/fxml/EncriptarFXML.fxml"));
                fxmlEncriptarController = loaderEncriptacion.getController();
                fxmlEncriptarController.setPrincipal(this);
            }

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void preloadLogin() {
        try {
            if (pantallaLogin == null) {
                pantallaLogin = loaderLogin.load(getClass().getResourceAsStream("/fxml/LoginFXML.fxml"));
                fxmlLoginController = loaderLogin.getController();
                fxmlLoginController.setPrincipal(this);

            }

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }


    public void chargeEncriptacion() {
        menu.setVisible(true);
        fxmlEncriptarController.load();
        root.setCenter(pantallaEncriptacion);
    }

    public void chargeLogin() {
        menu.setVisible(false);
        root.setCenter(pantallaLogin);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preloadEncriptacion();
        preloadLogin();
        chargeLogin();
    }
}
