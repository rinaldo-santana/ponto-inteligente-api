package com.everest.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	private static Logger logger =  LoggerFactory.getLogger(PasswordUtils.class);
	
	public static String gerarBCrypt(String senha){
		if (senha == null) {
			return senha;
		}		
		
		logger.info("Gerando hash com BCrypt...");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		
		return bCryptPasswordEncoder.encode(senha);				
	}
}
