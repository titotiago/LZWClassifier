package lzw;

import java.nio.ByteBuffer;

public class Longtobinary {
	
	public static String converter(long l, int k) {
		String resultado = Long.toBinaryString(l);
		int i;
		if(resultado.length() < k*8) {
			StringBuffer ajeitar = new StringBuffer("");
			i = resultado.length();
			while(i < k*8) {
				ajeitar.append("0");
				i++;
			}
			return ajeitar + resultado;
		} 
		return	resultado;
	}
	
	public static void main(String args[]) {
		long x = 65536;
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		ByteBuffer inverter = ByteBuffer.allocate(Long.BYTES);
	    byte a[] = buffer.putLong(x).array();
	    System.out.println();
	    inverter.put(a);
	    inverter.flip();
	    System.out.println(inverter.getLong());
	    
	}
}


