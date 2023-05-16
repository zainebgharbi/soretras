package tn.soretras.contestmanagement.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContestformMapperTest {

    private ContestformMapper contestformMapper;

    @BeforeEach
    public void setUp() {
        contestformMapper = new ContestformMapperImpl();
    }
}
