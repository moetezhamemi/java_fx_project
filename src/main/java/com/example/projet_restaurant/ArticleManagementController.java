package com.example.projet_restaurant;

import com.example.projet_restaurant.daos.ArticleDAO;
import com.example.projet_restaurant.daos.CategoryDAO;
import com.example.projet_restaurant.models.Article;
import com.example.projet_restaurant.models.Category;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class ArticleManagementController {
    @FXML private TableView<Article> articlesTable;
    @FXML private TextField nomField;
    @FXML private TextField prixField;
    @FXML private TextField imageField;
    @FXML private ComboBox<Category> categoryCombo;

    private ArticleDAO articleDAO;
    private CategoryDAO categoryDAO;

    @FXML
    public void initialize() {
        try {
            articleDAO = new ArticleDAO();
            categoryDAO = new CategoryDAO();

            // Charger les catégories
            List<Category> categories = categoryDAO.getAllCategories();
            categoryCombo.setItems(FXCollections.observableArrayList(categories));

            // Charger les articles
            refreshArticlesTable();

        } catch (SQLException e) {
            showAlert("Erreur de base de données", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void refreshArticlesTable() throws SQLException {
        articlesTable.setItems(FXCollections.observableArrayList(articleDAO.getAllArticles()));
    }

    @FXML
    private void handleAddArticle() {
        try {
            Article article = new Article();
            article.setNom(nomField.getText());
            article.setPrix(Double.parseDouble(prixField.getText()));
            article.setImagePath(imageField.getText());
            article.setCategoryId(categoryCombo.getSelectionModel().getSelectedItem().getId());

            articleDAO.addArticle(article);
            refreshArticlesTable();
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Veuillez entrer un prix valide", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur de base de données", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditArticle() {
        Article selected = articlesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un article à modifier", Alert.AlertType.WARNING);
            return;
        }

        try {
            selected.setNom(nomField.getText());
            selected.setPrix(Double.parseDouble(prixField.getText()));
            selected.setImagePath(imageField.getText());
            selected.setCategoryId(categoryCombo.getSelectionModel().getSelectedItem().getId());

            articleDAO.updateArticle(selected);
            refreshArticlesTable();

        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Veuillez entrer un prix valide", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur de base de données", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteArticle() {
        Article selected = articlesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un article à supprimer", Alert.AlertType.WARNING);
            return;
        }

        try {
            articleDAO.deleteArticle(selected.getId());
            refreshArticlesTable();
            clearFields();

        } catch (SQLException e) {
            showAlert("Erreur de base de données", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void clearFields() {
        nomField.clear();
        prixField.clear();
        imageField.clear();
        categoryCombo.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}