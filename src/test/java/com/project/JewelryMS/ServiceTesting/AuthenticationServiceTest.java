package com.project.JewelryMS.ServiceTesting;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.enumClass.RoleEnum;
import com.project.JewelryMS.model.AccountResponse;
import com.project.JewelryMS.model.LoginRequest;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.repository.AuthenticationRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.service.AuthenticationService;
import com.project.JewelryMS.service.EmailService;
import com.project.JewelryMS.service.JWTservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private EmailService emailService;

    @Mock
    private JWTservice jwTservice;

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private StaffAccountRepository staffAccountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        CreateStaffAccountRequest request = new CreateStaffAccountRequest();
        request.setAccountName("TestName");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setUsername("username");
        request.setSalary(50000.0F);
        request.setPhoneNumber("1234567890");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        CreateStaffAccountRequest result = authenticationService.register(request);

        assertEquals(request, result);
        verify(staffAccountRepository, times(1)).save(any(StaffAccount.class));
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testHandleRegisterCheckEmail() {
        CreateStaffAccountRequest request = new CreateStaffAccountRequest();
        request.setEmail("test@example.com");

        when(emailService.validEmail(anyString())).thenReturn(true);

        Boolean result = authenticationService.handleRegisterCheckEmail(request);

        assertTrue(result);
    }

    @Test
    void testGetAllAccount() {
        authenticationService.getAllAccount();
        verify(authenticationRepository, times(1)).findAll();
    }

    @Test
    void testGetAccount() {
        when(authenticationRepository.findAccountByemail(anyString())).thenReturn(new Account());
        Account result = authenticationService.getAccount("test@example.com");
        assertNotNull(result);
    }

    @Test
    void testGetAccountById() {
        when(authenticationRepository.findAccountById(anyLong())).thenReturn(new Account());
        Account result = authenticationService.getAccountById(1L);
        assertNotNull(result);
    }

    @Test
    void testSendForgotPasswordEmail() {
        Account account = new Account();
        account.setAPassword("oldPassword");

        when(authenticationRepository.findAccountByemail(anyString())).thenReturn(account);
        when(emailService.sendTempPassword(any(Account.class))).thenReturn("tempPassword");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String result = authenticationService.sendForgotPasswordEmail("test@example.com");
        assertEquals("Successfully sent new temporary password", result);
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("username");
        request.setPassword("password");

        Account account = new Account();
        account.setAUsername("username");
        account.setRole(RoleEnum.ROLE_STAFF);
        account.setPK_userID(1);

        when(authenticationRepository.findAccountByUsername(anyString())).thenReturn(account);
        when(jwTservice.generateToken(any(Account.class))).thenReturn("token");

        AccountResponse result = authenticationService.login(request);

        assertEquals("username", result.getUsername());
        assertEquals("token", result.getToken());
        assertEquals(RoleEnum.ROLE_STAFF, result.getRole());
        assertEquals(1L, result.getId());
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");

        Account account = new Account();
        account.setAPassword("encodedOldPassword");

        when(authenticationRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        ResponseEntity<String> result = authenticationService.changePassword(request, 1L);

        assertEquals("Password changed successfully", result.getBody());
        assertEquals(200, result.getStatusCodeValue());
        verify(authenticationRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testLoadUserByUsername() {
        Account account = new Account();
        when(authenticationRepository.findAccountByUsername(anyString())).thenReturn(account);

        UserDetails result = authenticationService.loadUserByUsername("username");

        assertNotNull(result);
    }

    @Test
    void testGetAllManagerAccount() {
        authenticationService.getAllManagerAccount();
        verify(authenticationRepository, times(1)).findByRole(RoleEnum.ROLE_MANAGER);
    }

    @Test
    void testLoginGoogle() throws FirebaseAuthException {
        LoginGoogleRequest request = new LoginGoogleRequest();
        request.setToken("testToken");

        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseToken.getEmail()).thenReturn("test@example.com");
        when(firebaseToken.getName()).thenReturn("Test User");

        FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);
        setField(authenticationService, "firebaseAuth", firebaseAuth);
        when(firebaseAuth.verifyIdToken(anyString())).thenReturn(firebaseToken);

        Account account = new Account();
        account.setEmail("test@example.com");
        account.setAUsername("Test User");

        when(authenticationRepository.findAccountByemail(anyString())).thenReturn(null);
        when(authenticationRepository.save(any(Account.class))).thenReturn(account);
        when(jwTservice.generateToken(any(Account.class))).thenReturn("token");

        AccountResponseGG result = authenticationService.loginGoogle(request);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getAUsername());
        assertEquals("token", result.getToken());
    }
}
