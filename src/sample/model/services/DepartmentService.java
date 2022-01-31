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
}
