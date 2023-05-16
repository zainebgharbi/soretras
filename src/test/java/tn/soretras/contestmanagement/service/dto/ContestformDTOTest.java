package tn.soretras.contestmanagement.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.soretras.contestmanagement.web.rest.TestUtil;

class ContestformDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContestformDTO.class);
        ContestformDTO contestformDTO1 = new ContestformDTO();
        contestformDTO1.setId(1L);
        ContestformDTO contestformDTO2 = new ContestformDTO();
        assertThat(contestformDTO1).isNotEqualTo(contestformDTO2);
        contestformDTO2.setId(contestformDTO1.getId());
        assertThat(contestformDTO1).isEqualTo(contestformDTO2);
        contestformDTO2.setId(2L);
        assertThat(contestformDTO1).isNotEqualTo(contestformDTO2);
        contestformDTO1.setId(null);
        assertThat(contestformDTO1).isNotEqualTo(contestformDTO2);
    }
}
