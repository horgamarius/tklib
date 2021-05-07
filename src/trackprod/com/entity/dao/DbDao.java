/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package trackprod.com.entity.dao;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import trackprod.com.entity.*;


/**
 *
 * @author Marius
 */
public abstract class DbDao implements Dao {
    //java.sql.Statement stm;
    java.sql.Connection con;
    SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    QueryRunner qry;

    public DbDao() {
        qry= new QueryRunner();
    }

    public DbDao(Connection con) throws SQLException {
        this.con = con;
        qry = new QueryRunner();
        //this.stm = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        
    }
    
    
    /*public int executa(String sql) throws SQLException {
                  
        int v = stm.executeUpdate(sql);
        return v;
        
    }*/

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
    
    
    
    public int exista(String tb, String field, String value) throws SQLException {
        String sql = "Select count("+ field +") from "+ tb +" where "+ field +" ='" + value + "' ;";
            Statement stm = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
                
    }

    

    @Override
    public void setConnection(java.sql.Connection c) throws SQLException{
        //this.stm = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        this.con = c;
    }
    @Override
    public abstract boolean delete(Object o) throws SQLException;   
    @Override
    public abstract int save(Object o) throws SQLException;
    
    
    public abstract <T>T getObject(int id) throws SQLException;
    
    
    @Override
    public String getSql(String sql, int offset, int limit,String whr, String ord){
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        boolean noSqlWhere = haveWhere(sql);
        
        if(!noSqlWhere){  // daca SQL-ul nu contine o clauza WHERE
            if(whr!=null && whr.trim().startsWith("and")){
                whr= whr.trim().replaceFirst("and", " where ");
            }

            if(whr!=null && !whr.trim().isEmpty() && !whr.trim().toLowerCase().startsWith("where ")){
                whr = " where "+whr;
            }

            if(whr!=null && !whr.isEmpty()){
                if(!whr.trim().toLowerCase().contains("where")) whr = " where "+whr;
                sb.append(" "+whr);            
            }
        }else {
            // daca SQL-ul Contine o clauza WHERE
            if(whr!=null && !whr.trim().isEmpty() && whr.trim().toLowerCase().startsWith("where ")){
                whr = whr.replaceFirst("where", " and ");
                //whr = " and "+whr;
                sb.append(whr);
            }
            if(whr!=null && !whr.trim().isEmpty() && whr.trim().toLowerCase().startsWith("and ")){
                whr = " "+whr;
                sb.append(whr);
            }else {
                sb.append(" where "+whr);
            }
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
        //System.out.println(" sql: "+sb.toString());
        return sb.toString();
    }

    public static String clearLastCharIfExists(String str, char c){
        String strCh = String.valueOf(c);
        StringBuilder sb = new StringBuilder(str);
        String rtmp = sb.reverse().toString();
        if(rtmp.trim().startsWith(strCh)){
            rtmp = rtmp.replaceFirst(strCh, "");
        }
        sb = new StringBuilder(rtmp);
        String r = sb.reverse().toString();
        System.out.println(" rezultat ini "+r);
        
        return r;
    }
    public static String removeOrderBy(String str){
        int poz = str.toLowerCase().indexOf("order by");
        if(poz!=-1){
        str = str.substring(0, poz);
        return str;
        } else return str;
    }
    public static String getOrderBy(String str){        
        int poz = str.toLowerCase().indexOf("order by");
        if(poz!=-1){
            str = str.substring(poz);
            return str;
        }else {
            return "";
        }
    }
    
    
     public static boolean haveWhere(String sql){
        String rezSql = getLastChunk(sql);
        //rezSql = getLastFrom(rezSql);   am comentat chestia asta in 03.05.2018 - nu imi e f.f.f clar de ce am pus apelul getLastFrom de 2 ori
        if(rezSql.contains("(")){
            rezSql = eliminareParanteza(rezSql);
        }
        /*while(rezSql.trim().startsWith("(")){
            rezSql = getLastFrom(rezSql);
        }*/
         String regexSpec ="\\(\\(.+?\\)";
        String regex ="\\(.+?\\)";
        String regexFrom ="\\bfrom\\b{1}.+?\\bwhere\\b{1}";
        String regExConcat ="\\bconcat\\s*\\(.+?\\)";
//        String regex ="\\(";
        sql = sql.toLowerCase().replaceAll(regexSpec, "(");
        sql = sql.toLowerCase().replaceAll(regExConcat, "");
        sql =sql.toLowerCase().replaceAll(regex, "");
        Pattern pattern = Pattern.compile(regexFrom, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rezSql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end() + " ");
            System.out.println(matcher.group());
            String rez = sql.replaceFirst(regex, "");
            return true;
        }
        
        //boolean b = rez.contains("where ");
        
        return false;
    }
   
    private static String eliminareParanteza(String sql){
        int valStart =0;
        int valEnd =0;
        /*String regexSpec ="\\(\\(.+?\\)";
        Pattern  p = Pattern.compile(regexSpec, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(sql);
        boolean first =true;
        
        while(matcher.find()){
            if(first){
                valStart =matcher.start();                
            }
            valEnd = matcher.end();
        }*/
        
        
        //int[] pozIni= new int[0];
        //int[] pozFin= new int[0];
        boolean first =true;
        int k=0,j=0;
        for( int i=0;i<sql.length();i++){
            char c = sql.charAt(i);
            if(first && c=='('){
                valStart= i;
                first = false;
            }
            if(c==')'){
                valEnd = i;                
            }
        }
        StringBuilder sb = new StringBuilder(sql);
        sb = sb.replace(valStart, valEnd, "");
        return sb.toString();
        
    }
     
    private boolean haveMoreFrom(String sql){
        StringBuilder sb = new StringBuilder(sql);
        sql= sql.toLowerCase();
        int pUnu = sql.indexOf(" from ");
        int pDoi = sql.lastIndexOf(" from ");
        return pUnu!=pDoi?true:false;            
    } 
    private static String getLastChunk(String sql){
        StringTokenizer stk = new StringTokenizer(sql+",",",");
        String rez ="";
        boolean first = false;
        while(stk.hasMoreTokens()){
            rez = stk.nextToken();
            first = true;
        }
        //rez = stk.nextToken();
        return first?rez:sql;
    } 
    private static String getLastFrom(String sql){
        StringTokenizer stk= new StringTokenizer(sql.toLowerCase()+" from "," from");
        String rez ="";
        boolean first = false;
        while(stk.hasMoreTokens()){
            rez = stk.nextToken();
            first = true;
        }
        //rez = stk.nextToken();
        return first?rez:sql;
    }
    
    
    public  Object[] getInsertParam(Object o){
        ArrayList<Object> params = new ArrayList<Object>();
        
        Class<? extends Object> c=o.getClass();
        Field[] fields = c.getDeclaredFields();
        //StringBuilder sb = new StringBuilder("INSERT INTO ");
        //StringBuilder sbQry= new StringBuilder("(");
        Table t = c.getAnnotation(Table.class);
        /*if(t!=null){            
            sb.append(t.name());
            sb.append(" (");
        }*/
        
        for(Field field: fields){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);            
            IgnoreInsert ignoreInsert = field.getAnnotation(IgnoreInsert.class);
            Ignore ignore = field.getAnnotation(Ignore.class);
            ForceDefaultValue forceDefaultValue = field.getAnnotation(ForceDefaultValue.class);

            try {
                if(id==null && ignore==null && ignoreInsert==null && forceDefaultValue==null){
                params.add(field.get(o));
                continue;
                }
                if(forceDefaultValue!=null){
                    params.add(forceDefaultValue.value());
                    continue;
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return params.toArray();
    }
    
    
    
    public  Object[] getUpdateParam(Object o){
        ArrayList<Object> params = new ArrayList<Object>();
        
        Class<? extends Object> c=o.getClass();
        Field[] fields = c.getDeclaredFields();
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        StringBuilder sbQry= new StringBuilder("(");
        Table t = c.getAnnotation(Table.class);
        if(t!=null){            
            sb.append(t.name());
            sb.append(" (");
        }
        
        for(Field field: fields){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);            
            IgnoreInsert ignoreInsert = field.getAnnotation(IgnoreInsert.class);
            Ignore ignore = field.getAnnotation(Ignore.class);
            ForceDefaultValue forceDefaultValue = field.getAnnotation(ForceDefaultValue.class);

            try {
                if(id==null && ignore==null ){
                params.add(field.get(o));
                continue;
                }
                
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        params.add(DbDao.getIdValue(o));        // adauga la final Id-ul
        return params.toArray();
    }
    
    public  String getUpdateDbUtil(Object o){
        Class<? extends Object>c=o.getClass();
        Field[] fields =c.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        
        sb.append("UPDATE ");
        Table t = c.getAnnotation(Table.class);
        if(t!=null){            
            sb.append(t.name());
            sb.append(" ");
        }
        sb.append(" SET ");
        
        for(Field field: fields){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);                        
            Ignore ignore = field.getAnnotation(Ignore.class);
            IgnoreUpdate ignoreUpdate = field.getAnnotation(IgnoreUpdate.class);
            ForceDefaultValue forceDefaultValue = field.getAnnotation(ForceDefaultValue.class);
            
            if (id==null && ignoreUpdate==null && ignore==null){
                try {
                    sb.append(" ");
                    sb.append(field.getName()+"="); 
                    
                    // adaugare valoare default daca e cazul
                    if(forceDefaultValue!=null){
                        sb.append(forceDefaultValue.value());
                        sb.append(",");
                        continue;
                    }else{
                        sb.append("?,");
                    }
                                                           
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        sb.replace(sb.length()-1, sb.length(), " ");
        sb.append(" WHERE ");
        sb.append(getIdField(o));
        sb.append("=?");
        //sb.append();
        
        return sb.toString();
    }
    
    
    public int saveData(Object o, java.sql.Connection c) throws SQLException{
        QueryRunner qrun = new QueryRunner();
        int id = getIdValue(o);
        if(id==-1) return -1;
        if(id==0){
            String ins = getInsertDbUtil(o);
            ScalarHandler<Long> sch = new ScalarHandler<Long>();
            Object[] prm = getInsertParam(o);
            long rez = qrun.insert(c,ins, sch, prm);
            return (int)rez;
        }else {
            String upd = getUpdateDbUtil(o);
            Object[] prm = getUpdateParam(o);
            qrun.update(c, upd,prm);
            return id;
        }
    }
    
    public static String getInsertDbUtil(Object o){
        Class<? extends Object> c=o.getClass();
        Field[] fields = c.getDeclaredFields();
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        StringBuilder sbQry= new StringBuilder("(");
        Table t = c.getAnnotation(Table.class);
        if(t!=null){            
            sb.append(t.name());
            sb.append(" (");
        }
        
        for(Field field: fields){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);            
            IgnoreInsert ignoreInsert = field.getAnnotation(IgnoreInsert.class);
            Ignore ignore = field.getAnnotation(Ignore.class);
            ForceDefaultValue forceDefaultValue = field.getAnnotation(ForceDefaultValue.class);
            
            if (id==null && ignoreInsert==null && ignore==null){
                try {
                    sb.append(field.getName()+",");                 
                    
                    // adaugare valoare default daca e cazul
                    if(forceDefaultValue!=null){
                        sbQry.append(forceDefaultValue.value());                       
                        sbQry.append(",");
                        continue;
                    }else{
                        sbQry.append("?,");
                    }
                    
                    
                    
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        
        sbQry.replace(sbQry.length()-1, sbQry.length(), ")");
        sb.replace(sb.length()-1, sb.length(), ")");
        sb.append(" VALUES ");
        sb.append(sbQry);
        sb.append(";");
        return sb.toString();
    }
    
    public  String getInsert(Object o){
        Class<? extends Object> c=o.getClass();
        Field[] fields = c.getDeclaredFields();
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        StringBuilder sbQry= new StringBuilder("(");
        Table t = c.getAnnotation(Table.class);
        if(t!=null){            
            sb.append(t.name());
            sb.append(" (");
        }
        System.out.println("------------------------------------");
        for(Field field: fields){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);            
            IgnoreInsert ignoreInsert = field.getAnnotation(IgnoreInsert.class);
            Ignore ignore = field.getAnnotation(Ignore.class);
            ForceDefaultValue forceDefaultValue = field.getAnnotation(ForceDefaultValue.class);
            
            if (id==null && ignoreInsert==null && ignore==null){
                try {
                    sb.append(field.getName()+",");                 
                    
                    // adaugare valoare default daca e cazul
                    if(forceDefaultValue!=null){
                        sbQry.append(forceDefaultValue.value());
                        sbQry.append(",");
                        continue;
                    }
                    //System.out.println(sb.toString());
                    //System.out.println(sbQry.toString());
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.String")){
                        sbQry.append("'");
                        sbQry.append((String)field.get(o));
                        sbQry.append("',");
                        continue;
                    }
                    
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Integer")){                        
                        sbQry.append((int)field.get(o));                        
                        sbQry.append(",");
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Double")){                        
                        sbQry.append((double)field.get(o));    
                        sbQry.append(",");
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Long")){                        
                        sbQry.append((long)field.get(o));                        
                        sbQry.append(",");
                        continue;
                    } 
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Float")){                        
                        sbQry.append((float)field.get(o));                        
                        sbQry.append(",");
                        continue;
                    } 
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.BigDecimal")){                        
                        sbQry.append(((BigDecimal)field.get(o)).toString());                        
                        sbQry.append(",");
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Char")){                        
                        sbQry.append(((char)field.get(o)));                        
                        sbQry.append(",");
                        continue;
                    }
                    
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Boolean")){                        
                        sbQry.append(((Boolean)field.get(o))?"1":"0");                        
                        sbQry.append(",");
                        continue;
                    }
                    
                    if(field.get(o).getClass().getCanonicalName().equals("java.util.Date")){
                        TimeStampFormat timeStampformat = field.getAnnotation(TimeStampFormat.class);
                        if(timeStampformat ==null){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sbQry.append("'");
                            sbQry.append(sdf.format(field.get(o)));
                            sbQry.append("',");
                        }else{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            sbQry.append("'");
                            sbQry.append(sdf.format(field.get(o)));
                            sbQry.append("',");
                        }
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.sql.Date")){
                        TimeStampFormat timeStampformat = field.getAnnotation(TimeStampFormat.class);
                        if(timeStampformat ==null){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sbQry.append("'");
                            sbQry.append(sdf.format(field.get(o)));
                            sbQry.append("',");
                        }else{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            sbQry.append("'");
                            sbQry.append(sdf.format(field.get(o)));
                            sbQry.append("',");
                        }
                        continue;
                    }
                    
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        sbQry.replace(sbQry.length()-1, sbQry.length(), ")");
        sb.replace(sb.length()-1, sb.length(), ")");
        sb.append(" VALUES ");
        sb.append(sbQry);
        sb.append(";");
        return sb.toString();
    }
    
    public String getUpdate(Object o){
        Class<? extends Object>c=o.getClass();
        Field[] fields =c.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        
        sb.append("UPDATE ");
        Table t = c.getAnnotation(Table.class);
        if(t!=null){            
            sb.append(t.name());
            sb.append(" ");
        }
        sb.append(" SET ");
        
        for(Field field: fields){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);                        
            Ignore ignore = field.getAnnotation(Ignore.class);
            IgnoreUpdate ignoreUpdate = field.getAnnotation(IgnoreUpdate.class);
            ForceDefaultValue forceDefaultValue = field.getAnnotation(ForceDefaultValue.class);
            
            if (id==null && ignoreUpdate==null && ignore==null){
                try {
                    sb.append(" ");
                    sb.append(field.getName()+"="); 
                    
                    // adaugare valoare default daca e cazul
                    if(forceDefaultValue!=null){
                        sb.append(forceDefaultValue.value());
                        sb.append(",");
                        continue;
                    }
                    
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.String")){
                        sb.append("'");
                        sb.append((String)field.get(o));
                        sb.append("'");
                        sb.append(",");
                        continue;
                    }
                    
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Integer")){                        
                        sb.append((int)field.get(o)); 
                        sb.append(",");
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Double")){                        
                        sb.append((double)field.get(o)); 
                        sb.append(",");
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Long")){                        
                        sb.append((long)field.get(o));
                        sb.append(",");
                        continue;
                    } 
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Float")){                        
                        sb.append((float)field.get(o)); 
                        sb.append(",");
                        continue;
                    } 
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.BigDecimal")){                        
                        sb.append(((BigDecimal)field.get(o)).toString()); 
                        sb.append(",");
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Char")){                        
                        sb.append(((char)field.get(o)));         
                        continue;
                    }
                    
                    if(field.get(o).getClass().getCanonicalName().equals("java.lang.Boolean")){                        
                        sb.append(((Boolean)field.get(o))?"1":"0"); 
                        sb.append(",");
                        continue;
                    }
                    
                    if(field.get(o).getClass().getCanonicalName().equals("java.util.Date")){
                        TimeStampFormat timeStampformat = field.getAnnotation(TimeStampFormat.class);
                        if(timeStampformat ==null){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sb.append("'");
                            sb.append(sdf.format(field.get(o)));
                            sb.append("'");
                            sb.append(",");
                        }else{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            sb.append("'");
                            sb.append(sdf.format(field.get(o)));
                            sb.append("'");
                            sb.append(",");
                        }
                        
                        continue;
                    }
                    if(field.get(o).getClass().getCanonicalName().equals("java.sql.Date")){
                        TimeStampFormat timeStampformat = field.getAnnotation(TimeStampFormat.class);
                        if(timeStampformat ==null){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            sb.append("'");
                            sb.append(sdf.format(field.get(o)));
                            sb.append("'");
                            sb.append(",");
                        }else{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            sb.append("'");
                            sb.append(sdf.format(field.get(o)));
                            sb.append("'");
                            sb.append(",");
                        }
                        continue;
                    }
                    
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        sb.replace(sb.length()-1, sb.length(), " ");
        sb.append(" WHERE ");
        sb.append(getIdField(o));
        sb.append("=");
        sb.append(DbDao.getIdValue(o));
        
        return sb.toString();
    }
    
    /**
     * returneaza campul ID - autoincrement al tabelei
     * @param o
     * @return 
     */
    public String getIdField(Object o){
        Class<? extends Object> c=o.getClass();
        Field[] fields = c.getDeclaredFields();
        for(Field f: fields){
            f.setAccessible(true);
            java.lang.annotation.Annotation a=f.getAnnotation(Id.class);
            if(a!=null){
                try {
                    return (String) f.getName();
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        return "";
    }
    /**
     * returneaza valoarea campului Autoincrement
     * @param o
     * @return 
     */
    public static int getIdValue(Object o) {
        Class<? extends Object> c=o.getClass();
        Field[] fields =c.getDeclaredFields();
        for(Field f:fields){
            f.setAccessible(true);
            java.lang.annotation.Annotation a=f.getAnnotation(Id.class);
            if(a!=null){
                try {
                    return (int) f.get(o);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return -1;
    }
    
    
    public static String getSelect (Object o){
    
        
        Class<? extends Object> c=o.getClass();
        Table t = c.getAnnotation(Table.class);
        Field[] fields=c.getDeclaredFields();
        StringBuilder sb = new StringBuilder("");
        sb.append("SELECT ");
        String numeTb = t.name();
        for(Field f: fields){
            f.setAccessible(true);
            Ignore ignore = f.getAnnotation(Ignore.class);
            if (ignore==null){                                         
            sb.append(" ");
            sb.append(numeTb+"."+f.getName()+","); 
            }
        }
        
        sb.replace(sb.length()-1, sb.length(), " ");
        
        
        if(t!=null){ 
            sb.append(" FROM ");
            sb.append(t.name());
            sb.append(" ");
        }
        return sb.toString();
    }
                
    public String getWhereId(Object o){
        String s =" where "+getIdField(o)+"=? ";
        return s;
    }
    
    
    /**
     * returneaza select-ul pentru o anumita inregistrare in baza Id-ului
     * @param o
     * @return 
     */
    public String getSelectObj(Object o){
        String sql = DbDao.getSelect(o );
        sql +=getWhereId(o);
        return sql;
    }
    
    
    
    public String getDeleteObj(Object o){
        StringBuilder sb = new StringBuilder("Delete from ");
        Class<? extends Object> c=o.getClass();
        Field[] fields = c.getDeclaredFields();

        Table t = c.getAnnotation(Table.class);
        if(t!=null){            
            sb.append(t.name());
            sb.append(" ");
        }
        sb.append(getWhereId(o));
        return sb.toString();
    }
}
