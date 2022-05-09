package entity;

import java.util.List;
import java.util.Objects;

public class UserEntity {

    private Long id;
    private String name;
    private String lastname;
    private String patronymic;
    private Integer age;
    private List<AccountEntity> accounts;


    public UserEntity() {
    }

    public UserEntity(Long id, String name, String lastname, String patronymic, Integer age) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.age = age;
    }

    public UserEntity(Long id, String name, String lastname, String patronymic, Integer age, List<AccountEntity> accounts) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.age = age;
        this.accounts = accounts;
    }


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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", lastname='" + lastname + '\'' +
               ", patronymic='" + patronymic + '\'' +
               ", age=" + age +
               ", accounts=" + accounts +
               '}';
    }
}
