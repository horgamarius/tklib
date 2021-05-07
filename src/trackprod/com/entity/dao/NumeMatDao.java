/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.entity.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import trackprod.com.entity.NumeMat;

/**
 *
 * @author marius
 */
public class NumeMatDao extends DbDao {

    public NumeMatDao(java.sql.Connection con) throws SQLException {
        super(con);
    }

    
    
    @Override
    public boolean delete(Object o) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int save(Object o) throws SQLException {
        NumeMat m = (NumeMat)o;
        if(m.getIdMaterial()==0){
            String sql = getInsert(m);
            ScalarHandler<Long> sch = new ScalarHandler<Long>();
            long id = qry.insert(con, sql,sch);
            return (int)id;
        }else{
            String sql = getUpdate(m);
            qry.update(con,sql);
            return m.getIdMaterial();
        }
    }

    @Override
    public NumeMat getObject(int id) throws SQLException {
        String sql = this.getSelectObj(id);
        ResultSetHandler<NumeMat> rsh = new BeanHandler<NumeMat>(NumeMat.class);
        NumeMat p = qry.query(con,sql, rsh, id);
        return p;

    }
    
     public NumeMat getObject(int idMaterial, int idProdus) throws SQLException{
        String sql = getSelect(new NumeMat());
        String whr =" idMaterial=? and idProdus=? ";
        String sqlObj = this.getSql(sql, 0, 0, whr, "");
        ResultSetHandler<NumeMat> rsh = new BeanHandler<NumeMat>(NumeMat.class);
        NumeMat p = qry.query(con,sqlObj, rsh, idMaterial, idProdus);
        return p;
    }
    
     
     public ArrayList<NumeMat> getListaFromTipMat(int idProdus, int idTipMat) throws SQLException{
         String sql = getSelect(new NumeMat());         
         sql +=" INNER JOIN materiale ON (numemat.idMaterial=materiale.IdMaterial) WHERE numemat.IdProdus=? AND materiale.IdTipMaterial=?; ";
                 ResultSetHandler<List<NumeMat>> rsh = new BeanListHandler<NumeMat>(NumeMat.class);
        ArrayList<NumeMat> rez = (ArrayList<NumeMat>) qry.query(con,sql, rsh, idProdus, idTipMat);
        return rez;
     }
}
