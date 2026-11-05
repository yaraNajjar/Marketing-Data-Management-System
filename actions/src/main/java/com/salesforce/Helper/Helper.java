package com.salesforce.Helper;

import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Configuration
public class Helper {
    public List<String> getDays() {
        return Arrays.asList(
                "SUNDAY",
                "MONDAY",
                "TUESDAY",
                "WEDNESDAY",
                "THURSDAY",
                "FRIDAY",
                "SATURDAY",
                "DAILY"
        );
    }


    public boolean isValidSendTo(String sendTo , String actionType) {
        String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(sendTo);

      if(actionType.equals("SMS") || actionType.equals("WHATSAPP")){
          return sendTo.startsWith("+972") && sendTo.length() == 13;
      }
      if(actionType.equals("EMAIL")){
          return matcher.matches();
      }
      return false;
    }
}


