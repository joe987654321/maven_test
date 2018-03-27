package jsonparser.test.decodeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class Phone implements java.io.Serializable {

    private PhoneType type;

    private String countryCode;

    private String areaCode;

    private String number;

    private String extension;


    public PhoneType getType() { return type; }

    public String getCountryCode() { return countryCode; }

    public String getAreaCode() { return areaCode; }

    public String getNumber() { return number; }

    public String getExtension() { return extension; }


    public Phone setType(PhoneType type) { this.type = type; return this; }

    public Phone setCountryCode(String countryCode) { this.countryCode = countryCode; return this; }

    public Phone setAreaCode(String areaCode) { this.areaCode = areaCode; return this; }

    public Phone setNumber(String number) { this.number = number; return this; }

    public Phone setExtension(String extension) { this.extension = extension; return this; }
    public Phone() {  }

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