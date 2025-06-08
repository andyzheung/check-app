package com.pensun.inspection_app.model;

import java.util.List;

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