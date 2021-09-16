package dto;

import lombok.Data;

@Data 
public class NRole {
	 private Long id;
    private  String name;
    public NRole(Long id , String name) {
    	this.id = id;
        this.name = name;
    }
}