package com.miniplm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.FormData;

public interface FormDataRepository extends JpaRepository<FormData, Long> {
}
