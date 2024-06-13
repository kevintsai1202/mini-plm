package com.miniplm.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.Form;
import com.miniplm.entity.FormData;
import com.miniplm.entity.Menu;
import com.miniplm.request.FormDataRequest;
import com.miniplm.request.FormRequest;
import com.miniplm.request.MenuRequest;
import com.miniplm.response.FormResponse;
import com.miniplm.response.MenuResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring")
public interface MenuConverter {
	MenuConverter INSTANCT = Mappers.getMapper(MenuConverter.class);
	Menu requestToEntity(MenuRequest menuRequest);
	MenuResponse entityToResponse(Menu menu);
	List<MenuResponse> entityToResponse(List<Menu> menu);
}
