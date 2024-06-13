package com.miniplm.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.Form;
import com.miniplm.entity.FormData;
import com.miniplm.entity.Role;
import com.miniplm.request.FormDataRequest;
import com.miniplm.request.FormRequest;
import com.miniplm.request.RoleRequest;
import com.miniplm.response.FormResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring")
public interface RoleConverter {
	RoleConverter INSTANCT = Mappers.getMapper(RoleConverter.class);
	Role requestToEntity(RoleRequest roleRequest);
//	FormResponse entityToResponse(Form form);
}
