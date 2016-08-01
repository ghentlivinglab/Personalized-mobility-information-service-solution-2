/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.CRUDModel;
import database_v2.models.ForeignKeyAttribute;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for the travel table from the relational database.
 * <p>
 * All columns:
 * <pre>
 * +-----------------+---------+----------------------+
 * |     column      |  type   |     foreign key      |
 * +-----------------+---------+----------------------+
 * | travelid        | serial  |                      |
 * | accountid       | integer | => account.accountid |
 * | startpoint      | integer | => address.addressid |
 * | endpoint        | integer | => address.addressid |
 * | name            | text    |                      |
 * | begin_time      | time    |                      |
 * | end_time        | time    |                      |
 * | is_arrival_time | boolean |                      |
 * | mon             | boolean |                      |
 * | tue             | boolean |                      |
 * | wed             | boolean |                      |
 * | thu             | boolean |                      |
 * | fri             | boolean |                      |
 * | sat             | boolean |                      |
 * | sun             | boolean |                      |
 * +-----------------+---------+----------------------+
 * </pre>
 *
 */
public class TravelDBModel implements CRUDModel {

    private Integer travelId;
    private Integer accountId;
    private Integer startpoint;
    private Integer endpoint;
    private String name;
    private Time beginTime;
    private Time endTime;
    private Boolean isArrivalTime;
    private Boolean mon;
    private Boolean tue;
    private Boolean wed;
    private Boolean thu;
    private Boolean fri;
    private Boolean sat;
    private Boolean sun;
    private static final String tableName = "travel";

    public TravelDBModel() {
    }

    public TravelDBModel(Integer accountId, Integer startpoint, Integer endpoint, String name, Time beginTime,
            Time endTime, Boolean isArrivalTime, Boolean mon, Boolean tue, Boolean wed, Boolean thu, Boolean fri, Boolean sat, Boolean sun) {
        this.accountId = accountId;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.isArrivalTime = isArrivalTime;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public TravelDBModel(Integer accountId, Integer startpoint, Integer endpoint, String name, Time beginTime, Time endTime) {
        this.accountId = accountId;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        // required attributes
        out.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        out.add(new ForeignKeyAttribute<>("startpoint", "startpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        out.add(new ForeignKeyAttribute<>("endpoint", "endpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        out.add(new Attribute("name", "name", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("beginTime", "begin_time", AttributeType.TIME, tableName, true));
        out.add(new Attribute("endTime", "end_time", AttributeType.TIME, tableName, true));

        // not required attributes
        out.add(new Attribute("isArrivalTime", "is_arrival_time", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("mon", "mon", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("tue", "tue", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("wed", "wed", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("thu", "thu", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("fri", "fri", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("sat", "sat", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("sun", "sun", AttributeType.BOOLEAN, tableName, false));
        return out;
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        List<Attribute> out = new ArrayList<>();
        // required attributes
        out.add(new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class));
        out.add(new ForeignKeyAttribute<>("startpoint", "startpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        out.add(new ForeignKeyAttribute<>("endpoint", "endpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class));
        out.add(new Attribute("name", "name", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("beginTime", "begin_time", AttributeType.TIME, tableName, true));
        out.add(new Attribute("endTime", "end_time", AttributeType.TIME, tableName, true));

        // not required attributes
        if (isArrivalTime != null) {
            out.add(new Attribute("isArrivalTime", "is_arrival_time", AttributeType.BOOLEAN, tableName, false));
        }
        if (mon != null) {
            out.add(new Attribute("mon", "mon", AttributeType.BOOLEAN, tableName, false));
        }
        if (tue != null) {
            out.add(new Attribute("tue", "tue", AttributeType.BOOLEAN, tableName, false));
        }
        if (wed != null) {
            out.add(new Attribute("wed", "wed", AttributeType.BOOLEAN, tableName, false));
        }
        if (thu != null) {
            out.add(new Attribute("thu", "thu", AttributeType.BOOLEAN, tableName, false));
        }
        if (fri != null) {
            out.add(new Attribute("fri", "fri", AttributeType.BOOLEAN, tableName, false));
        }
        if (sat != null) {
            out.add(new Attribute("sat", "sat", AttributeType.BOOLEAN, tableName, false));
        }
        if (sun != null) {
            out.add(new Attribute("sun", "sun", AttributeType.BOOLEAN, tableName, false));
        }
        return out;
    }

    @Override
    public List<Attribute> getAllAttributeList() {
        return allAttributes;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void setId(int id) {
        setTravelId(id);
    }

    @Override
    public int getId() {
        return getTravelId();
    }

    @Override
    public String getIdColumnName() {
        return tableName + "id";
    }
    
    public boolean[] getWeekArray() {
        return new boolean[]{mon, tue, wed, thu, fri, sat, sun};
    }

    public Integer getTravelId() {
        return travelId;
    }

    public static Attribute getTravelIdAttribute() {
        return new Attribute("travelId", "travelid", AttributeType.INTEGER, tableName, true);
    }

    public void setTravelId(Integer travelId) {
        this.travelId = travelId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public static ForeignKeyAttribute<AccountDBModel> getAccountIdAttribute() {
        return new ForeignKeyAttribute<>("accountId", "accountid", AttributeType.INTEGER, tableName, true, AccountDBModel.class);
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getStartpoint() {
        return startpoint;
    }
    
    public static ForeignKeyAttribute<AddressDBModel> getStartpointAttribute() {
        return new ForeignKeyAttribute<>("startpoint", "startpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class);
    }

    public void setStartpoint(Integer startpoint) {
        this.startpoint = startpoint;
    }

    public Integer getEndpoint() {
        return endpoint;
    }
    
    public static ForeignKeyAttribute<AddressDBModel> getEndpointAttribute() {
        return new ForeignKeyAttribute<>("endpoint", "endpoint", AttributeType.INTEGER, tableName, true, AddressDBModel.class);
    }

    public void setEndpoint(Integer endpoint) {
        this.endpoint = endpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Time getBeginTime() {
        return beginTime;
    }

    public static Attribute getBeginTimeAttribute() {
        return new Attribute("beginTime", "begin_time", AttributeType.TIME, tableName, true);
    }

    public void setBeginTime(Time beginTime) {
        this.beginTime = beginTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public static Attribute getEndTimeAttribute() {
        return new Attribute("endTime", "end_time", AttributeType.TIME, tableName, true);
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsArrivalTime() {
        return isArrivalTime;
    }

    public static Attribute getIsArrivalTimeAttribute() {
        return new Attribute("isArrivalTime", "is_arrival_time", AttributeType.BOOLEAN, tableName, false);
    }

    public void setIsArrivalTime(Boolean isArrivalTime) {
        this.isArrivalTime = isArrivalTime;
    }

    public Boolean getMon() {
        return mon;
    }

    public static Attribute getMonAttribute() {
        return new Attribute("mon", "mon", AttributeType.BOOLEAN, tableName, false);
    }

    public void setMon(Boolean mon) {
        this.mon = mon;
    }

    public Boolean getTue() {
        return tue;
    }

    public static Attribute getTueAttribute() {
        return new Attribute("tue", "tue", AttributeType.BOOLEAN, tableName, false);
    }

    public void setTue(Boolean tue) {
        this.tue = tue;
    }

    public Boolean getWed() {
        return wed;
    }

    public static Attribute getWedAttribute() {
        return new Attribute("wed", "wed", AttributeType.BOOLEAN, tableName, false);
    }

    public void setWed(Boolean wed) {
        this.wed = wed;
    }

    public Boolean getThu() {
        return thu;
    }

    public static Attribute getThuAttribute() {
        return new Attribute("thu", "thu", AttributeType.BOOLEAN, tableName, false);
    }

    public void setThu(Boolean thu) {
        this.thu = thu;
    }

    public Boolean getFri() {
        return fri;
    }

    public static Attribute getFriAttribute() {
        return new Attribute("fri", "fri", AttributeType.BOOLEAN, tableName, false);
    }

    public void setFri(Boolean fri) {
        this.fri = fri;
    }

    public Boolean getSat() {
        return sat;
    }

    public static Attribute getSatAttribute() {
        return new Attribute("sat", "sat", AttributeType.BOOLEAN, tableName, false);
    }

    public void setSat(Boolean sat) {
        this.sat = sat;
    }

    public Boolean getSun() {
        return sun;
    }

    public static Attribute getSunAttribute() {
        return new Attribute("sun", "sun", AttributeType.BOOLEAN, tableName, false);
    }

    public void setSun(Boolean sun) {
        this.sun = sun;
    }

    public boolean[] getDaysArray() {
        return new boolean[]{
            mon, tue, wed, thu, fri, sat, sun
        };
    }
}
