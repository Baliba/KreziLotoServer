package dto;

import lombok.Data;

@Data 
public class NewPassword {
    private  String email,  pass;
    private int pin;
    public NewPassword() {
    }
}