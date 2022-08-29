package com.mybank.payments.integration;

import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mybank.payments.outbound.api.PaymentOrderIntegrationOutboundApi;
import com.mybank.payments.outbound.model.CancelResponse;
import com.mybank.payments.outbound.model.PaymentOrderPutRequestBody;
import com.mybank.payments.outbound.model.PaymentOrderPutResponseBody;
import com.mybank.payments.outbound.model.PaymentOrdersPostRequestBody;
import com.mybank.payments.outbound.model.PaymentOrdersPostResponseBody;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentOrdersController implements PaymentOrderIntegrationOutboundApi {

    private static final String JSON_EXTENSION = ".json";
    private static String ROOT_ORDER_PATH = "/tmp/orders";

    private Logger log = LoggerFactory.getLogger(PaymentOrdersController.class);

    private final ObjectWriter objectWriter;

    public PaymentOrdersController(ObjectWriter objectWriter) {
        this.objectWriter = objectWriter;
    }

    @Override
    public ResponseEntity<PaymentOrdersPostResponseBody> postPaymentOrders(
        @Valid PaymentOrdersPostRequestBody paymentOrdersPostRequestBody) {
        log.info("Received payment order {}", paymentOrdersPostRequestBody);
        try {
            String bankReferenceId = UUID.randomUUID().toString();
            File file = new File(ROOT_ORDER_PATH, makePaymentOrderFileName(bankReferenceId));
            objectWriter.writeValue(file, paymentOrdersPostRequestBody);
            return ResponseEntity.accepted().body(new PaymentOrdersPostResponseBody()
                .bankReferenceId(bankReferenceId)
                .bankStatus("ACCEPTED"));
        } catch (Exception e) {
            log.error("Error saving file", e);
            throw new InternalServerErrorException().withMessage("Saving payment order failed");
        }
    }

    @Override
    public ResponseEntity<CancelResponse> postCancelPaymentOrder(@Size(max = 64) String bankReferenceId) {
        return ResponseEntity.ok(new CancelResponse()
            .accepted(new File(ROOT_ORDER_PATH, makePaymentOrderFileName(bankReferenceId)).delete()));
    }

    @Override
    public ResponseEntity<PaymentOrderPutResponseBody> putPaymentOrder(String bankReferenceId,
        PaymentOrderPutRequestBody paymentOrderPutRequestBody) {

        try {
            File file = new File(ROOT_ORDER_PATH, makePaymentOrderFileName(bankReferenceId));
            objectWriter.writeValue(file, paymentOrderPutRequestBody);

            PaymentOrderPutResponseBody accepted = (PaymentOrderPutResponseBody) new PaymentOrderPutResponseBody()
                .bankReferenceId(bankReferenceId)
                .bankStatus("ACCEPTED");
            return ResponseEntity.accepted().body(accepted);
        } catch (IOException e) {
            log.error("Error saving file", e);
            throw new InternalServerErrorException().withMessage("Saving payment order failed");
        }
    }

    private String makePaymentOrderFileName(String bankReferenceId) {
        return bankReferenceId.concat(JSON_EXTENSION);
    }
}
