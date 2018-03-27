package jsonparser.test.decodeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAnyElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class EinvoiceCarrier implements java.io.Serializable {

    private long id;

    private String userId;
    private CarrierType carrierType;
    private String carrierId;

    private String ctime;

    private String mtime;


    // This annotated field 'reserved' is used to handle the Moxy unmarshall error
    // case when user requests some unknown fields which are nullable.
    @XmlAnyElement(lax=true)
    private Object parsecReserved;

    public long getId() { return id; }

    public String getUserId() { return userId; }

    public CarrierType getCarrierType() { return carrierType; }

    public String getCarrierId() { return carrierId; }

    public String getCtime() { return ctime; }

    public String getMtime() { return mtime; }


    public EinvoiceCarrier setId(long id) { this.id = id; return this; }

    public EinvoiceCarrier setUserId(String userId) { this.userId = userId; return this; }

    public EinvoiceCarrier setCarrierType(CarrierType carrierType) { this.carrierType = carrierType; return this; }

    public EinvoiceCarrier setCarrierId(String carrierId) { this.carrierId = carrierId; return this; }

    public EinvoiceCarrier setCtime(String ctime) { this.ctime = ctime; return this; }

    public EinvoiceCarrier setMtime(String mtime) { this.mtime = mtime; return this; }
    public EinvoiceCarrier() {  }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}