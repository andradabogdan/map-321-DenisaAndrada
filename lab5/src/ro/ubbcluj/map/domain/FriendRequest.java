package ro.ubbcluj.map.domain;

import java.util.Objects;

public class FriendRequest extends Entity<Long>{
    private Long id1;
    private Long id2;
    private String status;

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return id1 == that.id1 && id2 == that.id2 && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, status);
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public FriendRequest(Long id1, Long id2, String status) {
        this.id1 = id1;
        this.id2 = id2;
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + id +
                ", id1=" + id1 +
                ", id2=" + id2 +
                ", status='" + status + '\'' +
                '}';
    }
}
