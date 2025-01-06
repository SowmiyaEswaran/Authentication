package com.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.data.dto.OTPResponseDTO;
import com.auth.data.entity.OTPRequest;
import com.auth.data.entity.OTPValidationRequest;
import com.auth.service.SmsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/es")
@Slf4j
public class OTPController {

    @Autowired
    SmsService smsService;

    @PostMapping("/sendOtp")
    public OTPResponseDTO sendOtp(@RequestBody OTPRequest otpRequest) {
       log.info("inside send otp :" + otpRequest.getPhoneNo());
       return smsService.sendSMS(otpRequest);
    }

    @PostMapping("/validateOtp")
    public OTPResponseDTO sendOtp(@RequestBody OTPValidationRequest otpValidationRequest) {
        log.info("inside validate otp :" + otpValidationRequest.getOtpNumber() + "phone Number "+ otpValidationRequest.getPhoneNo());
        return smsService.validateOtp(otpValidationRequest);
    }
}
