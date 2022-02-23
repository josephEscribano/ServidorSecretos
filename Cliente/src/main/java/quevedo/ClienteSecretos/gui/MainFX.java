package quevedo.ClienteSecretos.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import quevedo.ClienteSecretos.gui.utils.ConstantesGui;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;

@Log4j2
public class MainFX {

    @Inject
    FXMLLoader fxmlLoader;

    public void start(@Observes @StartupScene Stage stage) {
        try {
            Parent fxmlparent = fxmlLoader.load(getClass().getResourceAsStream(ConstantesGui.PATH_PRINCIPAL));
            stage.setScene(new Scene(fxmlparent));
            stage.setTitle(ConstantesGui.ENCRIPTACION);
            stage.show();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }
}
