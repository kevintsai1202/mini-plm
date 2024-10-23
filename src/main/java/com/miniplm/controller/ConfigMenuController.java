package com.miniplm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miniplm.convert.MenuConverter;
import com.miniplm.entity.ConfigCriteriaNode;
import com.miniplm.entity.ConfigListItem;
import com.miniplm.entity.ConfigListNode;
import com.miniplm.entity.Menu;
import com.miniplm.entity.MenuEnum;
import com.miniplm.repository.ConfigCriteriaNodeRepository;
import com.miniplm.repository.ConfigListItemRepository;
import com.miniplm.repository.ConfigListNodeRepository;
import com.miniplm.repository.MenuRepository;
import com.miniplm.request.MenuRequest;
import com.miniplm.response.TableResultResponse;
import com.miniplm.service.MenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "導覽列選單", description = "與導覽列選單相關的API")
@RestController
@RequestMapping({ "/api/v1/admin/config/menus", "/api/v1/config/menus" })
@CrossOrigin
@Slf4j
public class ConfigMenuController {
	@Autowired
	private MenuService menuService;

	@Autowired
	private ConfigCriteriaNodeRepository configCriteriaNodeRepository;

	@GetMapping("/{type}")
	@Operation(summary = "取得資料夾", description = "返回所有資料夾")
	public ResponseEntity<TableResultResponse> listAllMenus(@PathVariable("type") String type) {
		if (MenuEnum.Folder.toString().equals(type))
			return ResponseEntity.ok(new TableResultResponse(menuService.getAllFolders()));
		else if (MenuEnum.Menu.toString().equals(type)) {
			return ResponseEntity.ok(new TableResultResponse(menuService.getAllMenus()));
		} else
			return ResponseEntity.noContent().build();
	}

	@GetMapping("/folder/{fId}")
	@Operation(summary = "取得資料夾", description = "返回所有資料夾")
	public ResponseEntity<TableResultResponse> listFolderSubMenus(@PathVariable("fId") Long fId) {
		Menu folder = menuService.getMenu(fId);
		Set<Menu> setSubMenus = folder.getChildren();
		List<Menu> listSubMenus = new ArrayList<>(setSubMenus);
		return ResponseEntity.ok(new TableResultResponse(MenuConverter.INSTANCT.entityToResponse(listSubMenus)));
	}

	@PostMapping
	public ResponseEntity<Menu> createMenu(@RequestBody MenuRequest menuRequest) {
		log.info("create menu: {}", menuRequest);
//		System.out.println("create menu:"+menuRequest);
		Menu newMenu = MenuConverter.INSTANCT.requestToEntity(menuRequest);
		Menu parent = menuService.getMenu(menuRequest.getParentId());
		newMenu.setParent(parent);
		Menu savedMenu = menuService.createMenu(newMenu);
		return ResponseEntity.ok(savedMenu);
	}

	@PutMapping
	public ResponseEntity<Menu> updateMenu(@RequestBody MenuRequest menuRequest) {
		log.info("request menu: {}", menuRequest);
//		System.out.println("update menu:"+menuRequest);
		Menu updateMenu = MenuConverter.INSTANCT.requestToEntity(menuRequest);
		if (menuRequest.getConfigCriteriaNodeId() != null) {
			ConfigCriteriaNode ccNode = configCriteriaNodeRepository
					.getReferenceById(menuRequest.getConfigCriteriaNodeId());
			updateMenu.setConfigCriteriaNode(ccNode);
		}
		Menu parent = menuService.getMenu(menuRequest.getParentId());
		updateMenu.setParent(parent);
		log.info("update menu: {}", updateMenu);
		Menu savedMenu = menuService.updateMenu(updateMenu);
		return ResponseEntity.ok(savedMenu);
	}
}
