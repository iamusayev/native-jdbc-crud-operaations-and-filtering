package entity;

import java.util.Objects;

public class AccountEntity {


    private Long id;
    private Integer number;

    public AccountEntity(){}

    public AccountEntity(Long id, Integer number){
        this.id = id;
        this.number = number;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
               "id=" + id +
               ", number=" + number +
               '}';
    }
}
