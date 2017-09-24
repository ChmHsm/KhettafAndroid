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
public class Khettaf  extends BaseModel implements Serializable  {

    @PrimaryKey
    private Long id;

    @Column
    private String utilisateur;

    @Column
    private String password;

    public Khettaf(){
        //JPA
    }

    public Khettaf(String username, String password) {
        this.utilisateur = username;
        this.password = password;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
