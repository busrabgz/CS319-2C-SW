package SevenWonders;

import SevenWonders.Network.Client;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SceneManager {
    private static SceneManager instance;

    public Stage getStage() {
        return stage;
    }

    private Stage stage;
    public static Client client = Client.getInstance();

    Parent currentRoot;

    public Parent getCurrentRoot() {
        return currentRoot;
    }

    private SceneManager(Stage firstStage) {
        stage = firstStage;
    }

    public static void initialize(Stage firstStage) {
        instance = new SceneManager(firstStage);
        Font.loadFont(SceneManager.getInstance().getClass().getClassLoader().getResource("fonts/Assassin$.ttf").toExternalForm(), 60);
    }

    public static SceneManager getInstance() {
        return instance;
    }

    public void popPaneOnScreenNow(Parent root) {
        for (Node n : ((Pane) currentRoot).getChildren()) {
            if (n == root) {
                ((Pane) currentRoot).getChildren().remove(n);
                break;
            }
        }
    }

    public void showPaneOnScreenNow(Parent root) {
        StackPane sp = (StackPane) currentRoot;
        sp.setAlignment(Pos.CENTER);;
        sp.getChildren().add(root);
    }

    public void changeScene(String sceneName) {
        currentRoot = AssetManager.getInstance().getSceneByName(sceneName);
        changeScene(currentRoot);
    }

    public void changeScene(Parent root) {
        SoundManager.getInstance();
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(currentRoot);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
       Image image = new Image("images/tokens/arrow.png");
       scene.setCursor(new ImageCursor(image));
    }

    public void actOnExit() {
        stage.close();
    }

    public void show(){
        stage.show();
    }
}
