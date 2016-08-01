package DTO.mappers;

import DTO.models.SourceDTO;
import java.net.MalformedURLException;
import java.net.URL;
import models.event.Source;

/**
 * Class to convert a source object from/into a sourceDTO (json) object
 */
public class SourceMapper {
    
    /**
     * Method to convert Source into SourceDTO
     * @param source Source object to convert
     * @return SourceDTO object
     */
    public SourceDTO convertToDTO(Source source){
        return new SourceDTO(source.getName(),source.getUrl().toString());
    }
    
    /**
     * Method to convert SourceDTO into Source
     * @param sourceDTO SourceDTO object to convert
     * @return Source object
     * @throws MalformedURLException
     */
    public Source convertFromDTO(SourceDTO sourceDTO) throws MalformedURLException{
        return new Source(new URL(sourceDTO.getIconURL()),sourceDTO.getName());
    }
}
