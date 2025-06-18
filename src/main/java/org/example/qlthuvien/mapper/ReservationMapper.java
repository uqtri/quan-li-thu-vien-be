package org.example.qlthuvien.mapper;

import org.example.qlthuvien.dto.reservation.CreateReservationRequest;
import org.example.qlthuvien.dto.reservation.UpdateReservationRequest;
import org.example.qlthuvien.dto.reservation.ReservationResponse;
import org.example.qlthuvien.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReservationMapper {

    ReservationResponse toResponse(Reservation entity);

    Reservation toEntity(CreateReservationRequest dto);

    Reservation toEntity(UpdateReservationRequest dto);

    Reservation updateEntity(@MappingTarget Reservation target, Reservation source);
}
