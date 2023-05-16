package tn.soretras.contestmanagement.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link tn.soretras.contestmanagement.domain.Contestform} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContestformDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    private LocalDate birthdate;

    private ContestDTO contest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public ContestDTO getContest() {
        return contest;
    }

    public void setContest(ContestDTO contest) {
        this.contest = contest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContestformDTO)) {
            return false;
        }

        ContestformDTO contestformDTO = (ContestformDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contestformDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContestformDTO{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", contest=" + getContest() +
            "}";
    }
}
