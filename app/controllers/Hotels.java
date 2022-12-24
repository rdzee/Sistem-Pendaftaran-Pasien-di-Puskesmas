package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;

public class Hotels extends Application {
    
    @Before
    static void checkUser() {
        if(connected() == null) {
            flash.error("Silahkan Masuk");
            Application.index();
        }
    }
    
    // ~~~
    
    public static void index() {
        List<Booking> bookings = Booking.find("byUser", connected()).fetch();
        render(bookings);
    }

    public static void list(String search, Integer size, Integer page) {
        List<Hotel> hotels = null;
        page = page != null ? page : 1;
        if(search.trim().length() == 0) {
            hotels = Hotel.all().fetch(page, size);
        } else {
            search = search.toLowerCase();
            hotels = Hotel.find("lower(name) like ?1 OR lower(city) like ?2", "%"+search+"%", "%"+search+"%").fetch(page, size);
        }
        render(hotels, search, size, page);
    }
    
    public static void show(Long id) {
        Hotel hotel = Hotel.findById(id);
        render(hotel);
    }
    
    public static void book(Long id) {
        Hotel hotel = Hotel.findById(id);
        render(hotel);
    }
    
    public static void confirmBooking(Long id, Booking booking) {
        Hotel hotel = Hotel.findById(id);
        booking.hotel = hotel;
        booking.user = connected();
        validation.valid(booking);
        
        // Errors or revise
        if(validation.hasErrors() || params.get("revise") != null) {
            render("@book", hotel, booking);
        }
        
        // Confirm
        if(params.get("confirm") != null) {
            booking.create();
            flash.success("Terima Kasih, %s, Konfirmasi Pemesanan untuk %s is %s", connected().name, hotel.name, booking.id);
            index();
        }
        
        // Display booking
        render(hotel, booking);
    }
    
    public static void cancelBooking(Long id) {
        Booking booking = Booking.findById(id);
        booking.delete();
        flash.success("Pemesanan dibatalkan untuk no pesanan %s", booking.id);
        index();
    }
    
    public static void settings() {
        render();
    }
    
    public static void saveSettings(String password, String verifyPassword) {
        User connected = connected();
        connected.password = password;
        validation.valid(connected);
        validation.required(verifyPassword);
        validation.equals(verifyPassword, password).message("Password tidak sama");
        if(validation.hasErrors()) {
            render("@settings", connected, verifyPassword);
        }
        connected.save();
        flash.success("Password diperbarui");
        index();
    }
    
}

