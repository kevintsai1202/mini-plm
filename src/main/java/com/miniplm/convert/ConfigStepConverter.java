package com.miniplm.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.ConfigStep;
import com.miniplm.request.ConfigStepRequest;
import com.miniplm.response.ConfigStepResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring")
public interface ConfigStepConverter {
	ConfigStepConverter INSTANCT = Mappers.getMapper(ConfigStepConverter.class);
	
	ConfigStep requestToEntity(ConfigStepRequest configStepRequest);
	
	@Mapping(source = "rejectStep.csId", target = "rejectStepId")
	ConfigStepResponse entityToResponse(ConfigStep step);
	
	@Mapping(source = "rejectStep.csId", target = "rejectStepId")
	List<ConfigStepResponse> entityToResponse(List<ConfigStep> steps);
}
