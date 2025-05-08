package com.healchow.transaction.detail.web.controller.detail;

import com.healchow.transaction.detail.api.DetailAppService;
import com.healchow.transaction.detail.domain.page.PageInfo;
import com.healchow.transaction.detail.request.CreateDetailRequest;
import com.healchow.transaction.detail.request.UpdateDetailRequest;
import com.healchow.transaction.detail.response.DetailResponse;
import com.healchow.transaction.detail.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction Detail Controller
 */
@Tag(name = "Transaction-Detail-API")
@Validated
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
    public Response<DetailResponse> get(@PathVariable(value = "tid") String tid) {
        return Response.success(detailAppService.get(MOCK_USER_ID, tid));
    }

    @Operation(summary = "List-Details")
    @GetMapping(value = "/list")
    @Parameters({
            @Parameter(name = "pageNum", description = "Current page number, default 1"),
            @Parameter(name = "pageSize", description = "Current page size, default 10")
    })
    public Response<PageInfo<DetailResponse>> list(
            @RequestParam(defaultValue = "1", required = false) @Min(1) Integer pageNum,
            @RequestParam(defaultValue = "10", required = false) @Min(1) @Max(100) Integer pageSize) {
        return Response.success(detailAppService.list(MOCK_USER_ID, pageNum, pageSize));
    }

    @Operation(summary = "Update-Detail")
    @PutMapping(value = "/update")
    public Response<String> update(@RequestBody @Validated UpdateDetailRequest request) {
        return Response.success(detailAppService.update(MOCK_USER_ID, request));
    }

    @Operation(summary = "Delete-Detail")
    @DeleteMapping(value = "/delete/{tid}")
    public Response<DetailResponse> delete(@PathVariable("tid") String tid) {
        return Response.success(detailAppService.delete(MOCK_USER_ID, tid));
    }

}
