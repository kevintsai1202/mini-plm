package com.miniplm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ConfigListItem;

public interface ConfigListItemRepository extends JpaRepository<ConfigListItem, Long> {
}
