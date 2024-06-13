package com.miniplm.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.miniplm.entity.ConfigStep;
import com.miniplm.repository.ConfigStepRepository;

@Component
@Converter(autoApply = false)
public class ConverterConfigStepJson implements AttributeConverter<Long, ConfigStep> {

	@Autowired
	private ConfigStepRepository configStepRepository;
	
	@Override
	public ConfigStep convertToDatabaseColumn(Long csId) {
		 if (csId == null) return null;
	     return configStepRepository.getReferenceById(csId);
	}

	@Override
	public Long convertToEntityAttribute(ConfigStep step) {
		if (step == null) return null;
		return step.getCsId();
	}
}
