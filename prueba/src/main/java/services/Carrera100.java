package services;

import java.util.concurrent.Semaphore;

import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("carrera100")
@Singleton
public class Carrera100 {
	
	static int preparados=0,listos=0;
	static boolean carrera=false;
	static Semaphore semaforoP,semaforoL,semaforoR;
	static Atleta[] atletas;
	
	@Path("reinicio")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public static String reinicio(@DefaultValue("4") @QueryParam(value="num") int num) throws InterruptedException {
		
		atletas = new Atleta[num];
		semaforoP = new Semaphore(num);	//preparados
		semaforoL = new Semaphore(num);	//listos
		semaforoR = new Semaphore(num);	//resultados
		semaforoP.acquire(num);
		
		for (int i=0; i<num; i++) {
			atletas[i] = new Atleta(i, semaforoP, semaforoL, semaforoR);	//creo que no hace falta pasar el semaforo
			atletas[i].start();	
		}
		
		carrera=true;
		return "Inicio.";
	}
	
	
	@Path("preparado")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public static String preparado() throws InterruptedException {

		if (!carrera) {return "ERROR: No hay carrera!";}
		
		for (int i=0; i<atletas.length; i++) {
			if (atletas[i].flag) {preparados++;}
			if (preparados==atletas.length) {
				semaforoP.release(atletas.length);	//sueltan semaforos Preparados
				semaforoL.acquire(atletas.length);	//cogen semaforos Listos
			}
			//semaforoP.release();
		}
		
		return "Preparado...";
	}
	
	@Path("listo")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public static String listo() throws InterruptedException {

		if (!carrera || preparados<atletas.length) {return "ERROR: No estan preparados!";}
		
		for (int i=0; i<atletas.length; i++) {
			if (atletas[i].flag2) {listos++;}
			if (listos==atletas.length) {
				semaforoL.release(atletas.length);}
				semaforoR.acquire(atletas.length);
		}
		
		return "Listo...";
	}
	
	@Path("llegada")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public static String llegada(@QueryParam(value="midorsal") int midorsal) {
		carrera=false;
		//tiene que mandar el tiempo a resultados
		return "Dorsal: " +midorsal+ " y he tardado: " +atletas[midorsal].tiempo;
	}
	
	@Path("resultados")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public static String resultados() {
		if(!carrera) {
			String s ="\n";
			for (int i=0; i<atletas.length;i++) {
				s+=("Soy el corredor con el dorsal " + atletas[i].dorsal +
					" y he tardadado " + (Math.round(atletas[i].tiempo)/1000d) + " segundos.\n");
			}
			return s;
		}else {return "ERROR: No han terminado de correr!";}
	}
	
}

/*
int preparados=0;
Semaphore semaforo = new Semaphore(8);
semaforo.acquire(4);
Atleta[] atletas = new Atleta[8];


//reinicio
for (int i=0; i<4; i++) {
	atletas[i] = new Atleta(i, semaforo);
	atletas[i].start();
}

System.out.println("Comienza la carrera de 100m lisos!");
System.out.println("\nPreparados!");
Thread.sleep(1000);
System.out.println("Listos!");
Thread.sleep(1000);
System.out.println("YA!\n");

for (int i=0; i<4; i++) {
	if (atletas[i].flag) {preparados++;}
	if (preparados==4) {
		semaforo.release(4);}
}

return "algo"+ atletas[2].resultados();
*/