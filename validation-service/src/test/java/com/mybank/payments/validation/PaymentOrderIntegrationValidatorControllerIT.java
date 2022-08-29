package com.mybank.payments.validation;

import static com.backbase.buildingblocks.test.http.TestRestTemplateConfiguration.TEST_SERVICE_TOKEN;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.payments.integration.validation.api.model.PaymentValidationRequest;
import com.mybank.payments.integration.validation.api.model.PaymentValidationUserContext;
import com.mybank.payments.integration.validation.api.model.Status;
import com.mybank.payments.validation.controller.PaymentOrderIntegrationValidatorController;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class PaymentOrderIntegrationValidatorControllerIT {

    public static final String INVALID_PAYMENT_TYPE = "INVALID_PAYMENT_TYPE";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidPaymentType() throws Exception {
        performRequest(PaymentOrderIntegrationValidatorController.VALID_PAYMENT_TYPE)
            .andExpect(jsonPath("message").value(is(PaymentOrderIntegrationValidatorController.MSG_PAYMENT_VALID)));
    }

    @Test
    public void testInvalidPaymentType() throws Exception {
        performRequest(INVALID_PAYMENT_TYPE)
            .andExpect(jsonPath("message").value(is(PaymentOrderIntegrationValidatorController.MSG_PAYMENT_INVALID)))
            .andExpect(jsonPath("errors[0].message")
                .value(is(PaymentOrderIntegrationValidatorController.MSG_INVALID_PAYMENT_TYPE)));
    }

    private ResultActions performRequest(String paymentType) throws Exception {
        PaymentValidationRequest requestBody = createPaymentValidationRequest(paymentType);

        return mvc.perform(post("/service-api/v1/webhooks/payment-orders/validate")
                .header("Authorization", TEST_SERVICE_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestBody)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
            .andExpect(content().contentType(APPLICATION_JSON));
    }

    private PaymentValidationRequest createPaymentValidationRequest(String paymentType) {
        return new PaymentValidationRequest()
            .payment(new com.mybank.payments.integration.validation.api.model.BasicIdentifiedPaymentOrder()
                .id(UUID.randomUUID().toString())
                .status(Status.ENTERED)
                .paymentType(paymentType))
            .userContext(new PaymentValidationUserContext());
    }

}
