package lzw;

public class Dicionario {
	 String[] dicionario;
	 int k=2;
	 int fim=-1;
	 int dictSize = (int) Math.pow(2,k*8);
	 boolean isFull = false;
	 
	 Dicionario(){
	 dicionario = new String[dictSize]; 

		 for(int i=0; i<256; i++) {
			 char c = (char) i;
			 inserir(""+ c);
		 }
	 }
	 
	 public int codigo(String wc) {
		 if(isFull) {
			 for(int i=0; i < dictSize; i++) {
				 if(dicionario[i].compareTo(wc) == 0) {
					 return i;
				 }
			 }
			return -1;
		 }
		 
		 else {
			 
		 for(int i=0; i < fim; i++) {
			 if(dicionario[i].equals(wc)) {
				 return i;
			 }
		 }
		 	return -1;
		}
	 }
	 public void printar() {
		 for(int i=0;i<fim+1;i++) {
			 System.out.println(dicionario[i]);
		 }
	 }
	 public void resetar() {
		 fim=-1;
		 for(int i=0;i<128;i++) {
			 fim++;
			 dicionario[fim] = new String(""+(char)i);
		 }
	 }
	 
	 public void inserir(String a) {  
			fim++;
		 	if(fim >= dictSize) {
		 		isFull = true;
		 		System.out.println("--------------------------------Encheu");
		 	}
		 	else
			 	dicionario[fim] = new String(a);
	}
	 
	 String retornar(int code) {
	        if ( code > fim )
	            return "";
	        else        
	            return dicionario[code];
	    }
}