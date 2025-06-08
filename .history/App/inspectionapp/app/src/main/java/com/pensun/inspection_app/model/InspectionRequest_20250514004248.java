package com.pensun.inspection_app.model;

import java.util.List;

public class InspectionRequest {
    private long areaId;
    private String status;
    private String remarks;
    private List<String> photos;

    public InspectionRequest(long areaId, String status, String remarks, List<String> photos) {
        this.areaId = areaId;
        this.status = status;
        this.remarks = remarks;
        this.photos = photos;
    }

    // Getters and setters
    public long getAreaId() { return areaId; }
    public void setAreaId(long areaId) { this.areaId = areaId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
} 