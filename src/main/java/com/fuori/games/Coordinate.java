package com.fuori.games;

/**
 * Created with IntelliJ IDEA.
 * User: gavinastur
 * Date: 22/02/15
 * Time: 12:13
 *
 */
public final class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
