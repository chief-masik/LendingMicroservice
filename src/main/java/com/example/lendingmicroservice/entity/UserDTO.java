package com.example.lendingmicroservice.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    @NonNull
    private Long userId;
}
