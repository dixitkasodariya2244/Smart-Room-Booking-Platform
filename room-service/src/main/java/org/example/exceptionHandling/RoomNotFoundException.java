package org.example.exceptionHandling;

public class RoomNotFoundException extends RuntimeException{

    public RoomNotFoundException(String s){
        super(s);
    }
}
