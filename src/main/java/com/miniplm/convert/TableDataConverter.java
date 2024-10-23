package com.miniplm.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.miniplm.entity.FormData;
import com.miniplm.entity.TableData;
import com.miniplm.request.TableDataRequest;
import com.miniplm.response.FormResponse;
import com.miniplm.response.TableDataResponse;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.WARN)
@Mapper(componentModel="spring")
public interface TableDataConverter {
	TableDataConverter INSTANCT = Mappers.getMapper(TableDataConverter.class);
	TableData requestToEntity(TableDataRequest tableDataRequest);
//	TableDataResponse entityToResponse(TableData tableData);
}
