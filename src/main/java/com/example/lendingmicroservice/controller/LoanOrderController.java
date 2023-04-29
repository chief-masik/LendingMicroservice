package com.example.lendingmicroservice.controller;

import com.example.lendingmicroservice.response.ResponseData;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.LoanOrderDeleteDTO;
import com.example.lendingmicroservice.service.LoanOrderService;
import com.example.lendingmicroservice.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan-controller")
public class LoanOrderController {
    private final LoanOrderService loanOrderService;
    private final TariffService tariffService;

    @GetMapping("getTariffs")
    public ResponseEntity<?> getTariffs() {
        return ResponseEntity.ok(ResponseData.builder().data(tariffService.getTariffs()).build());
    }

    @PostMapping("order")
    public ResponseEntity<?> newOrder(@RequestBody LoanOrderCreateDTO loanOrderCreateDTO) {
        tariffService.checkTariffExistence(loanOrderCreateDTO.getTariffId());
        loanOrderService.canCreateLoanOrder(loanOrderCreateDTO);
        return ResponseEntity.ok(ResponseData.builder().data(loanOrderService.setNewLoanOrder(loanOrderCreateDTO)).build());
    }

    @GetMapping("getStatusOrder")
    public ResponseEntity<?> getStatusOrder(@RequestParam UUID orderId) {
        return ResponseEntity.ok(ResponseData.builder().data(loanOrderService.getStatusOrder(orderId)).build());
    }

    @DeleteMapping("deleteOrder")
    public void deleteLoanOrder(@RequestBody LoanOrderDeleteDTO loanOrderDeleteDTO) {
        loanOrderService.deleteLoanOrder(loanOrderDeleteDTO.getUserId(), loanOrderDeleteDTO.getOrderId());
    }
}
