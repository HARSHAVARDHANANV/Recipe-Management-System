package com.mycompany.recipe.management.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.mycompany.recipe.management.system.DatabaseConnection;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class RecipeManagerGUI extends JFrame {
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private static final Color SECONDARY_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);     // Green
    private static final Color TEXT_COLOR = new Color(52, 73, 94);         // Dark Gray
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // Off White

    private JTextField recipeNameField;
    private JTextArea ingredientsArea, instructionsArea;
    private JComboBox<String> recipeList;
    private JButton addButton, viewButton, modifyButton;

    public RecipeManagerGUI() {
        setTitle("Recipe Manager Pro");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Custom window decoration
        JPanel titleBar = createTitleBar();
        
        // Main panel with custom styling
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Recipe Selection Panel with styled combo box
        JPanel selectPanel = createSelectionPanel();
        mainPanel.add(selectPanel, BorderLayout.NORTH);

        // Recipe Details Panel with enhanced styling
        JPanel detailsPanel = createDetailsPanel();
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Buttons Panel with modern styling
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add all components to frame
        setLayout(new BorderLayout());
        add(titleBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setupActionListeners();
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(PRIMARY_COLOR);
        titleBar.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel titleLabel = new JLabel("Recipe Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        titleBar.add(titleLabel);
        return titleBar;
    }

    private JPanel createSelectionPanel() {
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        selectPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel selectLabel = new JLabel("Select Recipe:");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectLabel.setForeground(TEXT_COLOR);
        
        recipeList = new JComboBox<>();
        recipeList.setPreferredSize(new Dimension(300, 30));
        recipeList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recipeList.setBackground(Color.WHITE);
        
        selectPanel.add(selectLabel);
        selectPanel.add(recipeList);
        
        populateRecipeList();
        return selectPanel;
    }

   // ... (previous imports and color definitions remain the same)

    private JPanel createDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                "Recipe Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        // Recipe Name Section
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = createStyledLabel("Recipe Name:");
        detailsPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        recipeNameField = createStyledTextField();
        detailsPanel.add(recipeNameField, gbc);

        // Ingredients Section
        gbc.gridy = 2;
        JLabel ingredientsLabel = createStyledLabel("Ingredients:");
        detailsPanel.add(ingredientsLabel, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        ingredientsArea = createStyledTextArea("Enter ingredients (one per line)");
        JScrollPane ingredientsScroll = new JScrollPane(ingredientsArea);
        ingredientsScroll.setPreferredSize(new Dimension(300, 150));
        detailsPanel.add(ingredientsScroll, gbc);

        // Instructions Section
        gbc.gridy = 4;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel instructionsLabel = createStyledLabel("Instructions:");
        detailsPanel.add(instructionsLabel, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        instructionsArea = createStyledTextArea("Enter cooking instructions");
        JScrollPane instructionsScroll = new JScrollPane(instructionsArea);
        instructionsScroll.setPreferredSize(new Dimension(300, 150));
        detailsPanel.add(instructionsScroll, gbc);

        return detailsPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return label;
    }

    // ... (rest of the code remains the same)

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 30));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JTextArea createStyledTextArea(String placeholder) {
        JTextArea area = new JTextArea(6, 30);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        area.setForeground(TEXT_COLOR);
        return area;
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel lblField = new JLabel(label);
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblField.setForeground(TEXT_COLOR);
        panel.add(lblField, gbc);

        gbc.gridx = 0;
        gbc.gridy = row + 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addButton = createStyledButton("Add Recipe", ACCENT_COLOR);
        viewButton = createStyledButton("View Recipes", PRIMARY_COLOR);
        modifyButton = createStyledButton("Modify Recipe", PRIMARY_COLOR);

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(modifyButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void setupActionListeners() {
        addButton.addActionListener(e -> addRecipe());
        viewButton.addActionListener(e -> viewRecipes());
        modifyButton.addActionListener(e -> modifyRecipe());

        // Add recipe list selection listener
        recipeList.addActionListener(e -> {
            String selectedRecipe = (String) recipeList.getSelectedItem();
            if (selectedRecipe != null) {
                loadRecipeDetails(selectedRecipe);
            }
        });
    }

    private void loadRecipeDetails(String recipeName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM recipes WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, recipeName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                recipeNameField.setText(rs.getString("name"));
                ingredientsArea.setText(rs.getString("ingredients"));
                instructionsArea.setText(rs.getString("instructions"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading recipe details.");
        }
    }

    private void addRecipe() {
        if (validateInput()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO recipes (name, ingredients, instructions) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, recipeNameField.getText());
                stmt.setString(2, ingredientsArea.getText());
                stmt.setString(3, instructionsArea.getText());
                stmt.executeUpdate();
                
                showSuccess("Recipe added successfully!");
                clearFields();
                populateRecipeList();
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error adding recipe.");
            }
        }
    }

    private void viewRecipes() {
        JDialog dialog = new JDialog(this, "All Recipes", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JTextArea recipesText = new JTextArea();
        recipesText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recipesText.setEditable(false);
        recipesText.setMargin(new Insets(10, 10, 10, 10));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM recipes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            StringBuilder content = new StringBuilder();
            while (rs.next()) {
                content.append("Recipe: ").append(rs.getString("name")).append("\n\n");
                content.append("Ingredients:\n").append(rs.getString("ingredients")).append("\n\n");
                content.append("Instructions:\n").append(rs.getString("instructions")).append("\n");
                content.append("\n----------------------------------------\n\n");
            }
            recipesText.setText(content.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error fetching recipes.");
        }

        JScrollPane scrollPane = new JScrollPane(recipesText);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    private void modifyRecipe() {
        String selectedRecipe = (String) recipeList.getSelectedItem();
        if (selectedRecipe == null) {
            showError("Please select a recipe to modify.");
            return;
        }

        if (validateInput()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE recipes SET ingredients = ?, instructions = ? WHERE name = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, ingredientsArea.getText());
                stmt.setString(2, instructionsArea.getText());
                stmt.setString(3, selectedRecipe);
                stmt.executeUpdate();
                
                showSuccess("Recipe modified successfully!");
                populateRecipeList();
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error modifying recipe.");
            }
        }
    }

    private boolean validateInput() {
        if (recipeNameField.getText().trim().isEmpty()) {
            showError("Please enter a recipe name.");
            return false;
        }
        if (ingredientsArea.getText().trim().isEmpty()) {
            showError("Please enter ingredients.");
            return false;
        }
        if (instructionsArea.getText().trim().isEmpty()) {
            showError("Please enter instructions.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        recipeNameField.setText("");
        ingredientsArea.setText("");
        instructionsArea.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void populateRecipeList() {
        recipeList.removeAllItems();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT name FROM recipes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                recipeList.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading recipe list.");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new RecipeManagerGUI().setVisible(true);
        });
    }
}