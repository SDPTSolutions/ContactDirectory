package com.patrick.contactdirectory;

public class Contact {

    private String name;
    private String location;
    private String number;
    private String schedule;
    private String key;
    private String mapCoordinates;

    Contact(){

    }

    Contact(String name, String location, String number, String schedule){
        this.name = name;
        this.location = location;
        this.number = number;
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getNumber() {
        return number;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getKey(){return key;}

    public String getMapCoordinates() { return mapCoordinates; }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setKey(String key) { this.key = key; }

    public void setMapCoordinates(String mapCoordinates) { this.mapCoordinates = mapCoordinates; }
}
