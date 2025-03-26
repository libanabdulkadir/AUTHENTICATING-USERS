package org.example;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


import static org.junit.jupiter.api.Assertions.*;

class UserAppTest {

    @Test
    void testStyleComponent() {
        UserApp app = new UserApp();
        JTextField testField = new JTextField();
        app.styleComponent(testField);

        assertEquals(new Font("Arial", Font.PLAIN, 15), testField.getFont());
        assertEquals(new Dimension(200, 35), testField.getPreferredSize());
    }

    @Test
    void CreateLoginPanel() {
        UserApp app = new UserApp();
        JPanel loginPanel = app.createLoginPanel();

        assertNotNull(loginPanel, "Login panel should not be null.");
        assertEquals(3, loginPanel.getComponentCount());
    }

    @Test
    void CreateSignUpPanel() {
        UserApp app = new UserApp();
        JPanel signUpPanel = app.createSignUpPanel();

        assertNotNull(signUpPanel, "Sign-up panel should not be null.");
        assertTrue(signUpPanel.getLayout() instanceof BorderLayout);
        assertEquals(2, signUpPanel.getComponentCount());
    }



    @Test
    void testHandleSignUp() {
        UserApp app = new UserApp();

        app.regUsernameField = new JTextField("Abdallah");
        app.emailField = new JTextField("Abdallah@gmail.com");
        app.phoneField = new JTextField("097654");
        app.regPasswordField = new JPasswordField("566");

        app.handleSignUp();

        // validate login after registration
        assertTrue(DatabaseHelper.validateLogin("Abdallah", "566"), "User should be able to log in after sign-up.");
    }

    @Test
    void testHandleLogin() {
        UserApp app = new UserApp();

        boolean result = app.handleLogin("Abdallah", "566");
        assertTrue(result, "Login should be successful with correct credentials");
    }

    @Test
    void testCreateCenteredPanel() {
        UserApp app = new UserApp();
        JPanel testPanel = new JPanel();
        JPanel centeredPanel = app.CenteredPanel(testPanel);

        assertNotNull(centeredPanel, "Centered panel should not be null.");
        assertEquals(1, centeredPanel.getComponentCount());
    }

    @Test
    public void testRegisterUser() {
        String username = "jamac";
        String email = "jamac@gmail.com";
        String phone = "08766";
        String password = "5521";

        // Register the user and check the result
        boolean result = DatabaseHelper.registerUser(username, email, phone, password);
        assertTrue(result, "User should be registered successfully");

        // check if the user is in the database
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
    }

    @Test
    public void testValidateLogin() {

        String username = "jamac";
        String password = "5521";

        // test correct login
        boolean validLogin = DatabaseHelper.validateLogin(username, password);
        assertTrue(validLogin, "Login should be valid for correct credentials");

        boolean invalidLogin = DatabaseHelper.validateLogin(username, "wrongpassword");
        assertFalse(invalidLogin, "Login should fail with incorrect password");
    }




    @Test
    void CreateUI() {
        StudentMS studentMS = new StudentMS();
        studentMS.createUI();
        assertAll("UI components",
                () -> assertNotNull(studentMS.cardPanel),
                () -> assertEquals(3, studentMS.cardPanel.getComponentCount())
        );
    }


    @Test
    void CreateMainMenu() {
        StudentMS studentMS = new StudentMS();
        JPanel mainMenu = studentMS.createMainMenu();

        assertAll("Main menu checks",
                () -> assertNotNull(mainMenu),
                () -> assertEquals(2, mainMenu.getComponentCount()), // we expect two components tittle and buttonwrapper
                () -> assertTrue(mainMenu.getComponent(0) instanceof JLabel),
                () -> assertTrue(mainMenu.getComponent(1) instanceof JPanel),
                () -> assertEquals("Student Management System", ((JLabel) mainMenu.getComponent(0)).getText())
        );
    }



    @Test
    void CreateInputPanel() {
        StudentMS studentMS = new StudentMS();
        JPanel inputPanel = studentMS.createInputPanel();

        assertNotNull(inputPanel);

        JPanel inputFormControl = (JPanel) inputPanel.getComponent(0);
        assertEquals(2, inputFormControl.getComponentCount());

        JPanel formInput = (JPanel) inputFormControl.getComponent(0);
        assertEquals(10, formInput.getComponentCount());

        JPanel buttonPanel = (JPanel) inputFormControl.getComponent(1);
        assertTrue(buttonPanel.getComponentCount() >= 3);
    }


    @Test
    void CreateViewPanel() {
        StudentMS studentMS = new StudentMS();
        JPanel viewPanel = studentMS.createViewPanel();
        assertAll("View panel checks",
                () -> assertNotNull(viewPanel),
                () -> assertTrue(viewPanel instanceof JPanel),
                () -> assertNotNull(studentMS.displayArea)
        );
    }

    @Test
    void UpdateStudentList() {
        StudentMS studentMS = new StudentMS();
        studentMS.createUI();
        studentMS.studentRecords.add("Name: ALi\nCourse: Computer Science\nGender: Male\nBirth Date: 2000-01-01\nAddress: Kampala");

        studentMS.updateStudentList();

        // Get the update text from the display area
        String updatedText = studentMS.displayArea.getText().trim();

        // Define the expected output
        String expectedText = "Student Records:\n\n" +
                "Name: ALi\nCourse: Computer Science\nGender: Male\nBirth Date: 2000-01-01\nAddress: Kampala\n" +
                "-".repeat(50);

        // Display area matches the expected output
        assertEquals(expectedText, updatedText);
    }


    @Test
    void ToggleDarkMode() {
        SwingUtilities.invokeLater(() -> {
            StudentMS studentMS = new StudentMS();

            JFrame frame = studentMS.frame;
            frame.setVisible(true);
            assertDoesNotThrow(() -> studentMS.toggleDarkMode());

            assertDoesNotThrow(() -> {
                studentMS.toggleDarkMode();
                assertNotNull(studentMS.frame.getContentPane());
            });
        });
    }

}
