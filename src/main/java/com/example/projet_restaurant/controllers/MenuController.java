package com.example.projet_restaurant.controllers;

import com.example.projet_restaurant.daos.ArticleDAO;
import com.example.projet_restaurant.models.Article;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.List;

public class MenuController {
    @FXML private FlowPane articlesFlowPane;
    @FXML private ListView<String> orderItemsList;
    @FXML private Label orderTotalLabel;

    private ArticleDAO articleDAO = new ArticleDAO();
    private boolean isAdmin = false; // À définir selon le rôle de l'utilisateur

    @FXML
    public void initialize() {
        loadArticles();
        setupOrderList();
    }

    private void loadArticles() {
        articlesFlowPane.getChildren().clear();
        List<Article> articles = articleDAO.getAllArticles();

        for (Article article : articles) {
            VBox card = createArticleCard(article);
            articlesFlowPane.getChildren().add(card);
        }
    }

    private VBox createArticleCard(Article article) {
        VBox card = new VBox();
        card.getStyleClass().add("article-card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(10));
        card.setSpacing(8);
        card.setPrefWidth(180);

        // Image avec fond de placeholder
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(160, 120);
        imageContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 3px;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        try {
            if (article.getImagePath() != null) {
                imageView.setImage(new Image("file:" + article.getImagePath()));
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("@margherita.jpg")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageContainer.getChildren().add(imageView);

        // Nom et prix
        Label nameLabel = new Label(article.getNom());
        nameLabel.getStyleClass().add("article-name");
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(160);
        nameLabel.setWrapText(true);

        Label priceLabel = new Label(String.format("%.2f DH", article.getPrix()));
        priceLabel.getStyleClass().add("article-price");
        priceLabel.setAlignment(Pos.CENTER);

        // Contrôle de quantité
        HBox quantityBox = new HBox(5);
        quantityBox.setAlignment(Pos.CENTER);

        TextField quantityInput = new TextField();
        quantityInput.setPromptText("Qté");
        quantityInput.getStyleClass().add("quantity-input");
        quantityInput.setAlignment(Pos.CENTER);

        Button addButton = new Button("+");
        addButton.getStyleClass().add("add-btn");
        addButton.setOnAction(e -> {
            try {
                int qty = quantityInput.getText().isEmpty() ? 1 : Integer.parseInt(quantityInput.getText());
                addToOrder(article, qty);
                quantityInput.clear();
            } catch (NumberFormatException ex) {

            }
        });

        quantityBox.getChildren().addAll(quantityInput, addButton);

        card.getChildren().addAll(
                imageContainer,
                nameLabel,
                priceLabel,
                quantityBox
        );

        return card;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void setupOrderList() {
        orderItemsList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);

                    // Ajouter un bouton de suppression pour chaque élément
                    Button deleteBtn = new Button("×");
                    deleteBtn.getStyleClass().add("delete-btn");
                    deleteBtn.setOnAction(e -> {
                        // Logique pour supprimer l'élément de la commande
                        orderItemsList.getItems().remove(item);
                        updateOrderTotal();
                    });

                    HBox hbox = new HBox(new Label(item), new Region(), deleteBtn);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setSpacing(10);
                    HBox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);

                    setGraphic(hbox);
                }
            }
        });
    }

    private void addToOrder(Article article, int quantity) {
        String item = String.format("%s x%d - %.2f DH", article.getNom(), quantity, article.getPrix() * quantity);
        orderItemsList.getItems().add(item);
        updateOrderTotal();
    }

    private void updateOrderTotal() {
        double total = 0.0;
        for (String item : orderItemsList.getItems()) {
            // Extraire le prix total de chaque ligne
            String[] parts = item.split(" - ");
            if (parts.length > 1) {
                String priceStr = parts[1].replace(" DH", "");
                total += Double.parseDouble(priceStr);
            }
        }
        orderTotalLabel.setText(String.format("%.2f DH", total));
    }

    private void editArticle(Article article) {
        // Logique pour éditer l'article
        System.out.println("Édition de l'article: " + article.getNom());
    }

    private void deleteArticle(Article article) {
        // Logique pour supprimer l'article
        System.out.println("Suppression de l'article: " + article.getNom());
    }

    @FXML
    private void confirmOrder() {
        // Logique pour confirmer la commande
        System.out.println("Commande confirmée");
    }

    @FXML
    private void clearOrder() {
        orderItemsList.getItems().clear();
        orderTotalLabel.setText("0.00 DH");
    }
}