package com.example.projet_restaurant.controllers;

import com.example.projet_restaurant.daos.UserDAO;
import com.example.projet_restaurant.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AuthController {
    @FXML
    private VBox loginPane;
    @FXML
    private VBox registerPane;
    @FXML
    private Button switchButton;
    @FXML
    private Label sideTitle;

    // Login fields
    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Label loginError;

    // Register fields
    @FXML
    private TextField registerFirstName;
    @FXML
    private TextField registerLastName;
    @FXML
    private TextField registerEmail;
    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField registerConfirmPassword;
    @FXML
    private Label registerError;

    private boolean isLogin = true;
    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        showLogin();
    }

    @FXML
    private void handleSwitch() {
        if (isLogin) {
            showRegister();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        loginPane.setVisible(true);
        loginPane.setManaged(true);
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        sideTitle.setText("You want to create an account ?");
        switchButton.setText("Create new Acount");
        isLogin = true;
    }

    private void showRegister() {
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        registerPane.setVisible(true);
        registerPane.setManaged(true);
        sideTitle.setText("Already have an account ?");
        switchButton.setText("click here to login");
        isLogin = false;
    }

    @FXML
    private void handleLogin() {
        loginError.setText("");
        String username = loginUsername.getText();
        String password = loginPassword.getText();
        if (username.isEmpty() || password.isEmpty()) {
            loginError.setText("Please fill all fields");
            return;
        }
        User user = userDAO.loginUser(username, password);
        if (user != null) {
            loginError.setText("Login successful! Welcome, " + user.getRole());
            // TODO: Rediriger vers le dashboard selon le rôle
        } else {
            loginError.setText("Invalid username or password");
        }
    }

    @FXML
    private void handleRegister() {
        registerError.setText("");
        String firstName = registerFirstName.getText();
        String lastName = registerLastName.getText();
        String email = registerEmail.getText();
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();
        String role = "CLIENT";
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty()) {
            registerError.setText("Please fill all fields");
            return;
        }
        if (!password.equals(confirmPassword)) {
            registerError.setText("Passwords do not match");
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            registerError.setText("Invalid email format");
            return;
        }
        User user = new User(username, password, email, role, firstName, lastName);
        boolean success = userDAO.registerUser(user);
        if (success) {
            registerError.setText("Registration successful! You can now log in.");
            clearRegisterFields();
            showLogin();
        } else {
            registerError.setText("Registration failed. Username or email may already exist.");
        }
    }

    private void clearRegisterFields() {
        registerFirstName.clear();
        registerLastName.clear();
        registerEmail.clear();
        registerUsername.clear();
        registerPassword.clear();
        registerConfirmPassword.clear();
        registerError.setText("");
    }
}