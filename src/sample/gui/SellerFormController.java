package sample.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import sample.db.DbException;
import sample.gui.listeners.DataChangeListener;
import sample.gui.util.Alerts;
import sample.gui.util.Constraints;
import sample.gui.util.Utils;
import sample.model.entities.Department;
import sample.model.entities.Seller;
import sample.model.exceptions.ValidationException;
import sample.model.services.DepartmentService;
import sample.model.services.SellerService;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;
    private SellerService sellerService;
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListenerList = new ArrayList<>();


    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBaseSalary;

    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;


    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList <Department> observableList;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setServices(SellerService sellerService, DepartmentService departmentService) {
        this.sellerService = sellerService;
        this.departmentService = departmentService;
    }

    public void subscribeDataChangedListener(DataChangeListener dataChangeListener) {
        dataChangeListenerList.add(dataChangeListener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {

        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (sellerService == null) {
            throw new IllegalStateException("Seller Service was null");
        }
        try {
            entity = getFormData();
            sellerService.saveOrUpdate(entity);
            notifyDataChangedListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessage(e.getErrors());
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", "null", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void notifyDataChangedListeners() {
        for (DataChangeListener dataChangeListener : dataChangeListenerList) {
            dataChangeListener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller obj = new Seller();

        ValidationException validationException = new ValidationException("Validation Exception");

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            validationException.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        if (validationException.getErrors().size() > 0) {
            throw validationException;
        }
        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitializeNodes();
    }

    public void InitializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Constraints.setTextFieldMaxLength(txtBaseSalary, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();

    }

    public void updateFormData() {

        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        if (entity.getBaseSalary() != null) {
            txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
        }
        if (entity.getBirthDate() != null) {
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }
        if (entity.getDepartment() == null){
            departmentComboBox.getSelectionModel().selectFirst();
        }
        else{
            departmentComboBox.setValue(entity.getDepartment());
        }

    }

    public void loadAssociateObjects(){
        if (departmentService == null){
            throw new IllegalStateException("Department Service was null");
        }
        List<Department> departmentList = departmentService.findAll();
        observableList = FXCollections.observableArrayList(departmentList);
        departmentComboBox.setItems(observableList);

    }

    private void setErrorMessage(Map<String, String> errorMessage) {
        Set<String> fields = errorMessage.keySet();
        if (fields.contains("name")) {
            labelErrorName.setText(errorMessage.get("name"));
        }
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        departmentComboBox.setCellFactory(factory);
        departmentComboBox.setButtonCell(factory.call(null));
    }
}
