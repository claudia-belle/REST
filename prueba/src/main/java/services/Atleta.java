package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;


public class Atleta extends Thread {

	int dorsal;
	boolean flag,flag2, meta;
	double tiempo;
	Semaphore semP,semL,semR;


	public Atleta(int id, Semaphore sP, Semaphore sL, Semaphore sR) {
		this.dorsal = id+1;
		this.flag=false;
		this.flag2=false;
		this.tiempo =0;
		this.semP = sP;
		this.semL = sL;
		this.semR = sR;
	}
	
	public void run() {
		
		try {
			preparado(this.semP);
			peticion2("GET","preparado");
			
			listo(this.semL);
			peticion2("GET","listo");
			
			this.tiempo=(Math.random()*(11.76-9.56)+9.56) *1000;
			Thread.sleep((long) this.tiempo);
			peticion2("GET","llegada?midorsal="+this.dorsal);

		} catch (Exception e) {e.printStackTrace();}
	}

	public void preparado(Semaphore sp) throws InterruptedException {
		this.semP =sp;
		semP.acquire();
		this.flag=true;
	}
	public void listo(Semaphore sl) throws InterruptedException {
		this.semL =sl;
		semL.acquire();
		this.flag2=true;
	}

	public static void peticion2(String metodo, String funcion) throws IOException {
		URL url = new URL("http://localhost:8080/prueba/carrera100/"+funcion);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
		}
		//StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;
		//System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			//sb.append(output);
		}
		conn.disconnect();
	}

}