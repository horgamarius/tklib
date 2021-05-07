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
import trackprod.com.entity.Materiale;

/**
 *
 * @author marius
 */
public class MaterialeDao extends DbDao {

    public MaterialeDao(Connection con) throws SQLException {
        super(con);
    }

    
    
    @Override
    public boolean delete(Object o) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int save(Object o) throws SQLException {
        Materiale m = (Materiale)o;
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
    public Materiale getObject(int id) throws SQLException {
        Materiale m = new Materiale();
        m.setIdMaterial(id);
        String sql = this.getSelectObj(m);
        ResultSetHandler<Materiale> rsh = new BeanHandler<Materiale>(Materiale.class);
        Materiale p = qry.query(con,sql, rsh, id);
        return p;

    }
    
    /**
     * returneaza combinari de materialul care are tip material ce apare o singura data si restul materialelor
     * NOTA numai primul material va fi luat in considerare
     * @param listaId
     * @return
     * @throws SQLException 
     */
    public ArrayList<int[]> getCombinari(int[] listaId) throws SQLException{
        String sql = this.getSelectObj(new Materiale());
        StringBuilder sb = new StringBuilder();
        
        for(int i=0;i<listaId.length;i++){
            sb.append(listaId[i]+",");
        }
        String sqlObj = this.getSql(sql, 0, 0, " idMaterial in ("+sb.toString()+")", "");
        ResultSetHandler<List<Materiale>> rsh = new BeanListHandler<Materiale>(Materiale.class);
        ArrayList<Materiale> rez = (ArrayList<Materiale>) qry.query(sql, rsh);
        
        Materiale master = getTipMatUnic(rez);
        
        rez.remove(master);
        
        ArrayList<int[]> refFin = new ArrayList<>();
        for(Materiale m:rez){
            int[] val = new int[2];
            val[1]=master.getIdMaterial();
            val[2]=m.getIdMaterial();
            refFin.add(val);
        }
        
        return null;
    }
    
    /**
     * returneaza materialul care are tip-ul de material doar o singura data in lista
     */
    private Materiale getTipMatUnic(ArrayList<Materiale> lista){
        ArrayList<NrAparitii> tab = new ArrayList<NrAparitii>();
        
        for(Materiale m: lista){
            boolean gasit =false;
            for(NrAparitii mx:tab){
                if(m.getIdTipMaterial()==mx.idTip){
                    gasit =true;
                    mx.nrAparitii=mx.nrAparitii+1;
                }
                if(!gasit){
                    tab.add(new NrAparitii(m.getIdTipMaterial(),1));
                }
            }
        }
        java.util.Collections.sort(tab);
        int idTipMaster = tab.get(0).idTip;
        
        for(Materiale m: lista){
            if(m.getIdTipMaterial()==idTipMaster) return m;
        }
        
        return lista.get(0);
    }
    
    
    
    
    private class NrAparitii implements Comparable  {
        int idTip;
        int nrAparitii;

        public NrAparitii(int idTip, int nrAparitii) {
            this.idTip = idTip;
            this.nrAparitii = nrAparitii;
        }

        
        public int getIdTip() {
            return idTip;
        }

        public void setIdTip(int idTip) {
            this.idTip = idTip;
        }

        public int getNrAparitii() {
            return nrAparitii;
        }

        public void setNrAparitii(int nrAparitii) {
            this.nrAparitii = nrAparitii;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NrAparitii other = (NrAparitii) obj;
            if (this.nrAparitii != other.nrAparitii) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Object o) {
            NrAparitii n=(NrAparitii)o;
            if(this.getNrAparitii()==n.getNrAparitii()){ return 0;}
            else if(this.getNrAparitii()>n.getNrAparitii()){ return 1;}
            else return -1;
            
        }
        
        
    }
}
