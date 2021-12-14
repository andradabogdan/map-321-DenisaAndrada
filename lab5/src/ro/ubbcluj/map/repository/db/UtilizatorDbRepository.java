package ro.ubbcluj.map.repository.db;

import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.*;

public class UtilizatorDbRepository extends InMemoryRepository<Long, Utilizator> {
    private final String url;
    private final String username;
    private final String password;

    public UtilizatorDbRepository(Validator<Utilizator> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Utilizator findOne(Long aLong) {
        String sql = "SELECT * from users u LEFT JOIN friendships f ON f.id2 = u.id or f.id1 = u.id WHERE u.id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            Utilizator utilizator = new Utilizator();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");

                utilizator.setFirstName(firstName);
                utilizator.setLastName(lastName);
                utilizator.setId(aLong);
                if (!Objects.equals(utilizator.getId(), id1) && id1 !=0) {
                    Utilizator utilizator1 = findOneDB(id1);
                    utilizator.makeFriend(utilizator1);
                }
                if (!Objects.equals(utilizator.getId(), id2) && id2 !=0) {
                    Utilizator utilizator2 = findOneDB(id2);
                    utilizator.makeFriend(utilizator2);
                }
            }

            return utilizator;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utilizator findOneDB(Long aLong) {
        String sql = "SELECT * from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");

            Utilizator utilizator = new Utilizator(firstName, lastName);
            utilizator.setId(aLong);

            return utilizator;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                users.add(findOne(id));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public Utilizator save(Utilizator entity) {

        String sql = "insert into users (first_name, last_name) values (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Utilizator delete(Long aLong) {
        String sql = "DELETE from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, aLong);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator update(Utilizator entity) {
        String sql = "UPDATE users SET (first_name, last_name) = (?, ?)  WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setLong(3, entity.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
