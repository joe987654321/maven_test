package bastet

/**
 * EInvoiceCarrier.
 */
class EinvoiceCarrier {

    EinvoiceCarrier() {
        userId = "y-testid"
        carrierType = "YMEMBER"
        carrierId = "orig yid"
    }

    def toUpdateValue() {
        carrierId = "new yid"
    }

    def String userId
    def String carrierType
    def String carrierId

}
