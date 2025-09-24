package com.tutorials.dao;

import com.tutorials.dao.impl.TutorialDAOImpl;
import com.tutorials.entity.Tutorial;
import com.tutorials.exceptions.DataBaseOperationException;
import com.tutorials.exceptions.TutorialNotFoundException;
import com.tutorials.util.DBConnection;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TutorialDAOImpl Integration Tests")
class TutorialDAOImplTest {
    private TutorialDAO tutorialDAO;

    @BeforeEach
    void setUp() throws SQLException {
        tutorialDAO = new TutorialDAOImpl();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM tutorials");
        } catch (SQLException e) {
            System.err.println("Failed to clear DB before test: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void shouldAddTutorialSuccessfully() throws DataBaseOperationException, TutorialNotFoundException {
        Tutorial tutorial = new Tutorial("Test Title","Test Author","http://test.com", LocalDate.now());
        tutorialDAO.addTutorial(tutorial);
        assertNotEquals(0, tutorial.getId());
        Tutorial retrieved = tutorialDAO.getTutorialById(tutorial.getId());
        assertEquals(tutorial.getTitle(), retrieved.getTitle());
    }

}
