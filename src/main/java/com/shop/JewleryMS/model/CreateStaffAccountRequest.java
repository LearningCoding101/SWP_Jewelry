package com.shop.JewleryMS.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class CreateStaffAccountRequest {
    int userID;
    int shiftID;
    Date startDate;
    String phoneNumber;
    float salary;

}
