package com.pensun.checkapp.dto;

import lombok.Data;

/**
 * 任务冲突检查DTO
 */
@Data
public class TaskConflictCheckDTO {
    
    /**
     * 是否存在冲突
     */
    private boolean hasConflict;
    
    /**
     * 冲突的任务信息
     */
    private TaskScheduleDTO conflictTask;
    
    /**
     * 冲突描述信息
     */
    private String conflictMessage;
    
    /**
     * 建议的时间段
     */
    private String suggestedTime;
    
    public static TaskConflictCheckDTO noConflict() {
        TaskConflictCheckDTO dto = new TaskConflictCheckDTO();
        dto.setHasConflict(false);
        dto.setConflictMessage("无冲突");
        return dto;
    }
    
    public static TaskConflictCheckDTO hasConflict(TaskScheduleDTO conflictTask, String message) {
        TaskConflictCheckDTO dto = new TaskConflictCheckDTO();
        dto.setHasConflict(true);
        dto.setConflictTask(conflictTask);
        dto.setConflictMessage(message);
        return dto;
    }
} 