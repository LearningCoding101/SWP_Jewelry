package com.project.JewelryMS.IntegrationTest;

import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import com.project.JewelryMS.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ShiftServiceIntegrationTest {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private StaffShiftRepository staffShiftRepository;

    @BeforeEach
    public void setup() {
        staffShiftRepository.deleteAll();
        shiftRepository.deleteAll();
    }

    @Test
    public void testCreateShift() {
        CreateShiftRequest createShiftRequest = new CreateShiftRequest(
                "2023-12-01 08",  1, "morning","2023-12-01 16", "active", "cashier"
        );

        ShiftRequest shiftRequest = shiftService.createShift(createShiftRequest);

        Optional<Shift> createdShiftOpt = shiftRepository.findById((long) shiftRequest.getShiftID());
        assertThat(createdShiftOpt).isPresent();
        Shift createdShift = createdShiftOpt.get();
        assertThat(createdShift.getStartTime()).isEqualTo(LocalDateTime.parse("2023-12-01T08:00"));
        assertThat(createdShift.getEndTime()).isEqualTo(LocalDateTime.parse("2023-12-01T16:00"));
        assertThat(createdShift.getRegister()).isEqualTo(1);
        assertThat(createdShift.getShiftType()).isEqualTo("morning");
        assertThat(createdShift.getStatus()).isEqualTo("active");
        assertThat(createdShift.getWorkArea()).isEqualTo("cashier");
    }

    @Test
    public void testGetShiftsByStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2023, 12, 1, 8, 0);
        Shift shift = new Shift();
        shift.setStartTime(startTime);
        shift.setEndTime(startTime.plusHours(8));
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");
        shiftRepository.save(shift);

        List<ShiftRequest> shifts = shiftService.getShiftsByStartTime("2023-12-01 08");
        assertThat(shifts).hasSize(1);
        assertThat(shifts.get(0).getShiftID()).isEqualTo(shift.getShiftID());
    }

    @Test
    public void testGetShiftsByEndTime() {
        LocalDateTime endTime = LocalDateTime.of(2023, 12, 1, 16, 0);
        Shift shift = new Shift();
        shift.setStartTime(endTime.minusHours(8));
        shift.setEndTime(endTime);
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");
        shiftRepository.save(shift);

        List<ShiftRequest> shifts = shiftService.getShiftsByEndTime("2023-12-01 16");
        assertThat(shifts).hasSize(1);
        assertThat(shifts.get(0).getShiftID()).isEqualTo(shift.getShiftID());
    }

    @Test
    public void testGetShiftsByShiftType() {
        Shift shift = new Shift();
        shift.setStartTime(LocalDateTime.of(2023, 12, 1, 8, 0));
        shift.setEndTime(LocalDateTime.of(2023, 12, 1, 16, 0));
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");
        shiftRepository.save(shift);

        List<ShiftRequest> shifts = shiftService.getShiftsByShiftType("morning");
        assertThat(shifts).hasSize(1);
        assertThat(shifts.get(0).getShiftID()).isEqualTo(shift.getShiftID());
    }

    @Test
    public void testGetShiftsByStatus() {
        Shift shift = new Shift();
        shift.setStartTime(LocalDateTime.of(2023, 12, 1, 8, 0));
        shift.setEndTime(LocalDateTime.of(2023, 12, 1, 16, 0));
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");
        shiftRepository.save(shift);

        List<ShiftRequest> shifts = shiftService.getShiftsByStatus("active");
        assertThat(shifts).hasSize(1);
        assertThat(shifts.get(0).getShiftID()).isEqualTo(shift.getShiftID());
    }

    @Test
    public void testGetShiftsByRegister() {
        Shift shift = new Shift();
        shift.setStartTime(LocalDateTime.of(2023, 12, 1, 8, 0));
        shift.setEndTime(LocalDateTime.of(2023, 12, 1, 16, 0));
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");
        shiftRepository.save(shift);

        List<ShiftRequest> shifts = shiftService.getShiftsByRegister(1);
        assertThat(shifts).hasSize(1);
        assertThat(shifts.get(0).getShiftID()).isEqualTo(shift.getShiftID());
    }

    @Test
    public void testGetShiftsByWorkArea() {
        Shift shift = new Shift();
        shift.setStartTime(LocalDateTime.of(2023, 12, 1, 8, 0));
        shift.setEndTime(LocalDateTime.of(2023, 12, 1, 16, 0));
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");
        shiftRepository.save(shift);

        List<ShiftRequest> shifts = shiftService.getShiftsByWorkArea("cashier");
        assertThat(shifts).hasSize(1);
        assertThat(shifts.get(0).getShiftID()).isEqualTo(shift.getShiftID());
    }

    @Test
    public void testReadAllShifts() {
        Shift shift1 = new Shift();
        shift1.setStartTime(LocalDateTime.of(2023, 12, 1, 8, 0));
        shift1.setEndTime(LocalDateTime.of(2023, 12, 1, 16, 0));
        shift1.setRegister(1);
        shift1.setShiftType("morning");
        shift1.setStatus("active");
        shift1.setWorkArea("cashier");
        shiftRepository.save(shift1);

        Shift shift2 = new Shift();
        shift2.setStartTime(LocalDateTime.of(2023, 12, 2, 8, 0));
        shift2.setEndTime(LocalDateTime.of(2023, 12, 2, 16, 0));
        shift2.setRegister(2);
        shift2.setShiftType("afternoon");
        shift2.setStatus("inactive");
        shift2.setWorkArea("area2");
        shiftRepository.save(shift2);

        List<ShiftRequest> shifts = shiftService.readAllShifts();
        assertThat(shifts).hasSize(2);
        assertThat(shifts.get(0).getShiftID()).isEqualTo(shift1.getShiftID());
        assertThat(shifts.get(1).getShiftID()).isEqualTo(shift2.getShiftID());
    }

    @Test
    public void testUpdateShiftDetails() {
        Shift shift = new Shift();
        shift.setStartTime(LocalDateTime.of(2023, 12, 1, 8, 0));
        shift.setEndTime(LocalDateTime.of(2023, 12, 1, 16, 0));
        shift.setRegister(1);
        shift.setShiftType("morning");
        shift.setStatus("active");
        shift.setWorkArea("cashier");
        shiftRepository.save(shift);

        ShiftRequest shiftRequest = new ShiftRequest();
        shiftRequest.setShiftID(Math.toIntExact(shift.getShiftID()));
        shiftRequest.setStartTime("2023-12-01 09");
        shiftRequest.setEndTime("2023-12-01 17");
        shiftRequest.setRegister(2);
        shiftRequest.setShiftType("afternoon");
        shiftRequest.setStatus("inactive");
        shiftRequest.setWorkArea("area2");

        Shift updatedShift = shiftService.updateShiftDetails(shiftRequest);
        Optional<Shift> updatedShiftOpt = shiftRepository.findById((long) updatedShift.getShiftID());
        assertThat(updatedShiftOpt).isPresent();
        Shift updatedShiftEntity = updatedShiftOpt.get();
        assertThat(updatedShiftEntity.getStartTime()).isEqualTo(LocalDateTime.parse("2023-12-01T09:00"));
        assertThat(updatedShiftEntity.getEndTime()).isEqualTo(LocalDateTime.parse("2023-12-01T17:00"));
        assertThat(updatedShiftEntity.getRegister()).isEqualTo(2);
        assertThat(updatedShiftEntity.getShiftType()).isEqualTo("afternoon");
        assertThat(updatedShiftEntity.getStatus()).isEqualTo("inactive");
        assertThat(updatedShiftEntity.getWorkArea()).isEqualTo("area2");
    }

    @Test
    public void testDeleteShiftsWithCriteria() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoMonthsAgo = now.minusMonths(3);

        Shift shiftWithoutStaff = new Shift();
        shiftWithoutStaff.setStartTime(now.minusDays(1));
        shiftWithoutStaff.setEndTime(now.minusDays(1).plusHours(8));
        shiftWithoutStaff.setStatus("active");
        shiftWithoutStaff.setRegister(1);
        shiftWithoutStaff.setShiftType("morning");
        shiftWithoutStaff.setWorkArea("area1");

        Shift shiftOlderThanTwoMonths = new Shift();
        shiftOlderThanTwoMonths.setStartTime(twoMonthsAgo.minusDays(1));
        shiftOlderThanTwoMonths.setEndTime(twoMonthsAgo.minusDays(1).plusHours(8));
        shiftOlderThanTwoMonths.setStatus("active");
        shiftOlderThanTwoMonths.setRegister(2);
        shiftOlderThanTwoMonths.setShiftType("afternoon");
        shiftOlderThanTwoMonths.setWorkArea("area2");

        shiftRepository.save(shiftWithoutStaff);
        shiftRepository.save(shiftOlderThanTwoMonths);

        shiftService.deleteShiftsWithCriteria();

        List<Shift> remainingShifts = shiftRepository.findAll();
        assertThat(remainingShifts).isEmpty();
    }
}