package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.Filedata;
import com.miniplm.entity.Form;

public interface FiledataRepository extends JpaRepository<Filedata, Long> {
	Filedata findByStorageUuid(String uuid);
	List<Filedata> findByForm(Form form);
	List<Filedata> findByFormAndDataIndex(Form form, String dataIndex);
//	void deleteByI(String uuid);
	
	@Modifying
	@Query(value = "UPDATE MP_FILEDATA SET ENABLED = 0 where STORAGE_UUID = :uuid " ,nativeQuery = true)
	void deleteByStorageUuid(String uuid);
	
//	@Modifying
//	@Query(value = "UPDATE MP_FILEDATA SET ENABLED = 0 where STORAGE_UUID = :uuid " ,nativeQuery = true)
//	void deleteByStorageUuid(@Param("uuid") String uuid);
}
