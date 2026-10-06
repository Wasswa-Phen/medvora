package com.medvora.desktopclient.controller;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Canvas stockDonutCanvas;

    @FXML
    private Canvas issuesLineChartCanvas;

    @FXML
    private ComboBox<String> storeComboBox;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnInventory;

    @FXML
    private Button btnStockOperations;

    @FXML
    private Button btnReplenishment;

    @FXML
    private Button btnSuppliers;

    @FXML
    private Button btnAlerts;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnAdministration;

    @FXML
    private Label totalMedicinesLabel;

    @FXML
    public void initialize() {
        // Initialize Store selector
        if (storeComboBox != null) {
            storeComboBox.getItems().addAll("Main Store", "Emergency Care Unit", "Pediatric Pharmacy");
            storeComboBox.setValue("Main Store");
        }

        // Draw the charts
        drawDonutChart();
        drawIssuesLineChart();
    }

    private void drawDonutChart() {
        if (stockDonutCanvas == null) return;
        GraphicsContext gc = stockDonutCanvas.getGraphicsContext2D();
        double w = stockDonutCanvas.getWidth();
        double h = stockDonutCanvas.getHeight();

        gc.clearRect(0, 0, w, h);

        double centerX = w / 2.0;
        double centerY = h / 2.0;
        double radius = Math.min(centerX, centerY) - 8;
        double lineWidth = 15.0;

        gc.setLineWidth(lineWidth);
        gc.setLineCap(StrokeLineCap.BUTT);

        // 12 medicines total:
        // In stock: 8 / 12 = 66.7% -> 240 deg (Teal / Green)
        // Low stock: 3 / 12 = 25.0% -> 90 deg (Amber / Orange)
        // Out of stock: 1 / 12 = 8.3% -> 30 deg (Coral / Red)

        // Small gap between slices: 3 degrees
        double gap = 3.5;

        // In stock slice (Green #0E9384)
        gc.setStroke(Color.web("#0E9384"));
        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                -30 + gap, 240 - gap * 2, ArcType.OPEN);

        // Low stock slice (Amber #F79009)
        gc.setStroke(Color.web("#F79009"));
        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                210 + gap, 90 - gap * 2, ArcType.OPEN);

        // Out of stock slice (Red #F04438)
        gc.setStroke(Color.web("#F04438"));
        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                300 + gap, 30 - gap * 2, ArcType.OPEN);
    }

    private void drawIssuesLineChart() {
        if (issuesLineChartCanvas == null) return;
        GraphicsContext gc = issuesLineChartCanvas.getGraphicsContext2D();
        double w = issuesLineChartCanvas.getWidth();
        double h = issuesLineChartCanvas.getHeight();

        gc.clearRect(0, 0, w, h);

        double leftPadding = 30;
        double rightPadding = 20;
        double topPadding = 15;
        double bottomPadding = 28;

        double plotWidth = w - leftPadding - rightPadding;
        double plotHeight = h - topPadding - bottomPadding;

        // Y-axis grid lines (0, 20, 40, 60)
        gc.setStroke(Color.web("#F2F4F7"));
        gc.setLineWidth(1.0);
        gc.setFill(Color.web("#98A2B3"));
        gc.setFont(javafx.scene.text.Font.font("Segoe UI", 10));

        double[] yValues = {0, 20, 40, 60};
        for (double yVal : yValues) {
            double yPos = topPadding + plotHeight - (yVal / 60.0 * plotHeight);
            gc.strokeLine(leftPadding, yPos, w - rightPadding, yPos);
            gc.fillText(String.valueOf((int) yVal), 8, yPos + 4);
        }

        // Data points: 7 days
        // [12 Sep: 20, 13 Sep: 30, 14 Sep: 25, 15 Sep: 42, 16 Sep: 35, 17 Sep: 46, 18 Sep: 30]
        double[] xDates = {0, 1, 2, 3, 4, 5, 6};
        double[] values = {20, 30, 25, 42, 35, 46, 30};

        double[] xCoords = new double[values.length];
        double[] yCoords = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            xCoords[i] = leftPadding + (i / 6.0) * plotWidth;
            yCoords[i] = topPadding + plotHeight - (values[i] / 60.0 * plotHeight);
        }

        // Gradient fill under curve
        gc.beginPath();
        gc.moveTo(xCoords[0], topPadding + plotHeight);
        for (int i = 0; i < values.length; i++) {
            gc.lineTo(xCoords[i], yCoords[i]);
        }
        gc.lineTo(xCoords[values.length - 1], topPadding + plotHeight);
        gc.closePath();

        LinearGradient areaGradient = new LinearGradient(
                0, topPadding, 0, topPadding + plotHeight, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#155EEF", 0.15)),
                new Stop(1, Color.web("#155EEF", 0.01))
        );
        gc.setFill(areaGradient);
        gc.fill();

        // Blue Line
        gc.setStroke(Color.web("#155EEF"));
        gc.setLineWidth(2.2);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        gc.beginPath();
        gc.moveTo(xCoords[0], yCoords[0]);
        for (int i = 1; i < values.length; i++) {
            gc.lineTo(xCoords[i], yCoords[i]);
        }
        gc.stroke();

        // Data dots
        for (int i = 0; i < values.length; i++) {
            if (i == values.length - 1) {
                // Highlight last point (18 Sep with label 30)
                gc.setFill(Color.web("#155EEF"));
                gc.fillOval(xCoords[i] - 5, yCoords[i] - 5, 10, 10);
                gc.setFill(Color.WHITE);
                gc.fillOval(xCoords[i] - 2.5, yCoords[i] - 2.5, 5, 5);

                // Label 30 above point
                gc.setFill(Color.web("#155EEF"));
                gc.setFont(javafx.scene.text.Font.font("Segoe UI", javafx.scene.text.FontWeight.BOLD, 11));
                gc.fillText("30", xCoords[i] + 6, yCoords[i] - 4);
            } else {
                gc.setFill(Color.web("#155EEF"));
                gc.fillOval(xCoords[i] - 3.5, yCoords[i] - 3.5, 7, 7);
                gc.setFill(Color.WHITE);
                gc.fillOval(xCoords[i] - 1.5, yCoords[i] - 1.5, 3, 3);
            }
        }

        // X-axis date labels: 12 Sep, 14 Sep, 16 Sep, 18 Sep
        gc.setFill(Color.web("#667085"));
        gc.setFont(javafx.scene.text.Font.font("Segoe UI", 10));
        gc.fillText("12 Sep", xCoords[0] - 8, h - 8);
        gc.fillText("14 Sep", xCoords[2] - 12, h - 8);
        gc.fillText("16 Sep", xCoords[4] - 12, h - 8);
        gc.fillText("18 Sep", xCoords[6] - 14, h - 8);
    }

    @FXML
    public void handleSignOut(ActionEvent event) {
        Stage stage = (Stage) btnDashboard.getScene().getWindow();
        SceneNavigator.navigate(stage, SceneNavigator.SIGN_IN_VIEW);
    }

    @FXML
    public void handleLockApp(ActionEvent event) {
        Stage stage = (Stage) btnDashboard.getScene().getWindow();
        SceneNavigator.navigate(stage, SceneNavigator.SIGN_IN_VIEW);
    }

    @FXML
    public void handleIssueStock(ActionEvent event) {
        System.out.println("Issue Stock modal triggered");
    }

    @FXML
    public void handleReceiveStock(ActionEvent event) {
        System.out.println("Receive Stock modal triggered");
    }

    @FXML
    public void handleReviewQueue(ActionEvent event) {
        System.out.println("Review approval queue triggered");
    }

    @FXML
    public void handleViewAllExpiry(ActionEvent event) {
        System.out.println("View upcoming expiry list triggered");
    }

    @FXML
    public void handleViewExpiredBatches(ActionEvent event) {
        System.out.println("View expired batch details triggered");
    }

    @FXML
    public void handleViewAllAttention(ActionEvent event) {
        System.out.println("View all needs attention triggered");
    }

    @FXML
    public void handleViewAllActivity(ActionEvent event) {
        System.out.println("View all recent activity triggered");
    }
}
