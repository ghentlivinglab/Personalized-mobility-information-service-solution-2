package hello;


public class Jam {

    private Coordinate start;
    private Coordinate end;
    private Integer speed;
    private Integer delay;

    public Jam(Coordinate start, Coordinate end, Integer speed, Integer delay) {
        this.start = start;
        this.end = end;
        this.speed = speed;
        this.delay = delay;
    }

    public Jam(){};

    public Coordinate getStart() {
        return start;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
