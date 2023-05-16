package tn.soretras.contestmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.soretras.contestmanagement.web.rest.TestUtil;

class ContestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contest.class);
        Contest contest1 = new Contest();
        contest1.setId(1L);
        Contest contest2 = new Contest();
        contest2.setId(contest1.getId());
        assertThat(contest1).isEqualTo(contest2);
        contest2.setId(2L);
        assertThat(contest1).isNotEqualTo(contest2);
        contest1.setId(null);
        assertThat(contest1).isNotEqualTo(contest2);
    }
}
