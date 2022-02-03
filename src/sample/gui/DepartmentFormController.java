package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.db.DbException;
import sample.gui.listeners.DataChangeListener;
import sample.gui.util.Alerts;
import sample.gui.util.Constraints;
import sample.gui.util.Utils;
import sample.model.entities.Department;
import sample.model.exceptions.ValidationException;
import sample.model.services.DepartmentService;

import javax.swing.*;
import java.net.URL;
import java.security.PublicKey;
import java.security.Security;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private Department entity;
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListenerList = new ArrayList<>();


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

    public void subscribeDataChangedListener(DataChangeListener dataChangeListener){
        dataChangeListenerList.add(dataChangeListener);
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
            notifyDataChangedListeners();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e){
            setErrorMessage(e.getErrors());
        }
        catch (DbException e){
            Alerts.showAlert("Error saving object", "null", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void notifyDataChangedListeners() {
        for (DataChangeListener dataChangeListener : dataChangeListenerList){
            dataChangeListener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department obj = new Department();

        ValidationException validationException = new ValidationException("Validation Exception");

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            validationException.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        if (validationException.getErrors().size() > 0){
            throw validationException;
        }
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

    private void setErrorMessage(Map<String, String> errorMessage){
        Set<String> fields = errorMessage.keySet();
        if (fields.contains("name")){
            labelErrorName.setText(errorMessage.get("name"));
        }
    }
}
