package tn.soretras.contestmanagement.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;
import tn.soretras.contestmanagement.domain.enumeration.elevel;

/**
 * A DTO for the {@link tn.soretras.contestmanagement.domain.Contest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContestDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private LocalDate begindate;

    private LocalDate enddate;

    private elevel level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBegindate() {
        return begindate;
    }

    public void setBegindate(LocalDate begindate) {
        this.begindate = begindate;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public elevel getLevel() {
        return level;
    }

    public void setLevel(elevel level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContestDTO)) {
            return false;
        }

        ContestDTO contestDTO = (ContestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", begindate='" + getBegindate() + "'" +
            ", enddate='" + getEnddate() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
