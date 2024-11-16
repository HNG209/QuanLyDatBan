package org.login.quanlydatban.utilities;

public class NumberFormatter {
    public static String formatPrice(String number) {//input: 5000, output: 5.000
        number = number.replace(".", "");

        StringBuilder reversed = new StringBuilder(number).reverse();
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < reversed.length(); i++) {
            if (i > 0 && i % 3 == 0) {
                formatted.append('.');
            }
            formatted.append(reversed.charAt(i));
        }

        return formatted.reverse().toString();
    }
}
