package jsonparser.test.decodeEntity;

public enum CarrierType {
    YMEMBER,
    CDC,
    BARCODE;

    public static CarrierType fromString(String v) {
        for (CarrierType e : values()) {
            if (e.toString().equals(v)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid string representation for CarrierType: " + v);
    }
}

