package packageClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private FXMLLoader loaderWindowStorage;
    private Parent rootWindowStorage;
    private Scene sceneWindowStorage;
    private static Controller controllerWindowStorage;
    private static Stage stageWindowStorage;

    private void setPrivariStageClient(Stage stage){
        Main.stageWindowStorage = stage;
    }
    public static Stage getStageClient(){
        return Main.stageWindowStorage;
    }

    public static Controller getControllerWindowStorage(){
        return controllerWindowStorage;
    }
    @Override
    public void start(Stage stage) throws Exception{
        stageWindowStorage = new Stage();
        loaderWindowStorage = new FXMLLoader(getClass().getResource("/Client.fxml"));
        rootWindowStorage = (Parent)loaderWindowStorage.load();
        sceneWindowStorage = new Scene(rootWindowStorage,300,275);
        stageWindowStorage.setScene(sceneWindowStorage);
        stageWindowStorage.setTitle("Network Storage");
        controllerWindowStorage = loaderWindowStorage.getController();

        stageWindowStorage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
