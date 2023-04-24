package com.example.lendingmicroservice.controller;

import com.example.lendingmicroservice.Response.ResponseData;
import com.example.lendingmicroservice.Response.ResponseError;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.service.LoanOrderService;
import com.example.lendingmicroservice.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan-controller")
public class LoanOrderController {
    private final LoanOrderService loanOrderService;
    private final TariffService tariffService;

    @GetMapping("getTariffs")
    public ResponseData<?> getTariffs() {

        ArrayList<Tariff> tariffs = tariffService.getTariffs();
        ResponseData<?> responseData = new ResponseData<>(tariffs);
        return responseData;
    }

    @PostMapping("order")
    public ResponseData<?> newOrder(@RequestBody LoanOrderCreateDTO loanOrderCreateDTO) throws ResponseStatusException {

        tariffService.isTariff(loanOrderCreateDTO.getTariffId());
        ResponseData<?> response = new ResponseData<>(loanOrderService.setNewLoanOrder(loanOrderCreateDTO));
        return response;
    }

    @GetMapping("getStatusOrder")
    public ResponseData<?> getStatusOrder(@RequestParam UUID orderId) {

        ResponseData<?> response = new ResponseData(loanOrderService.getStatusOrder(orderId));
        return response;
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseError> handleException(ResponseStatusException e) {
        try {
            ResponseError responseError1 = new ResponseError(e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(responseError1);
        }
        catch (NullPointerException exception) {
            ResponseError responseError2 = new ResponseError("REASON_IS_NULL");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseError2);
        }
    }
}
