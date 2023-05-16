package tn.soretras.contestmanagement.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContestMapperTest {

    private ContestMapper contestMapper;

    @BeforeEach
    public void setUp() {
        contestMapper = new ContestMapperImpl();
    }
}
