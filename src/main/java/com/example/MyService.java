package com.example;

import io.advantageous.qbit.annotation.RequestMapping;
import java.util.Collections;
import java.util.List;

public class MyService {

	@RequestMapping
	public List<String> ping() {
		return Collections.singletonList("Hello World!");
	}
	/*
	 * @RequestMapping public List<?> puke() {
	 * 
	 * throw new IllegalStateException("Crap"); }
	 */
}