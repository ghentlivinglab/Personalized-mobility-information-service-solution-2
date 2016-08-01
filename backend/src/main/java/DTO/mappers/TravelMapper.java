package DTO.mappers;

import DTO.models.TravelDTO;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import models.Travel;
import models.exceptions.InvalidCountryCodeException;
import models.repetition.Repetition;
import models.repetition.RepetitionWeek;

/**
 * Class to convert a travel object from/into a travelDTO (json) object
 */
public class TravelMapper {
    
    /**
     * Method to convert Travel into TravelDTO
     * @param travel Travel object to convert
     * @param id the id of the travel
     * @return TravelDTO object
     */
    public TravelDTO convertToDTO (Travel travel, String id) {
        String [] timeinterval = new String [2];
        timeinterval[0] = travel.getBeginDate().format(DateTimeFormatter.ISO_LOCAL_TIME);
        timeinterval[1] = travel.getEndDate().format(DateTimeFormatter.ISO_LOCAL_TIME);
        
        boolean [] recur = new boolean [7];
        
        if (!travel.getRecurring().isEmpty()) {
            RepetitionWeek repweek = (RepetitionWeek) travel.getRecurring().get(0);
            recur = repweek.getAllWeek();
        }
        
        AddressMapper addressmapper = new AddressMapper();
        
        return new TravelDTO(
                id, 
                travel.getName(), 
                timeinterval, 
                travel.isArrivalTime(), 
                recur,
                addressmapper.convertToDTO(travel.getStartPoint()), 
                addressmapper.convertToDTO(travel.getEndPoint()));
    }
    
    /**
     * Method to convert Travel into TravelDTO
     * @param travel Travel object to convert
     * @param id the id of the travel
     * @return TravelDTO object
     */
    public TravelDTO convertToDTO (Travel travel, int id) {
        return convertToDTO(travel, Integer.toString(id));
    }
    
    /**
     * Method to convert TravelDTO into Travel
     * @param traveldto TravelDTO object to convert
     * @return Travel object
     * @throws InvalidCountryCodeException is the country is not in a valid format (e.g. 'BE' is valid)
     */
    public Travel convertFromDTO (TravelDTO traveldto) throws InvalidCountryCodeException {
        LocalTime begintime = LocalTime.parse(traveldto.getTimeInterval()[0], DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime endtime = LocalTime.parse(traveldto.getTimeInterval()[1], DateTimeFormatter.ISO_LOCAL_TIME);
        
        RepetitionWeek rep = new RepetitionWeek();
        rep.setAllWeek(traveldto.getRecurring()); //TODO ADJUST
        
        ArrayList<Repetition>repweek = new ArrayList<>();
        repweek.add(rep);
        
        AddressMapper addressmapper = new AddressMapper();
        return new Travel(
                traveldto.getName(), 
                begintime, 
                endtime, 
                traveldto.isArrivalTime(), 
                addressmapper.convertFromDTO(traveldto.getStartpoint()), 
                addressmapper.convertFromDTO(traveldto.getEndpoint()), 
                repweek, 
                new HashMap<>(), 
                new ArrayList<>());
    }
}
