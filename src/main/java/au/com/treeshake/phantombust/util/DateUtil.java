package au.com.treeshake.phantombust.util;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@UtilityClass
public final class DateUtil {

    public static final DateTimeFormatter ISO_INSTANT_MILLISECOND = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneId.of("UTC"));
}
