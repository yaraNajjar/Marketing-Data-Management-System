package com.tsfn.controller;

import com.tsfn.service.SmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sms")
@RestController
public class SmsSenderController{

    @Autowired
    private SmsSenderService smsSenderService;

    @GetMapping("/send")
    public ResponseEntity<?> sendSMS() {
        smsSenderService.sendSMS("+972503448889", "Hello, this is a test SMS!");
        return new ResponseEntity<>("SMS sent successfully", HttpStatus.OK);
    }
}
