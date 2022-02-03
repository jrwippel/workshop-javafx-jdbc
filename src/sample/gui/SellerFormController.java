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
import sample.model.entities.Seller;
import sample.model.exceptions.ValidationException;
import sample.model.services.SellerService;

import java.net.URL;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;
    private SellerService sellerService;
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

    public void setSeller(Seller entity){
        this.entity = entity;
    }
    public void setSellerService(SellerService sellerService){
        this.sellerService = sellerService;
    }

    public void subscribeDataChangedListener(DataChangeListener dataChangeListener){
        dataChangeListenerList.add(dataChangeListener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event){

        if (entity == null){
            throw new IllegalStateException("Entity was null");
        }
        if (sellerService == null){
            throw new IllegalStateException("Seller Service was null");
        }
        try {
            entity = getFormData();
            sellerService.saveOrUpdate(entity);
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

    private Seller getFormData() {
        Seller obj = new Seller();

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
