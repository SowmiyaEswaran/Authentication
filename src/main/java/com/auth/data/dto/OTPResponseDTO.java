package com.auth.data.dto;

import com.auth.common.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OTPResponseDTO {
    private Status status;
    private String message;
}
