package com.tcd.ase.externaldata.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> generateDatesWithHourInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<String> encodedDates = new ArrayList<>();
        try {
            while (startDateTime.compareTo(endDateTime) != 0) {
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = startDateTime.format(myFormatObj);
                String encodedDate = URLEncoder.encode(formattedDate, "UTF-8").replace("+", "%20");
                encodedDates.add(encodedDate);
                startDateTime = startDateTime.plusHours(1);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedDates;
    }
}
