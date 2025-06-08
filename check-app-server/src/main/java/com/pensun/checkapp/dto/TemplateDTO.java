package com.pensun.checkapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class TemplateDTO {
    private List<InspectionItemDTO> environmentItems;
    private List<InspectionItemDTO> securityItems;

    @Data
    public static class InspectionItemDTO {
        private Long id;
        private String name;
        private String type;
        private Integer sort;
    }
} 