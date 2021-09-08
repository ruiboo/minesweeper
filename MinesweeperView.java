import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/** represents a Car.
 * @author rsong48
 * @version 1.1
 */
public class MinesweeperView extends Application {
    private Stage window;
    // Scene scene, scene2;
    /**
    * Our main method. Some kind of handy description goes here.
    * @param args The command line arguments.
    **/
    public static void main(String[] args) {
        launch(args);
    }

    /** a getter for my private Stage.
     * @return window - the stage */
    public Stage getWindow() {
        return window;
    }

    // public void setWindow(Stage window) {
    //     this.window = window;
    // }
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        final ComboBox<Difficulty> difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll(Difficulty.values());
        Text textComboBox = new Text(125, 240, "Difficulty: ");
        textComboBox.setFont(
            Font.font("Roboto", FontPosture.ITALIC, 15));
        textComboBox.setFill(Color.BLACK);


        Text text = new Text(90, 135, "Welcome to Minesweeper!");
        text.setFont(
            Font.font("Roboto", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 100));
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font: 24 arial;");
        BorderPane pane = new BorderPane();
        HBox hbox = new HBox(difficultyComboBox);
        hbox.setAlignment(Pos.CENTER);
        pane.setCenter(hbox);

        //setting up a text input box
        HBox box = new HBox(5);
        TextField textField1 = new TextField();
        Text textName = new Text(120, 485, "Name: ");
        text.setFont(
            Font.font("Roboto", FontPosture.ITALIC, 15));
        text.setFill(Color.BLACK);
        box.setAlignment(Pos.BOTTOM_LEFT);
        box.setPadding(new Insets(25, 5, 5, 170));
        box.getChildren().addAll(textField1);
        pane.setBottom(box);


        //making a start button
        Button button = new Button();
        button.setText("Start");
        button.setTranslateX(225);
        button.setTranslateY(300);
        pane.setTop(button);

        // this causes new scene
        button.setOnAction(e -> {
            if ((difficultyComboBox.getValue() == null) || textField1.getText().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Invalid, please select a difficulty and enter your name");
                a.showAndWait();
            } else {
                playMinesweeper(primaryStage, difficultyComboBox.getValue(), textField1.getText());
            }
        });


        pane.getChildren().add(text);
        pane.getChildren().add(textName);
        pane.getChildren().add(textComboBox);
        Scene scene = new Scene(pane, 500, 500);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    //new window method
    /** method that calls sets the gameboard stuff.
     * @param primaryStage the stage name
     * @param gameDifficulty the difficulty of the game
     * @param name the name of the player
     */
    public void playMinesweeper(Stage primaryStage, Difficulty gameDifficulty, String name) {
        MinesweeperGame game = new MinesweeperGame(gameDifficulty);

        GridPane gridPane = new GridPane();
        Button[][] button1 = new Button[15][15];

        for (int i = 0; i < button1.length; i++) {
            for (int j = 0; j < button1[i].length; j++) {
                //button1[i][j] = new Button("X");
                Button button2 = new Button("X");
                button2.setUserData(new int[] {i, j});
                button2.setOnAction(
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e1) {
                            int[] data = (int[]) button2.getUserData();
                            int x = data[0];
                            int y = data[1];

                            //&& gameboard[0].isMine() == true
                            Tile[] gameboard = game.check(y, x);
                            if (gameboard.length == 1 && gameboard[0].isMine()) {
                                //create a new screen: you lost
                                Stage stage2 = new Stage();
                                String resultMessage = "You Lost, " + name;
                                result(stage2, resultMessage);

                            } else {
                                for (int k = 0; k < gameboard.length; k++) {
                                    Tile my = gameboard[k];
                                    int x1 = my.getX();
                                    int y2 = my.getY();
                                    button1[x1][y2].setText(String.valueOf(my.getBorderingMines()));
                                }
                            }
                            if (game.isWon()) {

                                //Text textfield3 = new Text("You Won, " + name);
                                Stage stage3 = new Stage();
                                String resultMessage = "You Win, " + name;
                                result(stage3, resultMessage);
                            }
                        }
                    }
                );

                button1[i][j] = button2;
                gridPane.add(button2, i, j);




            }
        }
        //this is the game board scene
        Scene scene4 = new Scene(gridPane, 400, 410);
        primaryStage.setScene(scene4);
        // BorderPane pane2 = new BorderPane();
        // Scene scene2 = new Scene(pane2, 500, 500);
        // primaryStage.setScene(scene2);



    }
    /** a helper method that takes care of the resulting pop up
     * when you lose or win.
     * @param primaryStage this is the stage
     * @param message this is the message of whether they won or lost
     */
    public void result(Stage primaryStage, String message) {

        primaryStage.setTitle("Minesweeper");
        BorderPane pane1 = new BorderPane();
        Button button1 = new Button();
        button1.setText("Restart");
        button1.setTranslateX(5);
        button1.setTranslateY(10);
        button1.setAlignment(Pos.CENTER);
        pane1.setCenter(button1);

        HBox box1 = new HBox(5);
        Text textName2 = new Text(100, 300, message);
        textName2.setFont(
            Font.font("Roboto", FontPosture.ITALIC, 30));
        textName2.setFill(Color.BLACK);
        box1.getChildren().addAll(textName2);
        pane1.setTop(box1);
        // Text textfield3 = new Text(message);
        // pane1.getChildren().add(textfield3);

        //this is the "you lost/won" scene
        GridPane gridPane1 = new GridPane();
        Scene scene2 = new Scene(pane1, 200, 100);
        primaryStage.setScene(scene2);
        primaryStage.show();

        //sets back to welcome screen
        button1.setOnAction(e1 -> {
            primaryStage.close();
            start(window);
        });

    }
}
