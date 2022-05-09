import dao.UserDao;
import entity.UserEntity;
import model.filter.UserFilter;

import java.sql.SQLException;

public class ApplicationRunner {

    public static void main(String[] args) throws SQLException {
        UserDao userDao = UserDao.getINSTANCE();
        UserEntity user = new UserEntity();

        user.setName("Somebody");
        user.setLastname("Somebody");
        user.setPatronymic("Somebody");
        user.setAge(24);


        userDao.save(user);

        //DELETE
        System.out.println(userDao.delete(4L));

        //FIND BY ID
        System.out.println(userDao.findById(2L));

        //FIND ALL
        System.out.println(userDao.findAll());

        //FIND ALL WITH PAGINATION
        System.out.println(userDao.findAll(new UserFilter(1, 1, null, null)));

        //FIND ALL WITH PAGINATION AND SORTING
        System.out.println(userDao.findAll(new UserFilter(1, 1, "an", null)));

        //FIND ALL WITH PAGINATION AND SEARCHING
        System.out.println(userDao.findAll(new UserFilter(1, 1, null, "Musayev")));

        //GET USER WITH HIS ACCOUNTS
        System.out.println(userDao.findUserWithAccounts2ndWayLazy(2L));

        //UPDATE USER
        var maybeUser = userDao.findById(2L);
        maybeUser.ifPresent(userEntity -> {
            userEntity.setName("AnotherName");
            userEntity.setLastname("AnotherLastname");
            userEntity.setAge(30);
            userDao.update(userEntity);
        });


    }

}
