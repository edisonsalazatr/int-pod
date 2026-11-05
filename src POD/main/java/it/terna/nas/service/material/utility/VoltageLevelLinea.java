package it.terna.nas.service.material.utility;

import lombok.ToString;

@ToString
public enum VoltageLevelLinea {
    OVER_430_KV("0", "> 430 kV", "TRG.LID.1", "TRG.LID.2"),
    KV_400("1", "400 kV (360 - 420 kV)", "TRG.LID.1", "TRG.LID.2"),
    KV_220("2", "220 kV (195 - 250 kV)", "TRG.LID.3", "TRG.LID.4"),
    KV_150("3", "150 kV (141 - 170 kV)", "TRG.LID.5", "TRG.LID.6"),
    KV_130("4", "130 kV (110 - 140 kV)", "TRG.LID.5", "TRG.LID.6"),
    KV_60("5", "60 kV (40 - 90 kV)", "TRG.LID.7", "TRG.LID.8"),
    KV_30("6", "30 kV (23 - 39 kV)", "TRG.LID.7", "TRG.LID.8"),
    KV_20("7", "20 kV (17 - 22 kV)", "TRG.LID.7", "TRG.LID.8"),
    KV_15("8", "15 kV (11 - 16 kV)", "TRG.LID.7", "TRG.LID.8"),
    KV_10("9", "10 kV (1 - 10 kV)", "TRG.LID.7", "TRG.LID.8"),
    KV_400_CC("A", "400 kV C.C.", "TRG.LID.1", "TRG.LID.2"),
    KV_200_CC("B", "200 kV C.C.", "TRG.LID.3", "TRG.LID.4"),
    KV_150_CC("C", "150 kV C.C.", "TRG.LID.5", "TRG.LID.6");

    private final String value;
    private final String description;
    private final String targetUrgente;
    private final String targetDifferibile;

    VoltageLevelLinea(String string, String description, String targetUrgente, String targetDifferibile) {
        this.value = string;
        this.description = description;
        this.targetUrgente = targetUrgente;
        this.targetDifferibile = targetDifferibile;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getTargetUrgente() {
        return targetUrgente;
    }

    public String getTargetDifferibile() {
        return targetDifferibile;
    }

    public static VoltageLevelLinea fromValue(String value) {
        for (VoltageLevelLinea level : values()) {
            if (level.value.equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public String determineGravita(double distance, double urgentDistance, double deferrableDistance) {
        if (distance <= urgentDistance) {
            return "URGENTI";
        } else if (distance <= deferrableDistance) {
            return "DIFFERIBILI";
        } else {
            return null;
        }
    }
}
