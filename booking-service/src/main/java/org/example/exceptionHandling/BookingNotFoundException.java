package org.example.exceptionHandling;

public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException(String s){
        super(s);
    }
}
