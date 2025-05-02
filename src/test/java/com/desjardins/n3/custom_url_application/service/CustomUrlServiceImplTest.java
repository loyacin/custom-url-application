package com.desjardins.n3.custom_url_application.service;

import java.io.IOException;
import java.util.stream.Stream;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

class CustomUrlServiceImplTest {
    private static final String BASE_URL = "https://www.foo.com";

    private static MockWebServer mockWebServer;

    private CustomUrlServiceImpl testedClass;


    @BeforeAll
    static void initAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.create(mockWebServer.url("/").toString());
        this.testedClass = new CustomUrlServiceImpl(webClient, BASE_URL);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    /**
     * Case 1
     * Given a response and a path
     */
    @ParameterizedTest
    @ValueSource(strings = "/foo")
    @NullAndEmptySource
    void givenCase1_whenAppelerUrl_thenGetMethodIsDone(String notMatteringPath) throws InterruptedException {
        mockWebServer.enqueue(new MockResponse());

        this.testedClass.appelerUrl(notMatteringPath);

        RecordedRequest request = mockWebServer.takeRequest();
        Assertions.assertEquals("GET", request.getMethod());
    }

    @ParameterizedTest
    @ValueSource(strings = "/foo")
    @NullAndEmptySource
    void givenCase1_whenAppelerUrl_thenPathIsValid(String path) throws InterruptedException {
        mockWebServer.enqueue(new MockResponse());

        this.testedClass.appelerUrl(path);

        RecordedRequest request = mockWebServer.takeRequest();
        String expectedPath = StringUtils.isBlank(path) ? "/" : path;
        Assertions.assertEquals(expectedPath, request.getPath());
    }

    @ParameterizedTest
    @ValueSource(strings = "/foo")
    @NullAndEmptySource
    void givenCase1_whenAppelerUrl_thenJsonIsAccepted(String path) throws InterruptedException {
        mockWebServer.enqueue(new MockResponse());

        this.testedClass.appelerUrl(path);

        RecordedRequest request = mockWebServer.takeRequest();
        Assertions.assertEquals(MediaType.APPLICATION_JSON.toString(), request.getHeader("Accept"));
    }

    /**
     * Case 2
     * Given an ok response with a body.
     */
    @ParameterizedTest
    @ValueSource(strings = "/foo")
    @NullAndEmptySource
    void givenCase2_whenAppelerUrl_thenResponseIsReturned(String notMatteringPath) {
        String expectedResponse =
                """
                    {
                        "name" : "Loïc",
                        "age" : 42
                    }
                """;
        mockWebServer.enqueue(new MockResponse().setBody(expectedResponse));

        String actualResponse = this.testedClass.appelerUrl(notMatteringPath);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    /**
     * Case 3
     * Given an error response with a body.
     */
    @ParameterizedTest
    @MethodSource("provideCase3")
    void givenCase3_whenAppelerUrl_thenGetMethodIsDone(int code, String message) {
        mockWebServer.enqueue(new MockResponse()
                                      .setResponseCode(code)
                                      .setBody(message));

        String actualResponse = this.testedClass.appelerUrl("");

        String expectedResponse = getCase3ExpectedResponse(code, message);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @NotNull
    private static String getCase3ExpectedResponse(int code, String message) {
        String expectedResponseToFormat =
                """
                {
                    "error": "There was an error while calling url %s.",
                    "code": %d,
                    "message" : %s
                }
                """;
        return expectedResponseToFormat.formatted(BASE_URL,
                                                  code,
                                                  message);
    }

    private static Stream<Arguments> provideCase3() {
        return Stream.of(Arguments.of(400, "BadRequest"),
                         Arguments.of(401, "Unauthorized"),
                         Arguments.of(404, "NotFound"),
                         Arguments.of(500, "InternalServerError"));
    }
}