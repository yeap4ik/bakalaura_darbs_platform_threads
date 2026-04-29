package com.banking.api.bakalaura_darbs_platform_threads.controller;

import com.banking.api.bakalaura_darbs_platform_threads.dto.payment.CreatePaymentRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.payment.PaymentResponse;
import com.banking.api.bakalaura_darbs_platform_threads.service.PaymentService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse processPayment(@RequestBody CreatePaymentRequest request) {
        log.info("Processing POST, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return paymentService.createPayment(request);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable UUID id) {
        log.info("Processing light GET, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/search")
    public Page<PaymentResponse> searchPayments(Pageable pageable) {
        log.info("Processing heavy search, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return paymentService.search(pageable);
    }
}
