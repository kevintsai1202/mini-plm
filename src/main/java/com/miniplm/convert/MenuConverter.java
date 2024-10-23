package com.miniplm.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.Menu;
import com.miniplm.request.MenuRequest;
import com.miniplm.response.MenuResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring" )
public interface MenuConverter {
	MenuConverter INSTANCT = Mappers.getMapper(MenuConverter.class);
	
	Menu requestToEntity(MenuRequest menuRequest);
	
	@Mapping(source = "configCriteriaNode.cnId", target = "configCriteriaNodeId")
	MenuResponse entityToResponse(Menu menu);
	
	@Mapping(source = "configCriteriaNode.cnId", target = "configCriteriaNodeId")
	List<MenuResponse> entityToResponse(List<Menu> menu);
}
