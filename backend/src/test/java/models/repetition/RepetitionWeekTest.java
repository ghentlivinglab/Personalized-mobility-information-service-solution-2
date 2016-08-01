/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.repetition;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hannedesutter
 */
public class RepetitionWeekTest {
    
    public RepetitionWeekTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test of today method, of class RepetitionWeek.
     */
    @Test
    public void testTodayFalse() {
        System.out.println("today");
        RepetitionWeek instance = new RepetitionWeek();
        boolean expResult = false;
        boolean result = instance.today();
        assertEquals("The result should be false.",expResult, result);
    }

    /**
     * Test of today method, of class RepetitionWeek.
     */
    @Test
    public void testTodayTrue() {
        System.out.println("today");
        RepetitionWeek instance = new RepetitionWeek();
        boolean [] week = new boolean [7];
        for(int i = 0; i< week.length; i++){
            week[i]=true;
        }
        instance.setAllWeek(week);
        boolean expResult = true;
        boolean result = instance.today();
        assertEquals("The result should be true",expResult, result);
    }
    
    /**
     * Test of getMon method, of class RepetitionWeek.
     */
    @Test
    public void testGetMon() {
        System.out.println("getMon");
        RepetitionWeek instance = new RepetitionWeek();
        Boolean expResult = false;
        Boolean result = instance.getMon();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMon method, of class RepetitionWeek.
     */
    @Test
    public void testSetMon() {
        System.out.println("setMon");
        Boolean mon = true;
        RepetitionWeek instance = new RepetitionWeek();
        instance.setMon(mon);
        assertTrue("The value should be true",instance.getMon());
    }

    /**
     * Test of getTue method, of class RepetitionWeek.
     */
    @Test
    public void testGetTue() {
        System.out.println("getTue");
        RepetitionWeek instance = new RepetitionWeek();
        Boolean expResult = false;
        Boolean result = instance.getTue();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTue method, of class RepetitionWeek.
     */
    @Test
    public void testSetTue() {
        System.out.println("setTue");
        Boolean tue = true;
        RepetitionWeek instance = new RepetitionWeek();
        instance.setTue(tue);
        assertTrue("The value should be true",instance.getTue());
    }

    /**
     * Test of getWed method, of class RepetitionWeek.
     */
    @Test
    public void testGetWed() {
        System.out.println("getWed");
        RepetitionWeek instance = new RepetitionWeek();
        Boolean expResult = false;
        Boolean result = instance.getWed();
        assertEquals(expResult, result);
    }

    /**
     * Test of setWed method, of class RepetitionWeek.
     */
    @Test
    public void testSetWed() {
        System.out.println("setWed");
        Boolean wed = true;
        RepetitionWeek instance = new RepetitionWeek();
        instance.setWed(wed);
        assertTrue("The value should be true",instance.getWed());
    }

    /**
     * Test of getThu method, of class RepetitionWeek.
     */
    @Test
    public void testGetThu() {
        System.out.println("getThu");
        RepetitionWeek instance = new RepetitionWeek();
        Boolean expResult = false;
        Boolean result = instance.getThu();
        assertEquals(expResult, result);
    }

    /**
     * Test of setThu method, of class RepetitionWeek.
     */
    @Test
    public void testSetThu() {
        System.out.println("setThu");
        Boolean thu = true;
        RepetitionWeek instance = new RepetitionWeek();
        instance.setThu(thu);
        assertTrue("The value should be true",instance.getThu());
    }

    /**
     * Test of getFri method, of class RepetitionWeek.
     */
    @Test
    public void testGetFri() {
        System.out.println("getFri");
        RepetitionWeek instance = new RepetitionWeek();
        Boolean expResult = false;
        Boolean result = instance.getFri();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFri method, of class RepetitionWeek.
     */
    @Test
    public void testSetFri() {
        System.out.println("setFri");
        Boolean fri = true;
        RepetitionWeek instance = new RepetitionWeek();
        instance.setFri(fri);
        assertTrue("The value should be true",instance.getFri());
    }

    /**
     * Test of getSat method, of class RepetitionWeek.
     */
    @Test
    public void testGetSat() {
        System.out.println("getSat");
        RepetitionWeek instance = new RepetitionWeek();
        Boolean expResult = false;
        Boolean result = instance.getSat();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSat method, of class RepetitionWeek.
     */
    @Test
    public void testSetSat() {
        System.out.println("setSat");
        Boolean sat = true;
        RepetitionWeek instance = new RepetitionWeek();
        instance.setSat(sat);
        assertTrue("The value should be true",instance.getSat());
    }

    /**
     * Test of getSun method, of class RepetitionWeek.
     */
    @Test
    public void testGetSun() {
        System.out.println("getSun");
        RepetitionWeek instance = new RepetitionWeek();
        Boolean expResult = false;
        Boolean result = instance.getSun();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSun method, of class RepetitionWeek.
     */
    @Test
    public void testSetSun() {
        System.out.println("setSun");
        Boolean sun = true;
        RepetitionWeek instance = new RepetitionWeek();
        instance.setSun(sun);
        assertTrue("The value should be true",instance.getSun());
    }



    /**
     * Test of setAllWeek method, of class RepetitionWeek.
     */
    @Test
    public void testSetAllWeek() {
        System.out.println("setAllWeek");
        boolean[] bool = new boolean[7];
        for(int i =0 ; i<bool.length; i ++){
            bool[i]=true;
        }
        RepetitionWeek instance = new RepetitionWeek();
        instance.setAllWeek(bool);
        
        for(int i=0; i<instance.getAllWeek().length ; i ++){
            assertEquals(instance.getAllWeek()[i],bool[i]);
        }
    }

    /**
     * Test of setAllWeek method, of class RepetitionWeek.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetAllWeek_Exception() {
        System.out.println("setAllWeek");
        boolean[] bool = new boolean[2];
        RepetitionWeek instance = new RepetitionWeek();
        instance.setAllWeek(bool);
    }
    
    /**
     * Test of getAllWeek method, of class RepetitionWeek.
     */
    @Test
    public void testGetAllWeek() {
        System.out.println("getAllWeek");
        boolean[] bool = new boolean[7];
        for(int i =0 ; i<bool.length; i ++){
            bool[i]=true;
        }
        RepetitionWeek instance = new RepetitionWeek();
        instance.setAllWeek(bool);
        
        for(int i=0; i<instance.getAllWeek().length ; i ++){
            assertEquals(instance.getAllWeek()[i],bool[i]);
        }
    }
    
    
    
}