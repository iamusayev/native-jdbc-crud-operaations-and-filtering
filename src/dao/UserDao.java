package dao;

import entity.AccountEntity;
import entity.UserEntity;
import exception.DaoException;
import model.filter.UserFilter;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static model.constants.ExceptionConstants.DAO_EXCEPTION_CODE;
import static model.constants.ExceptionConstants.DAO_EXCEPTION_MESSAGE;

public class UserDao implements Dao<UserEntity, Long> {

    private UserDao() {
    }

    private static final UserDao INSTANCE = new UserDao();

    private final AccountDao accountDao = AccountDao.getInstance();


    private static final String SAVE_SQL = """
                        
            INSERT INTO users (name, lastname, patronymic, age) 
            VALUES  (?, ?, ? ,?)
                        
            """;

    private static final String FIND_ALL_SQL = """
                        
            SELECT 
            id,
            name,
            lastname,
            patronymic,
            age
            FROM users
                        
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + "WHERE id = ?";


    private static final String DELETE_SQL = """
                        
            DELETE FROM users 
            WHERE id = ?
                        
            """;


    private static final String UPDATE_SQL = """
                        
            UPDATE users
            SET name = ?, lastname = ?, patronymic = ?, age = ?
            WHERE id = ?
                        
            """;


    private static final String FIND_USER_WITH_ACCOUNTS_SQL = """
                        
            SELECT 
            users.id,
            name,
            lastname,
            patronymic,
            age,
            a.id accountId ,
            a.number
            FROM users
            INNER JOIN accounts a 
            ON users.id = a.user_id
            WHERE users.id = ?

            """;


    @Override
    public List<UserEntity> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<UserEntity> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        } catch (SQLException ex) {
            throw new DaoException(DAO_EXCEPTION_MESSAGE, DAO_EXCEPTION_CODE);
        }
    }

    public List<UserEntity> findAll(UserFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.getName() != null) {
            whereSql.add(" name ILIKE ? ");
            parameters.add("%" + filter.getName() + "%");
        }
        if (filter.getLastname() != null) {
            whereSql.add(" lastname ILIKE ?");
            parameters.add(filter.getLastname());
        }
        parameters.add(filter.getLimit());
        parameters.add(filter.getOffset());
        String where = whereSql.stream().collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ? OFFSET ?"));
        String sql = whereSql.isEmpty() ? FIND_ALL_SQL + " LIMIT ? OFFSET ?" : FIND_ALL_SQL + where;
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            var resultSet = preparedStatement.executeQuery();
            List<UserEntity> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        } catch (SQLException ex) {
            throw new DaoException(DAO_EXCEPTION_MESSAGE, DAO_EXCEPTION_CODE);
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            UserEntity user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException ex) {
            throw new DaoException(DAO_EXCEPTION_MESSAGE, DAO_EXCEPTION_CODE);
        }
    }

    public Optional<UserEntity> findUserWithAccounts1stEager(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_USER_WITH_ACCOUNTS_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            List<AccountEntity> accounts = new ArrayList<>();
            UserEntity user = new UserEntity();
            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastname(resultSet.getString("lastname"));
                user.setPatronymic(resultSet.getString("patronymic"));
                user.setAge(resultSet.getInt("age"));
                accounts.add(new AccountEntity(
                        resultSet.getLong("accountId"),
                        resultSet.getInt("number")
                ));
            }
            while (resultSet.next()) {
                accounts.add(new AccountEntity(
                        resultSet.getLong("accountId"),
                        resultSet.getInt("number")
                ));
            }
            user.setAccounts(accounts);
            return Optional.ofNullable(user);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    public Optional<UserEntity> findUserWithAccounts2ndWayLazy(Long id) {
        var accounts = accountDao.findAllByUserId(id);
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            UserEntity user = null;
            if (resultSet.next()) {
                user = new UserEntity(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("patronymic"),
                        resultSet.getInt("age"),
                        accounts
                );
            }
            return Optional.ofNullable(user);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public UserEntity save(UserEntity entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setInt(4, entity.getAge());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong("id"));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DaoException(DAO_EXCEPTION_MESSAGE, DAO_EXCEPTION_CODE);
        }
    }



    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DaoException(DAO_EXCEPTION_MESSAGE, DAO_EXCEPTION_CODE);
        }
    }

    @Override
    public void update(UserEntity entity) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setInt(4, entity.getAge());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DaoException(DAO_EXCEPTION_MESSAGE, DAO_EXCEPTION_CODE);
        }
    }




        private UserEntity buildUser(ResultSet resultSet) throws SQLException {
            return new UserEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("lastname"),
                    resultSet.getString("patronymic"),
                    resultSet.getInt("age")
            );
        }

            public static UserDao getINSTANCE() {
                return INSTANCE;
            }
}
