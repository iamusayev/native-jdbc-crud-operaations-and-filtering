package model.filter;

public class UserFilter {

    private final Integer limit;
    private final Integer offset;
    private final String name;
    private final String lastname;

    public UserFilter(Integer limit, Integer offset, String name, String lastname) {
        this.limit = limit;
        this.offset = offset;
        this.name = name;
        this.lastname = lastname;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }
}
