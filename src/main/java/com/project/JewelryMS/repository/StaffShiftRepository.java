package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Staff_Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffShiftRepository extends JpaRepository<Staff_Shift, Long> {
    // Add custom query methods if needed
}
