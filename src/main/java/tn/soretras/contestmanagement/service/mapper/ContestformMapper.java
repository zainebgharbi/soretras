package tn.soretras.contestmanagement.service.mapper;

import org.mapstruct.*;
import tn.soretras.contestmanagement.domain.Contest;
import tn.soretras.contestmanagement.domain.Contestform;
import tn.soretras.contestmanagement.service.dto.ContestDTO;
import tn.soretras.contestmanagement.service.dto.ContestformDTO;

/**
 * Mapper for the entity {@link Contestform} and its DTO {@link ContestformDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContestformMapper extends EntityMapper<ContestformDTO, Contestform> {
    @Mapping(target = "contest", source = "contest", qualifiedByName = "contestId")
    ContestformDTO toDto(Contestform s);

    @Named("contestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContestDTO toDtoContestId(Contest contest);
}
