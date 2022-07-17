package com.plantalot.utils;

public class IntPair {
	public int x;
	public int y;
	
	public IntPair() {
		this.x = 0;
		this.y = 0;
	}
	
	public IntPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return x + " × " + y;
	}
}
