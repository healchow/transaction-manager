package com.healchow.transaction.detail.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health API
 */
@RestController
@RequestMapping({"/health"})
public class HealthController {

    private static final String HEALTH_SUCCESS = "success";

    @GetMapping(value = "/alive")
    public String alive() {
        return HEALTH_SUCCESS;
    }
}
