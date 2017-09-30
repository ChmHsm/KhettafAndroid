package me.khettaf.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import me.khettaf.database.AppDatabase;

/**
 * Created by Me on 24/09/2017.
 */
@Table(database = AppDatabase.class)
public class POI  extends BaseModel {

    @Column
    @PrimaryKey
    private Long id;

    @Column
    private Double X;

    @Column
    private Double Y;

    @Column
    private String nom;

//    public POI(Double x, Double y, String nom) {
//        X = x;
//        Y = y;
//        this.nom = nom;
//    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
