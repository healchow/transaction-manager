package com.healchow.transaction.detail.web.controller.detail;

import com.healchow.transaction.detail.api.DetailAppService;
import com.healchow.transaction.detail.request.CreateDetailRequest;
import com.healchow.transaction.detail.response.DetailResponse;
import com.healchow.transaction.detail.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction Detail Controller
 */
@Tag(name = "Transaction-Detail-API")
@RestController
@RequestMapping({"/detail"})
public class DetailController {

    /**
     * TODO After supporting authentication, use the real user ID
     */
    private static final String MOCK_USER_ID = "admin";

    @Autowired
    private DetailAppService detailAppService;

    @Operation(summary = "Create-Detail")
    @PostMapping(value = "/create")
    public Response<String> create(@RequestBody @Validated CreateDetailRequest request) {
        return Response.success(detailAppService.create(MOCK_USER_ID, request));
    }

    @Operation(summary = "Get-Detail")
    @GetMapping(value = "/get/{tid}")
    public Response<DetailResponse> get(@PathVariable("tid") String tid) {
        return Response.success(detailAppService.get(MOCK_USER_ID, tid));
    }

}
