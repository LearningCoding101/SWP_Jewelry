package com.project.JewelryMS.model.Shift;

import java.util.Map;
import java.util.List;

public class AssignStaffByShiftTypePatternRequest {

    private Map<Integer, List<List<String>>> staffShiftPatterns;

    public Map<Integer, List<List<String>>> getStaffShiftPatterns() {
        return staffShiftPatterns;
    }

    public void setStaffShiftPatterns(Map<Integer, List<List<String>>> staffShiftPatterns) {
        this.staffShiftPatterns = staffShiftPatterns;
    }
}
