package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.db.DbException;
import sample.gui.util.Alerts;
import sample.gui.util.Constraints;
import sample.gui.util.Utils;
import sample.model.entities.Department;
import sample.model.services.DepartmentService;

import javax.swing.*;
import java.net.URL;
import java.security.PublicKey;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;
    private DepartmentService departmentService;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setDepartment(Department entity){
        this.entity = entity;
    }
    public void setDepartmentService(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){

        if (entity == null){
            throw new IllegalStateException("Entity was null");
        }
        if (departmentService == null){
            throw new IllegalStateException("Department Service was null");
        }
        try {
            entity = getFormData();
            departmentService.saveOrUpdate(entity);
            Utils.currentStage(event).close();
        }catch (DbException e){
            Alerts.showAlert("Error saving object", "null", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setName(txtName.getText());
        return obj;

    }

    @FXML
    public void onBtCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitializeNodes();
    }

    public void InitializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
    public void updateFormData(){

        if (entity == null){
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }
}
