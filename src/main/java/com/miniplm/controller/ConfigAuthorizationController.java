package com.miniplm.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.convert.PermissionConverter;
import com.miniplm.convert.PrivilegeConverter;
import com.miniplm.convert.RoleConverter;
import com.miniplm.entity.ConfigCriteriaItem;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigFormType;
import com.miniplm.entity.Menu;
import com.miniplm.entity.Permission;
import com.miniplm.entity.Privilege;
import com.miniplm.entity.PrivilegeEnum;
import com.miniplm.entity.Role;
import com.miniplm.entity.UriEntity;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.PermissionRepository;
import com.miniplm.repository.PrivilegeRepository;
import com.miniplm.repository.RoleRepository;
import com.miniplm.request.ConfigCriteriaNodeRequest;
import com.miniplm.request.PermissionRequest;
import com.miniplm.request.PrivilegeRequest;
import com.miniplm.request.RoleRequest;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "授權" , description = "維護授權的API")
@RestController
@RequestMapping({"/api/v1/admin/config/authorization","/api/v1/config/authorization"})
@CrossOrigin
public class ConfigAuthorizationController {
	
	@Autowired
	AuthorizationService authorizationService;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PermissionRepository permissionRepository;
	
	@Autowired
	PrivilegeRepository privilegeRepository;
	
	@GetMapping("/roles")
	public ResponseEntity<TableResultResponse<Role>> getAllRoles() {
		return ResponseEntity.ok(new TableResultResponse<Role>(authorizationService.listAllRoles()));
	}
	
	@GetMapping("/permissions")
	public ResponseEntity<TableResultResponse<Permission>> getAllPermissions() {
		return ResponseEntity.ok(new TableResultResponse<Permission>(authorizationService.listAllPermissions()));
	}
	
	@GetMapping("/roles/{rId}/permissions")
	public ResponseEntity<TableResultResponse<Permission>> getAllPermissionsByRoleId(@PathVariable("rId") Long rId) {
		return ResponseEntity.ok(new TableResultResponse<Permission>(authorizationService.listAllPermissionsByRoleId(rId)));
	}
	
	@GetMapping("/roles/{rId}/privileges")
	public ResponseEntity<TableResultResponse<Privilege>> getAllPrivilegesByRoleId(@PathVariable("rId") Long rId) {
		return ResponseEntity.ok(new TableResultResponse<Privilege>(authorizationService.listAllPrivilegesByRoleId(rId)));
	}
	
	@GetMapping("/roles/{rId}/users")
	public ResponseEntity<TableResultResponse<ZAccount>> getAllUsersByRoleId(@PathVariable("rId") Long rId) {
		return ResponseEntity.ok(new TableResultResponse<ZAccount>(authorizationService.listAllUsersByRoleId(rId)));
	}
	
	@GetMapping("/roles/{rId}/folders")
	public ResponseEntity<TableResultResponse<Menu>> getAllFoldersByRoleId(@PathVariable("rId") Long rId) {
		return ResponseEntity.ok(new TableResultResponse<Menu>(authorizationService.listAllFoldersByRoleId(rId)));
	}
	
	@GetMapping("/privileges")
	public ResponseEntity<TableResultResponse> getAllPrivileges() {
		return ResponseEntity.ok(new TableResultResponse(authorizationService.listAllPrivileges()));
	}
	
	
	@GetMapping("/roles/{rId}")
	public ResponseEntity<Role> getRole(@PathVariable("rId") Long rId) {
		return ResponseEntity.ok(authorizationService.getRoleById(rId));
	}
	
	@GetMapping("/permissions/{pId}")
	public ResponseEntity<Permission> getPermission(@PathVariable("pId") Long pId) {
		return ResponseEntity.ok(authorizationService.getPermissionById(pId));
	}
	
	@GetMapping("/privileges/{pId}")
	public ResponseEntity<Privilege> getAllPrivilege(@PathVariable("pId") Long pId) {
		return ResponseEntity.ok(authorizationService.getPrivilegeById(pId));
	}
	
	@PostMapping("/roles")
	public ResponseEntity<Role> createRole(@RequestBody RoleRequest roleRequest) {
		Role role = RoleConverter.INSTANCT.requestToEntity(roleRequest);
		return ResponseEntity.ok(authorizationService.createRole(role));
	}
	
	@PostMapping("/permissions")
	public ResponseEntity<Permission> createPermission(@RequestBody PermissionRequest permissionRequest) {
		Permission permission = PermissionConverter.INSTANCT.requestToEntity(permissionRequest);
		return ResponseEntity.ok(authorizationService.createPermission(permission));
	}
	
	@PostMapping("/privileges")
	public ResponseEntity<Privilege> createPrivilege(@RequestBody PrivilegeRequest privilegeRequest) {
		Privilege privilege = PrivilegeConverter.INSTANCT.requestToEntity(privilegeRequest);
		return ResponseEntity.ok(authorizationService.createPrivilege(privilege));
	}
	
	@PostMapping("/privileges/{sourceId}/saveas")
	@Operation(summary = "複製Privilege",
               description = "複製Privilege")
	public ResponseEntity<Privilege> saveAsPrivilege(@PathVariable Long sourceId, @RequestBody @Validated PrivilegeRequest privilegeRequest){
//		List<ConfigCriteriaItem> targetCriteriaItems = new ArrayList<>();
		Privilege source = privilegeRepository.getReferenceById(sourceId);
//		List<ConfigCriteriaItem> sourceCriteriaItems = source.getCriteriaItems();
		
//		ConfigFormType formType = configFormTypeRepository.getReferenceById(privilegeRequest. .getCfId());
		
		Privilege privilege = Privilege.builder()
				.privilegeName(privilegeRequest.getPrivilegeName())
				.fields(privilegeRequest.getFields().stream().collect(Collectors.toList()))
				.objId(privilegeRequest.getObjId())
				.privilege(PrivilegeEnum.valueOf(privilegeRequest.getPrivilege()))
				.build();
		
		Privilege savedPrivilege = privilegeRepository.save(privilege);
		
		return ResponseEntity.ok(savedPrivilege);
	}
	
	@PutMapping("/roles/{rId}/{newRoleName}")
	public ResponseEntity<Role> updateRole(@PathVariable("rId") Long rId, @PathVariable("newRoleName") String newRoleName) {
		return ResponseEntity.ok(authorizationService.updateRoleName(rId, newRoleName));
	}
	
	@PutMapping("/permissions")
	public ResponseEntity<Permission> updatePermissionDetails(@RequestBody Permission permission) {
			return ResponseEntity.ok(authorizationService.updatePermissionDetails(permission));
	}
	
	@PutMapping("/privileges")
	public ResponseEntity<Privilege> updatePrivilegeDetails(@RequestBody Privilege privilege) {
			return ResponseEntity.ok(authorizationService.updatePrivilegeDetails(privilege));
	}
	
	@PutMapping("/users/{uId}/updateroles")
	public ResponseEntity<Set<Role>> updateUserRoles(@PathVariable("uId") String uId, @RequestBody Set<Long> roleIds) {
		return ResponseEntity.ok(authorizationService.updateUserRoles(uId, roleIds));
	}
	
	@PutMapping("/roles/{rId}/updatepermissions")
	public ResponseEntity<Set<Permission>> updateRolePermissions(@PathVariable("rId") Long rId, @RequestBody Set<Long> permissionIds) {
		return ResponseEntity.ok(authorizationService.updateRolePermissions(rId, permissionIds));
	}
	
	@PutMapping("/roles/{rId}/updateprivileges")
	public ResponseEntity<Set<Privilege>> updateRolePrivileges(@PathVariable("rId") Long rId, @RequestBody Set<Long> privilegeIds) {
		return ResponseEntity.ok(authorizationService.updateRolePrivileges(rId, privilegeIds));
	}
	
	@PutMapping("/roles/{rId}/updatefolders")
	public ResponseEntity<Set<Menu>> updateRoleFolders(@PathVariable("rId") Long rId, @RequestBody Set<Long> folderIds) {
		return ResponseEntity.ok(authorizationService.updateRoleFolders(rId, folderIds));
	}
	
	@PutMapping("/roles/{rId}/updateusers")
	public ResponseEntity<Set<ZAccount>> updateRoleUsers(@PathVariable("rId") Long rId, @RequestBody Set<String> userIds) {
		return ResponseEntity.ok(authorizationService.updateRoleUsers(rId, userIds));
	}
	
	@PutMapping("/users/{uId}/addroles")
	public ResponseEntity<Set<Role>> addUserRoles(@PathVariable("uId") String uId, @RequestBody Set<Long> roleIds) {
		return ResponseEntity.ok(authorizationService.addUserRoles(uId, roleIds));
	}
	
	
	@PutMapping("/user/{uId}/removeroles")
	public ResponseEntity<Set<Role>> removeUserRoles(@PathVariable("uId") String uId, @RequestBody Set<Long> roleIds) {
		return ResponseEntity.ok(authorizationService.removeUserRoles(uId, roleIds));
	}
	
	@PutMapping("/roles/{rId}/removepermissions")
	public ResponseEntity<Set<Permission>> removeRolePermissions(@PathVariable("rId") Long rId, @RequestBody Set<Long> permissionIds) {
		return ResponseEntity.ok(authorizationService.removeRolePermissions(rId, permissionIds));
	}
	
	@PutMapping("/roles/{rId}/removeprivileges")
	public ResponseEntity<Set<Privilege>> removeRolePrivileges(@PathVariable("rId") Long rId, @RequestBody Set<Long> privilegeIds) {
			return ResponseEntity.ok(authorizationService.removeRolePrivileges(rId, privilegeIds));
	}
	
	@GetMapping("/me/urlpatterns")
	public ResponseEntity<Set<String>> getMyPermission(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(authorizationService.getUserUrlPatterns(username));
	}
	
	@GetMapping("/me/privileges")
	public ResponseEntity<Set<Privilege>> getMyPrivilege(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(authorizationService.getUserPrivileges(username));
	}
	
	@GetMapping("/me/menus")
	public ResponseEntity<Set<Menu>> getMyMenu(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(authorizationService.getUserMenus(username));
	}
	
	@GetMapping("/uris")
	public ResponseEntity<Set<UriEntity>> getAllUri(){
		return ResponseEntity.ok(authorizationService.getAllUri());
	}

}
