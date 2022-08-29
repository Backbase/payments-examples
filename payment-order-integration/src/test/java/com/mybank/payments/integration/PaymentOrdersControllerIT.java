package com.mybank.payments.integration;

import static com.backbase.buildingblocks.test.http.TestRestTemplateConfiguration.TEST_SERVICE_TOKEN;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.payments.outbound.model.PaymentOrdersPostRequestBody;
import com.mybank.payments.outbound.model.PaymentOrdersPostResponseBody;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class PaymentOrdersControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPostPutCancelPaymentOrder() throws Exception {
        // Create Payment
        PaymentOrdersPostRequestBody requestBody = createPaymentOrdersPostRequestBody();

        String contentString = mvc.perform(post("/service-api/v2/payment-orders")
                .header("Authorization", TEST_SERVICE_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestBody)))
            .andDo(print())
            .andExpect(status().isAccepted())
            .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        PaymentOrdersPostResponseBody postResponse =
            objectMapper.readValue(contentString, PaymentOrdersPostResponseBody.class);
        String bankReferenceId = postResponse.getBankReferenceId();

        // Update Payment
        mvc.perform(put("/service-api/v2/payment-orders/" + bankReferenceId)
                .header("Authorization", TEST_SERVICE_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestBody)))
            .andDo(print())
            .andExpect(status().isAccepted())
            .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
            .andExpect(content().contentType(APPLICATION_JSON));

        // Cancel Payment
        mvc.perform(post("/service-api/v2/payment-orders/" + bankReferenceId + "/cancel")
                .header("Authorization", TEST_SERVICE_TOKEN)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestBody)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE))
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("accepted").value(is(true)));
    }

    private PaymentOrdersPostRequestBody createPaymentOrdersPostRequestBody() {
        return new PaymentOrdersPostRequestBody()
            .id(UUID.randomUUID().toString())
            .requestedExecutionDate(LocalDate.now());
    }

}
