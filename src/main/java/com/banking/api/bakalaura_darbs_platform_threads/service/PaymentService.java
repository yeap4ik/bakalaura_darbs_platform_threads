package com.banking.api.bakalaura_darbs_platform_threads.service;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao.CustomPaymentDao;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Account;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository.AccountRepository;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.BankTransferRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.BankTransferResponse;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.FraudCheckRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.FraudCheckResponse;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.LoyaltyRewardRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.LoyaltyRewardResponse;
import com.banking.api.bakalaura_darbs_platform_threads.dto.payment.CreatePaymentRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.payment.PaymentResponse;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Payment;
import com.banking.api.bakalaura_darbs_platform_threads.exception.BadRequestException;
import com.banking.api.bakalaura_darbs_platform_threads.exception.ExternalServiceException;
import com.banking.api.bakalaura_darbs_platform_threads.exception.FraudDetectedException;
import com.banking.api.bakalaura_darbs_platform_threads.service.integration.BankServiceClient;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository.PaymentRepository;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import com.banking.api.bakalaura_darbs_platform_threads.service.integration.FraudServiceClient;
import com.banking.api.bakalaura_darbs_platform_threads.service.integration.LoyaltyServiceClient;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private static final String BANK_STATUS_APPROVED = "APPROVED";
    private static final String LOYALTY_STATUS_SUCCESS = "SUCCESS";

    private final PaymentRepository paymentRepository;
    private final BankServiceClient bankServiceClient;
    private final FraudServiceClient fraudServiceClient;
    private final LoyaltyServiceClient loyaltyServiceClient;
    private final Executor paymentParallelExecutor;
    private final CustomPaymentDao customPaymentDao;
    private final AccountRepository accountRepository;
    private final PaymentDbService paymentDbService;

    public PaymentService(
            PaymentRepository paymentRepository,
            BankServiceClient bankServiceClient,
            FraudServiceClient fraudServiceClient,
            LoyaltyServiceClient loyaltyServiceClient,
            @Qualifier("paymentParallelExecutor") Executor paymentParallelExecutor,
            CustomPaymentDao customPaymentDao,
            AccountRepository accountRepository,
            PaymentDbService paymentDbService) {
        this.paymentRepository = paymentRepository;
        this.bankServiceClient = bankServiceClient;
        this.fraudServiceClient = fraudServiceClient;
        this.loyaltyServiceClient = loyaltyServiceClient;
        this.customPaymentDao = customPaymentDao;
        this.accountRepository = accountRepository;
        this.paymentParallelExecutor = paymentParallelExecutor;
        this.paymentDbService = paymentDbService;
    }

    private void callAndValidateFraud(Long userId, BigDecimal amount, UUID paymentId) {
        FraudCheckResponse fraudResponse = fraudServiceClient.checkFraud(new FraudCheckRequest(userId, amount));

        if (validateFraundResponse(fraudResponse)){
            throw new FraudDetectedException(MessageFormatter.format("Payment with ID {} rejected by fraud service for user with such userID: {}", paymentId, userId).getMessage());
        }
    }

    private Boolean validateFraundResponse(FraudCheckResponse fraudResponse){
        return Boolean.TRUE.equals(fraudResponse.isFraud());
    }

    @Transactional
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = customPaymentDao.findPaymentById(id);
        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> search(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(this::toResponse);
    }


    public PaymentResponse createPayment(CreatePaymentRequest request) {
        validateCreatePaymentRequest(request);
        Account senderAccount = accountRepository.findAccountById(request.senderAccountId());
        Account receiverAccount = accountRepository.findAccountById(request.receiverAccountId());
        Payment payment = paymentDbService.createPendingPayment(request, senderAccount, receiverAccount);
        //Step 1, fraud call for sender
        try {
            callAndValidateFraud(senderAccount.getUser().getId(), payment.getAmount(), payment.getId());
        } catch (FraudDetectedException senderFraudException){
            paymentDbService.rejectPayment(payment.getId());
            throw senderFraudException;
        }

        //Step 2, fraud call for reciever
        try {
            callAndValidateFraud(receiverAccount.getUser().getId(), payment.getAmount(), payment.getId());
        } catch (FraudDetectedException receiverFraudException) {
            paymentDbService.rejectPayment(payment.getId());
            throw receiverFraudException;
        }

        try {
            CompletableFuture<BankTransferResponse> bankFuture = CompletableFuture.supplyAsync(
                    () -> bankServiceClient.transfer(
                            new BankTransferRequest(payment.getSenderAccount(), payment.getReceiverAccount(), payment.getAmount(), payment.getId())
                    ),
                    paymentParallelExecutor
            );

            CompletableFuture<LoyaltyRewardResponse> loyaltyFuture = CompletableFuture.supplyAsync(
                    () -> loyaltyServiceClient.reward(
                            new LoyaltyRewardRequest(payment.getSender(), payment.getAmount())
                    ),
                    paymentParallelExecutor
            );

            CompletableFuture.allOf(bankFuture, loyaltyFuture).join();

            BankTransferResponse bankResponse = bankFuture.join();
            LoyaltyRewardResponse loyaltyResponse = loyaltyFuture.join();

            ensureExternalResponsesAreSuccessful(bankResponse, loyaltyResponse);
        } catch (CompletionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new ExternalServiceException("Parallel external calls failed", ex);
        }

        Payment updated = paymentDbService.markPaymentSuccess(payment.getId());
        return toResponse(updated);
    }

    protected Payment createPendingPayment(CreatePaymentRequest request, Account senderAccount, Account recieverAccount) {
        Payment payment = Payment.createPending(senderAccount, recieverAccount, request);
        return paymentRepository.save(payment);
    }

    private void validateCreatePaymentRequest(CreatePaymentRequest request) {
        if (request == null) {
            throw new BadRequestException("Request body is required");
        }
        if (request.senderAccountId() == null || request.receiverAccountId() <= 0) {
            throw new BadRequestException("userId must be a positive number");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("amount must be greater than zero");
        }
    }

    private void ensureExternalResponsesAreSuccessful(
            BankTransferResponse bankResponse,
            LoyaltyRewardResponse loyaltyResponse
    ) {
        if (!BANK_STATUS_APPROVED.equalsIgnoreCase(bankResponse.status())) {
            throw new ExternalServiceException("Bank transfer was not approved");
        }
        if (!LOYALTY_STATUS_SUCCESS.equalsIgnoreCase(loyaltyResponse.status())) {
            throw new ExternalServiceException("Loyalty reward did not complete successfully");
        }
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getSender().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}
