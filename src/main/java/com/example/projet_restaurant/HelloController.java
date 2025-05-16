package com.example.projet_restaurant;

import com.example.projet_restaurant.daos.ArticleDAO;
import com.example.projet_restaurant.models.Article;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.SQLException;

public class HelloController {
    @FXML
    private TableView<Article> articlesTable;
    private ArticleDAO articleDAO = new ArticleDAO();

    @FXML
    private Button consulterCommandesButton;

    @FXML
    private Button ajouterCommandeButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button deconnexionButton;

    @FXML
    private void handleConsulterCommandes() {
        System.out.println("Consulter les commandes clicked");
        // Add your logic here
    }

    @FXML
    private void handleAjouterCommande() {
        System.out.println("Ajouter une commande clicked");
        // Add your logic here
    }

    @FXML
    private void handleDeconnexion() {
        System.out.println("Déconnexion clicked");
        // Add your logic here
    }

    @FXML
    private void handleMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/projet_restaurant/articleManagement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestion des Articles");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddArticle() {
        Article newArticle = new Article();
        // Configurez votre article ici
        articleDAO.addArticle(newArticle);
        refreshArticlesTable();
    }

    private void refreshArticlesTable() {
        articlesTable.getItems().setAll(articleDAO.getAllArticles());
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}