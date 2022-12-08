package com.mybank.payments.extension.controller;

import com.mybank.payments.extension.service.PaymentOrderIntegrationExtensionService;
import com.mybank.payments.integration.extension.api.PaymentOrderOptionsIntegrationExtensionApi;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsEvaluateRequest;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsEvaluateResponse;
import com.mybank.payments.integration.extension.api.model.PaymentTypeInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentOrderIntegrationExtensionController implements PaymentOrderOptionsIntegrationExtensionApi {

    private final PaymentOrderIntegrationExtensionService extensionService;

    public PaymentOrderIntegrationExtensionController(
        PaymentOrderIntegrationExtensionService extensionService) {
        this.extensionService = extensionService;
    }

    @Override
    public ResponseEntity<PaymentOptionsEvaluateResponse> evaluatePostPaymentOptions(
        PaymentOptionsEvaluateRequest request) {
        extensionService.enrichPostOptions(request);
        return ResponseEntity.ok(new PaymentOptionsEvaluateResponse().paymentTypes(request.getPaymentTypes()));
    }

    @Override
    public ResponseEntity<PaymentTypeInformation> evaluateGetPaymentOptionsByPaymentType(String paymentType,
        PaymentTypeInformation paymentTypeInformation) {
        extensionService.enrichGetPaymentOptionsByPaymentType(paymentTypeInformation);
        return ResponseEntity.ok(paymentTypeInformation);
    }
}
