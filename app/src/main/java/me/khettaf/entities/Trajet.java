package me.khettaf.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.Date;

import me.khettaf.database.AppDatabase;

/**
 * Created by Me on 24/09/2017.
 */
@Table(database = AppDatabase.class)
public class Trajet  extends BaseModel {

    @Column
    @PrimaryKey
    private Long id;

    @Column
    @ForeignKey
    private Khettaf khettaf;

    @Column
    @ForeignKey
    private POI depart;

    @Column
    private String description;

    @Column
    private Long dateDepart;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Long dateDepart) {
        this.dateDepart = dateDepart;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    @ForeignKey
    private POI destination;

//    public Trajet(Khettaf khettaf, POI depart, POI destination) {
//        this.khettaf = khettaf;
//        this.depart = depart;
//        this.destination = destination;
//    }

    public Long getId() {
        return id;
    }

    public Khettaf getKhettaf() {
        return khettaf;
    }

    public void setKhettaf(Khettaf khettaf) {
        this.khettaf = khettaf;
    }

    public POI getDepart() {
        return depart;
    }

    public void setDepart(POI depart) {
        this.depart = depart;
    }

    public POI getDestination() {
        return destination;
    }

    public void setDestination(POI destination) {
        this.destination = destination;
    }
}
