package com.miniplm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.FormHistory;

public interface FormHistoryRepository extends JpaRepository<FormHistory, Long> {
}
