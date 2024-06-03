package ru.asphaltica.restaurantvoting.util;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Setter
public class DateTimeUtil {

    private int endOfVotingHours = 11;
    private int endVotingMinutes = 0;

    public LocalDateTime atStartOfToday(){
        return LocalDate.now().atStartOfDay();
    }

    public LocalDateTime atEndOfToday(){
        return LocalDate.now().atTime(LocalTime.MAX);
    }

    public LocalDateTime atEndOfVoting(){
        return LocalDate.now().atTime(endOfVotingHours, endVotingMinutes);
    }

    public boolean isVoteChangePeriod(){
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(atStartOfToday()) && now.isBefore(atEndOfVoting());
    }
}
