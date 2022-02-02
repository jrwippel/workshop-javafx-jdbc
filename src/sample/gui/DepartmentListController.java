package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.gui.util.Alerts;
import sample.gui.util.Utils;
import sample.model.entities.Department;
import sample.model.services.DepartmentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    //Injeção de dependencias
    private DepartmentService service;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private Button btNew;

    private ObservableList<Department> observableList;

    @FXML
    public void onBtNewAction(ActionEvent actionEvent){
        Stage parentStage = Utils.currentStage(actionEvent);
        Department obj = new Department();

        createDialogForm(obj,"/sample/gui/DepartmentForm.fxml", parentStage);
    }

    //Inversão de controle
    public void setDepartmentService (DepartmentService departmentService){
        this.service = departmentService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    private void initializeNodes() {

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service null");
        }
        List<Department> departmentList = service.findAll();
        observableList = FXCollections.observableArrayList(departmentList);
        tableViewDepartment.setItems(observableList);
    }

    public void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = fxmlLoader.load();

            DepartmentFormController departmentFormController = fxmlLoader.getController();
            departmentFormController.setDepartment(obj);
            departmentFormController.setDepartmentService(new DepartmentService());
            departmentFormController.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department Data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        }catch (IOException e){
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }



    }
}
