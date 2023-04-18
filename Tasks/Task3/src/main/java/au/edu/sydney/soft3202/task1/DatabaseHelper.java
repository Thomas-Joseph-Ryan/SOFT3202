package au.edu.sydney.soft3202.task1;
import au.edu.sydney.soft3202.task1.model.Item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DatabaseHelper {
  private static final String DB_NAME = "fruitbasket.db";
  private Connection connection;

  private void connect() throws SQLException {
    connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
  }
  private void ensureUsersTable() throws SQLException {
    String sql =
      "CREATE TABLE IF NOT EXISTS users (user TEXT PRIMARY KEY NOT NULL)";
    try (Statement statement = connection.createStatement()) {
      statement.execute(sql);
    }
  }

  public void ensureShoppingBasketTable() throws SQLException {
    String sql =
            "CREATE TABLE IF NOT EXISTS shoppingcart " +
                    "(user TEXT REFERENCES users(user)," +
                    "item TEXT," +
                    "count INTEGER," +
                    "cost DOUBLE," +
                    "PRIMARY KEY (user, item))";
    try (Statement statement = connection.createStatement()) {
      statement.execute(sql);
    }
  }

  public List<Item> getUserCart(String name) throws SQLException {
    String sql =
            "SELECT item, count, cost " +
                    "FROM shoppingcart " +
                    "WHERE user = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, name);
    ResultSet resultSet = preparedStatement.executeQuery();

    List<Item> items = new ArrayList<>();
    while (resultSet.next()) {
      String itemName = resultSet.getString("item");
      Integer count = resultSet.getInt("count");
      Double cost = resultSet.getDouble("cost");
      Item item = new Item(itemName, count, cost);
      items.add(item);
    }
    return items;
  }

  public void updateCartItemCount(String itemName, Integer count, String user) throws SQLException{
    String sql =
            "UPDATE shoppingcart " +
            "SET count = ? " +
            "WHERE item = ? AND user = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, count);
      preparedStatement.setString(2, itemName);
      preparedStatement.setString(3, user);
      preparedStatement.executeUpdate();
    }
  }

  public void addItem(String user, Item item) throws SQLException{
    String sql =
            "INSERT INTO shoppingcart (user, item, count, cost) " +
                    "VALUES (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, user);
      preparedStatement.setString(2, item.item());
      preparedStatement.setInt(3, item.count());
      preparedStatement.setDouble(4, item.cost());
      preparedStatement.execute();
    }
  }

  private void deleteUsersTable() throws SQLException {
    String sql = "DROP TABLE IF EXISTS users";
    try (Statement statement = connection.createStatement()) {
      statement.execute(sql);
    }
  }

  public void addUser(String name) throws SQLException {
    String sql = "INSERT INTO users (user) VALUES (?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, name);
      preparedStatement.executeUpdate();
    }
  }

  public void deleteUserList(String[] names) throws SQLException {
    StringBuilder sb = new StringBuilder("DELETE FROM users WHERE user IN (");
    for (int i = 0; i < names.length; i++) {
      if (i > 0) {
        sb.append(",");
      }
      sb.append("?");
    }
    sb.append(")");
    String sql = sb.toString();
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      for (int i = 0; i < names.length; i++) {
        preparedStatement.setString(i + 1, names[i]);
      }
      preparedStatement.executeUpdate();
    }
  }



  public List<String> getUsers() throws SQLException {
    String sql = "SELECT user FROM users";

    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ResultSet resultSet = preparedStatement.executeQuery();
    List<String> users = new ArrayList<String>();

    while (resultSet.next()) {
      String user = resultSet.getString("user");
      users.add(user);
    }
    return users;
  }

  public String getUser(String name) throws SQLException {
    String sql = "SELECT user FROM users WHERE user = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, name);
    ResultSet resultSet = preparedStatement.executeQuery();

    while (resultSet.next()) {
      String user = resultSet.getString("user");
      return user;
    }
    return null;
  }

  public DatabaseHelper() throws SQLException {
    connect();
    ensureUsersTable();
    ensureShoppingBasketTable();
  }

}

