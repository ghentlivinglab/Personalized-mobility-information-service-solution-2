package hello;

import java.util.List;

public class User {

    private String id, first_name, last_name, email, password, cellNumber;
    private Gender gender;
    private List<PointsOfInterest> pointsOfinterest;
    private boolean muteNotifications;
    private List<Travel> travels;
    public User(String id, String first_name, String last_name, String email, String password, String cellNumber,
                Gender gender, List<PointsOfInterest> pointsOfinterest, boolean muteNotifications, List<Travel> travels) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.cellNumber = cellNumber;
        this.gender = gender;
        this.pointsOfinterest = pointsOfinterest;
        this.muteNotifications = muteNotifications;
        this.travels = travels;
    }

    public User(){};

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name(){
        return last_name;
    }

    public String getEmail(){
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirst_name(String firstName) {
        this.first_name = firstName;
    }

    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCell_number() {
        return cellNumber;
    }

    public void setCell_number(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<PointsOfInterest> getPoints_of_interest() {
        return pointsOfinterest;
    }

    public void setPoints_of_interest(List<PointsOfInterest> pointsOfinterest) {
        this.pointsOfinterest = pointsOfinterest;
    }

    public boolean getMute_notifications() {
        return muteNotifications;
    }

    public void setMute_notifications(boolean muteNotifications) {
        this.muteNotifications = muteNotifications;
    }

    public List<Travel> getTravels() {
        return travels;
    }

    public void setTravels(List<Travel> travels) {
        this.travels = travels;
    }

    public void addTravel(Travel t) {
        t.setId(String.valueOf(travels.size()));
        travels.add(t);

    }
}