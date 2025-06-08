package com.pensun.inspection_app.model;

import java.util.List;

public class AreaListResponse {
    private List<Area> areas;
    private String message;

    // Getters and setters
    public List<Area> getAreas() { return areas; }
    public void setAreas(List<Area> areas) { this.areas = areas; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
} 