package uk.co.nikodem.dFProxyPlugin.Bans;

import org.intellij.lang.annotations.Subst;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static Pattern formatPattern = Pattern.compile("\\d+|\\D");

    // this may be a terrible algorithm
    // but it's MY terrible algorithm
    public static long formatInputIntoDuration(@Subst("6d10h") String input) {
        long finalDuration = 0L;

        Matcher m = formatPattern.matcher(input);
        int i = 0;
        String lastGroup = "";
        while (m.find()) {
            if (i % 2 == 1) {
                int timeAmount = Integer.parseInt(lastGroup);
                String timeType = m.group();

                switch (timeType) {
                    case "s":
                        finalDuration += Duration.ofSeconds(timeAmount).toMillis();
                        break;

                    case "m":
                        finalDuration += Duration.ofMinutes(timeAmount).toMillis();
                        break;

                    case "h":
                        finalDuration += Duration.ofHours(timeAmount).toMillis();
                        break;

                    case "d":
                        finalDuration += Duration.ofDays(timeAmount).toMillis();
                        break;

                    case "y":
                        finalDuration += Duration.ofDays(timeAmount* 365L).toMillis();
                        break;
                }
            } else {
                lastGroup = m.group();
            }
            i++;
        }

        return finalDuration;
    }
}
