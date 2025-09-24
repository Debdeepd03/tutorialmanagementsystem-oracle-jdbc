package com.tutorials.dao.impl;
import com.tutorials.dao.TutorialDAO;
import com.tutorials.entity.Tutorial;
import com.tutorials.exceptions.DataBaseOperationException;
import com.tutorials.exceptions.TutorialNotFoundException;
import com.tutorials.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TutorialDAOImpl implements TutorialDAO {

    private com.tutorials.entity.Tutorial extractTutorialFromResultSet(ResultSet rs) throws SQLException {
        Tutorial tutorial = new Tutorial();
        tutorial.setId(rs.getInt("tutorial_id"));
        tutorial.setTitle(rs.getString("title"));
        tutorial.setAuthor(rs.getString("author"));
        tutorial.setUrl(rs.getString("url"));
        Date sqlDate = rs.getDate("published_date");
        if (sqlDate != null) {
            tutorial.setPublishedDate(sqlDate.toLocalDate());
        }
        return tutorial;
    }

    @Override
    public void addTutorial(Tutorial tutorial) throws DataBaseOperationException {
        String SQL = "INSERT INTO tutorials (title, author, url, published_date) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL, new String[]{"tutorial_id"});
            pstmt.setString(1, tutorial.getTitle());
            pstmt.setString(2, tutorial.getAuthor());
            pstmt.setString(3, tutorial.getUrl());
            pstmt.setDate(4, tutorial.getPublishedDate() != null ? Date.valueOf(tutorial.getPublishedDate()) : null);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataBaseOperationException("Creating tutorial failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tutorial.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating tutorial failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataBaseOperationException("Error adding tutorial: " + e.getMessage(), e);
        } finally {
            DBConnection.closeConnection(conn);
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { /* log */ } }
        }
    }

    @Override
    public Tutorial getTutorialById(int id) throws TutorialNotFoundException, DataBaseOperationException {
        String SQL = "SELECT * FROM tutorials WHERE tutorial_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTutorialFromResultSet(rs);
            } else {
                throw new TutorialNotFoundException("Tutorial with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new DataBaseOperationException("Error retrieving tutorial by ID: " + e.getMessage(), e);
        } finally {
            DBConnection.closeConnection(conn);
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { } }
            if (rs != null) { try { rs.close(); } catch (SQLException e) { } }
        }
    }

    @Override
    public ArrayList<Tutorial> getAllTutorials() throws DataBaseOperationException {
        ArrayList<Tutorial> tutorials = new ArrayList<>();
        String SQL = "SELECT * FROM tutorials ORDER BY tutorial_id";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                tutorials.add(extractTutorialFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DataBaseOperationException("Error retrieving all tutorials: " + e.getMessage(), e);
        } finally {
            DBConnection.closeConnection(conn);
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { } }
            if (rs != null) { try { rs.close(); } catch (SQLException e) { } }
        }
        return tutorials;
    }

    @Override
    public void updateTutorial(Tutorial tutorial) throws TutorialNotFoundException, DataBaseOperationException {
        String SQL = "UPDATE tutorials SET title = ?, author = ?, url = ?, published_date = ? WHERE tutorial_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, tutorial.getTitle());
            pstmt.setString(2, tutorial.getAuthor());
            pstmt.setString(3, tutorial.getUrl());
            pstmt.setDate(4, tutorial.getPublishedDate() != null ? Date.valueOf(tutorial.getPublishedDate()) : null);
            pstmt.setInt(5, tutorial.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new TutorialNotFoundException("Tutorial with ID " + tutorial.getId() + " not found for update.");
            }
        } catch (SQLException e) {
            throw new DataBaseOperationException("Error updating tutorial: " + e.getMessage(), e);
        } finally {
            DBConnection.closeConnection(conn);
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { } }
        }
    }

    @Override
    public void deleteTutorial(int id) throws TutorialNotFoundException, DataBaseOperationException {
        String SQL = "DELETE FROM tutorials WHERE tutorial_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new TutorialNotFoundException("Tutorial with ID " + id + " not found for deletion.");
            }
        } catch (SQLException e) {
            throw new DataBaseOperationException("Error deleting tutorial: " + e.getMessage(), e);
        } finally {
            DBConnection.closeConnection(conn);
            if (pstmt != null) { try { pstmt.close(); } catch (SQLException e) { } }
        }
    }
}
