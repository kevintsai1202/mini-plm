package com.miniplm.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.entity.Route;
import com.miniplm.response.MessageResponse;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.RouteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;



@Tag(name = "系統設置" , description = "維護系統設定的API")
@RestController
@RequestMapping({"/api/v1/admin/config/systemsetting","/api/v1/config/systemsetting"})
@CrossOrigin
@Slf4j
public class RouteController {

	@Autowired
	private RouteService systemSettingService;
	
	
	@Operation(summary = "建立前端路由",
		       description = "建立前端路由與元件對應關係")
	@PostMapping(value="/routes",  name="建立前端路由")
	public ResponseEntity<Route> createRoute(@RequestBody Route route){
		return ResponseEntity.ok(systemSettingService.createRoute(route));
	}
	
	@Operation(summary = "列出所有前端路由",
		       description = "列出所有前端路由與元件對應關係")
	@GetMapping(value="/routes",  name="列出所有前端路由與元件對應關係")
	public ResponseEntity<TableResultResponse<Route>> listAllRoute(){
		log.info("listAllRoute");
//		System.out.println("listAllRoute");
		return ResponseEntity.ok(systemSettingService.getAllRoute());
	}
	
	@Operation(summary = "依ID取得前端路由",
		       description = "依ID取得前端路由")
	@GetMapping(value="/routes/{id}",  name="依ID取得前端路由")
	public ResponseEntity<Route> getRouteById(@PathVariable("id") Long id){
		return ResponseEntity.ok(systemSettingService.getRouteById(id));
	}
	
	@Operation(summary = "刪除前端路由",
		       description = "刪除前端路由與元件對應關係")
	@DeleteMapping(value="/routes/{id}",  name="刪除前端路由")
	public ResponseEntity<MessageResponse> deleteRoute(@PathVariable Long id){
		systemSettingService.deleteRoute(id);
		return ResponseEntity.ok(new MessageResponse("路徑刪除成功"));
	}
	
	@Operation(summary = "修改前端路由",
		       description = "修改前端路由與元件對應關係")
	@PutMapping(value="/routes/{id}",  name="依據id修改前端路由")
	public ResponseEntity<Route> updateRoute(@PathVariable("id") Long id,  @RequestBody Route route){
		Route savedRoute = systemSettingService.getRouteById(id);
		savedRoute.setPath(route.getPath());
		savedRoute.setElement(route.getElement());
		
		return ResponseEntity.ok(systemSettingService.updateRoute(savedRoute));
	}
	
	
}
