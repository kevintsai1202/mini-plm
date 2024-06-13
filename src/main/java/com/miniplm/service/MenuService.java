package com.miniplm.service;

import java.util.ArrayList;
import java.util.List;

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

@Service
@Data
@RequiredArgsConstructor
public class MenuService {
	@Autowired
	private final MenuRepository menuRepository;

	@Transactional
	public List<MenuResponse> getAllFolders(){
		List<Menu> folders = new ArrayList<>();
		folders = menuRepository.findByMenuType(MenuEnum.Folder);
		
		return MenuConverter.INSTANCT.entityToResponse(folders);
	}
	
	@Transactional
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
	
	@Transactional
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
		Menu savedMenu = menuRepository.getReferenceById(menu.getMenuId());
		
		savedMenu.setIcon(menu.getIcon());
		savedMenu.setKey(menu.getKey());
		savedMenu.setLabel(menu.getLabel());
		savedMenu.setLink(menu.getLink());
		savedMenu.setMenuType(menu.getMenuType());
		savedMenu.setOrderBy(menu.getOrderBy());
		savedMenu.setParent(menu.getParent());
		
		return menuRepository.save(savedMenu);
	}
}
