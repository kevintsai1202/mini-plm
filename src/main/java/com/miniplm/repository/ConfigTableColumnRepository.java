package com.miniplm.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.ConfigTableColumn;
import com.miniplm.entity.ConfigTableHeader;

public interface ConfigTableColumnRepository extends JpaRepository<ConfigTableColumn, Long> {
	public List<ConfigTableColumn> findByConfigTableHeaderAndVisible(ConfigTableHeader cTableHeader, Boolean isVisible, Sort sort);
	public List<ConfigTableColumn> findByConfigTableHeader(ConfigTableHeader cTableHeader, Sort sort);
//	@Query(value = "select * from MP_CONFIG_TABLE_COLUMN where ENABLED = 1 and TABLE_HEADER_ID = :tableId and FIELD_INDEX = :fieldIndex order by ORDER_BY" ,nativeQuery = true)
//	public ConfigTableColumn findByTableHeaderIdAndFieldIndex(@Param("tableId") Long tableId, @Param("fieldIndex") String fieldIndex);
}
