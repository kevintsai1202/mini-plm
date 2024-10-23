package com.miniplm.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.ConfigWorkflow;
import com.miniplm.entity.FormData;
import com.miniplm.request.ConfigWorkflowRequest;
import com.miniplm.request.FormDataRequest;
import com.miniplm.response.FormResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring")
public interface ConfigWorkflowConverter {
	ConfigWorkflowConverter INSTANCT = Mappers.getMapper(ConfigWorkflowConverter.class);
	ConfigWorkflow requestToEntity(ConfigWorkflowRequest configWorkflowRequest);
//	FormResponse entityToResponse(FormData formData);
}
