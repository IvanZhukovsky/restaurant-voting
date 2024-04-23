package ru.asphaltica.restaurantvoting.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

    private static final int END_VOTING_HOURS = 16;
    private static final int END_VOTING_MINUTES = 0;

    private DateTimeUtil() {
    }

    public static LocalDateTime atStartOfToday(){
        return LocalDate.now().atStartOfDay();
    }

    public static LocalDateTime atEndOfToday(){
        return LocalDate.now().atTime(LocalTime.MAX);
    }

    public static LocalDateTime atEndOfVoting(){
        return LocalDate.now().atTime(END_VOTING_HOURS, END_VOTING_MINUTES);
    }

    public static boolean isVotePeriod(){
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(atStartOfToday()) && now.isBefore(atEndOfVoting());
    }


}
