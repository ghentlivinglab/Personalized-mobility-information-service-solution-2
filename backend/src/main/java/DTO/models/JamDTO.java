package DTO.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JamDTO {

    private CoordinateDTO[] line;
    private String publicationTime;
    private int speed;
    private int delay;

    public JamDTO() {
        line = new CoordinateDTO[0];
        publicationTime = "";
        this.speed = 0;
        this.delay = 0;
    }

    public JamDTO(CoordinateDTO[] line, String publicationTime, int speed, int delay) {
        this.line = line;
        this.publicationTime = publicationTime;
        this.speed = speed;
        this.delay = delay;
    }

    @JsonProperty("line")
    public CoordinateDTO[] getLine() {
        return line;
    }

    @JsonProperty("publicationTime")
    public String getPublicationTime() {
        return publicationTime;
    }

    @JsonProperty("speed")
    public int getSpeed() {
        return speed;
    }

    @JsonProperty("delay")
    public int getDelay() {
        return delay;
    }

    public void setLine(CoordinateDTO[] line) {
        this.line = line;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}
