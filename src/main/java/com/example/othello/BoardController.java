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
        setupBoard();
    }

    public void createButtons(){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Button button = new Button();
                button.setPrefWidth(80.0);
                button.setPrefHeight(80.0);
                button.setText("0");
                button.setOpacity(0);
                button.setOnAction(this::handleButtonClick);
                highlightSquare(button, row, col);
            }
        }
    }

    public void highlightSquare(Button button, int rowIndex, int colIndex){
        ImageView imageViewHovered = new ImageView();
        imageViewHovered.setImage(new Image(Objects.requireNonNull(getClass().getResource("assets/hover.png")).toExternalForm()));
        imageViewHovered.setFitWidth(50);
        imageViewHovered.setFitHeight(50);
        hoverPane.add(imageViewHovered, rowIndex, colIndex);
        imageViewHovered.setOpacity(0);
        button.setOnMouseEntered(event -> {
//            if(rules.checkValidity(turn, rowIndex, colIndex)){
                imageViewHovered.setOpacity(1);
//            }
//
//            else{
//                rules.printMatrix();
//                System.out.println("\n\n");
//            }
        });

        button.setOnMouseExited(event -> {
            imageViewHovered.setOpacity(0);
        });

        gridPane.add(button, rowIndex, colIndex);
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

        int shiftValue = 28;
        int shift = shiftValue;
        ImageView restingPiece;
        for(int i = 0; i<15; i++){
            restingPiece = new ImageView();
            anchorPane.getChildren().add(restingPiece);
            restingPiece.setFitWidth((originalRestingPiece.getFitWidth()));
            restingPiece.setFitHeight((originalRestingPiece.getFitHeight()));
            restingPiece.setX((originalRestingPiece.getX()-shift));
            restingPiece.setY((originalRestingPiece.getY()));
            shift+=shiftValue;
            restingPiece.setImage(originalRestingPiece.getImage());

            if(color == WHITE){
                whitePieces.add(restingPiece);
            }
            else {
                blackPieces.add(restingPiece);
            }
        }
    }

    public void setupBoard(){
        Image image;

        image = new Image(Objects.requireNonNull(getClass().getResource("assets/whitePiece.png")).toExternalForm());
        Objects.requireNonNull(getTargetImageView(3, 3)).setImage(image);
        Objects.requireNonNull(getTargetImageView(4, 4)).setImage(image);

        image = new Image(Objects.requireNonNull(getClass().getResource("assets/blackPiece.png")).toExternalForm());
        Objects.requireNonNull(getTargetImageView(3, 4)).setImage(image);
        Objects.requireNonNull(getTargetImageView(4, 3)).setImage(image);

        Objects.requireNonNull(getTargetButton(3, 3)).setText("1");
        Objects.requireNonNull(getTargetButton(4, 4)).setText("1");
        Objects.requireNonNull(getTargetButton(3, 4)).setText("2");
        Objects.requireNonNull(getTargetButton(4, 3)).setText("2");

        Objects.requireNonNull(getTargetButton(3, 3)).setDisable(true);
        Objects.requireNonNull(getTargetButton(4, 4)).setDisable(true);
        Objects.requireNonNull(getTargetButton(3, 4)).setDisable(true);
        Objects.requireNonNull(getTargetButton(4, 3)).setDisable(true);

        rules.printMatrix();
    }

    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int rowIndex = GridPane.getRowIndex(clickedButton);
        int colIndex = GridPane.getColumnIndex(clickedButton);

        System.out.println("\nROW: " + rowIndex + "   " + "COL: " + colIndex);

        if(rules.checkValidity(turn, rowIndex, colIndex)){
            rules.updateMatrixAfterMove(turn, rowIndex, colIndex);
            clickedButton.setDisable(true);
            movePiece(rowIndex, colIndex);
        }

        else{
            System.out.println("INVALID MOVE");
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

    public void movePiece(int rowIndex, int colIndex){
        if(turn == WHITE){
            Objects.requireNonNull(getTargetButton(rowIndex, colIndex)).setText("1");
        }

        else{
            Objects.requireNonNull(getTargetButton(rowIndex, colIndex)).setText("2");
        }

        moveAnimation(turn, rowIndex, colIndex);
    }

    public void moveAnimation(boolean color, int rowIndex, int colIndex){
        ImageView restingImageView = color == WHITE ? whitePieces.getLast() : blackPieces.getLast();
        ImageView targetImageView = getTargetImageView(rowIndex, colIndex);
        assert targetImageView != null;
        double destinationX = targetImageView.getLayoutX();
        double destinationY = targetImageView.getLayoutY();

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), restingImageView);
        transition.setToX(destinationX - restingImageView.getX() + 105); //why these offsets?
        transition.setToY(destinationY - restingImageView.getY() + 95);

        transition.setOnFinished(event -> {

            if(color == WHITE){
                whitePieces.removeLast();
            }

            else{
                blackPieces.removeLast();
            }

            putPiece(color, rowIndex, colIndex);
            restingImageView.setOpacity(0);

            rules.updateMatrixAfterFlip(rowIndex, colIndex);
            flipPieces();
            checkException();
        });

        transition.play();
    }

    public void flipPieces(){
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if(rules.getMatrix()[row][col] == 1 && Objects.requireNonNull(getTargetButton(row, col)).getText().equals("2")){
                    flipToAnimation(WHITE, row , col);
                    Objects.requireNonNull(getTargetButton(row, col)).setText("1");
                }

                else if (rules.getMatrix()[row][col] == 2 && Objects.requireNonNull(getTargetButton(row, col)).getText().equals("1")){
                    flipToAnimation(BLACK, row , col);
                    Objects.requireNonNull(getTargetButton(row, col)).setText("2");
                }
            }
        }
    }

    public void flipToAnimation(boolean color, int rowIndex, int colIndex) {
        int startFrame = color == BLACK ? 0 : 10;
        int step = color == BLACK? 1 : -1;

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

    public void checkException(){
        if(rules.checkWinner() == 0){
            if(rules.checkPassMove(turn) && rules.checkPassMove(!turn)){
                rules.declareDraw();
            }
            else if(!rules.checkPassMove(!turn)){
                turn = !turn;
            }
            else{
                System.out.println("MOVE PASSED");
            }
        }
        //code rest cases to declare result on GUI
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