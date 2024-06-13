package com.miniplm.repository;

import java.util.List;

import javax.persistence.OrderBy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniplm.entity.Menu;
import com.miniplm.entity.MenuEnum;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	@OrderBy
	public List<Menu> findByMenuType(MenuEnum type);
}
