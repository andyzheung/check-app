package com.inspection.app.model;

import java.util.List;

public class Area {
    private long id;
    private String name;
    private String description;
    private String qrCode;
    private String location;
    private List<String> photos;
    private String status;

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

public class AreaResponse {
    private Area area;
    private String message;

    // Getters and setters
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

public class AreaListResponse {
    private List<Area> areas;
    private String message;

    // Getters and setters
    public List<Area> getAreas() { return areas; }
    public void setAreas(List<Area> areas) { this.areas = areas; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
} 