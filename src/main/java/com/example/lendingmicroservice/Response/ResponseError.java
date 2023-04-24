package com.example.lendingmicroservice.Response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Data
public class ResponseError {
    @NonNull
    String code;
    String message;

    public ResponseError(String code) {
        this.code = code;
        switch (code) {
            case "LOAN_CONSIDERATION" -> this.message = "Заявка на рассмотрении";
            case "LOAN_ALREADY_APPROVED" -> this.message = "Заявка уже одобрена";
            case "TRY_LATER" -> this.message = "Попробуйте позже";
            case "TARIFF_NOT_FOUND" -> this.message = "Тариф не найден";
            case "ORDER_NOT_FOUND" -> this.message = "Заявка не найден";
            case "ORDER_IMPOSSIBLE_TO_DELETE" -> this.message = "Невозможно удалить заявку";
            case "REASON_IS_NULL" -> this.message = "Вызванный ResponseStatusException имеет reason == null";
            default -> this.message = "Что-то пошло не так";
        }
    }
}
