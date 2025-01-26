package com.sample.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class AuthRequest {
	
	private String userName;
	private String password;
	
	public AuthRequest() {
		
	}
	public AuthRequest(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

}
