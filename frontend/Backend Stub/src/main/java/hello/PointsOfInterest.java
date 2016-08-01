package hello;

public class PointsOfInterest {


        private String name;
        private Integer radius;
        private boolean isHome;
        private boolean active;
        private Address address;



    public PointsOfInterest(String name, Integer radius, boolean isHome, boolean active, Address address) {
        this.name = name;
        this.radius = radius;
        this.isHome = isHome;
        this.active = active;
        this.address = address;
    }

    public PointsOfInterest(){};


    public Integer getRadius() {
        return radius;
    }



    public String getName() {
        return name;
    }

    public boolean isHome() {
        return isHome;
    }

    public boolean isActive() {
        return active;
    }

    public Address getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public void setHome(boolean home) {
        isHome = home;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
