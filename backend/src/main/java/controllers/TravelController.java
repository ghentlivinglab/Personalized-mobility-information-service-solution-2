package controllers;

import DTO.mappers.TravelMapper;
import DTO.models.TravelDTO;
import database_v2.controlLayer.Database;
import database_v2.exceptions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import models.Travel;
import models.exceptions.InvalidCountryCodeException;
import models.users.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles incoming requests related to travels.
 */
@CrossOrigin
@RestController
@RequestMapping("/user/{user_id}/travel")
public class TravelController {

    private final Database database;
    private final TravelMapper travelmapper;

    /**
     *
     * @param database Interface to communicate with the database.
     */
    public TravelController(Database database) {
        this.database = database;
        travelmapper = new TravelMapper();
    }

    /**
     * Get all travels from a user.
     *
     * @param userIdString The id of the user of which we want to retrieve the
     * travels.
     * @param accessToken
     * @return A list of all the travels of the user with the corresponding id.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TravelDTO>> getTravels(@PathVariable("user_id") String userIdString,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            // first we get the user itself
            User user = database.getUser(userId);
            // thew we get all its travel application models and these we convert
            // to a list of DTO's
            ArrayList<TravelDTO> travels = new ArrayList<>();
            for (Entry<Integer, Travel> travel : user.getTravels().entrySet()) {
                travels.add(
                        travelmapper.convertToDTO(travel.getValue(), travel.getKey())
                );
            }
            return new ResponseEntity<>(travels, HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Add a new travel to a user.
     *
     * @param userIdString The id of the user that we want to add a travel to.
     * @param traveldto The travel-object we want to add to this user.
     * @param accessToken
     * @return The created travel.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TravelDTO> addTravel(
            @PathVariable("user_id") String userIdString,
            @RequestBody TravelDTO traveldto,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            // first we get ther user itself
            User user = database.getUser(userId);
            // then we convert the DTO to an application model
            Travel travel = travelmapper.convertFromDTO(traveldto);
            // this model we make persistent
            int travelId = database.createTravel(userId, travel);
            // and then we add this travel to the model
            user.addTravel(travelId, travel);
            return new ResponseEntity<>(travelmapper.convertToDTO(travel, travelId), HttpStatus.CREATED);
        } catch (NumberFormatException | DataAccessException | InvalidCountryCodeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (RecordNotFoundException | ForeignKeyNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Get specific travel of a user.
     *
     * @param userIdString The id of the user we want to get a travel from.
     * @param travelIdString The id of the travel we want to get.
     * @param accessToken
     * @return The travel with the corresponding id of a certain user.
     */
    @RequestMapping(value = "/{travel_id}", method = RequestMethod.GET)
    public ResponseEntity<TravelDTO> getTravel(@PathVariable("user_id") String userIdString,
                                            @PathVariable("travel_id") String travelIdString,
                                            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);

            int travelId = Integer.parseInt(travelIdString);
            // first we fetch the user.
            User user = database.getUser(userId);
            // and from this user we get the travel
            Map<Integer, Travel> travels = user.getTravels();
            if (!travels.containsKey(travelId)) {
                // this user doens't have a travel with this id.
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Travel travel = travels.get(travelId);
            return new ResponseEntity<>(travelmapper.convertToDTO(travel, travelId), HttpStatus.OK);
        } catch (NumberFormatException | DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Edit a specific travel of a user.
     *
     * @param userIdString The id of the user we want to adjust a travel from.
     * @param travelIdString The id of the travel we want to adjust.
     * @param traveldto The adjusted travel-object.
     * @param accessToken
     * @return The adjusted travel.
     */
    @RequestMapping(value = "/{travel_id}", method = RequestMethod.PUT)
    public ResponseEntity<TravelDTO> editTravel(
            @PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @RequestBody TravelDTO traveldto,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);

            // first we get the user
            User user = database.getUser(userId);

            // we check if the user even has this travel.
            if (!user.getTravels().containsKey(travelId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Travel oldTravel = user.getTravels().get(travelId);

            // then we convert the DTO to an application model
            Travel newTravel = travelmapper.convertFromDTO(traveldto);
            // and make this updated travel persistent.
            database.updateTravel(userId, travelId, newTravel);

            // and update the travel
            oldTravel.updateData(newTravel);

            // return the new DTO
            return new ResponseEntity<>(travelmapper.convertToDTO(oldTravel, travelId), HttpStatus.OK);
        } catch (InvalidCountryCodeException | NumberFormatException | DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException | ForeignKeyNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Delete a specific travel of a user.
     *
     * @param userIdString The id of the user we want to remove a travel from.
     * @param travelIdString The id of the travel we want to remove.
     * @param accessToken
     * @return An appropriate status message.
     */
    @RequestMapping(value = "/{travel_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTravel(@PathVariable("user_id") String userIdString,
            @PathVariable("travel_id") String travelIdString,
            @RequestHeader("Authorization") String accessToken) {

        try {
            int userId = Integer.parseInt(userIdString);
            int travelId = Integer.parseInt(travelIdString);

            User user = database.getUser(userId);
            database.deleteTravel(travelId);
            user.removeTravel(travelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException | DataAccessException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RecordNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
