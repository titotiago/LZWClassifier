package lzw;

import java.io.*;
import java.nio.ByteBuffer;


public class Testes {
	 public static void main (String arg[]) {
		 int minimoIndex=0;
		 double acerto=0;
		 long min=1000000000;
		 for(int pasta=1; pasta<41; pasta++) {
			 String path = "C:\\Users\\amori\\Downloads\\att_faces\\s"+ pasta + "\\resultado-dicionario-";
			 for(int i=1;i<41;i++) {
				 File teste = new File(path + Integer.toString(i));
				 if(min > teste.length()) {
				 	min = teste.length();
				 	minimoIndex = i;
				 	//System.out.println(i);
				 }
		 	}
			 System.out.println(minimoIndex);
			 if(minimoIndex == pasta) {
				 acerto++;
			 }
			 min=1000000000;
			 minimoIndex=-1;
		 }
		 System.out.println("A taxa de acerto foi de: " + (acerto/40)*100 + "%");
		 /*int k=1;
		 Testes conversor = new Testes();
		 long a = 254;
		 byte[] b = conversor.longToBytes(a);
		 byte[]c = conversor.parse(b,k); // o que deve ser inserido / será lido
		 byte[]d = conversor.preencher(c, k);
		 long r = conversor.bytesToLong(d);
		 
		 System.out.println("Long:" +a + "\n" + "Bytes:" + b +" - Bytes.lenght():"
		 +b.length +"\n"+ "Byte parseado: "+ c + " - ByteParseado.length(): " 
				 + c.length + "\nresultado: " + r );
		 */
	 }
	 public byte[] longToBytes(long x) {
		    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		    buffer.putLong(x);
		    return buffer.array();
		}
	 
		public long bytesToLong(byte[] bytes) {
		    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		    buffer.put(bytes);
		    buffer.flip();//need flip 
		    return buffer.getLong();
		}
		
		public byte[] preencher(byte[]b, int k) {
			byte[] saida = new byte[8];
			int j=0;
				for(int i=0;i< saida.length;i++) {
					if(i <8-k ) {
						saida[i]= 00000000;
					}
					else {
						saida[i] = b[j];
						j++;
					}
				}
				return saida;
		}
		
		public byte[] parse(byte[] b, int k ) {
			byte[] saida = new byte[k];
			for(int i=0; i < k; i++) {
				saida[i] = b[8-k+i];
			}
			return saida;
		}
}