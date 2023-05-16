package tn.soretras.contestmanagement.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.soretras.contestmanagement.domain.enumeration.elevel;

/**
 * A Contest.
 */
@Entity
@Table(name = "contest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "begindate")
    private LocalDate begindate;

    @Column(name = "enddate")
    private LocalDate enddate;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private elevel level;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Contest name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Contest description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBegindate() {
        return this.begindate;
    }

    public Contest begindate(LocalDate begindate) {
        this.setBegindate(begindate);
        return this;
    }

    public void setBegindate(LocalDate begindate) {
        this.begindate = begindate;
    }

    public LocalDate getEnddate() {
        return this.enddate;
    }

    public Contest enddate(LocalDate enddate) {
        this.setEnddate(enddate);
        return this;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public elevel getLevel() {
        return this.level;
    }

    public Contest level(elevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(elevel level) {
        this.level = level;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contest)) {
            return false;
        }
        return id != null && id.equals(((Contest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", begindate='" + getBegindate() + "'" +
            ", enddate='" + getEnddate() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
