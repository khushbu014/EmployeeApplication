package com.example.demo.mapper;

import com.example.demo.model.bo.EmployeeDataBo;
import com.example.demo.model.dto.EmployeeDataDto;
import com.example.demo.model.entity.EmployeeDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeDataMapper {

    EmployeeDataEntity boToEntity(EmployeeDataBo employeeDataBo);

    EmployeeDataBo entityToBo(EmployeeDataEntity employeeDataEntity);

    @Mapping(target = "id", ignore = true)
    EmployeeDataBo dtoToBo(EmployeeDataDto employeeDataDto);

    EmployeeDataDto boToDto(EmployeeDataBo employeeDataBo);

    List<EmployeeDataBo> employeeBoFromEntityList
            (List<EmployeeDataEntity> subscriptionEntityList);

    List<EmployeeDataDto> employeeBoFromDtoList
            (List<EmployeeDataBo> subscriptionBoList);
}