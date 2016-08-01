package models.repetition;

import java.util.Calendar;

/**
 * This class represents whether or not there is a repetition during the week. 
 * If the field of that day is true, there will be a repetition. 
 * This class implements the interface Repetition.
 * @author hannedesutter
 */
public class RepetitionWeek implements Repetition{
    
    private boolean [] days;

    /**
     * This constructor initializes the value of each day to false.
     * After that it puts every day value in an array for easier access. 
     */
    public RepetitionWeek(){
        days=new boolean [7];
        for(int i=0; i<7 ; i++){
            days[i]=false;
        }
        
    }
    
    public RepetitionWeek(boolean [] week){
        if(week.length!=7) {
            throw new IllegalArgumentException("The length of the array should be 7");
        }
        days = new boolean[7];
        System.arraycopy(week, 0, days, 0, 7);
    }
    
    /**
     * This method calculates weather or not the recurring takes place today.
     * @return 
     */
    @Override
    public boolean today() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return days[dayOfWeek-1];
    }

    /**
     * This method returns the value of the recurring on Monday.
     * @return the boolean value of Monday.
     */
    public boolean getMon() {
        return days[0];
    }

    /**
     * This method changes the value of the recurring on Monday.
     * @param mon the new value for the recurring on Monday.
     */
    public void setMon(boolean mon) {
        this.days[0]= mon;
    }

    /**
     * This method returns the value of the recurring on Tuesday.
     * @return the boolean value of Tuesday.
     */
    public boolean getTue() {
        return days[1];
    }

    /**
     * This method changes the value of the recurring on Tuesday.
     * @param tue the new value for the recurring on Tuesday.
     */
    public void setTue(boolean tue) {
        this.days[1]= tue;
    }

    /**
     * This method returns the value of the recurring on Wednesday.
     * @return the boolean value of Wednesday. 
     */
    public boolean getWed() {
        return days[2];
    }

    /**
     * This method changes the value of the recurring on Wednesday.
     * @param wed the new value for the recurring on Wednesday.
     */
    public void setWed(boolean wed) {
        this.days[2] = wed;
    }

    /**
     * This method returns the value of the recurring on Thursday.
     * @return the boolean value of Thursday.
     */
    public boolean getThu() {
        return days[3];
    }

    /**
     * This method changes the value of the recurring on Thursday.
     * @param thu the new value for the recurring on Thursday.
     */
    public void setThu(boolean thu) {
        this.days[3] = thu;
    }

    /**
     * This method returns the value of the recurring on Friday.
     * @return the boolean value of Friday.
     */
    public boolean getFri() {
        return days[4];
    }

    /**
     * This method changes the value of the recurring on Friday.
     * @param fri the new value for the recurring on Friday.
     */
    public void setFri(boolean fri) {
        this.days[4] = fri;
    }

    /**
     * This method returns the value of the recurring on Saturday.
     * @return the boolean value of Saturday. 
     */
    public boolean getSat() {
        return days[5];
    }

    /**
     * This method changes the value of the recurring on Saturday.
     * @param sat the new value for the recurring on Saturday.
     */
    public void setSat(boolean sat) {
        this.days[5] = sat;
    }

    /**
     * This method returns the value of the recurring on Sunday.
     * @return the boolean value of Sunday.
     */
    public boolean getSun() {
        return days[6];
    }

    /**
     * This method changes the value of the recurring on Sunday.
     * @param sun the new value for the recurring on Sunday.
     */
    public void setSun(boolean sun) {
        this.days[6] = sun;
    }

    public void setAllWeek(boolean[] bool){
        if(bool.length!=7) {
            throw new IllegalArgumentException("The length of the array should be 7.");
        }
        System.arraycopy(bool, 0, days, 0, 7);
    }
    
    public boolean [] getAllWeek(){
        return days;
    }
    
}
