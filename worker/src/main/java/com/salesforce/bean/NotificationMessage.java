package com.salesforce.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.serializer.Serializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage  {
    private String sendTo;
    private String notificationsText;
}
