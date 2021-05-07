/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackprod.com.entity;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author marius
 */
@Table(name="Materiale")
public class Materiale {
    @Id
    int idMaterial;
    int idTipMaterial;
    String denumire;
    String codMat;
    String um;
    int idStructuraMaterial;
    boolean inactiv;
    boolean ancoraTrasabilitate;
    int idMaterialMaster;
    int idUserAdd;
    int idUserMod;
    @TimeStampFormat
    java.util.Date dataAdd;
    @TimeStampFormat
    java.util.Date dataMod;

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public int getIdTipMaterial() {
        return idTipMaterial;
    }

    public void setIdTipMaterial(int idTipMaterial) {
        this.idTipMaterial = idTipMaterial;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getCodMat() {
        return codMat;
    }

    public void setCodMat(String codMat) {
        this.codMat = codMat;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public int getIdStructuraMaterial() {
        return idStructuraMaterial;
    }

    public void setIdStructuraMaterial(int idStructuraMaterial) {
        this.idStructuraMaterial = idStructuraMaterial;
    }

    public boolean isInactiv() {
        return inactiv;
    }

    public void setInactiv(boolean inactiv) {
        this.inactiv = inactiv;
    }

    public boolean isAncoraTrasabilitate() {
        return ancoraTrasabilitate;
    }

    public void setAncoraTrasabilitate(boolean ancoraTrasabilitate) {
        this.ancoraTrasabilitate = ancoraTrasabilitate;
    }

    public int getIdMaterialMaster() {
        return idMaterialMaster;
    }

    public void setIdMaterialMaster(int idMaterialMaster) {
        this.idMaterialMaster = idMaterialMaster;
    }

    public int getIdUserAdd() {
        return idUserAdd;
    }

    public void setIdUserAdd(int idUserAdd) {
        this.idUserAdd = idUserAdd;
    }

    public int getIdUserMod() {
        return idUserMod;
    }

    public void setIdUserMod(int idUserMod) {
        this.idUserMod = idUserMod;
    }

    public Date getDataAdd() {
        return dataAdd;
    }

    public void setDataAdd(Date dataAdd) {
        this.dataAdd = dataAdd;
    }

    public Date getDataMod() {
        return dataMod;
    }

    public void setDataMod(Date dataMod) {
        this.dataMod = dataMod;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Materiale other = (Materiale) obj;
        if (this.idMaterial != other.idMaterial) {
            return false;
        }
        if (this.idTipMaterial != other.idTipMaterial) {
            return false;
        }
        if (this.idStructuraMaterial != other.idStructuraMaterial) {
            return false;
        }
        if (this.inactiv != other.inactiv) {
            return false;
        }
        if (this.ancoraTrasabilitate != other.ancoraTrasabilitate) {
            return false;
        }
        if (this.idMaterialMaster != other.idMaterialMaster) {
            return false;
        }
        if (this.idUserAdd != other.idUserAdd) {
            return false;
        }
        if (this.idUserMod != other.idUserMod) {
            return false;
        }
        if (!Objects.equals(this.denumire, other.denumire)) {
            return false;
        }
        if (!Objects.equals(this.codMat, other.codMat)) {
            return false;
        }
        if (!Objects.equals(this.um, other.um)) {
            return false;
        }
        if (!Objects.equals(this.dataAdd, other.dataAdd)) {
            return false;
        }
        if (!Objects.equals(this.dataMod, other.dataMod)) {
            return false;
        }
        return true;
    }
    
    
}
