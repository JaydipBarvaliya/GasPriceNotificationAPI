package com.gpn.mapper;

import com.gpn.dto.AlertDTO;
import com.gpn.entity.Alert;
import com.gpn.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    AlertDTO toAlertDTO(Alert alert);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true) // If auto-generated
    Alert toAlert(AlertDTO alertDTO, User user);

}