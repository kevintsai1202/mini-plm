package com.miniplm.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.Form;
import com.miniplm.entity.FormData;
import com.miniplm.request.FormDataRequest;
import com.miniplm.request.FormRequest;
import com.miniplm.response.FormResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring")
public interface FormConverter {
	FormConverter INSTANCT = Mappers.getMapper(FormConverter.class);
	Form requestToEntity(FormRequest formRequest);
	FormResponse entityToResponse(Form form);
}
