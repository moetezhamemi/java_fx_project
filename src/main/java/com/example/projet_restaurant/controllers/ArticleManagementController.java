package com.example.projet_restaurant.controllers;

import com.example.projet_restaurant.models.Article;
import com.example.projet_restaurant.daos.ArticleDAO;
import com.example.projet_restaurant.daos.CategoryDAO;
import com.example.projet_restaurant.models.Category;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleManagementController {
    @FXML
    private TableView<Article> articleTable;
    @FXML
    private TableColumn<Article, String> nameColumn;
    @FXML
    private TableColumn<Article, Double> priceColumn;
    @FXML
    private TableColumn<Article, String> categoryColumn;
    @FXML
    private TableColumn<Article, String> imageColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private Label imageLabel;

    private final ArticleDAO articleDAO = new ArticleDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private ObservableList<Article> articleList = FXCollections.observableArrayList();
    private Map<String, Integer> categoryNameToId = new HashMap<>();
    private Article editingArticle = null;
    private String selectedImagePath = null;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        articleTable.setItems(articleList);
        loadCategories();
        loadArticles();
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            categoryCombo.getItems().clear();
            categoryNameToId.clear();
            for (Category cat : categories) {
                categoryCombo.getItems().add(cat.getNom());
                categoryNameToId.put(cat.getNom(), cat.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadArticles() {
        articleList.setAll(articleDAO.getAllArticles());
    }

    @FXML
    private void handleAdd() {
        clearForm();
        editingArticle = null;
    }

    @FXML
    private void handleEdit() {
        Article selected = articleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            nameField.setText(selected.getNom());
            priceField.setText(String.valueOf(selected.getPrix()));
            categoryCombo.setValue(selected.getCategoryName());
            imageLabel.setText(selected.getImagePath() != null ? selected.getImagePath() : "No image selected");
            selectedImagePath = selected.getImagePath();
            editingArticle = selected;
        }
    }

    @FXML
    private void handleDelete() {
        Article selected = articleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            articleDAO.deleteArticle(selected.getId());
            loadArticles();
            clearForm();
        }
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        Window window = nameField.getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            imageLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleSave() {
        String nom = nameField.getText();
        String prixStr = priceField.getText();
        String categoryName = categoryCombo.getValue();
        String image = selectedImagePath;
        if (nom.isEmpty() || prixStr.isEmpty() || categoryName == null)
            return;
        double prix;
        try {
            prix = Double.parseDouble(prixStr);
        } catch (Exception e) {
            return;
        }
        int categoryId = categoryNameToId.getOrDefault(categoryName, 1);
        if (editingArticle == null) {
            Article article = new Article(0, nom, prix, image, categoryId, categoryName);
            articleDAO.addArticle(article);
        } else {
            editingArticle.setNom(nom);
            editingArticle.setPrix(prix);
            editingArticle.setImagePath(image);
            editingArticle.setCategoryId(categoryId);
            editingArticle.setCategoryName(categoryName);
            articleDAO.updateArticle(editingArticle);
        }
        loadArticles();
        clearForm();
        editingArticle = null;
    }

    @FXML
    private void handleCancel() {
        clearForm();
        editingArticle = null;
    }

    private void clearForm() {
        nameField.clear();
        priceField.clear();
        categoryCombo.setValue(null);
        imageLabel.setText("No image selected");
        selectedImagePath = null;
    }
}