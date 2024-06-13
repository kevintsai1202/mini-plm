package com.miniplm.repository;

import com.miniplm.entity.ZAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZAccountRepository extends JpaRepository<ZAccount, String> {

  @Query(
      value =
          "SELECT RAWTOHEX(DBMS_OBFUSCATION_TOOLKIT.md5 (input => UTL_I18N.string_to_raw (:pass))) RP FROM DUAL",
      nativeQuery = true)
  String md5Hash(@Param("pass") String pass);

  @Query(
      value =
          "SELECT ACCOUNT_ID, ACCOUNT_NAME, RAWTOHEX(PASSWORD) PASSWORD, ALIAS_NAME, CREATE_DATE, "
              + "CREATOR, ACCOUNT_TYPE, DEPT_ID, BU FROM Z_ACCOUNT "
              + "WHERE ACCOUNT_TYPE = 'USER' ORDER BY ACCOUNT_NAME",
      nativeQuery = true)
  List<ZAccount> getPDMUserList();

  @Query(
      value =
          "SELECT ACCOUNT_ID, ACCOUNT_NAME, RAWTOHEX(PASSWORD) PASSWORD, ALIAS_NAME, CREATE_DATE, "
              + "CREATOR, ACCOUNT_TYPE, DEPT_ID, BU FROM Z_ACCOUNT "
              + "WHERE ACCOUNT_TYPE = 'USER'"
              + " AND (ACCOUNT_NAME LIKE %:FILTER% OR ALIAS_NAME LIKE %:FILTER%) "
              + "ORDER BY ACCOUNT_NAME",
      nativeQuery = true)
  List<ZAccount> getFilteredPDMUserList(@Param("FILTER") String filter);

  @Query(
      value =
          "SELECT ACCOUNT_ID, ACCOUNT_NAME, RAWTOHEX(PASSWORD) PASSWORD, ALIAS_NAME, CREATE_DATE, "
              + "CREATOR, ACCOUNT_TYPE, DEPT_ID, BU FROM Z_ACCOUNT "
              + "WHERE ACCOUNT_NAME = :LoginID",
      nativeQuery = true)
  ZAccount findByLoginId(@Param("LoginID") String id);
}
