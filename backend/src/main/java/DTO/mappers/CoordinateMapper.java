package DTO.mappers;

import DTO.models.CoordinateDTO;
import models.Coordinate;

/**
 * Class to convert a coordinate object from/into a coordinateDTO (json) object
 */
public class CoordinateMapper {
    
    /**
     * Method to convert Coordinate into CoordinateDTO
     * @param coordinate Coordinate (model) object to convert
     * @return CoordinateDTO object 
     */
    public CoordinateDTO convertToDTO (Coordinate coordinate) {
        return new CoordinateDTO(coordinate.getLat(), coordinate.getLon());
    }
    
    /**
     * Method to convert CoordinateDTO into Coordinate
     * @param coordinatedto CoordinateDTO object
     * @return Coordinate (model) object
     */
    public Coordinate convertFromDTO (CoordinateDTO coordinatedto) {
        return new Coordinate(coordinatedto.getLat(), coordinatedto.getLon());
    }
    
}
