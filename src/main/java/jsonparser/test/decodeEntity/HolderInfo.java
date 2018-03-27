package jsonparser.test.decodeEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class HolderInfo implements java.io.Serializable {


    private String holderName;


    private String birthday;

    private String email;

    private String nationalId;

    private AddressInfo addressInfo;

    private Phone phone1;

    private Phone phone2;


    public String getHolderName() { return holderName; }

    public String getBirthday() { return birthday; }

    public String getEmail() { return email; }

    public String getNationalId() { return nationalId; }

    public AddressInfo getAddressInfo() { return addressInfo; }

    public Phone getPhone1() { return phone1; }

    public Phone getPhone2() { return phone2; }


    public HolderInfo setHolderName(String holderName) { this.holderName = holderName; return this; }

    public HolderInfo setBirthday(String birthday) { this.birthday = birthday; return this; }

    public HolderInfo setEmail(String email) { this.email = email; return this; }

    public HolderInfo setNationalId(String nationalId) { this.nationalId = nationalId; return this; }

    public HolderInfo setAddressInfo(AddressInfo addressInfo) { this.addressInfo = addressInfo; return this; }

    public HolderInfo setPhone1(Phone phone1) { this.phone1 = phone1; return this; }

    public HolderInfo setPhone2(Phone phone2) { this.phone2 = phone2; return this; }
    public HolderInfo() {  }

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