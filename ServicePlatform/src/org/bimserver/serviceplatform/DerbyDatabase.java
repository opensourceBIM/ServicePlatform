package org.bimserver.serviceplatform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyDatabase implements Database {
	private Connection conn;
	private boolean isNew;

	public DerbyDatabase(Path path) {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			isNew = !Files.exists(path);
			conn = DriverManager.getConnection("jdbc:derby:" + path.toString() + ";create=true");
			if (isNew) {
				initStructure();
			}
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
	
	private void initStructure() {
		try {
			conn.prepareStatement("CREATE TABLE userx (id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), username VARCHAR(32) NOT NULL, password VARCHAR(32) NOT NULL, PRIMARY KEY (id))").executeUpdate();
			conn.prepareStatement("CREATE TABLE application (id VARCHAR(32) NOT NULL, name VARCHAR(32) NOT NULL, description VARCHAR(1024) NOT NULL, icon VARCHAR(1024) NOT NULL, "
					+ "secret VARCHAR(64) NOT NULL, url VARCHAR(1024) NOT NULL, redirectUrl VARCHAR(1024) NOT NULL, PRIMARY KEY (id))").executeUpdate();
			conn.prepareStatement("CREATE TABLE user_application (user_id INT NOT NULL, application_id VARCHAR(32) NOT NULL, PRIMARY KEY (user_id, application_id))").executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void initDemoData() {
		addUser("user1", "user1");
	}

	private void addUser(String username, String password) {
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO userx (username, password) VALUES (?, ?)");
			statement.setString(1, username);
			statement.setString(2, password);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() throws SQLException {
		conn.close();
	}

	@Override
	public User getUserById(long userId) {
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM userx WHERE id=?");
			statement.setLong(1, userId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				User user = new User();
				user.setId(userId);
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User getUserByUserName(String username) {
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM userx WHERE username=?");
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getLong("id"));
				user.setUsername(resultSet.getString("username"));
				user.setPassword(resultSet.getString("password"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void registerApplication(String clientId, String clientSecret, String clientName, String clientUrl, String clientDescription, String redirectURI, String clientIcon) {
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO application (id, secret, name, url, description, redirectUrl, icon) VALUES (?, ?, ?, ?, ?, ?, ?)");
			statement.setString(1, clientId);
			statement.setString(2, clientSecret);
			statement.setString(3, clientName);
			statement.setString(4, clientUrl);
			statement.setString(5, clientDescription);
			statement.setString(6, redirectURI);
			statement.setString(7, clientIcon);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Application getApplicationById(String applicationId) {
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM application WHERE id=?");
			statement.setString(1, applicationId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Application application = new Application();
				application.setId(resultSet.getString("id"));
				application.setClientDescript1ion(resultSet.getString("description"));
				application.setClientIcon(resultSet.getString("icon"));
				application.setClientName(resultSet.getString("name"));
				application.setClientSecret(resultSet.getString("secret"));
				application.setClientUrl(resultSet.getString("url"));
				application.setRedirectURI(resultSet.getString("redirectUrl"));
				return application;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}
	
	public void allowApplication(long userId, String applicationId) {
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO user_application (user_id, application_id) VALUES (?, ?)");
			statement.setLong(1, userId);
			statement.setString(2, applicationId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean userAllowsApplication(long userId, String applicationId) {
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM user_application WHERE user_id=? AND application_id=?");
			statement.setLong(1, userId);
			statement.setString(2, applicationId);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}