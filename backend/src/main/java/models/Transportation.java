package models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum to represent different tranportation options.
 */
public enum Transportation {

    BUS("bus"),
    CAR("car"),
    BIKE("bike"),
    TRAIN("train"),
    STREETCAR("streetcar");

    private final String name;

    private Transportation(String name) {
        this.name = name;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name;
    }

    public static Transportation getTransportation(String s) {
        return Transportation.fromString(s);
    }

    public static Transportation fromString(String text) {
        if (text != null) {
            for (Transportation b : Transportation.values()) {
                if (text.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }
}
