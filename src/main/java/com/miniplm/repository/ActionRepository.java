package com.miniplm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.entity.Form;
import com.miniplm.entity.ZAccount;
import com.miniplm.entity.Action;
import com.miniplm.entity.ConfigStep;

public interface ActionRepository extends JpaRepository<Action, Long> {

//	@Query(value = "select * from MP_ACTION where type = 'A' and user_id = ?1 and finish_flag != 1", nativeQuery = true)
	@Query(value = "select * from MP_ACTION A where type IN ('A','R') and user_id = ?1 and finish_flag != 1 and config_step_id IN (SELECT curr_step_id FROM MP_FORM WHERE ID = a.form_id) and enabled = 1"
			+ " union "
			+ "select * from MP_ACTION A where type IN ('A','R') and user_id in (select TA_ID from Z_ACCOUNT WHERE ACCOUNT_ID = ?1) and finish_flag != 1 and config_step_id IN (SELECT curr_step_id FROM MP_FORM WHERE ID = a.form_id) and enabled = 1",nativeQuery = true) 
	public List<Action> findByApprover(String userId);

	@Query(value = "select count(*) from MP_ACTION where type = 'A' and form_id = ?1 and config_step_id = ?2 and finish_flag != 1 and enabled = 1" , nativeQuery = true)
	public int countApproversByFormIdAndStepId(Long formId, Long stepId);
	
	@Query(value = "select * from MP_ACTION where type IN ('A','R') and form_id = ?1 and config_step_id = ?2 and user_id = ?3 and finish_flag != 1  and enabled = 1"
			+ " union "
			+ "select * from MP_ACTION where type IN ('A','R') and form_id = ?1 and config_step_id = ?2 and user_id in (select TA_ID from Z_ACCOUNT WHERE ACCOUNT_ID = ?3) and finish_flag != 1  and enabled = 1" , nativeQuery = true)
	public List<Action> FindMyActionsByFormIdAndStepId(Long formId, Long stepId, String userId);
	
	public List<Action> findByTypeAndFormAndConfigStepAndFinishFlag(String type, Form form, ConfigStep step, Boolean finishFlag);
	public List<Action> findByTypeAndFormAndConfigStepAndUserAndFinishFlag(String type, Form form, ConfigStep step,ZAccount user, Boolean finishFlag);
	
	@Query(value = "select count(*) from MP_ACTION where form_id = ?1 and finish_flag != 1 and enabled = 1" , nativeQuery = true)
	public int countAllUnfinishActions(Long formId);
	
	@Query(value = "select * from MP_ACTION where form_id = ?1 and enabled = 1 order by id desc", nativeQuery = true)
	public List<Action> findByFormId(Long formId);
	
	@Query(value = "select * from MP_ACTION where form_id = ?1 and type = ?2 and enabled = 1 order by id desc", nativeQuery = true)
	public List<Action> findByFormIdAndType(Long formId, String type);
	
	@Modifying
	@Query(value = "update MP_ACTION set FINISH_FLAG = 1 where FINISH_FLAG = 0 and form_id = ?1 and enabled = 1" , nativeQuery = true)
	public void updateAllUnfinishActions(Long formId);
	
	@Modifying
	@Query(value = "update MP_ACTION set FINISH_FLAG = 1 where FINISH_FLAG = 0 and form_id = ?1 and config_step_id = ?2 and enabled = 1" , nativeQuery = true)
	public void updateCurrUnfinishActions(Long formId, Long configStepId);
	
	@Modifying
	@Query(value = "update MP_ACTION set CAN_NOTICE_FLAG = 1 where FINISH_FLAG = 0 and CAN_NOTICE_FLAG = 0 and form_id = ?1 and config_step_id = ?2 and enabled = 1" , nativeQuery = true)
	public void updateCurrCanNoticeActions(Long formId, Long configStepId);
	
	public List<Action> findByTypeAndNoticeFlagAndCanNoticeFlag(String type, Boolean noticeFlag, Boolean canNoticeFlag);
//	@Query(value = "select * from MP_ACTION where type = 'A' and finish_flag != 1 and user_id = ?1  and form_id = ?2 and config_step_id = ?3", nativeQuery = true)
//	public List<Action> findApprovalAction(Long userId, Long formId, Long currStepId);
}
