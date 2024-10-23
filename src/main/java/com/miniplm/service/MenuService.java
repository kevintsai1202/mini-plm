package com.miniplm.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.miniplm.convert.MenuConverter;
import com.miniplm.entity.Menu;
import com.miniplm.entity.MenuEnum;
import com.miniplm.repository.MenuRepository;
import com.miniplm.response.MenuResponse;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class MenuService {
	@Autowired
	private final MenuRepository menuRepository;
	
	@Autowired
	EntityManager entityManager;

//	@Transactional
	public List<MenuResponse> getAllFolders(){
		List<Menu> folders = new ArrayList<>();
		folders = menuRepository.findByMenuType(MenuEnum.Folder);
		
		return MenuConverter.INSTANCT.entityToResponse(folders);
	}
	
//	@Transactional
	public List<Menu> getAllMenus(){
		List<Menu> menus = new ArrayList<>();
		menus = menuRepository.findByMenuType(MenuEnum.Menu);
		return menus;
	}
	
	@Transactional
	public Menu createMenu(Menu menu){
		Menu savedFolder = menuRepository.save(menu);
		return savedFolder;
	}
	
//	@Transactional
	public Menu getMenu(Long menuId){
		Menu menu = null;
		if (menuId != null)
			menu =menuRepository.getReferenceById(menuId);
		return menu;
	}
	
	@Transactional
	public void deleteMenu(Long menuId){
		menuRepository.deleteById(menuId);
	}
	
	@Transactional
	public Menu updateMenu(Menu menu){
//		Menu existingMenu = entityManager.find(Menu.class, menu.getMenuId());
//		entityManager.remove(existingMenu);
//		if (existingMenu != null) {
//		    // 更新现有实体的属性
//			log.info("更新已存在實體");
//			existingMenu.setIcon(menu.getIcon());
//			existingMenu.setKey(menu.getKey());
//			existingMenu.setLabel(menu.getLabel());
//			existingMenu.setLink(menu.getLink());
//			existingMenu.setMenuType(menu.getMenuType());
//			existingMenu.setOrderBy(menu.getOrderBy());
//			existingMenu.setParent(menu.getParent());
//		    // 其他更新操作
////			entityManager.merge(menuRepository.save(existingMenu));
//			
//			return entityManager.merge(menuRepository.save(existingMenu));
//		} else {
//		    // 如果不存在，直接合并
//			log.info("直接儲存");
//			return menuRepository.save(menu);
//		}
		
		Menu savedMenu = menuRepository.getReferenceById(menu.getMenuId());
		
		savedMenu.setIcon(menu.getIcon());
		savedMenu.setKey(menu.getKey());
		savedMenu.setLabel(menu.getLabel());
		savedMenu.setLink(menu.getLink());
		savedMenu.setMenuType(menu.getMenuType());
		savedMenu.setOrderBy(menu.getOrderBy());
		savedMenu.setParent(menu.getParent());
		savedMenu.setConfigCriteriaNode(menu.getConfigCriteriaNode());
		
		return menuRepository.save(savedMenu);
	}
}
