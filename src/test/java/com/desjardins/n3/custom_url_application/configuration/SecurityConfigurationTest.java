package com.desjardins.n3.custom_url_application.configuration;

import com.desjardins.n3.custom_url_application.service.CustomUrlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomUrlService mockCustomUrlService;

    /**
     * Case 1
     * Given unauthorized user
     */
    @Test
    void givenCase1_whenAppelerSite_thenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/appelerSite"))
               .andExpect(status().isUnauthorized());
    }

    /**
     * Case 2
     * Given authorized user.
     */
    @Test
    @WithMockUser(username = "user")
    void givenCase2_whenAppelerSite_thenStatusIsOk() throws Exception {
        Mockito.when(this.mockCustomUrlService.appelerUrl(Mockito.anyString()))
               .thenReturn(
                       """
                               {
                                   "message" : "OK"
                               }
                          """);

        mockMvc.perform(get("/appelerSite"))
               .andExpect(status().isOk());
    }
}