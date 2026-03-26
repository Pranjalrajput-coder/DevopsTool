package com.example.logs.ai.DevopsTool.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_table")
@Data
@Setter
@Getter
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String logSuggestion;

    @Column(columnDefinition = "vector")
    private float[] embedding;

    private LocalDateTime createdAt;
}
