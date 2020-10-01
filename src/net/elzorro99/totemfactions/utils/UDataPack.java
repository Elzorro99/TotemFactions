package net.elzorro99.totemfactions.utils;

public class UDataPack<K,V> {
	
	private K key;
	private V value;
	
	public UDataPack(K key, V value){
		this.key = key;
		this.value = value;
	}
	
	public K getKey(){
		return this.key;
	}
	
	public V getValue(){
		return this.value;
	}
	
}
