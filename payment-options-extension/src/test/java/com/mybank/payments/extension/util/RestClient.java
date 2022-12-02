package com.mybank.payments.extension.util;

import static com.backbase.buildingblocks.test.http.TestRestTemplateConfiguration.TEST_SERVICE_TOKEN;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsEvaluateRequest;
import com.mybank.payments.integration.extension.api.model.PaymentTypeInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class RestClient {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    private static final String BASE_URL = "/service-api/v1/webhooks/payment-options";

    public ResultActions performPostPaymentOptionsRequest(PaymentOptionsEvaluateRequest request) throws Exception {
        return mockMvc.perform(post(BASE_URL)
                .header("Authorization", TEST_SERVICE_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
            .andExpect(content().contentType(APPLICATION_JSON));
    }

    public ResultActions performGetPaymentOptionsByPaymentTypeRequest(PaymentTypeInformation request) throws Exception {
        return mockMvc.perform(post(BASE_URL + "/{paymentType}",
                request.getPaymentType())
                .header("Authorization", TEST_SERVICE_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
            .andExpect(content().contentType(APPLICATION_JSON));
    }

}
