package jsonparser.test.decodeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class AddressInfo implements java.io.Serializable {


    private String country;


    private String zipcode;


    private String city;


    private String district;


    private String street;


    public String getCountry() { return country; }

    public String getZipcode() { return zipcode; }

    public String getCity() { return city; }

    public String getDistrict() { return district; }

    public String getStreet() { return street; }


    public AddressInfo setCountry(String country) { this.country = country; return this; }

    public AddressInfo setZipcode(String zipcode) { this.zipcode = zipcode; return this; }

    public AddressInfo setCity(String city) { this.city = city; return this; }

    public AddressInfo setDistrict(String district) { this.district = district; return this; }

    public AddressInfo setStreet(String street) { this.street = street; return this; }
    public AddressInfo() {  }

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
