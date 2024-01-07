package com.example.othello;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class BoardController implements Constants{

    @FXML
    private GridPane gridPane;

    @FXML
    private GridPane hoverPane;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ImageView whiteRestingPiece1;

    @FXML
    private ImageView whiteRestingPiece2;

    @FXML
    private ImageView blackRestingPiece1;

    @FXML
    private ImageView blackRestingPiece2;

    private boolean turn = BLACK;
    private Rules rules;
    private final ArrayList <ImageView> whitePieces = new ArrayList<>();
    private final ArrayList <ImageView> blackPieces = new ArrayList<>();

    public void initialize(){
        createButtons();
        createImageViews();
        rules = new Rules();
        layPieces(WHITE, whiteRestingPiece1);
        layPieces(WHITE, whiteRestingPiece2);
        layPieces(BLACK, blackRestingPiece1);
        layPieces(BLACK, blackRestingPiece2);
        updateBoard();
    }

    public void createButtons(){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Button button = new Button();
                button.setPrefWidth(80.0);
                button.setPrefHeight(80.0);
                button.setText("0");
                button.setOpacity(0);
                button.setOnAction(this::handleButtonClick); // Set the action handler

                if((row == 3 && col == 3) || (row == 4 && col == 4)){
                    button.setText("1");
                    button.setDisable(true);
                }

                else if((row == 3 && col == 4) || (row == 4 && col == 3)){
                    button.setText("2");
                    button.setDisable(true);
                }

                ImageView imageViewHovered = new ImageView();
                imageViewHovered.setImage(new Image(Objects.requireNonNull(getClass().getResource("assets/hovered.png")).toExternalForm()));
                hoverPane.add(imageViewHovered, row, col);
                imageViewHovered.setOpacity(0);
                button.setOnMouseEntered(event -> {
                    imageViewHovered.setOpacity(1);
                });

                button.setOnMouseExited(event -> {
                    imageViewHovered.setOpacity(0);
                });

                gridPane.add(button, row, col);
            }
        }
    }

    public void createImageViews(){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                ImageView imageView = new ImageView();
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                gridPane.add(imageView, col, row);
            }
        }
    }

    public void layPieces(boolean color, ImageView originalRestingPiece){
        if(color == WHITE){
            whitePieces.add(originalRestingPiece);
        }
        else{
            blackPieces.add(originalRestingPiece);
        }

        int shift = 28;
        ImageView restingPiece;
        for(int i = 0; i<15; i++){
            restingPiece = new ImageView();
            anchorPane.getChildren().add(restingPiece);
            restingPiece.setFitWidth((originalRestingPiece.getFitWidth()));
            restingPiece.setFitHeight((originalRestingPiece.getFitHeight()));
            restingPiece.setX((originalRestingPiece.getX()-shift));
            restingPiece.setY((originalRestingPiece.getY()));
            shift+=28;
            restingPiece.setImage(originalRestingPiece.getImage());

            if(color == WHITE){
                whitePieces.add(restingPiece);
            }
            else {
                blackPieces.add(restingPiece);
            }
        }
    }

    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int rowIndex = GridPane.getRowIndex(clickedButton);
        int colIndex = GridPane.getColumnIndex(clickedButton);


        System.out.println("\nrow: " + rowIndex + "   " + "col: " + colIndex);

        if(rules.updateMatrixAfterMove(turn, rowIndex, colIndex)){
            clickedButton.setDisable(true);
            updateBoard();
        }
    }

    private ImageView getTargetImageView(int row, int column) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column
                    && node instanceof ImageView) {
                return (ImageView) node;
            }
        }
        return null;
    }

    private Button getTargetButton(int row, int column) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column
                    && node instanceof Button) {
                return (Button) node;
            }
        }
        return null;
    }


    public void putPiece(boolean turn, int rowIndex, int colIndex){
        Image targetImage;
        if(turn == BLACK){
            targetImage = new Image(Objects.requireNonNull(getClass().getResource("assets/blackPiece.png")).toExternalForm());
            Objects.requireNonNull(getTargetImageView(rowIndex, colIndex)).setImage(targetImage);
        }

        else{
            targetImage = new Image(Objects.requireNonNull(getClass().getResource("assets/whitePiece.png")).toExternalForm());
            Objects.requireNonNull(getTargetImageView(rowIndex, colIndex)).setImage(targetImage);
        }
    }

    public void updateBoard(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(rules.getMatrix()[i][j] == 1){
                    if(Objects.requireNonNull(getTargetButton(i, j)).getText().equals("2")){
                        flipToAnimation(WHITE, i , j);
                    }

                    else if(Objects.requireNonNull(getTargetButton(i, j)).getText().equals("0")){
                        moveAnimation(WHITE, i, j);
                    }

                    else{
                        //start animation
                        putPiece(WHITE, i, j);
                    }

                    Objects.requireNonNull(getTargetButton(i, j)).setText("1");
                }

                else if (rules.getMatrix()[i][j] == 2){
                    if(Objects.requireNonNull(getTargetButton(i, j)).getText().equals("1")){
                        flipToAnimation(BLACK, i , j);
                    }

                    else if(Objects.requireNonNull(getTargetButton(i, j)).getText().equals("0")){
                        moveAnimation(BLACK, i, j);

                    }

                    else{
                        //start animation
                        putPiece(BLACK, i, j);
                    }
                    Objects.requireNonNull(getTargetButton(i, j)).setText("2");
                }
            }
        }
    }

    public void flipToAnimation(boolean color, int rowIndex, int colIndex) {
        int startFrame = color == BLACK ? 0 : 10; // Start frame based on the option
        int step = color == BLACK? 1 : -1;       // Step based on the option

        int[] i = {startFrame};

        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.seconds(0.03), event -> {

                    ImageView imageView = getTargetImageView(rowIndex, colIndex);
                    Image image = new Image(Objects.requireNonNull(getClass().getResource("assets/flip animation/flip" + i[0] + ".png")).toExternalForm());
                    assert imageView != null;
                    imageView.setImage(image);
                    i[0] += step;
                })
        );

        timeline1.setCycleCount(11);
        timeline1.play();
    }

    public void moveAnimation(boolean color, int rowIndex, int colIndex){
        ImageView restingImageView = color == WHITE ? whitePieces.getLast() : blackPieces.getLast();
        ImageView targetImageView = getTargetImageView(rowIndex, colIndex);
        assert targetImageView != null;
        double destinationX = targetImageView.getLayoutX();
        double destinationY = targetImageView.getLayoutY();

        // Create TranslateTransition
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), restingImageView);
        transition.setToX(destinationX - restingImageView.getX() + 80 + 25);
        transition.setToY(destinationY - restingImageView.getY() + 80 + 15);

        transition.setOnFinished(event -> {

            if(color == WHITE){
                whitePieces.removeLast();
                putPiece(WHITE, rowIndex, colIndex);
            }

            else{
                blackPieces.removeLast();
                putPiece(BLACK, rowIndex, colIndex);
            }
            restingImageView.setOpacity(0);
            rules.updateMatrixAfterFlip(rowIndex, colIndex);
            updateBoard();
            if(!checkWinner(false)){
                if(checkPassMove(turn) && checkPassMove(!turn)){
                    checkWinner(true);
                }
                else if(!checkPassMove(!turn)){
                    turn = !turn;
                }
                else{
                    System.out.println("move passed");
                }
            };
        });

        transition.play();
    }

    public boolean checkWinner(boolean key){
        if(key){
            System.out.println("IT IS A DRAW (key)");
            return true;
        }

        else if(rules.getNumPieces()[0] == 0){
            if(rules.getNumPieces()[1] > rules.getNumPieces()[2]){
                System.out.println("WHITE IS WINNER");
            }

            else if(rules.getNumPieces()[1] < rules.getNumPieces()[2]){
                System.out.println("BLACK IS WINNER");
            }

            else{
                System.out.println("IT IS A DRAW");
            }

            return true;
        }
        return false;
    }

    public boolean checkPassMove(boolean color){
        for(int i = 0; i<8; i++){
            for(int j = 0; j< 8; j++){
                if(rules.getMatrix()[i][j] == 0 && rules.updateMatrixAfterMove(color, i, j)){
                    rules.getMatrix()[i][j] = 0;
                    return false;
                }
                else if(rules.getMatrix()[i][j] == 0){
                    rules.getMatrix()[i][j] = 0;
                }
            }
        }
        return true;
    }

    //GETTERS BELOW:
    public boolean getTurn() {
        return turn;
    }

    //SETTERS BELOW:
    public void setTurn(boolean turn) {
        this.turn = turn;
    }
}