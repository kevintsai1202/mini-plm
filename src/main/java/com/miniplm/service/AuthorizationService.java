package com.miniplm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.miniplm.entity.Menu;
import com.miniplm.entity.Permission;
import com.miniplm.entity.Privilege;
import com.miniplm.entity.PrivilegeEnum;
import com.miniplm.entity.Role;
import com.miniplm.entity.UriEntity;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.MenuRepository;
import com.miniplm.repository.PermissionRepository;
import com.miniplm.repository.PrivilegeRepository;
import com.miniplm.repository.RoleRepository;
import com.miniplm.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
//	@Autowired
    private final UserRepository userRepository;
	
//	@Autowired
    private final RoleRepository roleRepository;
	
//	@Autowired
    private final PermissionRepository permissionRepository;
	
//	@Autowired
	private final PrivilegeRepository	privilegeRepository;
	
//	@Autowired
	private final MenuRepository menuRepository;
	
//	@Autowired
	private final UserService userService;
	
//	@Autowired
	private final RequestMappingHandlerMapping uriMapping;
		
	
	public Role getRoleById(Long rId){
		return roleRepository.getReferenceById(rId); 		
	}
	
	public Permission getPermissionById(Long pId){
		return permissionRepository.getReferenceById(pId); 		
	}
	
	public Privilege getPrivilegeById(Long pId){
		return privilegeRepository.getReferenceById(pId); 		
	}
	
	@Transactional
	public Set<UriEntity> getAllUri() {
		Map<RequestMappingInfo, HandlerMethod> map = uriMapping.getHandlerMethods();
		Set<UriEntity> set = new HashSet<>();
		int count=0;
		for (Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
//			System.out.println("map:"+m);
			
			RequestMappingInfo info = m.getKey();  
//            HandlerMethod method = m.getValue();
//            System.out.println("getMethodsCondition"+info.getMethodsCondition());
//            System.out.println("getPatternValues"+info.getPatternValues());
//			Set<RequestMethod> requestMethods= info.getMethodsCondition().getMethods();
//			String httpMethod = requestMethods.toArray()[0].toString();
//			String httpMethod = requestMethod.toString();
			Set<String> uris = info.getPatternValues();
			Object[] o = uris.toArray();
            for (int i = 0; i < o.length ; i++) {
            	set.add(UriEntity.builder()
            	.index(count++)
            	.name(info.getName())
            	.uri(o[i].toString())
            	.httpMethod(info.getMethodsCondition().toString())
            	.build());
            }
//			for (String uri : uris) {
//            	map1.put(info.getMethodsCondition().toString() , uri);
//            }
//            set.add(map1);
		}
		return set;
	}
	
	@Transactional
	public List<Role> listAllRoles(){
		return roleRepository.findAll(); 		
	}
	
	@Transactional
	public List<Permission> listAllPermissions(){
		return permissionRepository.findAll(); 		
	}
	
	@Transactional
	public List<Permission> listAllPermissionsByRoleId(Long rId){
		Role role = roleRepository.getReferenceById(rId);
		Set<Permission> permissionsSet =role.getPermissions();
		return new ArrayList<Permission>(permissionsSet);	
	}
	
	@Transactional
	public List<Privilege> listAllPrivilegesByRoleId(Long rId){
		Role role = roleRepository.getReferenceById(rId);
		Set<Privilege> privilegesSet =role.getPrivileges();
		return new ArrayList<Privilege>(privilegesSet);	
	}
	
	@Transactional
	public List<Menu> listAllFoldersByRoleId(Long rId){
		Role role = roleRepository.getReferenceById(rId);
		Set<Menu> foldersSet =role.getFolders();
		return new ArrayList<Menu>(foldersSet);	
	}
	
	@Transactional
	public List<ZAccount> listAllUsersByRoleId(Long rId){
		Role role = roleRepository.getReferenceById(rId);
		Set<ZAccount> usersSet = role.getUsers();
		return new ArrayList<ZAccount>(usersSet);	
	}
	
	@Transactional
	public List<Privilege> listAllPrivileges(){
		return privilegeRepository.findAll(); 		
	}
	
	@Transactional
	public Set<Role> addUserRoles(String uId, Set<Long> roleIds) {
		Set<Role> roles = new HashSet<>();
		roleIds.forEach(roleId -> {
			Role role = roleRepository.getReferenceById(roleId);				
			roles.add(role);
		});
		ZAccount user = userRepository.getReferenceById(uId);
		Set<Role> userRoles = user.getRoles();
		userRoles.addAll(roles);
		user.setRoles(userRoles);
		userRepository.save(user);
		return user.getRoles();
	}
	
	@Transactional
	public Set<ZAccount> addRoleUsers(Long rId, Set<String> userIds) {
		Set<ZAccount> users = new HashSet<>();
		userIds.forEach(userId -> {
			ZAccount user = userRepository.getReferenceById(userId);				
			users.add(user);
		});
		Role role = roleRepository.getReferenceById(rId);
		Set<ZAccount> roleUsers = role.getUsers();
		roleUsers.addAll(users);
		role.setUsers(roleUsers);
		roleRepository.save(role);
		return role.getUsers();
	}
	
	@Transactional
	public Set<Role> updateUserRoles(String uId, Set<Long> roleIds) {
		Set<Role> roles = new HashSet<>();
		roleIds.forEach(roleId -> {
			Role role = roleRepository.getReferenceById(roleId);				
			roles.add(role);
		});
		ZAccount user = userRepository.getReferenceById(uId);
		user.setRoles(roles);
		userRepository.save(user);
		return user.getRoles();
	}
	
	@Transactional
	public Set<ZAccount> updateRoleUsers(Long rId, Set<String> userIds) {
		Set<ZAccount> users = new HashSet<>();
		userIds.forEach(uId -> {
			ZAccount user = userRepository.getReferenceById(uId);				
			users.add(user);
		});
		Role role = roleRepository.getReferenceById(rId);
		role.setUsers(users);
		roleRepository.save(role);
		return role.getUsers();
	}
	
	@Transactional
	public Set<Permission> updateRolePermissions(Long roleId, Set<Long> permissionIds) {
		Set<Permission> permissions = new HashSet<>();
		permissionIds.forEach(permissionId -> {
			Permission permission = permissionRepository.getReferenceById(permissionId);				
			permissions.add(permission);
		});
		Role role = roleRepository.getReferenceById(roleId);
//		Set<Permission> rolePermissions = role.getPermissions();
//		rolePermissions.addAll(permissions);
		role.setPermissions(permissions);
		roleRepository.save(role);
		return role.getPermissions();
	}
	
	@Transactional
	public Set<Menu> updateRoleFolders(Long roleId, Set<Long> folderIds) {
		Set<Menu> folders = new HashSet<>();
		folderIds.forEach(folderId -> {
			Menu folder = menuRepository.getReferenceById(folderId);				
			folders.add(folder);
		});
		Role role = roleRepository.getReferenceById(roleId);
//		Set<Permission> rolePermissions = role.getPermissions();
//		rolePermissions.addAll(permissions);
		role.setFolders(folders);
		roleRepository.save(role);
		return role.getFolders();
	}
	
	@Transactional
	public Set<Privilege> updateRolePrivileges(Long roleId, Set<Long> privilegeIds) {
		Set<Privilege> privileges = new HashSet<>();
		privilegeIds.forEach(privilegeId -> {
			Privilege privilege = privilegeRepository.getReferenceById(privilegeId);				
			privileges.add(privilege);
		});
		
		Role role = roleRepository.getReferenceById(roleId);
//		Set<Privilege> rolePrivileges = role.getPrivileges();
//		rolePrivileges.addAll(privileges);
		role.setPrivileges(privileges);
		roleRepository.save(role);
		return role.getPrivileges();
	}
	
	
	@Transactional
	public Role createRole(Role role) {
		return roleRepository.save(role);
	}
	
	@Transactional
	public Permission createPermission(Permission permission) {
		return permissionRepository.save(permission);
	}
	
	@Transactional
	public Privilege createPrivilege(Privilege privilege) {
		return privilegeRepository.save(privilege);
	}
	
	@Transactional
	public Role updateRoleName(Long rId, String name) {
		Role savedRole = roleRepository.getReferenceById(rId);
		savedRole.setRoleName(name);
		return roleRepository.save(savedRole);
	}
	
//	@Transactional
//	public Permission updatePermissionDetails(Long pId, String name, String urlPattern) {
//		Permission savedPermission = permissionRepository.getReferenceById(pId);
//		savedPermission.setPermissionName(name);
//		savedPermission.setUriPattern(urlPattern);
//		return permissionRepository.save(savedPermission);
//	}
	
	@Transactional
	public Permission updatePermissionDetails(Permission permission) {
		Permission savedPermission = permissionRepository.getReferenceById(permission.getPId());
		savedPermission.setPermissionName(permission.getPermissionName());
		savedPermission.setUriPattern(permission.getUriPattern());
		savedPermission.setMethod(permission.getMethod());
		return permissionRepository.save(savedPermission);
	}
	
	@Transactional
	public Privilege updatePrivilegeDetails(Privilege privilege) {
		Privilege savedPrivilege = privilegeRepository.getReferenceById(privilege.getPrivilegeId());
		savedPrivilege.setFields(privilege.getFields());
		savedPrivilege.setTables(privilege.getTables());
		savedPrivilege.setObjId(privilege.getObjId());
		savedPrivilege.setPrivilege(privilege.getPrivilege());
		savedPrivilege.setPrivilegeName(privilege.getPrivilegeName());
		return privilegeRepository.save(savedPrivilege);
	}
	
	@Transactional
	public Set<Role> removeUserRoles(String uId, Set<Long> roleIds) {
		Set<Role> roles = new HashSet<>();
		roleIds.forEach(roleId -> {
			Role role = roleRepository.getReferenceById(roleId);				
			roles.add(role);
		});
		
		ZAccount user = userRepository.getReferenceById(uId);
		Set<Role> userRoles = user.getRoles();
		userRoles.removeAll(roles);
		user.setRoles(userRoles);
		userRepository.save(user);
		
		return user.getRoles();
	}
	
	@Transactional
	public Set<Permission> removeRolePermissions(Long rId, Set<Long> permissionIds) {
		Set<Permission> permissions = new HashSet<>();
		permissionIds.forEach(permissionId -> {
			Permission permission = permissionRepository.getReferenceById(permissionId);				
			permissions.add(permission);
		});
		
		Role role = roleRepository.getReferenceById(rId);
		
		Set<Permission> userPermissions = role.getPermissions();
		userPermissions.removeAll(permissions);
		role.setPermissions(userPermissions);
		roleRepository.save(role);
		return role.getPermissions();
	}
	
	@Transactional
	public Set<Privilege> removeRolePrivileges(Long roleId, Set<Long> privilegeIds) {
		Set<Privilege> privileges = new HashSet<>();
		privilegeIds.forEach(privilegeId -> {
			Privilege privilege = privilegeRepository.getReferenceById(privilegeId);				
			privileges.add(privilege);
		});
		
		Role role = roleRepository.getReferenceById(roleId);
		
		Set<Privilege> rolePrivileges = role.getPrivileges();
		rolePrivileges.removeAll(privileges);
		role.setPrivileges(rolePrivileges);
		roleRepository.save(role);
		
		return role.getPrivileges();
	}
	
	@Transactional
	public Set<String> getUserUrlPatterns(String username) {
		Set<Permission> allPermissions = new HashSet<>();
		Set<String> urlPatterns = new HashSet<>();
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		Set<Role> roles = user.getRoles();
		roles.forEach(role -> {
			Set<Permission> permissions = role.getPermissions();
			allPermissions.addAll(permissions);
		});
		
		urlPatterns = allPermissions.stream().map(permission -> permission.getUriPattern()).collect(Collectors.toSet());
		return urlPatterns;
	}

	@Transactional
	public Set<Menu> getUserMenus(String username) {
		Set<Menu> allMenus = new TreeSet<>();
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		Set<Role> roles = user.getRoles();
//		System.out.println(roles.size());
		roles.forEach(role -> {
			Set<Menu> menus = role.getFolders();
			allMenus.addAll(menus);
		});
//		System.out.println(allMenus);
		return allMenus;
	}
	
	@Transactional
	public Set<Privilege> getUserPrivileges(String username) {
		Set<Privilege> allPrivileges = new HashSet<>();
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		Set<Role> roles = user.getRoles();
//		System.out.println(roles.size());
		roles.forEach(role -> {
			Set<Privilege> privileges = role.getPrivileges();
			allPrivileges.addAll(privileges);
		});
//		System.out.println(allMenus);
		return allPrivileges;
	}
	
	@Transactional
	public Set<Privilege> getUserPrivileges(String username, Long formTypeId, PrivilegeEnum privilegeType) {
		Set<Privilege> allPrivileges = new HashSet<>();
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		Set<Role> roles = user.getRoles();
//		System.out.println(roles.size());
		roles.forEach(role -> {
			Set<Privilege> privileges = role.getPrivileges();
			privileges.forEach( privilege -> {
//				System.out.println("privilege:"+privilege);
				if (privilege.getObjId().equals(formTypeId) && privilege.getPrivilege().equals(privilegeType))
					allPrivileges.add(privilege);
			});
		});
//		System.out.println(allMenus);
		return allPrivileges;
	}
	
	@Transactional
	public boolean matchMyModifyFields(Long formTypeId, String dataIndex) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Set<String> modifyFields = getUserModifyFields(username, formTypeId);
		return modifyFields.contains(dataIndex);
	}
	
	@Transactional
	public Set<String> getUserModifyFields(String username,Long formTypeId) {
		Set<Privilege> modifyPrivileges = getUserPrivileges(username,formTypeId, PrivilegeEnum.MODIFY);
		Set<Object> allFieldObjs = new HashSet<>();
		
		modifyPrivileges.forEach( modifyPrivilege -> {
			allFieldObjs.addAll( modifyPrivilege.getFields());
		});
		
		Set<String> allFields = allFieldObjs.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
		
		return allFields;
	}

	@Transactional
	public Set<Permission> getUserPermissions(String username) {
		Set<Permission> allPermissions = new HashSet<>();
		Set<String> urlPatterns = new HashSet<>();
		ZAccount user = (ZAccount) userService.loadUserByUsername(username);
		Set<Role> roles = user.getRoles();
		roles.forEach(role -> {
			Set<Permission> permissions = role.getPermissions();
			allPermissions.addAll(permissions);
		});
		
//		urlPatterns = allPermissions.stream().map(permission -> permission.getUriPattern()).collect(Collectors.toSet());
		return allPermissions;
	}
}
