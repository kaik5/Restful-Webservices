package com.appdeveloperblog.app.ws.ui.model.response;

import java.util.Date;

public class ErrorMessage 
{

    private Date timpestamp;
    private String message;

    public ErrorMessage(Date timpestamp, String message) 
    {
        this.timpestamp = timpestamp;
        this.message = message;
    }
    public Date getTimpestamp() {
        return timpestamp;
    }
    public void setTimpestamp(Date timpestamp) {
        this.timpestamp = timpestamp;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


    
}
