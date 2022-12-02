package com.mybank.payments.extension.service;

import static com.mybank.payments.extension.util.TestHelper.CUT_OFF_TIME;
import static com.mybank.payments.extension.util.TestHelper.INITIAL_CUT_OFF_TIME;
import static com.mybank.payments.extension.util.TestHelper.INITIAL_TRANSFER_FEE_AMOUNT;
import static com.mybank.payments.extension.util.TestHelper.TRANSFER_FEE_AMOUNT;
import static com.mybank.payments.extension.util.TestHelper.createPaymentOptionsEvaluateRequest;
import static com.mybank.payments.extension.util.TestHelper.createPaymentTypeInformation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.mybank.payments.integration.extension.api.model.PaymentOptionsEvaluateRequest;
import com.mybank.payments.integration.extension.api.model.PaymentTypeInformation;
import com.mybank.payments.integration.extension.api.model.PaymentTypeOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentOrderIntegrationExtensionServiceTest {
    
    private PaymentOrderIntegrationExtensionService extensionService;
    
    @BeforeEach
    void setUp(){
        extensionService = new PaymentOrderIntegrationExtensionService();
    }
    
    @Test
    void testEnrichPostOptions() {
        PaymentOptionsEvaluateRequest request = createPaymentOptionsEvaluateRequest();

        PaymentTypeOption paymentTypeOption = request.getPaymentTypes().get(0).getOptions().get(0);
        assertThat(paymentTypeOption.getCutOffTime(), is(INITIAL_CUT_OFF_TIME));
        assertThat(paymentTypeOption.getTransferFee().getAmount().toString(), is(INITIAL_TRANSFER_FEE_AMOUNT));
        
        extensionService.enrichPostOptions(request);

        paymentTypeOption = request.getPaymentTypes().get(0).getOptions().get(0);
        assertThat(paymentTypeOption.getCutOffTime(), is(CUT_OFF_TIME));
        assertThat(paymentTypeOption.getTransferFee().getAmount().toString(), is(TRANSFER_FEE_AMOUNT));
    }

    @Test
    void testEnrichGetPaymentOptionsByPaymentType() {
        PaymentTypeInformation request = createPaymentTypeInformation();

        assertThat(request.getOptions().get(0).getCutOffTime(), is(INITIAL_CUT_OFF_TIME));
        
        extensionService.enrichGetPaymentOptionsByPaymentType(request);

        assertThat(request.getOptions().get(0).getCutOffTime(), is(CUT_OFF_TIME));
    }
}
