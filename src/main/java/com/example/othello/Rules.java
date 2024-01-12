package com.example.othello;

import java.util.Arrays;

public class Rules implements Constants{
//    Elements in matrix follows
//    Unfilled slots are 0
//    White is 1
//    Black is 2

    private int[][] matrix = new int[8][8];
    private int[] numPieces = new int[]{0, 0, 0};

    public Rules(){
        createMatrix();
        updateNumPieces();
    }

    public void createMatrix(){
        for (int[] ints : matrix) {
            Arrays.fill(ints, 0);
        }

        matrix[3][3] = 1;
        matrix[4][4] = 1;
        matrix[3][4] = 2;
        matrix[4][3] = 2;
    }

    public void printMatrix(){
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public boolean checkValidity(boolean turn, int rowIndex, int colIndex){
        if (turn) {
            matrix[rowIndex][colIndex] = 1;
        } else {
            matrix[rowIndex][colIndex] = 2;
        }

        boolean validMove = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && flip(true, rowIndex, colIndex, i, j)) {
                    validMove = true;
                    break;
                }
            }
        }
        matrix[rowIndex][colIndex] = 0;
        return validMove;
    }

    public void updateMatrixAfterMove(boolean turn, int rowIndex, int colIndex) {
        if (turn) {
            matrix[rowIndex][colIndex] = 1;
        } else {
            matrix[rowIndex][colIndex] = 2;
        }
    }

    public void updateMatrixAfterFlip(int rowIndex, int colIndex){
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(!(i == 0 && j== 0)){
                    flip(false, rowIndex, colIndex, i, j);
                }
            }
        }
        updateNumPieces();
        printMatrix();
    }

    public boolean flip(boolean CHECK, int initialRowIndex, int initialColIndex, int verticalIncrement, int horizontalIncrement){
        int currentTurnNumber = matrix[initialRowIndex][initialColIndex];

        int i = initialRowIndex;
        int j = initialColIndex;

        int flipType;
        if(Math.abs(verticalIncrement) == 1 && horizontalIncrement == 0){
            flipType = 1;
        }
        else if(verticalIncrement == 0 && Math.abs(horizontalIncrement) == 1){
            flipType = 2;
        }
        else{
            flipType = 3;
        }

        boolean flag = false;

        while(0 <= i && i < rows && 0 <= j && j < columns && matrix[i][j] != 0){
            if (matrix[i][j] == currentTurnNumber) {
                if((flipType == 1 && (initialRowIndex != i)) ||
                        (flipType == 2 && (initialColIndex != j)) ||
                        (flipType == 3 && (initialRowIndex != i) && initialColIndex != j)){
                    flag = (Math.abs(i - initialRowIndex) != 1) && (Math.abs(j - initialColIndex) != 1);
                    break;
                }
                else{
                    i+= verticalIncrement;
                    j+= horizontalIncrement;
                }
            }
            else{
                i+= verticalIncrement;
                j+= horizontalIncrement;
            }
        }

        if(flag && !CHECK){
            int k = initialRowIndex;
            int l = initialColIndex;
            for(int x = 0; x < Math.max((Math.abs(initialRowIndex - i) + 1), (Math.abs(initialColIndex - j))); x++){
                matrix[k][l] = currentTurnNumber;
                k+=verticalIncrement;
                l+=horizontalIncrement;
            }
        }
        return flag;
    }

    public void updateNumPieces(){
        numPieces[0] = 0;
        numPieces[1] = 0;
        numPieces[2] = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                numPieces[matrix[i][j]]++;
            }
        }
    }

    public int checkWinner(){
        if(numPieces[0] == 0){
            if(numPieces[1] > numPieces[2]){
                System.out.println("WHITE IS WINNER");
                return 1;
            }

            else if(numPieces[1] < numPieces[2]){
                System.out.println("BLACK IS WINNER");
                return 2;
            }

            else{
                System.out.println("IT IS A DRAW");
                return 3;
            }
        }
        return 0;
    }

    public void declareDraw(){
        System.out.println("IT IS A DRAW (key)");
    }

    public boolean checkPassMove(boolean color){
        for(int i = 0; i<8; i++){
            for(int j = 0; j< 8; j++){
                if(matrix[i][j] == 0 && checkValidity(color, i, j)){
                    matrix[i][j] = 0;
                    return false;
                }
                else if(matrix[i][j] == 0){
                    matrix[i][j] = 0;
                }
            }
        }
        return true;
    }

    //GETTERS BELOW:

    public int[][] getMatrix() {
        return matrix;
    }

    public int[] getNumPieces() {
        return numPieces;
    }

    //SETTERS BELOW:

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void setNumPieces(int[] numPieces) {
        this.numPieces = numPieces;
    }
}
