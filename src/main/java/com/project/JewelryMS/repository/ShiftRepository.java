package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
}
