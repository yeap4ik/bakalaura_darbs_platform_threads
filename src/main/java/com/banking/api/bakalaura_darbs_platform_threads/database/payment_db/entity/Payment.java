package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity;

import com.banking.api.bakalaura_darbs_platform_threads.dto.payment.CreatePaymentRequest;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_account_id", nullable = false)
    private Account receiverAccount;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    // (EUR, USD, GBP, etc.)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private PaymentStatus status;

    @Column(name = "bank_transaction_id", length = 100)
    private String bankTransactionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // To ensure there are no duplicates
    @Column(name = "deduplicate_key", length = 36, unique = true, nullable = false)
    private String deduplicateKey;

    // Metadata, for ex: "{"device": "iOS", "ip": "192.168.1.1"}")
    // tipe: TEXT
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "receiver_name", length = 100, nullable = false)
    private String receiverName;

    @Column(name = "receiver_bic", length = 11, nullable = false)
    private String receiverBic;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = 20, nullable = false)
    private PaymentType paymentType;

    @Column(name = "fee_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal feeAmount;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Payment createPending(
            Account senderAccount,
            Account receiverAccount, // if receiver is other bank can be null
            CreatePaymentRequest request
    ) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setSender(senderAccount.getUser());
        payment.setSenderAccount(senderAccount);
        payment.setReceiverAccount(receiverAccount);
        payment.setAmount(request.amount());
        payment.setCurrency(request.currency());
        payment.setDescription(request.description());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setDeduplicateKey(request.deduplicationKey());
        payment.setReceiverName(request.receiverName());
        payment.setReceiverBic(request.receiverBic());
        payment.setPaymentType(request.paymentType());
        payment.setFeeAmount(request.amount() != null ? request.amount() : BigDecimal.ZERO);
        payment.setMetadata(request.metadata());
        return payment;
    }
}
