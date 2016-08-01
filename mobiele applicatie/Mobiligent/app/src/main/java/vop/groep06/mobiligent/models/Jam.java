package vop.groep06.mobiligent.models;


import java.util.ArrayList;

public class Jam {

    private ArrayList<Coordinate> line;
    private double speed;
    private double delay;

    public Jam() {
    }

    public Jam(ArrayList<Coordinate> line, double speed, double delay) {
        this.line = line;
        this.speed = speed;
        this.delay = delay;
    }

    public ArrayList<Coordinate> getLine() {
        return line;
    }

    public void setLine(ArrayList<Coordinate> line) {
        this.line = line;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
}
