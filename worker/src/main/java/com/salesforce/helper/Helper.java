package com.salesforce.helper;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static String buildCondition(String input) {
        // Remove leading and trailing square brackets
        input = input.substring(1, input.length() - 1);

        // Split the input string into clause strings
        String[] clauseStrings = input.split(",");

        StringBuilder conditionBuilder = new StringBuilder();

        // Iterate through each clause string
        for (int i = 0; i < clauseStrings.length; i++) {
            // Remove leading and trailing square brackets
            String clauseString = clauseStrings[i].trim();
            clauseString = clauseString.substring(1, clauseString.length() - 1);

            // Split the clause string into individual numbers
            String[] numbers = clauseString.split("\\s*,\\s*");

            // Skip empty clauses
            if (numbers.length == 0) {
                continue;
            }

            // Append opening parenthesis for the clause
            if (numbers.length > 1) {
                conditionBuilder.append("(");
            }

            // Iterate through each number in the clause
            for (int j = 0; j < numbers.length; j++) {
                // Append the number
                conditionBuilder.append(numbers[j]);

                // Append logical AND operator if not the last number in the clause
                if (j < numbers.length - 1) {
                    conditionBuilder.append(" && ");
                }
            }

            // Append closing parenthesis for the clause if there is more than one number
            if (numbers.length > 1) {
                conditionBuilder.append(")");
            }

            // Append logical OR operator if not the last non-empty clause
            if (i < clauseStrings.length - 1) {
                conditionBuilder.append(" || ");
            }
        }

        return conditionBuilder.toString();
    }

    public static Set<Integer> extractNumbers(String input) {
        Set<Integer> numbers = new HashSet<>();

        // Regular expression pattern to match numbers
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        // Iterate through matches and add them to the set
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            numbers.add(number);
        }

        return numbers;
    }
}
