package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController

@RequestMapping("/user")
public class UserController {

    private List<User> users = new ArrayList<>();
    private UserCreator userCreator = new UserCreator();

    // Contructor creating 2 users with the createUser class
    public UserController() {
        users.add(userCreator.createUser1());
        users.add(userCreator.createUser2());
    }

    // Return all users
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(this.users, HttpStatus.OK);
    }

    // Post method to make a new user
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> addUser(@RequestBody User user) {

        if(user.getId() != null) {
            // If the user already exists, return code 409.
            // In this stub, the existence of a user is checked by the id and not the email for simplicity
            if (!user.getId().equals("") && Integer.parseInt(user.getId()) <= users.size())
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //Check if the mail entered by the user is already in use
        for(int i = 0; i < users.size(); i++){
            if(user.getEmail().equals(users.get(i).getEmail()))
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //If the user does not already exist, make the new user and return code 200
        user.setId(String.valueOf(users.size()));
        user.setTravels(new ArrayList<>());
        users.add(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Return the user with id equals to the given id
    @RequestMapping(value = {"/{user_id}"}, method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("user_id") String id) {

        //Check if the id does not exceed the size of the list containing all users
        if(Integer.parseInt(id) < users.size())
        	return new ResponseEntity<>(users.get(Integer.parseInt(id)),HttpStatus.OK);
    	else
            //If the index is too big, return a not found error code
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Put method to edit a user
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> editUser(@PathVariable("id") String id, @RequestBody User user){

        //Check if user exists by checking the id
        if(Integer.parseInt(id) >= users.size())
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

        //Check if the mail entered by the user is already in use
        for(int i = 0; i < users.size(); i++){
            if(user.getEmail().equals(users.get(i).getEmail()) && !user.getId().equals(users.get(i).getId()))
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //If the mail is not in use, replace the user in the array
        users.set(Integer.parseInt(id),user);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    // Delete a user with the given id
    @RequestMapping(value = "/{user_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") String userId) {
        // Check if user does exist
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Delete user
        users.remove(Integer.parseInt(userId));
        for(int i=0; i < users.size(); i++){
            users.get(i).setId(String.valueOf(i));
        }
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    // Return all routes of a particular user
    @RequestMapping(value = {"/{user_id}/route"}, method = RequestMethod.GET)
    public ResponseEntity<List<Route>> getRoutes(@PathVariable String id) {

        //Check if the user exists, if not return 404
        if(Integer.parseInt(id) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        //Otherwise make a list which will contain all routes of the given user
        List<Route> routeList = new ArrayList<>();

        //Loop over all the travels of a user and add the routes of each travel to the routeList list
        for (int i = 0; i < users.get(Integer.parseInt(id)).getTravels().size(); i++ ){
            routeList.add(users.get(Integer.parseInt(id)).getTravels().get(i).getRoute());
        }

        //return the routes of the given user
        return new ResponseEntity<>(routeList, HttpStatus.OK);
    }

    // Post method to make a new route for a user with given id
    @RequestMapping(value = {"/{id}/route"}, method = RequestMethod.POST)
    public ResponseEntity<Route> addRoute( @PathVariable("id") String id, @RequestBody Route route){

        //If the id is not in the users array, it does not exist
        if(Integer.parseInt(id) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        //TODO: nog in te vullen als de rest api is aangepast/ uitgeklaard

        return new ResponseEntity<>(route,HttpStatus.OK);
    }

    // Return the specific route for a specific user
    @RequestMapping(value = {"/{user_id}/route/{route_id}"}, method = RequestMethod.GET)
    public ResponseEntity<Route> getRoute(@PathVariable("user_id") String id, @PathVariable("route_id") String id2) {

        // If the user does not exist, return 404
        if(Integer.parseInt(id) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // If the route does not exist for the given user (= index of the route is bigger than the amount of travels of a user), return 404
        if(Integer.parseInt(id2) >= users.get(Integer.parseInt(id)).getTravels().size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(users.get(Integer.valueOf(id)).getTravels().get(Integer.valueOf(id2)).getRoute(),HttpStatus.OK);
    }

    // Update specific route of a user
    @RequestMapping(value = {"/{user_id}/route/{route_id}"}, method = RequestMethod.PUT)
    public ResponseEntity<Route> editRoute(@PathVariable("user_id") String userId,
                                             @PathVariable("route_jd") String routeID,
                                             @RequestBody Route route) {

        // If the user does not exist, return 404
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Check if route exists
        if(Integer.parseInt(routeID) >= users.get(Integer.parseInt(userId)).getTravels().size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // TO DO: CHECK IF ROUTE ALREADY EXISTS

        // Modify route
        users.get(Integer.parseInt(userId)).getTravels().get(Integer.parseInt(routeID)).setRoute(route);
        return new ResponseEntity<>(users.get(Integer.parseInt(userId)).getTravels().
                get(Integer.parseInt(routeID)).getRoute(), HttpStatus.OK);
    }

    // Delete route from user
    @RequestMapping(value = {"/{user_id}/route/{route_id}"}, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRoute(@PathVariable("user_id") String userId,
                                              @PathVariable("route_jd") String routeID) {

        // If the user does not exist, return 404
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Check if route exists
        if(Integer.parseInt(routeID) >= users.get(Integer.parseInt(userId)).getTravels().size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Delete travel that corresponds to this route
        users.get(Integer.parseInt(userId)).getTravels().remove(Integer.parseInt(routeID));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all travels of a user
    @RequestMapping(value = {"/{user_id}/travel"}, method = RequestMethod.GET)
    public ResponseEntity<List<Travel>> getTravels(@PathVariable("user_id") String userId) {
        // If the user does not exist, return 404
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(users.get(Integer.parseInt(userId)).getTravels(), HttpStatus.OK);
    }

    // Add travel to a user
    @RequestMapping(value = {"/{user_id}/travel"}, method = RequestMethod.POST)
    public ResponseEntity<Travel> addTravel(@PathVariable("user_id") String userId,
                                            @RequestBody Travel travel) {

        // If the user does not exist, return 404
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        travel.getRoute().setId(String.valueOf(users.get(Integer.parseInt(userId)).getTravels().size()));
        users.get(Integer.parseInt(userId)).addTravel(travel);
        return new ResponseEntity<>(users.get(Integer.parseInt(userId)).getTravels().
                get(Integer.parseInt(travel.getId())), HttpStatus.CREATED);
    }

    // Get specific travel of a user
    @RequestMapping(value = "/{user_id}/travel/{travel_id}", method = RequestMethod.GET)
    public ResponseEntity<Travel> getTravel(@PathVariable("user_id") String userId,
                                            @PathVariable("travel_id") String travelId) {

        // If the user does not exist, return 404
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Check if travel exists
        if(Integer.parseInt(travelId) >= users.get(Integer.parseInt(userId)).getTravels().size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(users.get(Integer.parseInt(userId)).getTravels().get(Integer.parseInt(travelId)),
                HttpStatus.OK);
    }

    // Edit a specific travel of a user
    @RequestMapping(value = "/{user_id}/travel/{travel_id}", method = RequestMethod.PUT)
    public ResponseEntity<Travel> editTravel(@PathVariable("user_id") String userId,
                                             @PathVariable("travel_id") String travelId,
                                             @RequestBody Travel travel) {

        // If the user does not exist, return 404
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Check if travel exists
        if(Integer.parseInt(travelId) >= users.get(Integer.parseInt(userId)).getTravels().size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        users.get(Integer.parseInt(userId)).getTravels().set(Integer.parseInt(travelId), travel);
        return new ResponseEntity<>(users.get(Integer.parseInt(userId)).getTravels().
                get(Integer.parseInt(travelId)), HttpStatus.OK);
    }

    // Delete a specific travel of a user
    @RequestMapping(value = "/{user_id}/travel/{travel_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTravel(@PathVariable("user_id") String userId,
                                               @PathVariable("travel_id") String travelId) {

        // If the user does not exist, return 404
        if(Integer.parseInt(userId) >= users.size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Check if travel exists
        if(Integer.parseInt(travelId) >= users.get(Integer.parseInt(userId)).getTravels().size())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        users.get(Integer.parseInt(userId)).getTravels().remove(Integer.parseInt(travelId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}