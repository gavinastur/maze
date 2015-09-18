package com.fuori.games;

import java.util.ArrayList;
import java.util.List;

class Maze {

    private List<Coordinate> breadcrumbs = new ArrayList<>();

    private int counter = 0;

    private PathsAndHedges pathsAndHedges = new PathsAndHedges();

    public Maze() {
         this.init();
    }

    protected void init() {
        // validation to make sure we have a valid Maze
        if (count(Cell.FINISH) != 1) {
            throw new RuntimeException("Maze must contain one Cell of type: " + Cell.FINISH.getValue());
        }

        if (count(Cell.START) != 1) {
            throw new RuntimeException("Maze must contain one Cell of type: " + Cell.START.getValue());
        }

        if (count(Cell.PATH) < 1) {
            throw new RuntimeException("Maze must contain some space to move!");
        }
    }

    public int count(final Cell cell) {
        int count = 0;

        for(String[] row: getPathsAndHedges().getGrid()) {
            for(String col: row) {
                if (col.equals(cell.getValue())){
                    count++;
                }
            }
        }

        return count;
    }

    public PathsAndHedges getPathsAndHedges() {
        return pathsAndHedges;
    }

    List<Coordinate> getBreadcrumbs() {
        return breadcrumbs;
    }

    public Cell getCellType(Coordinate xy) {
        return Cell.value(getPathsAndHedges().getGrid()[xy.getY()][xy.getX()]);
    }

    public Coordinate getCurrentCoordinates(){
        return getXY(Cell.CURRENT);
    }

    public Coordinate getStartCoordinates(){
        return getXY(Cell.START);
    }

    public Coordinate getFinishCoordinates(){
        return getXY(Cell.FINISH);
    }

    public Coordinate getXY(Cell cell){

        Coordinate coordinate = new Coordinate(-1, -1);

        for (int row = 0; row < getPathsAndHedges().getGrid().length; row++) {
            for (int col = 0; col < getPathsAndHedges().getGrid()[row].length; col++) {
                if (getPathsAndHedges().getGrid()[row][col].equals(cell.getValue())){
                    coordinate = new Coordinate(col, row);
                }
            }
        }
        return coordinate;
    }

    public boolean moveForward(final int row, final int col) {

        boolean moved = isMovePossible(row + 1, col);

        if (moved) {
            getPathsAndHedges().getGrid()[row+1][col] = Cell.CURRENT.getValue();
            getPathsAndHedges().getGrid()[row][col] = Cell.BREADCRUMB.getValue();
            breadcrumbs.add(new Coordinate(col,row));
        }
        return moved;
    }

    public boolean moveBackward(final int row, final int col) {

        boolean moved = isMovePossible(row - 1, col);

        if (moved) {
            getPathsAndHedges().getGrid()[row-1][col] = Cell.CURRENT.getValue();
            getPathsAndHedges().getGrid()[row][col] = Cell.BREADCRUMB.getValue();
            breadcrumbs.add(new Coordinate(col,row));
        }
        return moved;
    }

    public boolean moveLeft(final int row, final int col) {
        boolean moved = isMovePossible(row, col + 1);

        if (moved) {
            getPathsAndHedges().getGrid()[row][col+1] = Cell.CURRENT.getValue();
            getPathsAndHedges().getGrid()[row][col] = Cell.BREADCRUMB.getValue();
            breadcrumbs.add(new Coordinate(col,row));
        }
        return moved;
    }

    public boolean moveRight(final int row, final int col) {
        boolean moved = isMovePossible(row, col - 1);

        if (moved) {
            getPathsAndHedges().getGrid()[row][col-1] = Cell.CURRENT.getValue();
            getPathsAndHedges().getGrid()[row][col] = Cell.BREADCRUMB.getValue();
            breadcrumbs.add(new Coordinate(col,row));
        }
        return moved;
    }

    protected boolean findAWay(final int row, final int col) {

       this.print();

        boolean done = false;

        if (isMovePossible(row, col)) {

            if (getPathsAndHedges().getGrid()[row][col].equals(Cell.FINISH.getValue())) {
                done = true;
            } else {

                getPathsAndHedges().getGrid()[row][col] = String.valueOf(counter);  // cell has been tried
                if (counter == 9) {
                    counter = 0;
                }
                counter++;

                done = findAWay(row + 1, col);  // forward

                if (!done) {
                    done = findAWay(row, col - 1);  // right
                }

                if (!done) {
                    done = findAWay(row - 1, col);  // back
                }

                if (!done) {
                    done = findAWay(row, col + 1);  // left
                }
            }
            if (done){
                // Breadcrumb
                getPathsAndHedges().getGrid()[row][col] = Cell.BREADCRUMB.getValue();
                counter = 0;
            }

        }

        return done;

    }

    private boolean isMovePossible(final int row, final int col) {

        boolean answer = false;

        // Check if we're trying to move sensibly within boundaries
        if (row >= 0 && row < getPathsAndHedges().getGrid().length && col >= 0 && col < getPathsAndHedges().getGrid()[0].length) {
            // Now can we move there?
            if (getPathsAndHedges().getGrid()[row][col].equals(Cell.PATH.getValue()) ||
                    getPathsAndHedges().getGrid()[row][col].equals(Cell.BREADCRUMB.getValue()) ||
                    getPathsAndHedges().getGrid()[row][col].equals(Cell.START.getValue()) ||
                    getPathsAndHedges().getGrid()[row][col].equals(Cell.FINISH.getValue())) {
                answer = true;
            }
        }

        return answer;

    }

    public void print() {

        for(String[] row: getPathsAndHedges().getGrid()) {
            for(String col: row) {
                System.out.print(col);
            }
            System.out.println();
        }
        System.out.println();
    }

    public enum Cell {

        CURRENT("*"),
        START("S"),
        FINISH("F"),
        HEDGE("X"),
        PATH(" "),
        BREADCRUMB("."),
        VISITED("-"),
        UNKNOWN("");

        private final String value;

        Cell(final String x) {
            value = x;
        }

        public String getValue() {
            return value;
        }

        public static Cell value(final String val) {
            Cell a = UNKNOWN;
            for (Cell value : values()) {

                if (value.getValue().equals(val)) {
                    a = value;
                }
            }
            return a;
        }
    }

    public class PathsAndHedges {

        //Default paths and hedges
        private String[][] grid = new String[][]{
                {"X", "S", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", " ", " ", " ", "X", " ", " ", "X", "X", "X", " ", " ", " ", " ", "X"},
                {"X", " ", "X", " ", " ", " ", "X", " ", " ", " ", " ", "X", "X", " ", "X"},
                {"X", " ", "X", "X", "X", " ", "X", " ", "X", " ", "X", " ", "X", "X", "X"},
                {"X", " ", "X", " ", "X", " ", " ", " ", "X", " ", "X", " ", " ", " ", "X"},
                {"X", "X", "X", " ", "X", "X", "X", "X", " ", " ", " ", "X", "X", " ", "X"},
                {"X", " ", " ", " ", " ", " ", " ", " ", " ", "X", " ", " ", " ", " ", "X"},
                {"X", " ", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"},
                {"X", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "X"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "F", "X"},

        };

        public String[][] getGrid() {
            return grid;
        }

    }
}
