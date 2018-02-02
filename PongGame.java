/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pong.game;

import static java.lang.System.exit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Brendon Lyra
 */
public class PongGame extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        
        //will close whole application when x on top right is clicked.
        stage.setOnCloseRequest(event -> exit(0));       
        
        
        
        
        Scene scene = new Scene(root);
        stage.setTitle("Pong!");       
        stage.setScene(scene);
        stage.show();               
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
