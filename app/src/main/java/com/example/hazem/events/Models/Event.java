package com.example.hazem.events.Models;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Hazem on 12/25/2016.
 */

public class Event extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    private String eventName;
    private String evenDescription;
    private String eventLocation;
    private String eventDate;
    private String color;

    public Event() {
    }

    public Event(int id, String eventName, String evenDescription, String eventLocation, String eventDate, String color) {
        this.id = id;
        this.eventName = eventName;
        this.evenDescription = evenDescription;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEvenDescription() {
        return evenDescription;
    }

    public void setEvenDescription(String evenDescription) {
        this.evenDescription = evenDescription;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
