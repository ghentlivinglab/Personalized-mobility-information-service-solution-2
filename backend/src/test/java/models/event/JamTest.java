/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.event;

import java.util.Arrays;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import models.Coordinate;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author hannedesutter
 */
public class JamTest {
    
    public JamTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_1(){
        Jam instance = new Jam("1200",Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
    }
    
    @Test
    public void testConstructor_2(){
        Jam instance = new Jam("1970-01-01T01:00:01.200",Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        assertEquals("1970-01-01T01:00:01.200",instance.getPublicationString());
        assertEquals(Arrays.asList(new Coordinate[]{new Coordinate(40,40),new Coordinate(50,50)}),instance.getLineView());
        assertEquals(10,instance.getSpeed());
        assertEquals(10,instance.getDelay());
    }
    
    /**
     * Test of getUuid method, of class Jam.
     */
    @Test
    public void testGetUuid() {
        System.out.println("getUuid");
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        String expResult = "uuid";
        String result = instance.getUuid();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPublicationTimeMillis method, of class Jam.
     */
    @Test
    public void testGetPublicationTimeMillis() {
        System.out.println("getPublicationTimeMillis");
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        long expResult = 1200;
        long result = instance.getPublicationTimeMillis();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPublicationString method, of class Jam.
     */
    @Test
    public void testGetPublicationString() {
        System.out.println("getPublicationString");
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        String expResult = "1970-01-01T01:00:01.200";
        String result = instance.getPublicationString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLineView method, of class Jam.
     */
    @Test
    public void testGetLineView() {
        System.out.println("getLineView");
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        List<Coordinate> expResult = Arrays.asList(new Coordinate[]{new Coordinate(40,40),new Coordinate(50,50)});
        List<Coordinate> result = instance.getLineView();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSpeed method, of class Jam.
     */
    @Test
    public void testGetSpeed() {
        System.out.println("getSpeed");
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        int expResult = 10;
        int result = instance.getSpeed();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSpeed method, of class Jam.
     */
    @Test
    public void testSetSpeed() {
        System.out.println("setSpeed");
        int speed = 100;
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        instance.setSpeed(speed);
        assertEquals(100,instance.getSpeed());
    }

    /**
     * Test of getDelay method, of class Jam.
     */
    @Test
    public void testGetDelay() {
        System.out.println("getDelay");
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        int expResult = 10;
        int result = instance.getDelay();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDelay method, of class Jam.
     */
    @Test
    public void testSetDelay() {
        System.out.println("setDelay");
        int delay = 100;
        Jam instance = new Jam("uuid",1200,Arrays.asList(new Coordinate[]{new Coordinate(40,40),
            new Coordinate(50,50)}),10,10);
        instance.setDelay(delay);
        assertEquals(delay,instance.getDelay());
    }


}