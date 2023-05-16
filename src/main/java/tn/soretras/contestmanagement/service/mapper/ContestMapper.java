package tn.soretras.contestmanagement.service.mapper;

import org.mapstruct.*;
import tn.soretras.contestmanagement.domain.Contest;
import tn.soretras.contestmanagement.service.dto.ContestDTO;

/**
 * Mapper for the entity {@link Contest} and its DTO {@link ContestDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContestMapper extends EntityMapper<ContestDTO, Contest> {}
