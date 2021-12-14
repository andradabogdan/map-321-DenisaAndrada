package ro.ubbcluj.map.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Prietenie extends Entity<Long> {
    private Long id1;
    private Long id2;
    private LocalDate data;

    public Prietenie(Long id1, Long id2, LocalDate data) {
        this.id1 = id1;
        this.id2 = id2;
        this.data = data;
    }

    public Long getId1() {
        return this.id1;
    }

    public Long getId2() {
        return this.id2;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String toString() {
        return "Prietenie{id_prietenie=" + this.id + ", id1=" + this.id1 + ", id2=" + this.id2 + ", data=" + this.data + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Prietenie prietenie = (Prietenie)o;
            return Objects.equals(this.id1, prietenie.id1) && Objects.equals(this.id2, prietenie.id2) && Objects.equals(this.data, prietenie.data);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id1, this.id2, this.data});
    }
}
