package org.example.reto2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.example.reto2.MainApp;
import org.example.reto2.Util.AppSession;
import org.example.reto2.dao.CopiaDAO;
import org.example.reto2.Model.Copia;
import org.example.reto2.Model.Pelicula;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PrincipalController {

    @FXML private Label lblUsuario;
    @FXML private Button btnNuevaPelicula;
    @FXML private TableView<Copia> tvCopias;
    @FXML private TableColumn<Copia, String> colTitulo;
    @FXML private TableColumn<Copia, String> colEstado;
    @FXML private TableColumn<Copia, String> colSoporte;
    @FXML private ComboBox<String> comboEstado;
    @FXML private ComboBox<String> comboSoporte;

    private final CopiaDAO copiaDAO = new CopiaDAO();
    private final ObservableList<Copia> listaCopias = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        lblUsuario.setText("Colección de: " + AppSession.getUsuarioActual().getNombreUsuario() +
                (AppSession.isAdmin() ? " (ADMIN)" : ""));

        colTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTituloPelicula()));
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        colSoporte.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSoporte()));

        tvCopias.setItems(listaCopias);

        btnNuevaPelicula.setVisible(AppSession.isAdmin());
        btnNuevaPelicula.setManaged(AppSession.isAdmin());

        cargarCopias();
        configurarFiltros();

        tvCopias.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Copia selectedCopia = tvCopias.getSelectionModel().getSelectedItem();
                if (selectedCopia != null) {
                    mostrarVentanaDetalle(selectedCopia);
                }
            }
        });
    }

    /**
     * Ahora es posible filtrar por estado y soporte asi como limpiar los filtros.
     */

    private void configurarFiltros() {
        comboEstado.getItems().clear();
        comboSoporte.getItems().clear();

        comboEstado.getItems().addAll("Todos", "Nueva", "Usada");
        comboSoporte.getItems().addAll("Todos", "Blu-Ray", "DVD");

        comboEstado.getSelectionModel().select("Todos");
        comboSoporte.getSelectionModel().select("Todos");
    }


    @FXML
    private void handleFiltrar() {
        String estado = comboEstado.getValue();
        String soporte = comboSoporte.getValue();

        if ("Todos".equals(estado)) estado = null;
        if ("Todos".equals(soporte)) soporte = null;

        listaCopias.clear();
        listaCopias.addAll(copiaDAO.filtrarCopias(AppSession.getUsuarioActual(), estado, soporte));
    }

    @FXML
    private void handleLimpiarFiltros() {
        comboEstado.getSelectionModel().select("Todos");
        comboSoporte.getSelectionModel().select("Todos");
        cargarCopias();
    }

    private void cargarCopias() {
        listaCopias.clear();
        listaCopias.addAll(copiaDAO.obtenerCopiasPorUsuario(AppSession.getUsuarioActual()));
    }

    @FXML
    private void handleCerrarSesion() {
        AppSession.cerrarSesion();
        try {
            MainApp.mostrarLogin();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo volver a la pantalla de Login.");
        }
    }

    @FXML
    private void handleCerrarAplicacion() {
        MainApp.cerrarAplicacion();
    }

    @FXML
    private void handleEliminarCopia() {
        Copia copiaSeleccionada = tvCopias.getSelectionModel().getSelectedItem();
        if (copiaSeleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("Eliminar Copia de " + copiaSeleccionada.getTituloPelicula());
            alert.setContentText("¿Estás seguro de que quieres eliminar esta copia de la colección? (ID: " + copiaSeleccionada.getId() + ")");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                copiaDAO.eliminarCopia(copiaSeleccionada);
                cargarCopias(); 
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona una copia para eliminar.");
        }
    }

    @FXML
    private void handleAnadirCopia() {
        mostrarDialogoNuevaCopia();
    }

    @FXML
    private void handleAnadirPelicula() {
        if (AppSession.isAdmin()) {
            mostrarDialogoNuevaPelicula();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Acceso Denegado", "Solo los administradores pueden añadir nuevas películas al catálogo.");
        }
    }
    private void mostrarVentanaDetalle(Copia copia) {
        Dialog<Copia> dialog = new Dialog<>();
        dialog.setTitle("Detalle y Edición de Copia");
        dialog.setHeaderText("Modificar copia de: " + copia.getTituloPelicula() + " (ID: " + copia.getId() + ")");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> estadoCombo = new ComboBox<>();
        estadoCombo.getItems().addAll("Nueva", "Usada");
        estadoCombo.setValue(copia.getEstado());

        ComboBox<String> soporteCombo = new ComboBox<>();
        soporteCombo.getItems().addAll("Blu-Ray", "DVD");
        soporteCombo.setValue(copia.getSoporte());

        grid.add(new Label("Título Película:"), 0, 0);
        grid.add(new Label(copia.getTituloPelicula()), 1, 0);
        grid.add(new Label("Director:"), 0, 1);
        grid.add(new Label(copia.getPelicula().getDirector()), 1, 1);
        grid.add(new Label("Estado (Modificable):"), 0, 2);
        grid.add(estadoCombo, 1, 2);
        grid.add(new Label("Soporte (Modificable):"), 0, 3);
        grid.add(soporteCombo, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType botonGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonGuardar, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == botonGuardar) {
                copia.setEstado(estadoCombo.getValue());
                copia.setSoporte(soporteCombo.getValue());
                copiaDAO.guardarCopia(copia); 
                cargarCopias(); 
                return copia;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void mostrarDialogoNuevaCopia() {
        Dialog<Copia> dialog = new Dialog<>();
        dialog.setTitle("Añadir Nueva Copia");
        dialog.setHeaderText("Selecciona película y define los detalles de la copia.");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<Pelicula> cmbPelicula = new ComboBox<>();
        cmbPelicula.setItems(FXCollections.observableArrayList(copiaDAO.obtenerTodasLasPeliculas()));
        cmbPelicula.setCellFactory(lv -> new ListCell<Pelicula>() {
            @Override
            protected void updateItem(Pelicula item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getTitulo() + " (" + item.getAno() + ")");
            }
        });
        cmbPelicula.setButtonCell(cmbPelicula.getCellFactory().call(null));

        ComboBox<String> estadoCombo = new ComboBox<>();
        estadoCombo.getItems().addAll("Nueva", "Usada");
        estadoCombo.setPromptText("Estado");

        ComboBox<String> soporteCombo = new ComboBox<>();
        soporteCombo.getItems().addAll("Blu-Ray", "DVD");
        soporteCombo.setPromptText("Soporte");

        grid.add(new Label("Película:"), 0, 0);
        grid.add(cmbPelicula, 1, 0);
        grid.add(new Label("Estado:"), 0, 1);
        grid.add(estadoCombo, 1, 1);
        grid.add(new Label("Soporte:"), 0, 2);
        grid.add(soporteCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType botonAnadir = new ButtonType("Añadir", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonAnadir, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == botonAnadir && cmbPelicula.getValue() != null) {
                Copia nuevaCopia = new Copia(
                        cmbPelicula.getValue(),
                        AppSession.getUsuarioActual(),
                        estadoCombo.getValue(),
                        soporteCombo.getValue()
                );
                copiaDAO.guardarCopia(nuevaCopia);
                cargarCopias();
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void mostrarDialogoNuevaPelicula() {
        Dialog<Pelicula> dialog = new Dialog<>();
        dialog.setTitle("Añadir Nueva Película (ADMIN)");
        dialog.setHeaderText("Introduce los datos de la nueva película para el catálogo común.");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField tituloField = new TextField();
        tituloField.setPromptText("Título");
        TextField generoField = new TextField();
        generoField.setPromptText("Género");
        TextField anoField = new TextField();
        anoField.setPromptText("Año");
        TextField directorField = new TextField();
        directorField.setPromptText("Director");
        TextField descripcionField = new TextField();
        descripcionField.setPromptText("Descripción");

        grid.add(new Label("Título:"), 0, 0);
        grid.add(tituloField, 1, 0);
        grid.add(new Label("Género:"), 0, 1);
        grid.add(generoField, 1, 1);
        grid.add(new Label("Año:"), 0, 2);
        grid.add(anoField, 1, 2);
        grid.add(new Label("Director:"), 0, 3);
        grid.add(directorField, 1, 3);
        grid.add(new Label("Descripción:"), 0, 4);
        grid.add(descripcionField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        ButtonType botonCrear = new ButtonType("Crear Película", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonCrear, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == botonCrear && !tituloField.getText().isEmpty()) {
                try {
                    Pelicula nuevaPelicula = new Pelicula(
                            tituloField.getText(),
                            generoField.getText(),
                            Integer.parseInt(anoField.getText()),
                            descripcionField.getText(),
                            directorField.getText()
                    );
                    copiaDAO.guardarPelicula(nuevaPelicula); 
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Película creada: " + nuevaPelicula.getTitulo());
                } catch (NumberFormatException e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "El campo Año debe ser un número válido.");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
