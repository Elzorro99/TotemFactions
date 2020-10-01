package net.elzorro99.totemfactions.utils;

import java.lang.reflect.Field;

public class UPacketsInjector {
	
	/*
	 * Dev By jeff le boss
	 * 
	 */
	
	public static Object reflect(Class<?> clazz, String var, Object instance, Object default_value){
		try{
			Field field = clazz.getDeclaredField(var);
			field.setAccessible(true);
			return field.get(instance);
		}catch(Exception e){
			e.printStackTrace();
		}
		return default_value;
	}
	
	public static Object reflect(String var, Object instance, Object default_value){
		return reflect(instance.getClass() ,var, instance, default_value);
	}
	
	public static void input(Class<?> clazz, String var, Object instance, Object value){
		try{
			Field field = clazz.getDeclaredField(var);
			field.setAccessible(true);
			field.set(instance, value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
