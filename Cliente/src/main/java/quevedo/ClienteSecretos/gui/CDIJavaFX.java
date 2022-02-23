package quevedo.ClienteSecretos.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.util.AnnotationLiteral;

public class CDIJavaFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        stage.setMinWidth(800);
        stage.setMinHeight(800);
        stage.setResizable(true);
        container.getBeanManager().fireEvent(stage, new AnnotationLiteral<StartupScene>() {
        });
    }
}
