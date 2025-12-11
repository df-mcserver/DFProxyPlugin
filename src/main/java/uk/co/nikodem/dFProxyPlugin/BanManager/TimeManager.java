package uk.co.nikodem.dFProxyPlugin.BanManager;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class TimeManager {
    public static String formatDuration(long duration) {
        long seconds = Duration.ofMillis(duration).toSeconds();

        return seconds <= 0 ? "now" :
                Arrays.stream(
                    new String[]{
                            formatTime("year",  (seconds / 31536000)),
                            formatTime("day",   (seconds / 86400)%365),
                            formatTime("hour",  (seconds / 3600)%24),
                            formatTime("minute",(seconds / 60)%60),
                            formatTime("second",(seconds%3600)%60)})
                            .filter(e-> !Objects.equals(e, ""))
                            .collect(Collectors.joining(", "))
                            .replaceAll(", (?!.+,)", " and ");
    }

    public static String formatTime(String s, long time){
        return time==0 ? "" : time + " " + s + (time==1?"" : "s");
    }
}
