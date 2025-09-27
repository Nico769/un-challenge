package io.unravel.challenge.fifth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class DatabaseManager {
    private static final String INSERT_INTO_ISSUE_ID_TITLE_VALUES = "INSERT INTO issue (id, title) VALUES (?, ?)";
    private static final String SELECT_COUNT_FROM_ISSUE = "SELECT COUNT(*) FROM issue";

    @Autowired
    private DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long countIssues() throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(SELECT_COUNT_FROM_ISSUE);
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } finally {
            closeConnection(conn);
        }
    }

    public void saveIssue() throws SQLException {
        Connection conn = null;
        try {
            conn = getConnection();
            try (PreparedStatement ps = conn.prepareStatement(INSERT_INTO_ISSUE_ID_TITLE_VALUES)) {
                String uuid = UUID.randomUUID().toString();
                ps.setString(1, uuid);
                ps.setString(2, "An issue title - " + uuid);
                ps.executeUpdate();
            }
        } finally {
            closeConnection(conn);
        }
    }
}
