/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.dao;

import java.sql.*;

/**
 *
 * @author Marius
 */
public class LungimeSerieNrProdusDao {
    private int lungimeSerieNrProdus=-1;

    public LungimeSerieNrProdusDao() {
    }

    public int getLungimeSerieNrProdus() {
        return lungimeSerieNrProdus;
    }

    public void setLungimeSerieNrProdus(int lungimeSerieNrProdus) {
        this.lungimeSerieNrProdus = lungimeSerieNrProdus;
    }
    
    public void loadData(java.sql.Statement s) throws SQLException{
        String sql="select valoare From paramgen where idParamGen = 5;";
        ResultSet rs=s.executeQuery(sql);
        if(rs.next()){
            this.lungimeSerieNrProdus= rs.getInt(1);
        }
    }
}
