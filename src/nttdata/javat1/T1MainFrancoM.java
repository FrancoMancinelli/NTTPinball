package nttdata.javat1;

import java.util.Scanner;

import nttdata.javat1.game.Game;
import nttdata.javat1.game.Player;
import nttdata.javat1.game.Sounds;

/**
 * Pinball
 * 
 * Clase Principal
 * 
 * @author Franco Mancinelli
 *
 */
public class T1MainFrancoM {

	/**
	 * Método principal
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*Ejecuta un sonido de Bienvenida*/
		Sounds sound = new Sounds();
		sound.ejecutarSonido("assets/inicioApp.wav");

		/* Hace una pausa de 10 segundos mientras suena la intro */
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/* Impresión del menú inicial */
		printMenuNewPlayer();
		String nick;

		/* Solicita una respuesta hasta que sea de 3 caracteres */
		do {
			nick = validationNickname();
		} while(nick.length() < 3 || nick.length() > 3);

		sound.ejecutarSonido("assets/opcionMenu.wav");
		
		/* Cuando todo sea correcto, se creará al 
		 * jugador y se pasará a crear un juego
		 */
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Game(new Player(nick)).launchGame();
		
	}

	/**
	 * Pedirá que se inserte un nickname para el jugador
	 * y comprobará que este cumpla con el requisito
	 * de tener un tamaño de 3 caracteres
	 * @return El nickname introducido
	 */
	private static String validationNickname() {
		String nick;
		System.out.print("\n • »» Respuesta: ");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		nick = sc.next().toUpperCase();
		
		/* Si la respuesta es incorrecta imprimirá un mensaje de error */
		if(nick.length() < 3 || nick.length() > 3) 
			printError();
		return nick;
	}
	
	/**
	 * Imprime un menu de bienvenida e indicará al jugador
	 * que debe introducir su nickname para comenzar
	 */
	public static void printMenuNewPlayer() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("#-------------------------------------#\n");
		sb.append("|    PINBALL NTTData - By Franco M    |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      ¡¡Bienvenido al PINBALL!!      |\n");
		sb.append("|        A continuación indica        |\n");
		sb.append("|     un nickname de 3 caracteres     |\n");
		sb.append("#-------------------------------------#\n");
		
		System.out.println(sb.toString());
	}
	
	/**
	 * Imprime un mensaje de error por introducir un nickname
	 * que no es valido
	 */
	public static void printError() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("\n#-------------------------------------#\n");
		sb.append("| ERR0R! - ERR0R! --- ERR0R! - ERR0R! |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("| El nickname debe ser de 3 cracteres |\n");
		sb.append("|        Vuelve a intentarlo...       |\n");
		sb.append("#-------------------------------------#");
		
		System.out.println(sb.toString());
	}
	


}
