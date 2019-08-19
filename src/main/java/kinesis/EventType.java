package kinesis;

public enum EventType {
    INSERT("insert"),
    MODIFY("update"),
    REMOVE("delete");

    private String op;

    EventType(String op) {
        this.op = op;
    }

    String getOp() {
        return op;
    }
}
