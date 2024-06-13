package com.miniplm.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.miniplm.entity.Route;
import com.miniplm.entity.SystemSetting;
import com.miniplm.repository.RouteRepository;
import com.miniplm.response.TableResultResponse;

@Service
public class RouteService {
	@Autowired
	private RouteRepository routeRepository;

	@Transactional
	public TableResultResponse<Route> getAllRoute() {
		return new TableResultResponse<Route>(routeRepository.findAll(Sort.by("path")));
	}

	@Transactional
	public Route getRouteById(Long id) {
		return routeRepository.getReferenceById(id);
	}

	@Transactional
	public Route createRoute(Route route) {
		return routeRepository.save(route);
	}

	@Transactional
	public void deleteRoute(Long id) {
		routeRepository.deleteById(id);
	}

	@Transactional
	public Route updateRoute(Route route) {
		return routeRepository.save(route);
	}

	@Transactional
	public void init() {
		if (routeRepository.count() == 0) {
			routeRepository.save(Route.builder().id(1000000005L).element("Home").path("/").build());
			routeRepository.save(Route.builder().id(1000000006L).element("Login").path("Login").build());
			routeRepository.save(Route.builder().id(1000000007L).element("Home").path("user").build());
			routeRepository.save(Route.builder().id(1000000008L).element("AllForm").path("user/allform/:formTypeId").build());
			routeRepository.save(Route.builder().id(1000000009L).element("FormPage").path("user/formnumber/:formno/:action").build());
			routeRepository.save(Route.builder().id(1000000010L).element("AllForm").path("user/allform/").build());
			routeRepository.save(Route.builder().id(1000000011L).element("CreateForm").path("user/createform/:formTypeId").build());
			routeRepository.save(Route.builder().id(1000000012L).element("FormPage").path("user/readform/:formid/:action").build());
			
			routeRepository.save(Route.builder().id(1000000013L).element("AdminHome").path("admin").build());
			routeRepository.save(Route.builder().id(1000000014L).element("ConfigFormNumber").path("admin/autonumber").build());
			routeRepository.save(Route.builder().id(1000000015L).element("ConfigWorkflowAndSteps").path("admin/workflow").build());
			routeRepository.save(Route.builder().id(1000000016L).element("ConfigPermission").path("admin/configpermission").build());
			routeRepository.save(Route.builder().id(1000000017L).element("ConfigList").path("admin/configlist").build());
			routeRepository.save(Route.builder().id(1000000018L).element("ConfigUser").path("admin/configuser").build());
			routeRepository.save(Route.builder().id(1000000019L).element("ConfigForm").path("admin/configform").build());
			routeRepository.save(Route.builder().id(1000000020L).element("ConfigRole").path("admin/configrole").build());
			routeRepository.save(Route.builder().id(1000000021L).element("ConfigMenu").path("admin/configmenu").build());
			routeRepository.save(Route.builder().id(1000000022L).element("ConfigRoute").path("admin/configrouter").build());
			
			routeRepository.save(Route.builder().id(1000000023L).element("NotFound").path("*").build());
		}
		System.out.println("Route init done!");
		long routeCount = routeRepository.count();
		System.out.println("目前有" + routeCount + "筆路由元件設定");
	}
}
