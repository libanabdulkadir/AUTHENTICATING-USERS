package org.example;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class UserAppIntegrationTest {

    @Test
    void testFullUserAppIntegration() {
        UserApp app = new UserApp();

        // test styleComponent

        JTextField testField = new JTextField();
        app.styleComponent(testField);
        assertEquals(new Font("Arial", Font.PLAIN, 15), testField.getFont(), "Font should be Arial, size 15.");
        assertEquals(new Dimension(200, 35), testField.getPreferredSize(), "Preferred size should be 200x35.");



        //test createLoginPanel
        JPanel loginPanel = app.createLoginPanel();
        assertNotNull(loginPanel, "Login panel should not be null.");
        assertEquals(3, loginPanel.getComponentCount(), "Login panel should have 3 components.");

        // test createSignUpPanel
        JPanel signUpPanel = app.createSignUpPanel();
        assertNotNull(signUpPanel, "Sign-up panel should not be null.");
        assertEquals(2, signUpPanel.getComponentCount(), "Sign-up panel should have 2 components.");


        // test created centerPanel
        JPanel testPanel = new JPanel();
        JPanel centeredPanel = app.CenteredPanel(testPanel);
        assertNotNull(centeredPanel, "Centered panel should not be null.");
        assertEquals(1, centeredPanel.getComponentCount(), "Centered panel should have 1 component.");

        //test HandleSignUp
        app.regUsernameField = new JTextField("Abdalla");
        app.emailField = new JTextField("Abdalla@gmail.com");
        app.phoneField = new JTextField("0987654");
        app.regPasswordField = new JPasswordField("5566");
        app.handleSignUp();
        assertTrue(DatabaseHelper.validateLogin("Abdalla", "5566"), "Login should be successful after sign-up.");

        // test database Registration
        String username = "jama";
        String email = "jama@gmail.com";
        String phone = "098766";
        String password = "5511";
        boolean result = DatabaseHelper.registerUser(username, email, phone, password);
        assertTrue(result, "User should be registered successfully.");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb", "root", "")) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next(), "User should exist in the database");
            String hashedPassword = rs.getString("password");
            assertNotNull(hashedPassword, "Hashed password should be stored in the database");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed");
        }

        // test validationLogin
        boolean validLogin = DatabaseHelper.validateLogin(username, password);
        assertTrue(validLogin, "Login should be valid for correct credentials.");
        boolean invalidLogin = DatabaseHelper.validateLogin(username, "wrongpassword");
        assertFalse(invalidLogin, "Login should fail for incorrect credentials.");

        // Initialize StudentMsS for UI
        StudentMS studentMS = new StudentMS();
        studentMS.createUI();

        assertAll("UI components",
                () -> assertNotNull(studentMS.cardPanel, "Card panel should not be null."),
                () -> assertEquals(3, studentMS.cardPanel.getComponentCount(), "Card panel should have 3 components.")
        );

        // Test CreateMainMenu
        JPanel mainMenu = studentMS.createMainMenu();
        assertAll("Main menu checks",
                () -> assertNotNull(mainMenu, "Main menu should not be null."),
                () -> assertEquals(2, mainMenu.getComponentCount(), "Main menu should have 2 components."),
                () -> assertTrue(mainMenu.getComponent(0) instanceof JLabel, "First component should be JLabel."),
                () -> assertTrue(mainMenu.getComponent(1) instanceof JPanel, "Second component should be JPanel."),
                () -> assertEquals("Student Management System", ((JLabel) mainMenu.getComponent(0)).getText(), "Title should be correct.")
        );

        // test createInputPanel
        JPanel inputPanel = studentMS.createInputPanel();
        assertNotNull(inputPanel, "Input panel should not be null.");
        JPanel inputFormControl = (JPanel) inputPanel.getComponent(0);
        assertEquals(2, inputFormControl.getComponentCount(), "Input form control should have 2 components.");
        JPanel formInput = (JPanel) inputFormControl.getComponent(0);
        assertEquals(10, formInput.getComponentCount(), "Form input should have 10 components.");
        JPanel buttonPanel = (JPanel) inputFormControl.getComponent(1);
        assertTrue(buttonPanel.getComponentCount() >= 3, "Button panel should have at least 3 components.");

        // test createViewPanel
        JPanel viewPanel = studentMS.createViewPanel();
        assertAll("View panel checks",
                () -> assertNotNull(viewPanel, "View panel should not be null."),
                () -> assertTrue(viewPanel instanceof JPanel, "View panel should be an instance of JPanel."),
                () -> assertNotNull(studentMS.displayArea, "Display area should not be null.")
        );

        // test Updating The Student list
        studentMS.studentRecords.add("Name: ALi\nCourse: Computer Science\nGender: Male\nBirth Date: 2000-01-01\nAddress: Kampala");
        studentMS.updateStudentList();
        String updatedText = studentMS.displayArea.getText().trim();
        String expectedText = "Student Records:\n\n" +
                "Name: ALi\nCourse: Computer Science\nGender: Male\nBirth Date: 2000-01-01\nAddress: Kampala\n" +
                "-".repeat(50);
        assertEquals(expectedText, updatedText, "Student list display should match expected output.");

        // Test dark mode toggle
        SwingUtilities.invokeLater(() -> {
            JFrame frame = studentMS.frame;
            frame.setVisible(true);
            assertDoesNotThrow(() -> studentMS.toggleDarkMode(), "Dark mode toggle should not throw exception.");
            assertDoesNotThrow(() -> {
                studentMS.toggleDarkMode(); // Toggle again
                assertNotNull(studentMS.frame.getContentPane(), "Frame content should not be null after dark mode toggle.");
            });
        });
    }
}
