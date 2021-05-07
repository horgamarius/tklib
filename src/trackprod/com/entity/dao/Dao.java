/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.entity.dao;
import java.sql.*;
import java.util.*;

/**
 *
 * @author marius
 */
public interface Dao {
    public void setConnection(java.sql.Connection c) throws SQLException;
    public String getSql(String sql, int offset, int limit,String whr, String ord);
    public int save(Object o) throws SQLException;
    public boolean delete(Object o) throws SQLException;
    //public ArrayList<?> getList(int offset, int limit,String whr, String ord) throws SQLException;
    
    public <T> T getObject(int id) throws SQLException;
    
    
}
