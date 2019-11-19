package com.ec.beans;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.ec.exeption.UnknownExeption;

@Component
public class Motor {
	public void start() {
		System.out.println("Motor.start()");
	}
	
	/*
	 * @PreDestroy public void destroy() throws Exception {
	 * System.out.println("Motor.destroy()"); throw new
	 * Exception("UnknoewEgeonbbj"); }
	 */
}
