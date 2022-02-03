package sample.model.services;

import sample.model.dao.factory.DaoFactory;
import sample.model.entities.Seller;
import sample.model.entities.SellerDao;

import java.util.List;

public class SellerService {

    private SellerDao sellerDao = DaoFactory.createSellerDao();

    public List<Seller> findAll(){
        return sellerDao.findAll();
    }
    public void saveOrUpdate(Seller obj){
        if (obj.getId() == null){
            sellerDao.insert(obj);
        }
        else{
            sellerDao.update(obj);
        }
    }

    public void remove (Seller obj){
        sellerDao.deleteById(obj.getId());
    }
}
