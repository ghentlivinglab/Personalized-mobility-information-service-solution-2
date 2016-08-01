/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.mappers;

import DTO.models.SourceDTO;
import java.net.URL;
import models.event.Source;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class SourceMapperTest {
    
    public SourceMapperTest() {
    }

    /**
     * Test of convertToDTO method, of class SourceMapper.
     */
    @Test
    public void testConvertToDTO() throws Exception{
        System.out.println("convertToDTO");
        Source source = new Source(new URL("http://www.google.com"),"google");
        SourceMapper instance = new SourceMapper();
        SourceDTO expResult = new SourceDTO("google", "http://www.google.com");
        SourceDTO result = instance.convertToDTO(source);
        assertEquals(expResult, result);
    }

    /**
     * Test of convertFromDTO method, of class SourceMapper.
     */
    @Test
    public void testConvertFromDTO() throws Exception {
        System.out.println("convertFromDTO");
        SourceDTO sourceDTO = new SourceDTO("google", "http://www.google.com");
        SourceMapper instance = new SourceMapper();
        Source expResult = new Source(new URL("http://www.google.com"),"google");
        Source result = instance.convertFromDTO(sourceDTO);
        assertEquals(expResult.getName(), result.getName());
        assertEquals(expResult.getUrl(), result.getUrl());
    }
    
}
