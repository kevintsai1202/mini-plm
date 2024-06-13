package com.miniplm.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.ZAccount;
import com.miniplm.request.RegisterRequest;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel = "spring")
public interface UserConverter {
	UserConverter INSTANCT = Mappers.getMapper(UserConverter.class);
	ZAccount requestToEntity(RegisterRequest userRequest);
//	FormResponse entityToResponse(User formData);
}
