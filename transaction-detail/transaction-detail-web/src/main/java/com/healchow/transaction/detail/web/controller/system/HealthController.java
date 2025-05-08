package com.healchow.transaction.detail.web.controller.system;

import com.healchow.transaction.detail.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health API
 */
@Tag(name = "Health-API")
@RestController
@RequestMapping({"/health"})
public class HealthController {

    private static final String HEALTH_SUCCESS = "success";

    @Operation(summary = "Check-Alive")
    @GetMapping(value = "/alive")
    public Response<String> alive() {
        return Response.success(HEALTH_SUCCESS);
    }
}
