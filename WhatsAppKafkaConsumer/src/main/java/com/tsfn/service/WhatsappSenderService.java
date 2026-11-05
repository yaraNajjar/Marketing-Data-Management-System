package com.tsfn.service;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;


import org.springframework.beans.factory.annotation.Value;

@Service
public class WhatsappSenderService {

    @Value("${twilio.account-sid}")
    private static String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private static String AUTH_TOKEN;

    @Value("${twilio.phone-number}")
    private static String TWILIO_PHONE_NUMBER;

    public static void sendWhatsAppMessage(String to, String message) {
        try {
       Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            System.out.println("to" + to );
            // Send a whatsapp message
            Message twilioMessage = Message.creator(
                    new com.twilio.type.PhoneNumber(to),
                    new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                    message ).create();

            System.out.println("WhatsApp message SID: " + twilioMessage.getSid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
