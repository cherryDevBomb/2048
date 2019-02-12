package main.java;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;


@ManagedBean
@ApplicationScoped
public class GameBean {

    private int[][] matrix = { {0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0} };
    private int score = 0;
    private int maxReached = 2;
    private boolean gameResult;

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public void setMaxReached(int maxReached) {
        this.maxReached = maxReached;
    }



    public GameBean() {
        reset();
    }

    public void reset() {
        matrix = new int[][] { {0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0} };
        score = 0;
        maxReached = 2;
        gameResult = false;
        generateNewNumber();
        generateNewNumber();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public String getCellValue(int row, int column) {
        if (matrix[row][column] == 0)
            return "";
        return String.valueOf(matrix[row][column]);
    }

    public String getCellColor(int row, int column) {
        int value = matrix[row][column];
        if (value == 0) return "#222222";
        if (value == 2) return "#011223";
        if (value == 4) return "#011930";
        if (value == 8) return "#05386b";
        if (value == 16) return "064254";
        if (value == 32) return "#1c6e7c";
        if (value == 64) return "#379683";
        if (value == 128) return "4ee4af";
        if (value == 256) return "#81d7a2";
        if (value == 512) return "#6fce93";
        if (value == 1024) return "#5fc987";
        if (value == 2048) return "#3fd884";
        return "#3fd884";
    }

    public String getCellFontStyle(int row, int column) {

        int value = matrix[row][column];
        if (value <= 32)
            return "font-size: 50px; color: #c0c0c0;";
        else if (value == 64)
            return "font-size: 50px; color: #161616;";
        else if (value <= 512)
            return "font-size: 41px; color: #161616;";
        return "font-size: 33px; color: #161616;";
    }

    public int getScore() {
        return score;
    }

    private void generateNewNumber() {
        int randomX = ThreadLocalRandom.current().nextInt(0, 4);
        int randomY = ThreadLocalRandom.current().nextInt(0, 4);
        while(matrix[randomY][randomX] != 0) {
            randomX = ThreadLocalRandom.current().nextInt(0, 4);
            randomY = ThreadLocalRandom.current().nextInt(0, 4);
        }
        int randomValue = ThreadLocalRandom.current().nextInt(0, 4);
        if (randomValue == 1)
            randomValue = 4;
        else
            randomValue = 2;
        matrix[randomY][randomX] = randomValue;

    }

    public void continueGame() {
        maxReached = 2;
    }


    public void moveLeft() {
        int[][] previousState = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                previousState[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < 4; i++) {
            //merge and sum up neighbour values with same value
            for (int j = 0; j < 3; j++) {
                if (matrix[i][j] != 0) {
                    int neighbourValColumn = j + 1;
                    //find next cell to the right that is not empty or stop at last cell
                    while (matrix[i][neighbourValColumn] == 0 && neighbourValColumn < 3) {
                        neighbourValColumn++;
                    }
                    if (matrix[i][j] == matrix[i][neighbourValColumn]) {
                        matrix[i][j] += matrix[i][neighbourValColumn];
                        matrix[i][neighbourValColumn] = 0;
                        score += matrix[i][j];
                        if (matrix[i][j] > maxReached)
                            maxReached = matrix[i][j];
                    }
                }
            }
            //move tiles to left side
            int j = 0;
            while (j < 3) {
                if (matrix[i][j] == 0) {
                    int nextValColumn = j + 1;
                    //find next cell that is not empty
                    while(matrix[i][nextValColumn] == 0 && nextValColumn < 3)  {
                        nextValColumn++;
                    }
                    matrix[i][j] = matrix[i][nextValColumn];
                    matrix[i][nextValColumn] = 0;
                    //skip intermediate free tiles
                    //j = nextValColumn;
                    j++;
                }
                else {
                    j++;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (previousState[i][j] != matrix[i][j]) {
                    generateNewNumber();
                    return;
                }
            }
        }
    }


    public void rotateMatrixClockwise() {
        int[][] newMatrix = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newMatrix[i][j] = matrix[j][abs(i-3)];
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = newMatrix[i][j];
            }
        }
    }


    public boolean gameOverCheck() {
        if (maxReached == 2048) {
            gameResult = true;
            return true;
        }

        int free = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] == 0)
                    free++;
            }
        }
        //2 neighbour cells with same value = move possible
        if (free == 0) {
            boolean movePossible = false;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if (matrix[i][j] == matrix[i][j+1])
                        movePossible = true;
                }
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (matrix[i][j] == matrix[i+1][j])
                        movePossible = true;
                }
            }

            if (!movePossible)
                return true;
        }
        return false;
    }

    public String getShowWonDialog() {
        if (gameOverCheck()) {
            if (gameResult)
                return "true";
        }
        return "false";
    }

    public String getShowLostDialog() {
        if (gameOverCheck()) {
            if (!gameResult)
                return "true";
        }
        return "false";
    }


    public String getGameOverMessage() {
        if (gameResult)
            return "You won";
        else
            return "You lost";
    }


    public void moveUp() {
        rotateMatrixClockwise();
        moveLeft();
        rotateMatrixClockwise();
        rotateMatrixClockwise();
        rotateMatrixClockwise();
    }

    public void moveRight() {
        rotateMatrixClockwise();
        rotateMatrixClockwise();
        moveLeft();
        rotateMatrixClockwise();
        rotateMatrixClockwise();
    }

    public void moveDown() {
        rotateMatrixClockwise();
        rotateMatrixClockwise();
        rotateMatrixClockwise();
        moveLeft();
        rotateMatrixClockwise();
    }
}
