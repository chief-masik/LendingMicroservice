package com.example.lendingmicroservice.controller;

import com.example.lendingmicroservice.entity.UserDTO;
import com.example.lendingmicroservice.response.ResponseData;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.LoanOrderDeleteDTO;
import com.example.lendingmicroservice.response.data.ResponseOrderId;
import com.example.lendingmicroservice.response.data.ResponseOrderStatus;
import com.example.lendingmicroservice.response.data.ResponseOrders;
import com.example.lendingmicroservice.response.data.ResponseTariffs;
import com.example.lendingmicroservice.response.error.ResponseError;
import com.example.lendingmicroservice.service.LoanOrderService;
import com.example.lendingmicroservice.service.TariffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan-service")
public class LoanOrderController {
    private final LoanOrderService loanOrderService;
    private final TariffService tariffService;

    @Operation(summary = "Get all tariffs")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseData.class)))
    @GetMapping("getTariffs")
    public ResponseEntity<?> getTariffs() {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .data(ResponseTariffs.builder()
                                .tariffs(tariffService.getAllTariffs()).build()).build());
    }

    @Operation(summary = "Get list of orders for user")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseData.class)))
    @PostMapping("getOrders")
    public ResponseEntity<?> getOrders(@RequestBody UserDTO user) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .data(ResponseOrders.builder()
                                .orders(loanOrderService.getOrdersByUserId(user)).build()).build());
    }

    @Operation(summary = "Create new loan order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseData.class))),
            @ApiResponse(responseCode = "400", description = "Tariff not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =  ResponseError.class)))
    })
    @PostMapping("order")
    //@TimeLimiter(name = "oneSecTimeLimiter")
    public ResponseEntity<?> newOrder(@RequestBody LoanOrderCreateDTO loanOrderCreateDTO) {

        //return CompletableFuture.supplyAsync( ()-> {
        tariffService.checkTariffExistence(loanOrderCreateDTO.getTariffId());
        loanOrderService.canCreateLoanOrder(loanOrderCreateDTO);
        return ResponseEntity.ok(
                ResponseData.builder()
                        .data(ResponseOrderId.builder()
                                .orderId(loanOrderService.setNewLoanOrder(loanOrderCreateDTO)).build()).build());
        //});
    }

    @Operation(summary = "Get status of loan order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseData.class))),
            @ApiResponse(responseCode = "400", description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("getStatusOrder")
    public ResponseEntity<?> getStatusOrder(@RequestParam UUID orderId) {
        return ResponseEntity.ok(
                ResponseData.builder()
                        .data(ResponseOrderStatus.builder()
                                .orderStatus(loanOrderService.getStatusOrder(orderId)).build()).build());
    }

    @Operation(summary = "Delete a loan order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Order impossible to delete",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)))
    })
    @DeleteMapping("deleteOrder")
    public void deleteLoanOrder(@RequestBody LoanOrderDeleteDTO loanOrderDeleteDTO) {
        loanOrderService.deleteLoanOrder(loanOrderDeleteDTO);
    }
}
