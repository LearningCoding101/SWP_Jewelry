package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;


@Repository
public interface StaffShiftRepository extends JpaRepository<Staff_Shift, Long> {

    Optional<Staff_Shift> findByStaffAccountAndShift(StaffAccount staffAccount, Shift shift);

    List<Staff_Shift> findAllByStaffAccountAndShift_StartTimeBetween(
            StaffAccount staffAccount,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Staff_Shift> findAllByStaffAccountAndShift_ShiftTypeLikeAndShift_DateBetween(
            StaffAccount staffAccount,
            String shiftTypePattern,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Staff_Shift> findAllByShift(Shift shift);

    List<Staff_Shift> findAllByShiftIn(List<Shift> shifts);

    List<Staff_Shift> findAllByShiftAndShift_DateBetween(
            Shift shift,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    boolean existsByStaffAccountAndShift(StaffAccount staffAccount, Shift shift);

    boolean existsByShiftAndShift_DateBetween(
            Shift shift,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Staff_Shift> findAllByStaffAccountAndShift_ShiftTypeLike(
            StaffAccount staffAccount,
            String shiftTypePattern
    );

    List<Staff_Shift> findAllByShiftTypeLikeAndDateBetween(
            String shiftTypePattern,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<Staff_Shift> findAllByStaffAccountAndShift_DateBetween(
            StaffAccount staffAccount,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
