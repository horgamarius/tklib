/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Marius
 */
public class TrackingOpSincroDao {
   private int idTrackingOperatie,idProdus, idOperatie, idMaterial, idUserAdd, refSincro;
   private String serieNrProdus, codBareMaterial, nrLotMaterial;
   private java.util.Date dataAdd, oraAdd;
   private boolean localSql=false;
   
   private String dataSincronizare;
   private java.sql.Statement stm;
   private int lungimeSerieNrProdus =-1;
   private String sincroErrMessage ="";
   private String refSincroLocation="";
   
   public TrackingOpSincroDao(){}

    public int getIdTrackingOperatie() {
        return idTrackingOperatie;
    }

    public void setIdTrackingOperatie(int idTrackingOperatie) {
        this.idTrackingOperatie = idTrackingOperatie;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public int getIdOperatie() {
        return idOperatie;
    }

    public void setIdOperatie(int idOperatie) {
        this.idOperatie = idOperatie;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public int getIdUserAdd() {
        return idUserAdd;
    }

    public void setIdUserAdd(int idUserAdd) {
        this.idUserAdd = idUserAdd;
    }

    public String getSerieNrProdus() {
        return serieNrProdus;
    }

    public void setSerieNrProdus(String serieNrProdus) {
        this.serieNrProdus = serieNrProdus;
    }

    public String getCodBareMaterial() {
        return codBareMaterial;
    }

    public void setCodBareMaterial(String codBareMaterial) {
        this.codBareMaterial = codBareMaterial;
    }

    public String getNrLotMaterial() {
        return nrLotMaterial;
    }

    public void setNrLotMaterial(String nrLotMaterial) {
        this.nrLotMaterial = nrLotMaterial;
    }

    public Date getDataAdd() {
        return dataAdd;
    }

    public void setDataAdd(Date dataAdd) {
        this.dataAdd = dataAdd;
    }

    public Date getOraAdd() {
        return oraAdd;
    }

    public void setOraAdd(Date oraAdd) {
        this.oraAdd = oraAdd;
    }

    public boolean isLocalSql() {
        return localSql;
    }

    public void setLocalSql(boolean localSql) {
        this.localSql = localSql;
    }


    public String getDataSincronizare() {
        return dataSincronizare;
    }

    public void setDataSincronizare(String dataSincronizare) {
        this.dataSincronizare = dataSincronizare;
    }

    public Statement getStm() {
        return stm;
    }

    public void setStm(Statement stm) {
        this.stm = stm;
    }

    public int getLungimeSerieNrProdus() {
        return lungimeSerieNrProdus;
    }

    public void setLungimeSerieNrProdus(int lungimeSerieNrProdus) {
        this.lungimeSerieNrProdus = lungimeSerieNrProdus;
    }

    public int getRefSincro() {
        return refSincro;
    }

    public void setRefSincro(int ref) {
        this.refSincro = ref;
    }

    public String getSincroErrMessage() {
        return sincroErrMessage;
    }

    public void setSincroErrMessage(String sincroErrMessage) {
        this.sincroErrMessage = sincroErrMessage;
    }

    public String getRefSincroLocation() {
        return refSincroLocation;
    }

    public void setRefSincroLocation(String refSincroLocation) {
        this.refSincroLocation = refSincroLocation;
    }
   
    
    
    public int checkIfIsExist() throws SQLException{
        
        String sql = "Select count(idTrackingOperatii) from trackingoperatii where SerieNrProdus ='"+   this.serieNrProdus +"' " +
                        "and idOperatie = "+ this.idOperatie +" and CodBareMat ='"+ this.codBareMaterial +"' and OraInreg ='"+ this.oraAdd +"' and RefSincro="+ this.refSincro +" and idUserAdd ="+  idUserAdd  +";";
        ResultSet rs = stm.executeQuery(sql);
        if(rs.next()){
            return rs.getInt(1);
        }else {
        return 0;
        }
    }
    public void saveSincronizat() throws SQLException{
        this.dataSincronizare = getDataSincro();
        String upd = "Update TrackingOperatii set  RefSincro="+ this.refSincro +",RefSincroLocation='"+ this.refSincroLocation +"', DataSincronizare='"+ this.dataSincronizare +"' where idTrackingOperatii ="+ this.idTrackingOperatie +";";
        System.out.println(upd);
        stm.execute(upd);
                
    }
    public void updateSQLiteConfirmSincronizare() throws SQLException {
        String upd = "Update TrackingOperatii set idTrackingOperatii ="+ this.idTrackingOperatie +", RefSincro ="+ this.refSincro +" where idTrackingOperatii ="+ this.refSincro +";";
        System.out.println(upd);
        stm.execute(upd);
        
    }
    
    public int add() throws SQLException{
        
        String add = "call addTrackingOperatii('"+ this.getSerieNrProdus() +"',"+ this.getIdProdus() +","+ this.getIdOperatie() +","+ this.getIdMaterial() +
                ",'"+ this.getCodBareMaterial() +"','"+ this.getNrLotMaterial() +"',"+ this.getIdUserAdd() +");";
        System.out.println(add);
        if (this.getSerieNrProdus()!=null || !this.getSerieNrProdus().isEmpty() || this.getSerieNrProdus().length()==this.lungimeSerieNrProdus ) {
            
            this.idTrackingOperatie = executa(add);
            return idTrackingOperatie;
        }else {        
            return -1;
        }
        
    }
    
    private int executa(String sql) throws SQLException {
               
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                return rs.getInt(1);
            }else
            return -1;
       
        
    }
    
    private String getDataSincro(){
        Calendar c = Calendar.getInstance();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }
    
    
    public static ArrayList<TrackingOpSincroDao> getLista(int offset, int limit, String whr, String ord, java.sql.Statement st, int idRefSincro) throws SQLException{
        String sql ="SELECT IdTrackingOperatii, SerieNrProdus, IDProdus, IdOperatie, IdMaterial, "
                + "CodBareMat,NrLotMat, DataInreg, OraInreg, IdUserAdd,DataSincronizare, RefSincro,RefSincroLocation\n" +
                " FROM trackingOperatii ";
        sql = TbScriptDao.getSql(sql, offset, limit, whr, ord);
        ResultSet rs = st.executeQuery(sql);
        ArrayList<TrackingOpSincroDao> tb = new ArrayList<TrackingOpSincroDao>();
        while(rs.next()){
            TrackingOpSincroDao t = new TrackingOpSincroDao();
            t.setIdTrackingOperatie(rs.getInt(1));
            t.setSerieNrProdus(rs.getString(2));
            t.setIdProdus(rs.getInt(3));
            t.setIdOperatie(rs.getInt(4));
            t.setIdMaterial(rs.getInt(5));
            t.setCodBareMaterial(rs.getString(6));
            t.setNrLotMaterial(rs.getString(7));
            t.setDataAdd(rs.getDate(8));
            t.setOraAdd(rs.getDate(9));
            t.setIdUserAdd(rs.getInt(10));
            t.setDataSincronizare(rs.getString(11));
            if(idRefSincro==0){
                t.setRefSincro(rs.getInt(12));
            }else {
                t.setRefSincro(idRefSincro);
            }
            t.setRefSincroLocation(rs.getString(13));
            tb.add(t);
        }
        return tb;
    }
    
    
    
    
}
