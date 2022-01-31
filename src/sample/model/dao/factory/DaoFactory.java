package sample.model.dao.factory;

import sample.db.DB;
import sample.model.dao.impl.DepartmentDaoJDBC;
import sample.model.dao.impl.SellerDaoJDBC;
import sample.model.entities.DepartmentDao;
import sample.model.entities.SellerDao;

public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }
    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnection());

    }

    public static DepartmentDao createDepartmentDao1(){
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
