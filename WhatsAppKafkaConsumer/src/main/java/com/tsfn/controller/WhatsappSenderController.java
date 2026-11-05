package com.tsfn.controller;

import com.tsfn.service.WhatsappSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/whatsapp") //  request mapping to whatsapp
@RestController
public class WhatsappSenderController {

    @Autowired
    private static WhatsappSenderService whatsappSenderService; // Inject WhatsappSenderService

    @GetMapping("/send")
    public static ResponseEntity<?> sendWhatsAppMessage() {
        whatsappSenderService.sendWhatsAppMessage("+972543330712", "Hello, this is a test WhatsApp message!"); // Call sendWhatsAppMessage method
        return new ResponseEntity<>("WhatsApp message sent successfully", HttpStatus.OK); // Return success response
    }


}
