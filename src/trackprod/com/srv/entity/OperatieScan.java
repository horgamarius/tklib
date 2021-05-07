/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.srv.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * @author marius
 */
public class OperatieScan {
    int idOperatie;
    String denumire;
    String portComScan;
    String portCommComand;
    int releuComandaOK;
    int releuComandaNotOK;
    String codMaterial;
    int idMaterial;
    int timpAfisareOk;  // exprimat in milisecunde
    int timpAfisareNotOk;   //exprimat in milisecunde
    int idOpAnterioara;
    int restrictieTimp;
    int idUser;

    private static final int oSecunda = 1000;
    
    public OperatieScan() {
        idOperatie=0;
        denumire ="";
        portComScan="";
        portCommComand="";
        releuComandaOK=0;
        releuComandaNotOK=0;
        idUser=0;
        idMaterial=0;
        String codMaterial="";
        timpAfisareOk=oSecunda;
        timpAfisareNotOk=oSecunda;
        idOpAnterioara=0;
        restrictieTimp=-1;
    }

    
    
    public int getIdOperatie() {
        return idOperatie;
    }

    public void setIdOperatie(int idOperatie) {
        this.idOperatie = idOperatie;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getPortComScan() {
        return portComScan;
    }

    public void setPortComScan(String portComScan) {
        this.portComScan = portComScan;
    }

    public int getReleuComandaOK() {
        return releuComandaOK;
    }

    public void setReleuComandaOK(int releuComandaOK) {
        this.releuComandaOK = releuComandaOK;
    }

    public int getReleuComandaNotOK() {
        return releuComandaNotOK;
    }

    public void setReleuComandaNotOK(int releuComandaNotOK) {
        this.releuComandaNotOK = releuComandaNotOK;
    }

    

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getPortCommComand() {
        return portCommComand;
    }

    public void setPortCommComand(String portCommComand) {
        this.portCommComand = portCommComand;
    }

    public String getCodMaterial() {
        return codMaterial;
    }

    public void setCodMaterial(String codMaterial) {
        this.codMaterial = codMaterial;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public int getTimpAfisareOk() {
        return timpAfisareOk;
    }

    public void setTimpAfisareOk(int timpAfisareOk) {
        this.timpAfisareOk = timpAfisareOk;
    }

    public int getTimpAfisareNotOk() {
        return timpAfisareNotOk;
    }

    public void setTimpAfisareNotOk(int timpAfisareNotOk) {
        this.timpAfisareNotOk = timpAfisareNotOk;
    }

    public int getIdOpAnterioara() {
        return idOpAnterioara;
    }

    public void setIdOpAnterioara(int idOpAnterioara) {
        this.idOpAnterioara = idOpAnterioara;
    }

    public int getRestrictieTimp() {
        return restrictieTimp;
    }

    public void setRestrictieTimp(int restrictieTimp) {
        this.restrictieTimp = restrictieTimp;
    }
    
    
    
    
    
    
    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }        
    public static String toJson(ArrayList<OperatieScan> lst){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<OperatieScan>>(){}.getType();
        String json = gson.toJson(lst, type);
        return json;
    }       

    public static void toJsonFile(ArrayList<OperatieScan> lst, String filePath) throws IOException{
        Gson gson = new GsonBuilder().create();
        FileWriter writer = new FileWriter(filePath);
        String json = toJson(lst);
        writer.append(json);
        writer.flush();
        writer.close();
    }
    
    public static ArrayList<OperatieScan> fromJsonFile(String filePath) throws FileNotFoundException{
        Gson gson = new GsonBuilder().create();
        File f = new File(filePath);
        if(!f.exists()) return new ArrayList<OperatieScan>();
        
        FileReader reader = new FileReader(filePath);
        Type type = new TypeToken<ArrayList<OperatieScan>>(){}.getType();
        ArrayList<OperatieScan> tab = new ArrayList<OperatieScan>();
        tab = gson.fromJson(reader, type);
        if(tab==null) tab=  new ArrayList<OperatieScan>();
        return tab;
    }
}
