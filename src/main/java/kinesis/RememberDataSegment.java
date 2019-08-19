package kinesis;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

public class RememberDataSegment {
    private String rId;
    private String appId;
    private Long updatedTs;
    private Map<String, JsonElement> datum = new HashMap<>();

    public RememberDataSegment withRid(String rId) {
        this.rId = rId;
        return this;
    }

    public RememberDataSegment withAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public RememberDataSegment withUpdatedTs(Long updatedTs) {
        this.updatedTs = updatedTs;
        return this;
    }

    public Map<String, JsonElement> getDatum() {
        return datum;
    }

    public String getrId() {
        return rId;
    }

    public String getAppId() {
        return appId;
    }

    public Long getUpdatedTs() {
        return updatedTs;
    }
}
