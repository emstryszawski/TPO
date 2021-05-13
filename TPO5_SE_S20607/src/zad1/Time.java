/**
 *
 *  @author Stryszawski Emil S20607
 *
 */

package zad1;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Locale;

public class Time {

    private static final Locale locale = new Locale("pl");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)", locale);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE) 'godz.' HH:mm", locale);
    private static final ZoneId zone = ZoneId.of("Europe/Warsaw");

    public static String passed(String from, String to) {
        StringBuilder builder = new StringBuilder();

        ZonedDateTime zonedFromDateTime = null;
        ZonedDateTime zonedToDateTime = null;
        LocalDateTime fromDateTime;
        LocalDateTime toDateTime;
        LocalDate fromDate = null;
        LocalDate toDate = null;


        try {
            if (isDateTime(from) && isDateTime(to)) {
                fromDateTime = LocalDateTime.parse(from);
                toDateTime = LocalDateTime.parse(to);
                zonedFromDateTime = ZonedDateTime.of(fromDateTime, zone);
                zonedToDateTime = ZonedDateTime.of(toDateTime, zone);
            } else {
                fromDate = LocalDate.parse(from);
                toDate = LocalDate.parse(to);
            }
        } catch (DateTimeParseException e) {
            System.out.print("*** " + e);
        }

        if (fromDate != null && toDate != null) {
            print(fromDate, toDate, builder, fromDate.format(dateFormatter), toDate.format(dateFormatter),
                    ChronoUnit.DAYS.between(fromDate, toDate),
                    getWeekDifference(fromDate, toDate),
                    false);

        } else if (zonedFromDateTime != null && zonedToDateTime != null) {
            print(zonedFromDateTime.toLocalDate(), zonedToDateTime.toLocalDate(), builder,
                    zonedFromDateTime.format(dateTimeFormatter), zonedToDateTime.format(dateTimeFormatter),
                    ChronoUnit.DAYS.between(zonedFromDateTime, zonedToDateTime),
                    getWeekDifference(zonedFromDateTime, zonedToDateTime), true,
                    ChronoUnit.MINUTES.between(zonedFromDateTime, zonedToDateTime),
                    ChronoUnit.HOURS.between(zonedFromDateTime, zonedToDateTime));
        }
        return builder.toString();
    }

    private static void print(LocalDate from, LocalDate to, StringBuilder builder,
                              String fromString, String toString, long days, BigDecimal weeks, boolean isDateTime,
                              long ... minutesAndHours) {

        String dayString = (days != 0 ? (days + " " + (days == 1 ? "dzień, " : "dni, ")) : "");
        String hoursAndMinutes = isDateTime ? "\n - godzin: " +
                minutesAndHours[1] + ", minut: " + minutesAndHours[0] : "";

        builder.append("Od ").append(fromString).append(" do ").append(toString)
                .append("\n - mija: ").append(dayString).append("tygodni ").append(weeks.stripTrailingZeros())
                .append(hoursAndMinutes)
                .append(getCalendar(from, to));
    }

    private static String getCalendar(LocalDate from, LocalDate to) {
        long daysBetween = ChronoUnit.DAYS.between(from, to);
        String calendar = "";

        if (daysBetween > 0) {
            int years = Period.between(from, to).getYears();
            int months = Period.between(from, to).getMonths();
            int days = Period.between(from, to).getDays();

            calendar = "\n - kalendarzowo: ";

            calendar += (years != 0 ? (years + " " + (years == 1 ? "rok" : ((years%10) > 1 && (years%10) < 5 ? "lata" : "lat" )) + ", ") : "");
            calendar += (months != 0 ? (months + " " + (months == 1 ? "miesiąc" : (months < 5 ?  "miesiące" : "miesięcy" )) + ", ") : "");
            calendar += (days != 0 ? (days + " " + (days == 1 ? "dzień, " : "dni, ")) : "");
            calendar = calendar.substring(0, calendar.length() - 2);
        }
        return calendar;
    }

    private static BigDecimal getWeekDifference(Temporal date1, Temporal date2) {
        long between = ChronoUnit.DAYS.between(date1, date2);
        BigDecimal bigDecimal = BigDecimal.valueOf(between / 7.0);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public static boolean isDateTime(String date) {
        try {
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withResolverStyle(ResolverStyle.STRICT).parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isDate(String date) {
        try {
            DateTimeFormatter.ofPattern("yyyy-MM-dd").withResolverStyle(ResolverStyle.STRICT).parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
