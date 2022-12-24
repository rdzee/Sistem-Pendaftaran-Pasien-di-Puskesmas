package models;

import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;
import java.util.*;
import java.text.*;
import java.math.*;

@Entity
public class Booking extends Model {
    
    @Required
    @ManyToOne
    public User user;
    
    @Required
    @ManyToOne
    public Hotel hotel;
    
    @Required
    @Temporal(TemporalType.DATE) 
    public Date checkinDate;

    
    @Required(message="NIK Harus di ISI")
    @Match(value="^\\d{15}$", message="NIK berisi 15 Angka dapat dilihat pada KTP atau KK")
    public String creditCard;
    
    @Required(message="Nama Lengkap Harus di ISI")

    public String creditCardName;
    public int creditCardExpiryMonth;
    public int creditCardExpiryYear;
    public boolean smoking;
    public String alamatp;
    public String kelamin;
    public String namakepala;
    public int rt;
    public int rw;
    public String bpjs;
    public int kec;
    public int kel;

    public Booking(Hotel hotel, User user) {
        this.hotel = hotel;
        this.user = user;
    }

    public String toString() {
        return "Booking(" + user + ","+ hotel + ")";
    }

}
