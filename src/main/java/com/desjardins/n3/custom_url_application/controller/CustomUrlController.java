package com.desjardins.n3.custom_url_application.controller;

import com.desjardins.n3.custom_url_application.service.CustomUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomUrlController {
    private final CustomUrlService customUrlService;

    @Autowired
    public CustomUrlController(CustomUrlService customUrlService) {
        this.customUrlService = customUrlService;
    }

    @GetMapping("/appelerSite")
    public String appelerSite(@RequestParam(required = false) String url) {
        return this.customUrlService.appelerUrl(url);
    }
}
