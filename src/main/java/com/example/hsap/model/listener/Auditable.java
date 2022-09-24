package com.example.hsap.model.listener;

import java.time.LocalDateTime;

public interface Auditable {
        void setCreatedAt(LocalDateTime createdAt);
        void setUpdatedAt(LocalDateTime updatedAt);
        LocalDateTime getCreatedAt();
        LocalDateTime getUpdatedAt();
}
