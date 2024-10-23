package com.miniplm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.ConfigTableHeader;

public interface ConfigTableHeaderRepository extends JpaRepository<ConfigTableHeader, Long> {
}
