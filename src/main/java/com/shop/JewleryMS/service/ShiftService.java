package com.shop.JewleryMS.service;

import com.shop.JewleryMS.entity.Shift;
import com.shop.JewleryMS.model.CreateShiftRequest;
import com.shop.JewleryMS.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {

    @Autowired
    ShiftRepository shiftRepository;

    public Shift CreateShift(CreateShiftRequest createShiftRequest){
        Shift shift = new Shift();
        shift.setStartTime(createShiftRequest.getStartTime());
        shift.setRegister(createShiftRequest.getRegister());
        shift.setEndTime(createShiftRequest.getEndTime());
        shift.setShiftType(createShiftRequest.getShiftType());
        shift.setStatus(createShiftRequest.getStatus());
        shift.setWorkArea(createShiftRequest.getWorkArea());
        return shiftRepository.save(shift);
    }

}
