/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.entity;

/**
 *
 * @author marius
 */
@Table(name="numemat")
public class NumeMat {
    @Id
    private int idNumeMat;
    private int idProdus;
    private int idMaterial;
    private String denMat;
    private String codMat;
    private String nrIndex;
    private String denProdus;
    private String infoProdus;

    public NumeMat() {
        idNumeMat=0;
        idProdus=0;
        idMaterial=0;
        denMat="";
        nrIndex="";
        denProdus="";
        infoProdus="";
    }

    public int getIdNumeMat() {
        return idNumeMat;
    }

    public void setIdNumeMat(int idNumeMat) {
        this.idNumeMat = idNumeMat;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getDenMat() {
        return denMat;
    }

    public void setDenMat(String denMat) {
        this.denMat = denMat;
    }

    public String getCodMat() {
        return codMat;
    }

    public void setCodMat(String codMat) {
        this.codMat = codMat;
    }

    public String getNrIndex() {
        return nrIndex;
    }

    public void setNrIndex(String nrIndex) {
        this.nrIndex = nrIndex;
    }

    public String getDenProdus() {
        return denProdus;
    }

    public void setDenProdus(String denProdus) {
        this.denProdus = denProdus;
    }

    public String getInfoProdus() {
        return infoProdus;
    }

    public void setInfoProdus(String infoProdus) {
        this.infoProdus = infoProdus;
    }
    
}
