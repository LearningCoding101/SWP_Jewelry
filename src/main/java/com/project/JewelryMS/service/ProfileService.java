package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.enumClass.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.exception.DuplicateEmailException;
import com.project.JewelryMS.exception.DuplicateUsernameException;
import com.project.JewelryMS.model.Profile.*;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    StaffAccountRepository staffAccountRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    ImageService imageService;

    public ManagerProfileResponse viewManagerProfile(Long managerId) {
        Optional<Account> authenticationOptional = authenticationRepository.findById(managerId);
        if (authenticationOptional.isPresent() && authenticationOptional.get().getRole() == RoleEnum.ROLE_MANAGER) {
            Account account = authenticationOptional.get();
            return new ManagerProfileResponse(account.getRole(), account.getEmail(), account.getUsername(),
                    account.getAccountName(), account.getStatus());
        }
        return null;
    }

    public UpdateManager updateManagerProfile(Long managerId, UpdateManager updateManagerRequest) {
        Optional<Account> authenticationOptional = authenticationRepository.findById(managerId);
        if (authenticationOptional.isPresent() && authenticationOptional.get().getRole() == RoleEnum.ROLE_MANAGER) {
            Account account = authenticationOptional.get();
            if (authenticationRepository.existsByAUsernameAndPkUserIDNot(account.getAUsername(), account.getPK_userID())) {
                throw new DuplicateUsernameException("Username đã được sử dụng.");
            }
            if (authenticationRepository.existsByEmailAndPkUserIDNot(account.getEmail(), account.getPK_userID())) {
                throw new DuplicateEmailException("Email đã được sử dụng.");
            }
            account.setAccountName(updateManagerRequest.getAccountName());
            account.setAUsername(updateManagerRequest.getUsername());
            account.setEmail(updateManagerRequest.getEmail());
            account.setAImage(imageService.uploadImageByPathService(updateManagerRequest.getImage()));
            authenticationRepository.save(account);
            return new UpdateManager(account.getEmail(), account.getUsername(),
                    account.getAccountName(), account.getAImage());
        }
        return null;
    }

    public AdminProfileResponse viewAdminProfile(Long adminId) {
        Optional<Account> authenticationOptional = authenticationRepository.findById(adminId);
        if (authenticationOptional.isPresent() && authenticationOptional.get().getRole() == RoleEnum.ROLE_ADMIN) {
            Account account = authenticationOptional.get();
            return new AdminProfileResponse(account.getRole(), account.getEmail(), account.getUsername(),
                    account.getAccountName(), account.getAImage(), account.getStatus());
        }
        return null;
    }

    public UpdateAdmin updateAdminProfile(Long adminId, UpdateAdmin updateAdminRequest) {
        Optional<Account> authenticationOptional = authenticationRepository.findById(adminId);
        if (authenticationOptional.isPresent() && authenticationOptional.get().getRole() == RoleEnum.ROLE_ADMIN) {
            Account account = authenticationOptional.get();
            if (authenticationRepository.existsByAUsernameAndPkUserIDNot(account.getAUsername(), account.getPK_userID())) {
                throw new DuplicateUsernameException("Username đã được sử dụng.");
            }
            if (authenticationRepository.existsByEmailAndPkUserIDNot(account.getEmail(), account.getPK_userID())) {
                throw new DuplicateEmailException("Email đã được sử dụng.");
            }
            account.setAccountName(updateAdminRequest.getAccountName());
            account.setAUsername(updateAdminRequest.getUsername());
            account.setEmail(updateAdminRequest.getEmail());
            account.setAImage(imageService.uploadImageByPathService(updateAdminRequest.getImage()));
            authenticationRepository.save(account);
            return new UpdateAdmin(account.getEmail(), account.getUsername(),
                    account.getAccountName(), account.getAImage());
        }
        return null;
    }

    public StaffProfileResponse viewStaffProfile(Integer staffId) {
        Optional<StaffAccount> staffOptional = staffAccountRepository.findById(staffId);
        if (staffOptional.isPresent()) {
            StaffAccount staffAccount = staffOptional.get();
            return new StaffProfileResponse(staffAccount.getAccount().getRole(), staffAccount.getAccount().getEmail(), staffAccount.getAccount().getUsername(),
                    staffAccount.getAccount().getAccountName(), staffAccount.getStartDate(), staffAccount.getPhoneNumber(),
                    staffAccount.getSalary(), staffAccount.getAccount().getStatus());
        }
        return null;
    }

    public UpdateStaff updateStaffProfile(Integer staffId, UpdateStaff updateStaffRequest) {
        Optional<StaffAccount> staffOptional = staffAccountRepository.findById(staffId);
        if (staffOptional.isPresent()) {
            StaffAccount staffAccount = staffOptional.get();
//            if(authenticationRepository.existsByAUsername(staffAccount.getAccount().getUsername())){
//                throw new DuplicateUsernameException("Username đã được sử dụng hoặc bạn đang cập nhật Username chính mình.");
//            }
//            if(authenticationRepository.existsByEmail(staffAccount.getAccount().getEmail())){
//                throw new DuplicateEmailException("Email đã được sử dụng hoặc bạn đang cập nhật Email chính mình.");
//            }
            if (authenticationRepository.existsByAUsernameAndPkUserIDNot(staffAccount.getAccount().getAUsername(), staffAccount.getAccount().getPK_userID())) {
                throw new DuplicateUsernameException("Username đã được sử dụng.");
            }
            if (authenticationRepository.existsByEmailAndPkUserIDNot(staffAccount.getAccount().getEmail(), staffAccount.getAccount().getPK_userID())) {
                throw new DuplicateEmailException("Email đã được sử dụng.");
            }
            staffAccount.getAccount().setAccountName(updateStaffRequest.getAccountName());
            staffAccount.getAccount().setAUsername(updateStaffRequest.getUsername());
            staffAccount.getAccount().setEmail(updateStaffRequest.getEmail());
            String image = imageService.uploadImageByPathService(updateStaffRequest.getImage());
            staffAccount.getAccount().setAImage(image);
            staffAccount.setPhoneNumber(updateStaffRequest.getPhoneNumber());
            staffAccountRepository.save(staffAccount);
            return new UpdateStaff(staffAccount.getAccount().getEmail(), staffAccount.getAccount().getUsername(),
                    staffAccount.getAccount().getAccountName(), staffAccount.getPhoneNumber(), image);
        }
        return null;
    }

//    public ViewShiftResponse viewShiftProfile(Integer staffId) {
//        Optional<StaffAccount> staffOptional = staffAccountRepository.findById(staffId);
//        if (staffOptional.isPresent()) {
//            StaffAccount staffAccount = staffOptional.get();
//            if (staffAccount.getStaffShifts() != null) {
//                List<Shift> shift = staffAccount.getShift();
//                return new ViewShiftResponse(
//                        staffAccount.getAccount().getEmail(),
//                        staffAccount.getAccount().getUsername(),
//                        staffAccount.getPhoneNumber(),
//                        shift
////                        shift.getShiftID(),
////                        shift.getStartTime(),
////                        shift.getRegister(),
////                        shift.getEndTime(),
////                        shift.getShiftType(),
////                        shift.getStatus(),
////                        shift.getWorkArea()
//                );
//            } else {
//                // If the staff doesn't have an assigned shift
//                return null;
//            }
//        }
//        return null; // Staff not found
//    }

    public List<StaffListResponse> getAllAccounts() {
        List<Account> accounts = authenticationRepository.findAllAccounts();
        return accounts.parallelStream()
                .map(a -> new StaffListResponse(
                        a.getPK_userID(),
                        a.getRole(),
                        a.getEmail(),
                        a.getAUsername(),
                        a.getAImage(),
                        a.getAccountName(),
                        a.getStatus() != null ? a.getStatus() : 0 // handle null status
                ))
                .collect(Collectors.toList());
    }
}
