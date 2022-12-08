package com.mybank.payments.extension.util;

import static com.mybank.payments.integration.extension.api.model.SchemeName.IBAN;

import com.mybank.payments.integration.extension.api.model.CounterpartyAccount;
import com.mybank.payments.integration.extension.api.model.Currency;
import com.mybank.payments.integration.extension.api.model.Identification;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsEvaluateRequest;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsRequest;
import com.mybank.payments.integration.extension.api.model.PaymentTypeInformation;
import com.mybank.payments.integration.extension.api.model.PaymentTypeOption;
import com.mybank.payments.integration.extension.api.model.TransferFee;
import java.math.BigDecimal;
import java.util.List;

public class TestHelper {

    public static final String INTERNATIONAL_TRANSFER = "INTERNATIONAL_TRANSFER";
    public static final String INITIAL_CUT_OFF_TIME = "14:00";
    public static final String CUT_OFF_TIME = "15:00";
    public static final String INSTRUCTED_AMOUNT = "100";
    public static final String INITIAL_TRANSFER_FEE_AMOUNT = "5.00";
    public static final String TRANSFER_FEE_AMOUNT = "2.00";
    public static final String COUNTERPARTY_IBAN = "NL31ABNA0599613165";
    public static final String ORIGINATOR_IBAN = "NL32AEGO0784420544";
    public static final String COUNTRY_CODE_US = "US";
    public static final String CURRENCY_CODE_US = "USD";

    private TestHelper() {
    }

    public static PaymentOptionsEvaluateRequest createPaymentOptionsEvaluateRequest() {
        return new PaymentOptionsEvaluateRequest()
            .paymentOptionsRequest(createPaymentOptionsRequest())
            .paymentTypes(List.of(createPaymentTypeInformation()));
    }

    public static PaymentTypeInformation createPaymentTypeInformation() {
        return new PaymentTypeInformation()
            .paymentType(INTERNATIONAL_TRANSFER)
            .options(List.of(createPaymentTypeOption()));
    }

    public static PaymentTypeOption createPaymentTypeOption() {
        return new PaymentTypeOption()
            .cutOffTime(INITIAL_CUT_OFF_TIME)
            .transferFee(new TransferFee()
                .amount(new BigDecimal(INITIAL_TRANSFER_FEE_AMOUNT))
                .currencyCode(CURRENCY_CODE_US)
            );
    }

    public static PaymentOptionsRequest createPaymentOptionsRequest() {
        return new PaymentOptionsRequest()
            .counterpartyCountry(COUNTRY_CODE_US)
            .originatorAccount(new Identification()
                .schemeName(IBAN)
                .identification(ORIGINATOR_IBAN))
            .counterpartyAccount(new CounterpartyAccount()
                .schemeName(IBAN)
                .identification(COUNTERPARTY_IBAN))
            .instructedAmount(new Currency()
                .amount(INSTRUCTED_AMOUNT)
                .currencyCode(CURRENCY_CODE_US));
    }
}
