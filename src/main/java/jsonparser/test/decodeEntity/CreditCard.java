package jsonparser.test.decodeEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class CreditCard implements java.io.Serializable {

    private long id;

    private String userId;

    private String displayName;

    private CardType cardType;

    private String issuingBank;

    private String cardNumber;

    private String cardNumberMasked;

    private String expirationYear;

    private String expirationMonth;

    private String isCardExpired;

    private long isCoBrandedCard;

    private HolderInfo holderInfo;

    private String ctime;

    private String mtime;


    public long getId() { return id; }

    public String getUserId() { return userId; }

    public String getDisplayName() { return displayName; }

    public CardType getCardType() { return cardType; }

    public String getIssuingBank() { return issuingBank; }

    public String getCardNumber() { return cardNumber; }

    public String getCardNumberMasked() { return cardNumberMasked; }

    public String getExpirationYear() { return expirationYear; }

    public String getExpirationMonth() { return expirationMonth; }

    public String getIsCardExpired() { return isCardExpired; }

    public long getIsCoBrandedCard() { return isCoBrandedCard; }

    public HolderInfo getHolderInfo() { return holderInfo; }

    public String getCtime() { return ctime; }

    public String getMtime() { return mtime; }


    public CreditCard setId(long id) { this.id = id; return this; }

    public CreditCard setUserId(String userId) { this.userId = userId; return this; }

    public CreditCard setDisplayName(String displayName) { this.displayName = displayName; return this; }

    public CreditCard setCardType(CardType cardType) { this.cardType = cardType; return this; }

    public CreditCard setIssuingBank(String issuingBank) { this.issuingBank = issuingBank; return this; }

    public CreditCard setCardNumber(String cardNumber) { this.cardNumber = cardNumber; return this; }

    public CreditCard setCardNumberMasked(String cardNumberMasked) { this.cardNumberMasked = cardNumberMasked; return this; }

    public CreditCard setExpirationYear(String expirationYear) { this.expirationYear = expirationYear; return this; }

    public CreditCard setExpirationMonth(String expirationMonth) { this.expirationMonth = expirationMonth; return this; }

    public CreditCard setIsCardExpired(String isCardExpired) { this.isCardExpired = isCardExpired; return this; }

    public CreditCard setIsCoBrandedCard(long isCoBrandedCard) { this.isCoBrandedCard = isCoBrandedCard; return this; }

    public CreditCard setHolderInfo(HolderInfo holderInfo) { this.holderInfo = holderInfo; return this; }

    public CreditCard setCtime(String ctime) { this.ctime = ctime; return this; }

    public CreditCard setMtime(String mtime) { this.mtime = mtime; return this; }
    public CreditCard() {  }

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