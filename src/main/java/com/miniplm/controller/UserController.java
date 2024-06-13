package com.miniplm.controller;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.ConfigFormField;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.Role;
import com.miniplm.entity.ZAccount;
import com.miniplm.repository.ConfigFormFieldRepository;
import com.miniplm.repository.ConfigListNodeRepository;
import com.miniplm.request.ConfigFormFieldRequest;
import com.miniplm.request.RegisterRequest;
import com.miniplm.response.FormResponse;
import com.miniplm.response.TableResultResponse;
import com.miniplm.response.UserFormFieldResponse;
import com.miniplm.service.ConfigFormFieldService;
import com.miniplm.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User API" , description = "與User相關的API")
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {
	@Autowired
	UserService userService;
	
	@GetMapping
    public ResponseEntity<TableResultResponse<ZAccount>> getAllUser() {
        return ResponseEntity.ok(new TableResultResponse<ZAccount>(userService.getAllUser()));
    }
	
	@GetMapping(value = "/{id}")
    public ResponseEntity getUser(@PathVariable String id) {
//        String userId = principal.getName();
//        if (!id.equals(userId)) {
//        	Map<String, String> errMap = new HashMap<String, String>();
//        	errMap.put("error", "get another user account is forbidden");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errMap);
//        }
        return ResponseEntity.ok(userService.getUser(id));
    }
	
	@GetMapping(value = "/{id}/roles")
    public ResponseEntity<Set<Role>> getUserRoles(@PathVariable String id) {
        return ResponseEntity.ok(userService.getRoles(id));
    }
	
	@GetMapping(value = "/me")
    public ResponseEntity<ZAccount> getUser(Principal principal) {
        String userName = principal.getName();
//        if (!id.equals(userId)) {
//        	Map<String, String> errMap = new HashMap<String, String>();
//        	errMap.put("error", "get another user account is forbidden");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errMap);
//        }
        ZAccount me = (ZAccount) userService.loadUserByUsername(userName);
        return ResponseEntity.ok(me);
    }
	
	@PostMapping
    public ResponseEntity<ZAccount> createUser(@RequestBody RegisterRequest register) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.createUser(register));
    }
}
