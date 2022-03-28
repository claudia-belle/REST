package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainCarrera {
	
	//public static Carrera100 c = new Carrera100();
	
	public static void main(String[] args) throws IOException {

		peticion("GET","reinicio");
		
		peticion("GET","resultados");

	}
	
	public static void peticion(String metodo,String funcion) throws IOException {
		URL url = new URL("http://localhost:8080/prueba/carrera100/"+funcion);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		//System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			sb.append(output);
		}
		conn.disconnect();
	}

}
