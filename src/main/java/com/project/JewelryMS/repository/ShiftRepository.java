package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    // Find shifts by start time
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s WHERE s.startTime = :startTime")
    List<ShiftRequest> findByStartTime(@Param("startTime") Timestamp startTime);

    // Find shifts by end time
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s WHERE s.endTime = :endTime")
    List<ShiftRequest> findByEndTime(@Param("endTime") Timestamp endTime);


    // Find shifts by shift type
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s WHERE s.shiftType = :shiftType")
    List<ShiftRequest> findByShiftType(@Param("shiftType") String shiftType);

    // Find shifts by status
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s WHERE s.status = :status")
    List<ShiftRequest> findByStatus(@Param("status") Boolean status);

    // Find shifts by register
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s WHERE s.register = :register")
    List<ShiftRequest> findByRegister(@Param("register") int register);

    // Find shifts by work area
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s WHERE s.workArea = :workArea")
    List<ShiftRequest> findByWorkArea(@Param("workArea") String workArea);

    // List all shifts
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s")
    List<ShiftRequest> listAll();

    // Find shifts by the actual ID
    @Query("SELECT new com.project.JewelryMS.model.Shift.ShiftRequest(" +
            "s.shiftID, s.endTime, s.register, s.shiftType, s.startTime, s.status, s.workArea) " +
            "FROM Shift s WHERE s.shiftID = :shiftID")
    Optional<ShiftRequest> findByShiftId(@Param("shiftID") int shiftID);
}
