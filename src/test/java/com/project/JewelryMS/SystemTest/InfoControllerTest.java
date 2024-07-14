package com.project.JewelryMS.SystemTest;

import com.project.JewelryMS.controller.InfoController;
import com.project.JewelryMS.service.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ApiService apiService;

    @InjectMocks
    private InfoController infoController;

    private final String goldUrl = "http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(infoController).build();
    }

    @Test
    public void testGetGoldPrice() throws Exception {
        String expectedPrice = "12345";
        when(apiService.getGoldPrice(goldUrl)).thenReturn(expectedPrice);

        mockMvc.perform(get("/Info/GoldPrice")
                        .header(HttpHeaders.ORIGIN, "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPrice));
    }



}