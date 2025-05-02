package com.desjardins.n3.custom_url_application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
class CustomUrlServiceImpl implements CustomUrlService {
    private final WebClient webClient;
    private final String baseUrl;

    @Autowired
    CustomUrlServiceImpl(WebClient webClient,
                         @Value("${custom.url}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public String appelerUrl(String url) {
        return this.webClient.get()
                             .uri(url)
                             .accept(MediaType.APPLICATION_JSON)
                             .exchangeToMono(response -> handleResponse(response, url))
                             .block();
    }

    private Mono<String> handleResponse(ClientResponse clientResponse, String url) {
        if (clientResponse.statusCode().isError()) {
            return getFormattedError(clientResponse, url);
        }
        return clientResponse.bodyToMono(String.class);
    }

    private Mono<String> getFormattedError(ClientResponse response, String url) {
        String formattedError =
            """
            {
                "error": "There was an error while calling url %s.",
                "code": %d,
                "message" : %s
            }
            """;
        return response.bodyToMono(String.class)
                       .map(body -> formattedError.formatted(this.baseUrl + url,
                                                             response.statusCode().value(),
                                                             body));
    }
}
