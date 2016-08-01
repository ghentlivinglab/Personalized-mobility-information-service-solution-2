package hello;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserCreator {

    private final AtomicInteger userCounter = new AtomicInteger();

    //Keep a counter to make new unique id's for new routes
    private final AtomicInteger routeCounter = new AtomicInteger();

    //Keep a counter to make new uniques id's for the different travels
    private final AtomicInteger travelCounter = new AtomicInteger();

    //This method will create user 1

    public User createUser1(){

        //Make a list containing 1 point of interest for the person
        List<PointsOfInterest> pointsOfInterestList =
                makePointsOfInterest("home", "Galglaan", "13", "Gent", "Belgium", "9000",
                        new Coordinate(51.0279417, 3.7167915 ), 100, true, true);

        //Make a new home address for the person
        Address home = new Address("Galglaan", "13", "Gent", "Belgium", "9000", new Coordinate(51.0279417, 3.7167915));

        //Make a new work address for the person
        Address work = new Address("Jozef PlateauStraat", "22", "Gent", "Belgium", "9000",
                new Coordinate(51.1679417, 3.2167915));

        //Make a String array containing the transportation type for the home-work travel
        List<Transportation> transportTypes =  Arrays.asList(Transportation.bike);

        //Make 2 new eventTypes to be included in the eventTypes list which contains all the eventTypes that are relevant for the travel
        EventType eventType1 = new EventType("0", "roadworks", "big",
                Arrays.asList(Transportation.car, Transportation.bike, Transportation.bus));

        //Make a String array containing the eventTypes that are relevant for the travel
        List<EventType> eventTypes = Arrays.asList(eventType1);

        //Make a timeInterval for the new travel
        String[] timeInterval = new String[] {"06:00", "09:00"};

        //Make a boolean array containing the occurences of the travel
        boolean[] recurrence = new boolean[]{true, true, true, true, true, false, false};

        //Make a new travels list containing one travel for the person
        List<Travel> travels = makeTravels("woon-werk", home, work, new ArrayList<>(), transportTypes, eventTypes, "25/02/16",
                timeInterval, false, recurrence);

        User user = new User(String.valueOf(userCounter.getAndIncrement()), "Daisy", "Bell", "Daisy.Bell@hotmail.com",
                "1234", "04851687455", Gender.f, pointsOfInterestList, false, travels);

        return user;
    }

    //Create a new user 2
    public User createUser2(){

        //Reset the travel counter
        travelCounter.set(0);

        //Make a list containing 1 point of interest for the person
        List<PointsOfInterest> pointsOfInterestList = makePointsOfInterest("home", "Kasteelstraat", "89",
                "Sint-Niklaas", "Belgium", "9100", new Coordinate(51.1659224, 4.1317858), 300, true, true);

        //Make a new home address for the person
        Address home = new Address("Kasteelstraat", "89", "Sint-Niklaas", "Belgium", "9100",
                new Coordinate(51.1659224, 4.1317858));

        //Make a new work address for the person
        Address work = new Address("Galglaan", "10", "Gent", "Belgium", "9000", new Coordinate(51.1679417, 3.2167915));
		
		Address school = new Address("De Pintelaan", "379", "Gent", "Belgium", "9000", new Coordinate(51.0224069, 3.7111262));


        //Make a String array containing the transportation type for the home-work travel
        List<Transportation> transportTypes =  Arrays.asList(Transportation.car);

        //Make 2 new eventTypes to be included in the eventTypes list which contains all the eventTypes that are relevant for the travel
        EventType eventType1 = new EventType("1", "road_closed", "completely_closed",
                Arrays.asList(Transportation.car, Transportation.bike, Transportation.bus));

        //Make a String array containing the eventTypes that are relevant for the travel
        List<EventType> eventTypes = Arrays.asList(eventType1);

        //Make a timeInterval for the new travel
        String[] timeInterval = new String[] {"09:00", "11:00"};

        //Make a boolean array containing the occurences of the travel
        boolean[] recurrence = new boolean[]{true, true, true, true, true, false, false};

        //Make a new travels list containing one travel for the person
        List<Travel> travels = makeTravels("woon-werk", home, work, new ArrayList<>(), transportTypes, eventTypes,
                "25/02/16", timeInterval, true, recurrence);

        User user = new User(String.valueOf(userCounter.getAndIncrement()), "Piet", "Modaal", "Piet.modaal@onbekend.org",
                "wachtwoord", "021564894", Gender.m ,pointsOfInterestList, false, travels);
				
		List<Coordinate> waypts = new ArrayList();
		waypts.add(new Coordinate(51.035152, 4.115782));
		
		addTravel(user,"kot-school", home, school, waypts, transportTypes, eventTypes, "28/03/16",
                          timeInterval, true, recurrence);

        return user;
    }

    //Make a new list that contains at least one travel. Also automatically make a route to store in the new made travel
    private List<Travel> makeTravels(String routeName, Address startPoint, Address endPoint, List<Coordinate> waypoints,
                                     List<Transportation> transportationTypes, List<EventType> notifyForEventTypes,
                                     String date, String[] timeInterval, boolean isArrivalTime, boolean[] recurring){

        List<Travel> travels = new ArrayList<>();
        Route route = new Route("0",startPoint,endPoint,waypoints,transportationTypes,notifyForEventTypes);
        Travel travel1_1 = new Travel(String.valueOf(travelCounter.getAndIncrement()), routeName, route, date, timeInterval,
                isArrivalTime, recurring);
        travels.add(travel1_1);
        return travels;

    }

    //Add a travel to the travels of the user and immediately make a route to store in it
    public void addTravel(User user,String name, Address startPoint, Address endPoint, List<Coordinate> waypoints,
                          List<Transportation> transportationTypes, List<EventType> notifyForEventTypes, String date,
                          String[] timeInterval, boolean isArrivalTime, boolean[] recurring){

        Route route = new Route(String.valueOf(user.getTravels().size()),  startPoint, endPoint, waypoints,
                transportationTypes, notifyForEventTypes);

        user.getTravels().add(new Travel(String.valueOf(travelCounter.getAndIncrement()), name, route, date, timeInterval,
                isArrivalTime, recurring));
    }

    //Make a new pointsOfInterest array with at least 1 point of interest
    private List<PointsOfInterest> makePointsOfInterest(String name, String streetName, String houseNumber, String city,
                                                        String country, String postal_code, Coordinate coordinate,
                                                        int radius, boolean is_home, boolean active){

        List<PointsOfInterest> pointsOfInterests = new ArrayList<>();
        Address address = new Address(streetName, houseNumber, city, country, postal_code, coordinate);
        pointsOfInterests.add(new PointsOfInterest(name, radius, is_home, active, address));
        return pointsOfInterests;
    }

    //Add a new point of interest to the user
    public void addPointOfInterest(User user, String name, String streetName, String houseNumber, String city,
                                   String country, String postal_code, Coordinate coordinate, int radius,
                                   boolean is_home, boolean active){

        Address address = new Address(streetName, houseNumber, city, country, postal_code, coordinate);
        user.getPoints_of_interest().add(new PointsOfInterest(name, radius, is_home, active, address));
    }



}
