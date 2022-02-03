package sample.model.services;

import sample.model.dao.factory.DaoFactory;
import sample.model.entities.Department;
import sample.model.entities.DepartmentDao;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return departmentDao.findAll();
    }
    public void saveOrUpdate(Department obj){
        if (obj.getId() == null){
            departmentDao.insert(obj);
        }
        else{
            departmentDao.update(obj);
        }
    }

    public void remove (Department obj){
        departmentDao.deleteById(obj.getId());
    }
}
