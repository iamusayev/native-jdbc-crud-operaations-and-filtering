package dao;

import entity.AccountEntity;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static model.constants.ExceptionConstants.*;

public class AccountDao {
    private AccountDao() {
    }

    private static final AccountDao INSTANCE = new AccountDao();


    private static final String FIND_BY_ID_SQL = """
                        
            SELECT 
            id,
            number
            FROM accounts
            WHERE user_id = ?
                        
            """;


    public List<AccountEntity> findAllByUserId(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            List<AccountEntity> accounts = new ArrayList<>();
            while (resultSet.next()) {
                accounts.add(new AccountEntity(
                        resultSet.getLong("id"),
                        resultSet.getInt("number")
                ));
            }
            return accounts;
        } catch (SQLException ex) {
            throw new DaoException(DAO_EXCEPTION_MESSAGE, DAO_EXCEPTION_CODE);
        }
    }



            public static AccountDao getInstance() {
                return INSTANCE;
            }
}
