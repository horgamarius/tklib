/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.dao;

import java.sql.ResultSet;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;


/**
 *
 * @author Marius
 */
public class TbScriptDao {
    private java.sql.Connection con;
    
    public TbScriptDao(){
        
    }

    public TbScriptDao(Connection con) {
        this.con = con;
    }
    
    public String getTbSqlDataScript(String tbName){
        String sql ="Select * from "+tbName +" ";
        //return getSqlDataScript(sql, tbName);
        return getSqlDataScript(sql);
    }
    
    public String getSqlDataScript(String sql) {
        
        StringBuilder sb = new StringBuilder();
        java.sql.Statement st=null;
        try {
             //dCon = new DbConnection();
             //dCon.init();
             
             st =con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
             //String sql ="select * from "+tbName+" ";
             System.out.println(sql);
             ResultSet rs = st.executeQuery(sql);
             ResultSetMetaData rsmd = rs.getMetaData();
             
             int nrCol = rsmd.getColumnCount();
             
             //sb.append("Insert into ");
             //sb.append(tbName);
             sb.append(" ( ");
             
             
             System.out.println("nr col"+nrCol);
             for( int i=1;i<=nrCol;i++){
                 //System.out.println(rsmd.getColumnName(i));
                 sb.append(rsmd.getColumnName(i));
                 if(i!=nrCol) sb.append(", ");
             }
             sb.append(" ) values ");
             
             boolean first =true;
             while(rs.next()){
                 if(first){                     
                     first = false;
                 }else{
                     sb.append("), ");
                 }
                 
                                  
                 sb.append("(");
                 for( int i=1; i<=nrCol;i++ ){
                                      
                 int type = rs.getMetaData().getColumnType(i);
                    switch(type){
                        case Types.BIT:{
                            sb.append(rs.getInt(i)); break;
                        }
                        
                        case -4:
                        case -3:
                        case -2:
                         
                     case Types.TIMESTAMP:{                            
                            sb.append("'"+rs.getString(i)+"'");
                            break;
                        }
                        case Types.TIME: {        
                            sb.append("'"+rs.getString(i)+"'");
                            break;
                        }
                        case Types.DATE:   {
                            sb.append("'"+rs.getString(i)+"'");
                            break;
                        }
                        case Types.BOOLEAN:{
                            sb.append(rs.getInt(i));
                            break;
                        }
                        case Types.BIGINT:{
                            sb.append(rs.getBigDecimal(i).toString());
                            break;
                        }
                        case Types.CHAR: {
                            sb.append("'"+rs.getString(i)+"'");
                            break;
                        }
                        case Types.DECIMAL: {
                            sb.append(rs.getBigDecimal(i).toString());
                            break;
                        }
                        case Types.DOUBLE: {
                            sb.append(rs.getDouble(i));
                            break;
                        }
                        case Types.FLOAT: {
                            sb.append(rs.getFloat(i)); break; }
                        case Types.INTEGER: {
                            sb.append(rs.getInt(i)); break; }
                        case Types.VARCHAR: {
                            sb.append("'"+rs.getString(i)+"'"); break; }
                        case Types.TINYINT: {
                            sb.append(rs.getInt(i)); break; }
                        case Types.SMALLINT: {
                            sb.append((rs.getInt(i))); break; }
                        default: {
                            sb.append("'"+rs.getString(i)+"'");   //strValue = _rs.getString(_columnName);
                            break;
                        }
                 }
                    
                 if(i!=nrCol)
                         sb.append(",");
                 }
                 //sb.append("), ");
             }
             if(first==false){
                 sb.append(");");
             }else{
                 sb.delete(0,sb.length());
             }
             
        } catch (SQLException ex) {
            Logger.getLogger(TbScriptDao.class.getName()).log(Level.SEVERE, null, ex);       
        } finally {
            try {
                if(st!=null)st.close();
                //if(dCon!=null)dCon.destroy();
            } catch (SQLException ex) {
                Logger.getLogger(TbScriptDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return sb.toString();
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
    
    
    public static String getSql(String sql, int offset, int limit,String whr, String ord){
        if(whr!=null && whr.trim().startsWith("and")){
            whr= whr.trim().replaceFirst("and", " where ");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        if(whr!=null && !whr.isEmpty()){
            if(!whr.toLowerCase().contains("where")) whr = " where "+whr;
            sb.append(" "+whr);            
        }
        if(ord!=null && !ord.isEmpty()){
            if(!ord.toLowerCase().contains("order by")) ord =" order by "+ord;
            sb.append(" "+ord);
        }
        if(limit>0){
            if(offset>0){
                sb.append(" limit "+offset+","+limit);
            }else{
                sb.append(" limit "+limit);
            }
        }
        System.out.println(" sql: "+sb.toString());
        return sb.toString();
    }

    
    
}
