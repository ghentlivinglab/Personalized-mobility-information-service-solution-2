/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class JamDTOTest {
    
    public JamDTOTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getLine method, of class JamDTO.
     */
    @Test
    public void testGetLine() {
        System.out.println("getLine");
        JamDTO instance = new JamDTO();
        CoordinateDTO[] expResult = new CoordinateDTO[0];
        CoordinateDTO[] result = instance.getLine();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getPublicationTime method, of class JamDTO.
     */
    @Test
    public void testGetPublicationTime() {
        System.out.println("getPublicationTime");
        JamDTO instance = new JamDTO();
        String expResult = "";
        String result = instance.getPublicationTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSpeed method, of class JamDTO.
     */
    @Test
    public void testGetSpeed() {
        System.out.println("getSpeed");
        JamDTO instance = new JamDTO();
        int expResult = 0;
        int result = instance.getSpeed();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDelay method, of class JamDTO.
     */
    @Test
    public void testGetDelay() {
        System.out.println("getDelay");
        JamDTO instance = new JamDTO();
        int expResult = 0;
        int result = instance.getDelay();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLine method, of class JamDTO.
     */
    @Test
    public void testSetLine() {
        System.out.println("setLine");
        CoordinateDTO[] line = new CoordinateDTO[]{new CoordinateDTO(50,50)};
        JamDTO instance = new JamDTO();
        instance.setLine(line);
        assertArrayEquals(line,instance.getLine());
    }

    /**
     * Test of setPublicationTime method, of class JamDTO.
     */
    @Test
    public void testSetPublicationTime() {
        System.out.println("setPublicationTime");
        String publicationTime = "00:00:00";
        JamDTO instance = new JamDTO();
        instance.setPublicationTime(publicationTime);
        assertEquals(publicationTime,instance.getPublicationTime());
    }

    /**
     * Test of setSpeed method, of class JamDTO.
     */
    @Test
    public void testSetSpeed() {
        System.out.println("setSpeed");
        int speed = 10;
        JamDTO instance = new JamDTO();
        instance.setSpeed(speed);
        assertEquals(speed,instance.getSpeed());
    }

    /**
     * Test of setDelay method, of class JamDTO.
     */
    @Test
    public void testSetDelay() {
        System.out.println("setDelay");
        int delay = 10;
        JamDTO instance = new JamDTO();
        instance.setDelay(delay);
        assertEquals(delay,instance.getDelay());
    }
    
}
