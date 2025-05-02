package com.desjardins.n3.custom_url_application.controller;

import com.desjardins.n3.custom_url_application.service.CustomUrlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomUrlControllerTest {
    private CustomUrlController testedClass;

    @Mock
    private CustomUrlService mockCustomUrlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.testedClass = new CustomUrlController(this.mockCustomUrlService);
    }

    /**
     * Case 1
     * Given a url that is null, empty or not empty.
     */
    @ParameterizedTest
    @ValueSource(strings = "https://www.not.empty")
    @NullAndEmptySource
    void givenCase1_whenAppelerSite_thenTreatmentDelegatedToService(String url) {
        String expectedValue =
                """
                {
                    "foo" : "Insane test!"
                }
                """;
        Mockito.when(this.mockCustomUrlService.appelerUrl(url))
               .thenReturn(expectedValue);

        String actualValue = this.testedClass.appelerSite(url);

        Assertions.assertEquals(expectedValue, actualValue);
    }
}