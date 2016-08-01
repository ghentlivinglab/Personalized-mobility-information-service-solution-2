package hello;


public class Travel {

    private String id;
    private String name;
    private Route route;
    private String date;
    private String[] timeInterval;
    private boolean isArrivalTime;
    private boolean[] recurring;

    public Travel(String id, String name, Route route, String date, String[] timeInterval, boolean isArrivalTime, boolean[] recurring) {
        this.id = id;
        this.name = name;
        this.route = route;
        this.date = date;
        this.timeInterval = timeInterval;
        this.isArrivalTime = isArrivalTime;
        this.recurring = recurring;
    }

    public Travel(){};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getTime_interval() {
        return timeInterval;
    }

    public void setTime_interval(String[] timeInterval) {
        this.timeInterval = timeInterval;
    }

    public boolean getIs_arrival_time() {
        return isArrivalTime;
    }

    public void setIs_arrival_time(boolean arrivalTime) {
        isArrivalTime = arrivalTime;
    }

    public boolean[] getRecurring() {
        return recurring;
    }

    public void setRecurring(boolean[] recurring) {
        this.recurring = recurring;
    }
}
