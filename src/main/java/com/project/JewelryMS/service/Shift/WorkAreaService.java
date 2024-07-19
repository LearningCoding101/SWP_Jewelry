package com.project.JewelryMS.service.Shift;

import com.project.JewelryMS.entity.WorkArea;
import com.project.JewelryMS.model.Shift.GetWorkAreaRequest;
import com.project.JewelryMS.model.Shift.WorkAreaRequest;
import com.project.JewelryMS.repository.WorkAreaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkAreaService {

    @Autowired
    private WorkAreaRepository workAreaRepository;

    // Create a new work area
    public WorkAreaRequest createWorkArea(WorkAreaRequest workAreaRequest) {
        if (!isValidWorkAreaID(workAreaRequest.getWorkAreaCode())) {
            throw new IllegalArgumentException("Invalid workAreaID format. Expected format: <4 Uppercase letters><3 numbers>.");
        }

        WorkArea workArea = new WorkArea();
        workArea.setWorkAreaCode(workAreaRequest.getWorkAreaCode());
        workArea.setRegister(workAreaRequest.getRegister());
        workArea.setDescription(workAreaRequest.getDescription());
        workArea = workAreaRepository.save(workArea);
        return toWorkAreaRequest(workArea);
    }

    // Get all work areas
    public List<GetWorkAreaRequest> getAllWorkAreas() {
        List<WorkArea> workAreas = workAreaRepository.findAll();
        return workAreas.stream()
                .map(this::toGetWorkAreaRequest)
                .collect(Collectors.toList());
    }

    // Get work area by ID
    public WorkAreaRequest getWorkAreaByWorkAreaID(String workAreaCode) {
        WorkArea workArea = workAreaRepository.findByWorkAreaCode(workAreaCode)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaCode + " not found"));
        return toWorkAreaRequest(workArea);
    }

    // Update work area
    public WorkAreaRequest updateWorkArea(String workAreaCode, WorkAreaRequest workAreaRequest) {
        WorkArea workArea = workAreaRepository.findByWorkAreaCode(workAreaCode)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaCode + " not found"));

        workArea.setRegister(workAreaRequest.getRegister());
        workArea.setDescription(workAreaRequest.getDescription());
        workArea = workAreaRepository.save(workArea);
        return toWorkAreaRequest(workArea);
    }

    // Delete work area
    @Transactional
    public void deleteWorkArea(String workAreaCode) {
        WorkArea workArea = workAreaRepository.findByWorkAreaCode(workAreaCode)
                .orElseThrow(() -> new RuntimeException("Work Area ID: " + workAreaCode + " not found"));

        workArea.setStatus("Inactive");  // Update status instead of deleting
        workAreaRepository.save(workArea);
    }

    // Convert WorkArea entity to WorkAreaRequest DTO
    private WorkAreaRequest toWorkAreaRequest(WorkArea workArea) {
        WorkAreaRequest workAreaRequest = new WorkAreaRequest();
        workAreaRequest.setWorkAreaCode(workArea.getWorkAreaCode());
        workAreaRequest.setRegister(workArea.getRegister());
        workAreaRequest.setDescription(workArea.getDescription());
        return workAreaRequest;
    }

    // Convert WorkArea entity to GetWorkAreaRequest DTO
    private GetWorkAreaRequest toGetWorkAreaRequest(WorkArea workArea) {
        return new GetWorkAreaRequest(
                workArea.getId(),
                workArea.getWorkAreaCode(),
                workArea.getRegister(),
                workArea.getDescription(),
                workArea.getStatus()
        );
    }

    // Method to validate the workAreaID format
    private boolean isValidWorkAreaID(String workAreaID) {
        // Check if the workAreaID matches the format: 4 letters followed by 3 numbers
        return workAreaID.matches("[A-Z]{4}\\d{3}");
    }
}
