package com.mybank.payments.validation.controller;

import com.mybank.payments.integration.validation.api.PaymentOrderIntegrationValidatorApi;
import com.mybank.payments.integration.validation.api.model.ErrorItem;
import com.mybank.payments.integration.validation.api.model.PaymentValidationRequest;
import com.mybank.payments.integration.validation.api.model.PaymentValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentOrderIntegrationValidatorController implements PaymentOrderIntegrationValidatorApi {

    public static final String VALID_PAYMENT_TYPE = "VALID_PAYMENT_TYPE";
    public static final String MSG_PAYMENT_VALID = "Payment is valid";
    public static final String MSG_PAYMENT_INVALID = "Payment is invalid";
    public static final String MSG_INVALID_PAYMENT_TYPE = "Invalid payment type";

    @Override
    public ResponseEntity<PaymentValidationResponse> postWebhookPaymentOrderValidate(PaymentValidationRequest request) {
        String paymentType = request.getPayment().getPaymentType();
        if (VALID_PAYMENT_TYPE.equals(paymentType)) {
            return ResponseEntity.ok(new PaymentValidationResponse().message(MSG_PAYMENT_VALID));
        }
        return ResponseEntity.ok(new PaymentValidationResponse()
            .message(MSG_PAYMENT_INVALID)
            .addErrorsItem(new ErrorItem()
                .message(MSG_INVALID_PAYMENT_TYPE)
                .putContextItem("paymentType", paymentType)));
    }

}
