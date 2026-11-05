package com.tsfn.service;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SmsSenderService {

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;

    @Value("${twilio.phone-number}")
    private String TWILIO_PHONE_NUMBER;

    public void sendSMS(String numbers, String message) {
        try {
            // Initialize Twilio client with your credentials
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            System.out.println("numbers" +numbers);
            // Send an SMS message
            Message twilioMessage = Message.creator(
                            new PhoneNumber(numbers),
                            new PhoneNumber(TWILIO_PHONE_NUMBER),
                            message)
                    .create();

            System.out.println("SMS SID: " + twilioMessage.getSid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
