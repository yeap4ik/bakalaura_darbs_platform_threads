package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "document_type", nullable = false, length = 100)
    private String documentType;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @CreationTimestamp
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "verification_status", nullable = false, length = 50)
    private String verificationStatus;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "notes", length = 1000)
    private String notes;
}
