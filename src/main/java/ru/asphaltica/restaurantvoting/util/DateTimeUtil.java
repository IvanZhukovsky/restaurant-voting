package ru.asphaltica.restaurantvoting.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

    private static final int END_VOTING_HOURS = 11;
    private static final int END_VOTING_MINUTES = 0;


//    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
//    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);
//
//    public static LocalDateTime atStartOfDayOrMin(LocalDate localDate) {
//        return localDate != null ? localDate.atStartOfDay() : MIN_DATE;
//    }
//
//    public static LocalDateTime atStartOfNextDayOrMax(LocalDate localDate) {
//        return localDate != null ? localDate.plusDays(1).atStartOfDay() : MAX_DATE;
//    }
//
//    public static @Nullable LocalDate parseLocalDate(@Nullable String str) {
//        return StringUtils.hasLength(str) ? LocalDate.parse(str) : null;
//    }

    public static LocalDateTime atStartOfToday(){
        return LocalDate.now().atStartOfDay();
    }

    public static LocalDateTime atEndOfToday(){
        return LocalDate.now().atTime(LocalTime.MAX);
    }

    public static LocalDateTime atEndOfVoting(){
        return LocalDate.now().atTime(END_VOTING_HOURS, END_VOTING_MINUTES);
    }


}
