package com.project.JewelryMS.model.Profile;

import com.project.JewelryMS.entity.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewShiftResponse {
    String email;
    String username;
    String phone;

    //Shift details
    private List<Shift> shift;

//    int shiftID;
//    Timestamp startTime;
//    int register;
//    Timestamp endTime;
//    String shiftType;
//    String status;
//    String workArea;
}
