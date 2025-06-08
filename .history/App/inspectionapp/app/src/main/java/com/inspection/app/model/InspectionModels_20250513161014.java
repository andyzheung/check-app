package com.inspection.app.model;

import java.util.Date;
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

public class Inspection {
    private long id;
    private long areaId;
    private long userId;
    private String status;
    private String remarks;
    private List<String> photos;
    private Date createdAt;
    private Date updatedAt;
    private User inspector;
    private Area area;

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getAreaId() { return areaId; }
    public void setAreaId(long areaId) { this.areaId = areaId; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public User getInspector() { return inspector; }
    public void setInspector(User inspector) { this.inspector = inspector; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }
}

public class InspectionResponse {
    private Inspection inspection;
    private String message;

    // Getters and setters
    public Inspection getInspection() { return inspection; }
    public void setInspection(Inspection inspection) { this.inspection = inspection; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

public class InspectionListResponse {
    private List<Inspection> inspections;
    private long total;
    private int page;
    private int pageSize;

    // Getters and setters
    public List<Inspection> getInspections() { return inspections; }
    public void setInspections(List<Inspection> inspections) { this.inspections = inspections; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
} 