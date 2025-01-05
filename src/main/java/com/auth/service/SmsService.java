package com.auth.service;


import com.auth.data.dto.OTPResponseDTO;
import com.auth.data.entity.OTPRequest;
import com.auth.data.entity.OTPValidationRequest;


public interface SmsService {
    public OTPResponseDTO sendSMS(OTPRequest otpRequest);
    public OTPResponseDTO validateOtp(OTPValidationRequest otpValidationRequest);

}
