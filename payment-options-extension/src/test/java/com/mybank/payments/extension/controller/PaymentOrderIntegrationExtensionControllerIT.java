package com.mybank.payments.extension.controller;

import static com.mybank.payments.extension.service.PaymentOrderIntegrationExtensionService.CUT_OFF_TIME;
import static com.mybank.payments.extension.util.TestHelper.TRANSFER_FEE_AMOUNT;
import static com.mybank.payments.extension.util.TestHelper.createPaymentOptionsEvaluateRequest;
import static com.mybank.payments.extension.util.TestHelper.createPaymentTypeInformation;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.mybank.payments.extension.Application;
import com.mybank.payments.extension.util.RestClient;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsEvaluateRequest;
import com.mybank.payments.integration.extension.api.model.PaymentTypeInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("it")
class PaymentOrderIntegrationExtensionControllerIT {

    @Autowired
    private RestClient restClient;

    @Test
    void testEvaluatePostPaymentOptions() throws Exception {
        PaymentOptionsEvaluateRequest request = createPaymentOptionsEvaluateRequest();

        restClient.performPostPaymentOptionsRequest(request)
            .andExpect(jsonPath("$.paymentTypes[*].options[*].cutOffTime").value(CUT_OFF_TIME))
            .andExpect(
                jsonPath("$.paymentTypes[*].options[*].transferFee.amount").value(Double.valueOf(TRANSFER_FEE_AMOUNT)));
    }

    @Test
    void testEvaluateGetPaymentOptionsByPaymentType() throws Exception {
        PaymentTypeInformation request = createPaymentTypeInformation();

        restClient.performGetPaymentOptionsByPaymentTypeRequest(request)
            .andExpect(jsonPath("$.options[*].cutOffTime").value(CUT_OFF_TIME));
    }
}
