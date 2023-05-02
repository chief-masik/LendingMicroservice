package com.example.lendingmicroservice.controller;

import com.example.lendingmicroservice.response.ResponseData;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.LoanOrderDeleteDTO;
import com.example.lendingmicroservice.response.data.ResponseOrderId;
import com.example.lendingmicroservice.response.data.ResponseOrderStatus;
import com.example.lendingmicroservice.response.data.ResponseTariffs;
import com.example.lendingmicroservice.service.LoanOrderService;
import com.example.lendingmicroservice.service.TariffService;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan-controller")
public class LoanOrderController {
    private final LoanOrderService loanOrderService;
    private final TariffService tariffService;

    @GetMapping("getTariffs")
    public ResponseEntity<?> getTariffs() {
        return ResponseEntity
                .ok(ResponseData.builder()
                        .data(ResponseTariffs.builder()
                                .tariffs(tariffService.getAllTariffs()).build()).build());
    }

    @PostMapping("order")
    //@TimeLimiter(name = "oneSecTimeLimiter")
    public ResponseEntity<?> newOrder(@RequestBody LoanOrderCreateDTO loanOrderCreateDTO) {

        //return CompletableFuture.supplyAsync( ()-> {
        tariffService.checkTariffExistence(loanOrderCreateDTO.getTariffId());
        loanOrderService.canCreateLoanOrder(loanOrderCreateDTO);
        return ResponseEntity
                .ok(ResponseData.builder()
                        .data(ResponseOrderId.builder()
                                .orderId(loanOrderService.setNewLoanOrder(loanOrderCreateDTO)).build()).build());
        //});
    }

    @GetMapping("getStatusOrder")
    public ResponseEntity<?> getStatusOrder(@RequestParam UUID orderId) {
        return ResponseEntity
                .ok(ResponseData.builder()
                        .data(ResponseOrderStatus.builder()
                                .orderStatus(loanOrderService.getStatusOrder(orderId)).build()).build());
    }

    @DeleteMapping("deleteOrder")
    public void deleteLoanOrder(@RequestBody LoanOrderDeleteDTO loanOrderDeleteDTO) {
        loanOrderService.deleteLoanOrder(loanOrderDeleteDTO);
    }
}
