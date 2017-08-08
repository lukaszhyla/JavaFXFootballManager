package pl.sdacademy.util;

import javafx.scene.control.Control;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    /**
     * Zwraca "ładnie" sformatowaną datę.
     * @param dateTime
     */
    public static String prettifyDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * Koduje tekst w taki sposób, aby nie zawierał przecinków ani złamań linii.
     * @param value
     */
    public static String encode(String value) {
        return value.replaceAll(",", "|||").replaceAll("\n", "|-|");
    }

    /**
     * Dekoduje tekst odwracając metodę kodowania z metody encode.
     * @param value
     */
    public static String decode(String value) {
        return value.replaceAll("\\|\\|\\|", ",").replaceAll("\\|-\\|", "\n");
    }

    /**
     * Metoda skraca przydługawy tekst (jeśli jest dłuższy od przekazanej maksymalnej długości skraca go,
     * i dokleja na końcu "(...)".
     * @param value
     * @param maxLength
     * @return
     */
    public static String ellipsize(String value, int maxLength) {
        if(value.length() > maxLength) {
            return value.substring(0, maxLength - 5) + "(...)";
        } else {
            return value;
        }
    }

    /**
     * Ustawia widoczność kontrolki (wraz z wpływaniem jej na rozmieszczenie elementów)
     * @param control
     * @param visible
     */
    public static void setControlVisibility(Control control, boolean visible) {
        control.setManaged(visible);
        control.setVisible(visible);
    }
}
