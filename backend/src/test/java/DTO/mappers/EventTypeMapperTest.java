/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.EventTypeDTO;
import java.util.ArrayList;
import models.event.EventType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class EventTypeMapperTest {
    
    public EventTypeMapperTest() {
    }

    /**
     * Test of convertToDTO method, of class EventTypeMapper.
     */
    @Test
    public void testConvertToDTO() {
        System.out.println("convertToDTO");
        EventType eventtype = new EventType("Jam",new ArrayList<>());
        EventTypeMapper instance = new EventTypeMapper();
        EventTypeDTO expResult = new EventTypeDTO("Jam");
        EventTypeDTO result = instance.convertToDTO(eventtype);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertFromDTO method, of class EventTypeMapper.
     */
    @Test
    public void testConvertFromDTO() {
        System.out.println("convertFromDTO");
        EventTypeDTO eventtypedto = new EventTypeDTO("Jam");
        EventTypeMapper instance = new EventTypeMapper();
        EventType expResult = new EventType("Jam",new ArrayList<>());
        EventType result = instance.convertFromDTO(eventtypedto);
        assertEquals(expResult, result);
    }
    
}
