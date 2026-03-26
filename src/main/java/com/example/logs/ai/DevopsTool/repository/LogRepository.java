package com.example.logs.ai.DevopsTool.repository;

import com.example.logs.ai.DevopsTool.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {


    @Query(value = """
                SELECT * FROM logs_table
                ORDER BY CAST(embedding AS vector) <-> CAST(:embedding AS vector)
                LIMIT 3
                """, nativeQuery = true)
    List<LogEntity> findSimilarLogs(@Param("embedding") float[] embedding);
}
