package com.miniplm.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.FormData;
import com.miniplm.request.FormDataRequest;
import com.miniplm.response.FormResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring")
public interface FormDataConverter {
	FormDataConverter INSTANCT = Mappers.getMapper(FormDataConverter.class);
	FormData requestToEntity(FormDataRequest formDataRequest);
//	FormResponse entityToResponse(FormData formData);
}
