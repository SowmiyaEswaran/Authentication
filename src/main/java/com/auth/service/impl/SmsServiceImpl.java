package com.auth.service.impl;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.common.CommonConstants;
import com.auth.common.Status;
import com.auth.data.config.TwilioConfig;
import com.auth.data.dto.OTPResponseDTO;
import com.auth.data.entity.OTPData;
import com.auth.data.entity.OTPRequest;
import com.auth.data.entity.OTPValidationRequest;
import com.auth.exception.AppException.InformationMismatchException;
import com.auth.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private TwilioConfig twilioConfig;

    private static final Map<String, OTPData> otpMap = new HashMap<>();

    public OTPResponseDTO sendSMS(OTPRequest otpRequest){
        OTPResponseDTO otpResponseDto = null;
        try {
            String toPhno = otpRequest.getPhoneNo();
            String otp = generateOTP();
            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
            String otpMessage = twilioConfig.getMessage1() + " " +  otp + " " + twilioConfig.getMessage2();
             Message.creator(
                            new com.twilio.type.PhoneNumber(toPhno),
                            new com.twilio.type.PhoneNumber(twilioConfig.getPhoneNumber()),
                            otpMessage)
                    .create();

            otpMap.put(toPhno, new OTPData(otp, Instant.now().toEpochMilli()));
            otpResponseDto = new OTPResponseDTO(Status.SUCCESS, otpMessage);
        } catch (Exception e) {
            otpResponseDto = new OTPResponseDTO(Status.FAILURE, e.getMessage());
        }
        return  otpResponseDto;
    }

    private String generateOTP(){
    	return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }

    public OTPResponseDTO validateOtp(OTPValidationRequest otpValidationRequest) {
        OTPResponseDTO otpResponseDTO = null;
        OTPData otpData = otpMap.get(otpValidationRequest.getPhoneNo());
        if(otpData!=null) {
            long currentTime = Instant.now().toEpochMilli();
            long timeDifference = currentTime - otpData.getTimestamp();
            if (timeDifference  <= twilioConfig.getExpireDuration().toMillis() && otpData.getOtp().equals(otpValidationRequest.getOtpNumber())) {
                otpMap.remove(otpValidationRequest.getPhoneNo());
                otpResponseDTO = new OTPResponseDTO(Status.SUCCESS, CommonConstants.VALID_OTP);
                return otpResponseDTO;
            } else {
                otpResponseDTO = new OTPResponseDTO(Status.FAILURE, CommonConstants.INVALID_OTP);
                return otpResponseDTO;
            }
        } else {
            throw new InformationMismatchException(CommonConstants.INFORMATIONMISMATCH);
        }


    }

}
