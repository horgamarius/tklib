/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.dao;

/**
 *
 * @author Marius
 */
public class LocalTableCfgDao {
    int id;
    String numeTabela;
    String script, scriptIndex;
    boolean inactiv = false;
    boolean nomenclator = false;
    boolean excludeSinc = false;

    public LocalTableCfgDao() {
    }

    public LocalTableCfgDao(int idLocalTableCfg, String numeTabela, String script, boolean _inactiv, boolean nome, String index, boolean ex) {
        this.id = idLocalTableCfg;
        this.numeTabela = numeTabela;
        this.script = script;
        this.inactiv=_inactiv;
        this.nomenclator=nome;
        this.scriptIndex = index;
        this.excludeSinc = ex;
    }

    public int getId() {
        return id;
    }

    public void setId(int idLocalTableCfg) {
        this.id = idLocalTableCfg;
    }

    public String getNumeTabela() {
        return numeTabela;
    }

    public void setNumeTabela(String numeTabela) {
        this.numeTabela = numeTabela;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
    
    public static final String getSelectLocalTableCfg(){
        return "Select idLocalTableCfg, NumeTabela, Script, Inactiv, Nomenclator, ScriptIndex, ExcludeSinc from localtableCfg;";
    }

    public boolean isInactiv() {
        return inactiv;
    }

    public void setInactiv(boolean inactiv) {
        this.inactiv = inactiv;
    }

    public boolean isNomenclator() {
        return nomenclator;
    }

    public void setNomenclator(boolean nomenclator) {
        this.nomenclator = nomenclator;
    }

    public String getScriptIndex() {
        return scriptIndex;
    }

    public void setScriptIndex(String scriptIndex) {
        this.scriptIndex = scriptIndex;
    }

    public boolean isExcludeSinc() {
        return excludeSinc;
    }

    public void setExcludeSinc(boolean excludeSinc) {
        this.excludeSinc = excludeSinc;
    }
    
}
