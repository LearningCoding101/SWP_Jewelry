package com.project.JewelryMS.AcceptanceTest;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.repository.PromotionRepository;
import com.project.JewelryMS.service.PromotionService;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PromotionServiceTest {

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionRepository promotionRepository;

    private static Long testPromotionId;

    @BeforeEach
    public void setUp() {
        // Add any setup logic here, such as initializing test data
        CreatePromotionRequest request = new CreatePromotionRequest();
        request.setCode("TEST_PROMO");
        request.setDescription("Test Promotion");
        request.setStartDate(LocalDate.parse("2024-08-01"));
        request.setEndDate(LocalDate.parse("2024-08-15"));
        request.setDiscount(15);

        Promotion createdPromotion = promotionService.createPromotion(request);
        testPromotionId = createdPromotion.getPK_promotionID();
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test, reset or delete any test data
        if (testPromotionId != null) {
            promotionService.deletePromotionById(testPromotionId);
        }
    }

    @Test
    @Order(1)
    public void testCreatePromotion() {
        CreatePromotionRequest request = new CreatePromotionRequest();
        request.setCode("SUMMER2025");
        request.setDescription("Summer Sale 2025");
        request.setStartDate(LocalDate.of(2025, 10, 1)); // Set start date
        request.setEndDate(LocalDate.of(2025, 10, 30)); // Set end date
        request.setDiscount(20);

        // Create a new Promotion object without saving it via repository
        Promotion promotion = new Promotion();
        promotion.setCode(request.getCode());
        promotion.setDescription(request.getDescription());
        promotion.setStartDate(Date.from(request.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        promotion.setEndDate(Date.from(request.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        promotion.setDiscount(request.getDiscount());

        // Invoke the service method directly
        Promotion createdPromotion = promotionService.createPromotion(request);

        // Assert
        assertNotNull(createdPromotion);
        assertEquals(request.getCode(), createdPromotion.getCode());
        assertEquals(request.getDescription(), createdPromotion.getDescription());
        assertEquals(request.getDiscount(), createdPromotion.getDiscount());
        assertNotNull(createdPromotion.getPK_promotionID()); // Ensure ID is generated or set
    }


    @Test
    @Order(2)
    public void testReadAllPromotions() {
        List<PromotionResponse> promotions = promotionService.readAllPromotion();

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
        assertTrue(promotions.stream().anyMatch(p -> p.getPromotionID()==(testPromotionId)));
    }

    @Test
    @Order(3)
    public void testUpdatePromotion() {
        PromotionResponse existingPromotion = promotionService.getPromotionById(testPromotionId);
        assertNotNull(existingPromotion);

        PromotionRequest request = new PromotionRequest();
        request.setPK_promotionID(existingPromotion.getPromotionID());
        request.setCode("UPDATEDCODE");
        request.setDescription("Updated Description");
        request.setStartDate(LocalDate.parse("2024-08-05"));
        request.setEndDate(LocalDate.parse("2024-08-20"));
        request.setDiscount(30);

        promotionService.updatePromotionDetails(request);

        PromotionResponse updatedPromotion = promotionService.getPromotionById(testPromotionId);

        assertNotNull(updatedPromotion);
        assertEquals(request.getCode(), updatedPromotion.getCode());
        assertEquals(request.getDescription(), updatedPromotion.getDescription());
        assertEquals(request.getDiscount(), updatedPromotion.getDiscount());
    }

    @Test
    @Order(4)
    public void testDeletePromotion() {
        // Create a new promotion for deletion
        CreatePromotionRequest request = new CreatePromotionRequest();
        request.setCode("DELETEME");
        request.setDescription("Promotion to Delete");
        request.setStartDate(LocalDate.parse("2024-09-01"));
        request.setEndDate(LocalDate.parse("2024-09-30"));
        request.setDiscount(10);

        Promotion createdPromotion = promotionService.createPromotion(request);
        Long promotionIdToDelete = createdPromotion.getPK_promotionID();

        assertNotNull(createdPromotion);

        promotionService.deletePromotionById(promotionIdToDelete);

        PromotionResponse deletedPromotion = promotionService.getPromotionById(promotionIdToDelete);

        assertNull(deletedPromotion);
    }

    @Test
    @Order(5)
    public void testGetPromotionById() {
        PromotionResponse promotion = promotionService.getPromotionById(testPromotionId);

        assertNotNull(promotion);
        assertEquals(testPromotionId, promotion.getPromotionID());
    }

    @Test
    @Order(6)
    public void testGetPromotionByCode() {
        List<PromotionResponse> promotions = promotionService.getPromotionByCode("TEST_PROMO");

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
        assertTrue(promotions.stream().anyMatch(p -> p.getPromotionID()==(testPromotionId)));
    }

    @Test
    @Order(7)
    public void testReadAllActivePromotions() {
        List<PromotionResponse> activePromotions = promotionService.readAllActivePromotion();

        assertNotNull(activePromotions);
        assertFalse(activePromotions.isEmpty());
        assertTrue(activePromotions.stream().anyMatch(p -> p.getPromotionID()==(testPromotionId)));
    }

    @Test
    @Order(8)
    public void testGetPromotionsByDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date targetDate = dateFormat.parse("2024-08-10");

        List<PromotionResponse> promotions = promotionService.getPromotionsByDate(targetDate);

        assertNotNull(promotions);
        assertFalse(promotions.isEmpty());
        assertTrue(promotions.stream().anyMatch(p -> p.getPromotionID()==(testPromotionId)));
    }

    // Add more test methods as needed for different scenarios

}