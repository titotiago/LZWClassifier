package lzw;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LZW {
	StringBuffer v = new StringBuffer("");
	Testes conversor = new Testes();
	
	public Dicionario Encode(byte[] bytes, FileOutputStream fos, int k, Dicionario dic) throws IOException{
	    int temp=0;
	    //Dicionario dic = new Dicionario();
	    dic.k = k;
	    String p = "";
	    String PC;
	    float i=1;
	    long l=0;
	    byte[] result;
	    for(byte b:bytes) {
	    char c = (char) (b & 0xFF);    
			PC = p + c;
			
			if (i%1000 == 0)
				System.out.println(i + "Codificando:" + (i/bytes.length)*100 + "%" + " - Tamanho do dicionário:" +  dic.fim); // Printar porcentagem do texto processado
	    	if (dic.codigo("" + PC ) != -1) {// se P + C estiver na lista
	        	p = PC;
	    	} else {
	    		
	    		temp = dic.codigo("" + p);
	    		l = (long) temp;
	    		result = conversor.longToBytes(l); // Long para bytes
	    		byte[] a = conversor.parse(result,k); // colocando apenas k bytes
	    		fos.write(a);
	        	
	        	//Inserir PC no dicionario
	        	if(!dic.isFull)
	        		dic.inserir(PC);
	        	p = "" + c;
	    	}
	    	i++;
	    }
	    temp = dic.codigo("" + p);
	    l = (long) temp;
		result = conversor.longToBytes(l);
		byte[] a = conversor.parse(result,k);
		fos.write(a);
		return dic;
//    	saida.append(temp + "");
	}
	
	public static Dicionario compress(File source, File saida, int k, Dicionario dicionario) throws IOException{
		byte[] bytesArray = new byte[(int) source.length()]; //array de bytes do tamanho do arquivo
		FileInputStream fis = new FileInputStream(source);
		FileOutputStream fos = new FileOutputStream(saida);
		fis.read(bytesArray,0,(int)source.length()); // insiro em bytesArray todos os bytes do arquivo
		
		LZW alg = new LZW(); // instancio o algoritmo
		Dicionario dic = alg.Encode(bytesArray, fos, k, dicionario);
		fis.close();
		fos.close();

		return dic;
	}
	
	
	
	public String Decode(long l, Dicionario dic) {
	    String entry;
 	    if (v.length() == 0) {
	        v = new StringBuffer( dic.retornar((int)l) );
	        return(v.toString());
	    } else {
	        entry = new String( dic.retornar((int)l) );

	        if (entry.length() == 0) {          
	            entry = new String( v.toString() + v.charAt(0) );
	        }
	        if(!dic.isFull) {
	        	dic.inserir("" + v + entry.charAt(0) );
	        }
	        v = new StringBuffer(entry);
	        return entry;
	    }
	}
	
	public static void decompress(File source, File saida, int k) throws IOException{
		LZW alg = new LZW(); // instancio o algoritmo
		List<Long> codigos = new ArrayList<Long>();
		Dicionario dic = new Dicionario();		
		byte[] bytesArray = new byte[(int) source.length()]; //array de bytes do tamanho do arquivo
		//dic.k = k;
		Testes conversor = new Testes();
		FileOutputStream fos = new FileOutputStream(saida);
		FileInputStream fis = new FileInputStream(source);
		fis.read(bytesArray,0,(int)source.length()); // insiro em bytesArray todos os bytes do arquivo

		byte[] buf = new byte[k];
		byte[] bufLong = new byte[8];
		long temp;
		StringBuffer result = new StringBuffer();
		
		// Trecho para pegar k Bytes por vez do array de bytes
		for (int i = 0; i < bytesArray.length; i=i+k) {
			for(int j=0; j < k; j++)
				buf[j] = bytesArray[i+j];
			bufLong = conversor.preencher(buf, k);
			temp = conversor.bytesToLong(bufLong);
			codigos.add(temp);
        }
		
		// percorrer os longs todos
		for(long l: codigos) {
			result.append(alg.Decode(l,dic));
		}
		byte[] strToBytes = result.toString().getBytes(); 

		fos.write(strToBytes);
		fos.close();
		fis.close();
	}
	
	public static void groupImages(int pasta) throws IOException {
		String path = "C:\\Users\\amori\\Downloads\\att_faces\\s" + Integer.toString(pasta) + "\\";
		File concat = new File(path + "concatenado3.pgm");

		for(int i=1; i<10; i++) {
			String pathIndex = path + Integer.toString(i) + ".pgm";
			InputStream in = new FileInputStream(pathIndex);
			byte[] buffer = new byte[1 << 20];  // loads 1 MB of the file
			// open file output stream to which files will be concatenated. 
			OutputStream os = new FileOutputStream(concat,true);
			int count;
			// read entire file1.txt and write it to file3.txt
			while ((count = in.read(buffer)) != -1) {
		    	os.write(buffer, 0, count);
		    	os.flush();
			}
			os.close();
			in.close();
		}
	}
	
	/*Passo a passo
	 * Entrar na pasta e agrupar 9
	 * Comprimir os 9 e gerar o dicionário
	 * Usar o dicionário para comprimir os 41 arquivos de teste 
	 * Guardar os tamanhos em um TXT
	 * Repetir até acabar as pastas
	 * 
	 * */
	
	public static void main(String args[]) throws IOException{
		//File source = new File("C:\\Users\\amori\\Desktop\\teste.txt");
		//File comprimido = new File("C:\\Users\\amori\\Desktop\\compressed1.txt");
		//File descomprimido = new File("C:\\Users\\amori\\Desktop\\decompressed.txt");
		
		int k=2;
		
		//Entrando em todas as pastas e agrupando as imagens
		/*for(int pasta=1; pasta<41; pasta++) {
			System.out.println("Agrupando as imagens pasta:" + pasta);
			groupImages(pasta);
		}*/
		
		for(int pasta=1; pasta<41; pasta++) {
			String path = "C:\\Users\\amori\\Downloads\\att_faces\\s" + Integer.toString(pasta) + "\\";
			File source = new File(path + "concatenado3.pgm"); // arquivo concatenado
			File comprimido = new File(path + "comprimido.pgm");
			Dicionario padrao = new Dicionario();
			System.out.println("Comprimindo a imagem concatenada da pasta: " + pasta);
			Dicionario dic = compress(source, comprimido, k, padrao); //treina o dicionario
			System.out.println("Terminou de comprimir a imagem concatenada da pasta: " + pasta);
			
			for(int i=1; i<41 ;i++) {
				Dicionario temp = new Dicionario();
				temp.dicionario = dic.dicionario;
				temp.k = dic.k;
				temp.fim = dic.fim;
				temp.dictSize = dic.dictSize;
				temp.isFull = dic.isFull;

				System.out.println("Testando o dicionário - "+ pasta + "- no grupo:" + i);
				File a = new File("C:\\Users\\amori\\Downloads\\att_faces\\s" + Integer.toString(i) + "\\resultado-dicionario-" + pasta);
				File b = new File( "C:\\Users\\amori\\Downloads\\att_faces\\s" + Integer.toString(i) +"\\10.pgm");
				compress(b,a,k,temp); //comprime
			}	
		}
			
		//Dicionario dic = compress(source, comprimido, k);
		//decompress(comprimido,descomprimido,k);
		
	}
}