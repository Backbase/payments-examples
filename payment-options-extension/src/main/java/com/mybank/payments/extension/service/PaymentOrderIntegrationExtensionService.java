package com.mybank.payments.extension.service;

import static com.mybank.payments.extension.util.MathUtil.calculatePercentage;

import com.mybank.payments.integration.extension.api.model.Currency;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsEvaluateRequest;
import com.mybank.payments.integration.extension.api.model.PaymentOptionsRequest;
import com.mybank.payments.integration.extension.api.model.PaymentTypeInformation;
import com.mybank.payments.integration.extension.api.model.PaymentTypeOption;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderIntegrationExtensionService {

    public static final String INTERNATIONAL_TRANSFER = "INTERNATIONAL_TRANSFER";
    public static final String CUT_OFF_TIME = "15:00";
    public static final int TRANSFER_FEE_PERCENTAGE = 2;

    /**
     * This method enriches the post options request payment type information.
     * <p>
     * This method enhances the cut-off date and transfer fee details for international payments. The new cut-off time
     * and transfer fee percentage is hardcoded in this file. In real implementation, for example, cut-off date can be
     * taken based on the calendar and also transfer fee percentage is taken for the particular user account privilege.
     *
     * @param evaluateRequest contains all request and response models from post options request.
     */
    public void enrichPostOptions(PaymentOptionsEvaluateRequest evaluateRequest) {
        List<PaymentTypeInformation> paymentTypes = evaluateRequest.getPaymentTypes();
        PaymentOptionsRequest optionsRequest = evaluateRequest.getPaymentOptionsRequest();

        if (paymentTypes.isEmpty() || optionsRequest == null) {
            return;
        }

        for (var paymentType : paymentTypes) {
            if (INTERNATIONAL_TRANSFER.equals(paymentType.getPaymentType())) {
                enrichCutOffDate(paymentType);
                enrichTransferFee(paymentType, optionsRequest.getInstructedAmount());
            }
        }
    }

    /**
     * This method enriches the get options by payment type request payment type information.
     * <p>
     * This method enhances the cut-off date for international payments.
     *
     * @param paymentType the payment type information which needs enrichment.
     */
    public void enrichGetPaymentOptionsByPaymentType(PaymentTypeInformation paymentType) {
        if (paymentType == null) {
            return;
        }
        if (INTERNATIONAL_TRANSFER.equals(paymentType.getPaymentType())) {
            enrichCutOffDate(paymentType);
        }
    }

    private void enrichCutOffDate(PaymentTypeInformation paymentType) {
        for (PaymentTypeOption paymentTypeOption : paymentType.getOptions()) {
            paymentTypeOption.setCutOffTime(CUT_OFF_TIME);
        }
    }

    private void enrichTransferFee(PaymentTypeInformation paymentType, Currency instructedAmount) {
        if (instructedAmount == null) {
            return;
        }
        var transferFee = calculatePercentage(new BigDecimal(instructedAmount.getAmount()), TRANSFER_FEE_PERCENTAGE);
        for (PaymentTypeOption paymentTypeOption : paymentType.getOptions()) {
            paymentTypeOption.getTransferFee().amount(transferFee);
        }
    }
}
