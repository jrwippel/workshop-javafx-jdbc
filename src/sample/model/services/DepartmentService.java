package sample.model.services;

import sample.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> findAll(){
        List<Department> departmentList = new ArrayList<>();
        departmentList.add( new Department(1, "Tecnology"));
        departmentList.add(new Department(2, "Books"));
        departmentList.add(new Department(3, "Eletronics"));
        return departmentList;

    }
}
