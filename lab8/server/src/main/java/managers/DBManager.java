package managers;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import structs.SQLAnswer;
import structs.classes.Color;
import structs.classes.Coordinates;
import structs.classes.Dragon;
import structs.classes.DragonCave;
import structs.classes.DragonCharacter;
import structs.classes.DragonType;

public class DBManager {
    String sqlInsert = "INSERT INTO users (login, salt, password) VALUES (?, ?, ?)";
    String sqlRequest = "INSERT INTO dragons (id, name, x, y, creation_date, age, color, type, character, depth, number_of_treasures, login) " +
            "VALUES (?,?, ?, ?, ?, ?, ?::dragon_color, ?::dragon_type, ?::dragon_character, ?, ?, ?)";
    String sqlDragon = "SELECT 1 FROM dragons WHERE id = ? AND login = ?";
    private String url;
    private Connection connection; // bad (for threads) (множество connection'ов)

    public DBManager(String url, int port) {
        this.url = "jdbc:postgresql://" + url + ":" + port + "/studs";
        this.connection = null;
    }

    public SQLAnswer registerUser(String login, String password) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return new SQLAnswer("Some problems occured in time of connection", false);
        }
        if (checkUserExists(login)) {
            System.out.println("Someone tries to create an user that already exists");
            return new SQLAnswer("Such user already exists", false);
        }


        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());
        }
        String salt = getRandomString();
        String pepper = "perpre ^_^";
        byte[] hashedPassword = new byte[0];
        try {
            hashedPassword = sha.digest((password + pepper + salt).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }

        try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
            statement.setString(1, login);
            statement.setString(2, salt);
            statement.setBytes(3, hashedPassword);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return new SQLAnswer("User " + login + " was added", true);
            } else {
                return new SQLAnswer("Something mysterious just happened", false);
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
            return new SQLAnswer("DB Error", false);
        }
    }

    public SQLAnswer addDragon(String key, Dragon dragon, String login) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return new SQLAnswer("Some problems occured in time of connection", false);
        }
        try (PreparedStatement addStatement = connection.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS)) {
            addStatement.setString(1, key);
            addStatement.setString(2, dragon.getName());
            addStatement.setDouble(3, dragon.getCoordinates().getX());
            addStatement.setLong(4, dragon.getCoordinates().getY());
            addStatement.setDate(5, Date.valueOf(dragon.getCreationDate()));
            addStatement.setInt(6, dragon.getAge());
            addStatement.setString(7, dragon.getColor().name());
            addStatement.setString(8, dragon.getType().name());
            addStatement.setString(9, dragon.getCharacter().name());
            if (dragon.getCave() == null) {
                addStatement.setNull(10, Types.NULL);
                addStatement.setNull(11, Types.NULL);
            } else {
                addStatement.setInt(10, dragon.getCave().getDepth());
                addStatement.setDouble(11, dragon.getCave().getNumberOfTreasures());
            }
            addStatement.setString(12, login);
            int rowsInserted = addStatement.executeUpdate();
            System.out.println(rowsInserted);
            ResultSet resultSet = addStatement.getGeneratedKeys();
            resultSet.next();
            if (rowsInserted > 0) {
                System.out.println("New dragon was added with id: " + resultSet.getString(1) + " by user " + login);
                return new SQLAnswer("Dragon with id " + resultSet.getString(1) + " was added", true);
            } else {
                return new SQLAnswer("Something mysterious just happened", false);
            }

        } catch (SQLException e) {
            System.out.println(e);
            return new SQLAnswer("DB Error", false);
        }
    }

    public boolean isDragonUsers(String dragonId, String login) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return false;
        }
        try (PreparedStatement selectStatement = connection.prepareStatement(sqlDragon)) {
            selectStatement.setString(1, dragonId);
            selectStatement.setString(2, login);
            ResultSet rs = selectStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SQLAnswer updateDragon(Dragon dragon, String dragonId, String login) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return new SQLAnswer("Some problems occured in time of connection", false);
        }
        if (!isDragonUsers(dragonId, login)) {
            return new SQLAnswer("Dragon not owned by user or does not exist", false);
        }

        String sql = "UPDATE dragons SET name = ?, x = ?, y = ?, creation_date = ?, age = ?, " +
                "color = ?::dragon_color, type = ?::dragon_type, character = ?::dragon_character, " +
                "depth = ?, number_of_treasures = ? WHERE id = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setString(1, dragon.getName());
            updateStatement.setDouble(2, dragon.getCoordinates().getX());
            updateStatement.setLong(3, dragon.getCoordinates().getY());
            updateStatement.setDate(4, Date.valueOf(dragon.getCreationDate()));
            updateStatement.setInt(5, dragon.getAge());
            updateStatement.setString(6, dragon.getColor().name());
            updateStatement.setString(7, dragon.getType().name());
            updateStatement.setString(8, dragon.getCharacter().name());

            if (dragon.getCave() == null) {
                updateStatement.setNull(9, Types.NULL);
                updateStatement.setNull(10, Types.NULL);
            } else {
                updateStatement.setInt(9, dragon.getCave().getDepth());
                updateStatement.setDouble(10, dragon.getCave().getNumberOfTreasures());
            }

            updateStatement.setString(11, dragonId);

            int rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return new SQLAnswer("Dragon updated successfully", true);
            } else {
                return new SQLAnswer("No rows updated", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new SQLAnswer("Database error during update", false);
        }
    }


    public SQLAnswer getAllDragons() {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return new SQLAnswer("Some problems occured in time of connection", false);
        }
        String sql = "SELECT * FROM dragons";
        try (Statement selectStatement = connection.createStatement();
             ResultSet resultSet = selectStatement.executeQuery(sql)) {
            return new SQLAnswer(resultSet, "All dragons result set", true);
        } catch (SQLException e) {
            e.printStackTrace();
            return new SQLAnswer("Error getting dragons", false);
        }
    }

    public SQLAnswer getUserDragons(String login) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return new SQLAnswer("Some problems occured in time of connection", false);
        }
        String sql = "SELECT * FROM dragons WHERE login = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            selectStatement.setString(1, login);
            ResultSet resultSet = selectStatement.executeQuery();
            return new SQLAnswer(resultSet, "User dragons result set", true);
        } catch (SQLException e) {
            e.printStackTrace();
            return new SQLAnswer("Error fetching user dragons", false);
        }
    }

    private String getRandomString() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[random.nextInt(3, 9)];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public boolean checkUserExists(String login) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return false;
        }
        String sqlCheck = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(sqlCheck)) {
            checkStatement.setString(1, login);
            ResultSet res = checkStatement.executeQuery();
            return res.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateUser(String login, String password) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return false;
        }
        String sql = "SELECT salt, password FROM users WHERE login = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            selectStatement.setString(1, login);
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }

            String storedSalt = resultSet.getString("salt");
            byte[] storedHash = resultSet.getBytes("password");

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            String pepper = "perpre ^_^";
            byte[] newHash = sha.digest((password + pepper + storedSalt).getBytes("UTF-8"));

            return MessageDigest.isEqual(storedHash, newHash);

        } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.err.println("Error validating user: " + e.getMessage());
            return false;
        }
    }

    public SQLAnswer deleteDragon(String dragonId, String login) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return new SQLAnswer("Some problems occured in time of connection", false);
        }
        String sql = "DELETE FROM dragons WHERE id = ? AND login = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
            deleteStatement.setString(1, dragonId);
            deleteStatement.setString(2, login);

            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted > 0) {
                return new SQLAnswer("Dragon deleted successfully", true);
            } else {
                return new SQLAnswer("Dragon not found or not owned by user", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new SQLAnswer("Database error during deletion", false);
        }
    }

    public SQLAnswer deleteDragons(Set<String> dragonIds, String login) {
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return new SQLAnswer("Some problems occured in time of connection", false);
        }
        if (dragonIds == null || dragonIds.isEmpty()) {
            return new SQLAnswer("No dragons to delete", false);
        }

        String placeholders = String.join(",", Collections.nCopies(dragonIds.size(), "?"));
        String sql = "DELETE FROM dragons WHERE id IN (" + placeholders + ") AND login = ?";

        try (PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
            int index = 1;

            for (String id : dragonIds) {
                deleteStatement.setString(index++, id);
            }

            deleteStatement.setString(index, login);

            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted > 0) {
                return new SQLAnswer("Deleted " + rowsDeleted + " dragons", true);
            } else {
                return new SQLAnswer("No dragons were deleted. Either they don't exist or are not owned by the user.", false);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting dragons: " + e.getMessage());
            e.printStackTrace();
            return new SQLAnswer("Database error during deletion", false);
        }
    }


    public Map<String, Dragon> getDragonMap() {

        Map<String, Dragon> dragonMap = new HashMap<>();
        try {
            String line = Files.readAllLines(Paths.get("/home/studs/s435169/.pgpass")).get(0);
            String[] parts = line.split(":");
            connection = DriverManager.getConnection(url, parts[3], parts[4]);
        } catch (IOException | SQLException e) {
            return dragonMap;
        }

        String sql = "SELECT * FROM dragons";
        try (Statement selectStatement = connection.createStatement();
             ResultSet resultSet = selectStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double x = resultSet.getDouble("x");
                long y = resultSet.getLong("y");
                int age = resultSet.getInt("age");
                String colorStr = resultSet.getString("color");
                String typeStr = resultSet.getString("type");
                String characterStr = resultSet.getString("character");
                Integer depth = resultSet.getObject("depth", Integer.class);
                Double treasures = resultSet.getObject("number_of_treasures", Double.class);
                String login = resultSet.getString("login");

                Color color = Color.valueOf(colorStr);
                DragonType type = DragonType.valueOf(typeStr);
                DragonCharacter character = DragonCharacter.valueOf(characterStr);

                Coordinates coordinates = new Coordinates(x, y);
                DragonCave cave = (depth != null && treasures != null)
                        ? new DragonCave(depth, treasures)
                        : null;

                Dragon dragon = new Dragon(name, coordinates, age, color, type, character, cave);
                dragon.setOwnerLogin(login);

                dragonMap.put(id, dragon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return dragonMap;
        }


        return dragonMap;
    }
}
