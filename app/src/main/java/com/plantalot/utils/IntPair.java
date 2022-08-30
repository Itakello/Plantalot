package com.plantalot.utils;

import androidx.annotation.NonNull;

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
	
	@NonNull
	public String toString() {
		return x + " × " + y;
	}
	
	public int max() {
		return Math.max(x, y);
	}
	
}
