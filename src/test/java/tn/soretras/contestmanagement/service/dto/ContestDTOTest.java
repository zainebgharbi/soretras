package tn.soretras.contestmanagement.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.soretras.contestmanagement.web.rest.TestUtil;

class ContestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContestDTO.class);
        ContestDTO contestDTO1 = new ContestDTO();
        contestDTO1.setId(1L);
        ContestDTO contestDTO2 = new ContestDTO();
        assertThat(contestDTO1).isNotEqualTo(contestDTO2);
        contestDTO2.setId(contestDTO1.getId());
        assertThat(contestDTO1).isEqualTo(contestDTO2);
        contestDTO2.setId(2L);
        assertThat(contestDTO1).isNotEqualTo(contestDTO2);
        contestDTO1.setId(null);
        assertThat(contestDTO1).isNotEqualTo(contestDTO2);
    }
}
