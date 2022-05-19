package nttdata.javat1.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Pinball
 * 
 * Clase Game
 * 
 * @author Franco Mancinelli
 *
 */
public class Game {

	/* ~~ ATRIBUTOS */
	private Player player;
	private List<Ball> balls = new ArrayList<Ball>(5);
	private final static Sounds sound = new Sounds();
	private int multiplier = 0;
	private int score = 0;
	private int bet;
	private Clip clip;

	/* ~~ CONSTRUCTOR */
	public Game(Player player) {
		super();
		this.player = player;
	}

	/* ~~ GETTERS && SETTERS */
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<Ball> getBolas() {
		return balls;
	}

	public void setBolas(List<Ball> bolas) {
		this.balls = bolas;
	}

	/* ~~ MÉTODOS */

	/**
	 * Inicia el juego imprimiendo el menú de juego Y solicitará al jugador indicar
	 * una opción del menú
	 */
	public void launchGame() {

		printLauncherMenu();
		requestAnswerMainMenu();
	}

	/**
	 * Solicitará una respuesta hasta que la misma sea validA. Según la opción que
	 * se elija, se realizará un proceso u otro.
	 */
	private void requestAnswerMainMenu() {
		String answer;

		/* Solicita una respuesta hasta que sea valida */
		do {
			System.out.print(" • »» Respuesta: ");
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			answer = sc.next().toUpperCase();

			/* Ejecuta un sonido */
			sound.ejecutarSonido("assets/opcionMenu2.wav");

			/* Comprueba la opción respondida */
			switch (answer) {

			case "A":
				/* Si es posible iniciara un nuevo juego */
				executeOptionA();
				break;

			case "B":
				/* Imprime información de como jugar */
				printHowToPlay();

				returnToPrincipalMenu();
				break;

			case "C":
				/* Imprime la información del jugador */
				printPlayerInfo();

				returnToPrincipalMenu();
				break;

			case "D":
				/* Entrará al cajero para realizar una compra */
				executeOptionD();
				break;

			case "X":
				/* Terminará y saldrá de la aplicación */
				printExit();
				break;

			default:
				/* Imprimirá un mensaje de respuesta invalida */
				printErrorMenus();
			}

		} while (!answer.equals("X"));
	}

	/**
	 * Comprueba si el jugador tiene dinero suficiente para realizar una compra, de
	 * ser así, entrará al cajero. De lo contrario imprimirá un mensaje de error.
	 */
	private void executeOptionD() {
		/* Si el jugador SI tiene dinero suficiente para realizar una compra... */
		if (player.haveEnoughMoney()) {
			printCashier();
			requestValidAmount();

			/* Si el jugador NO tiene dinero suficiente */
		} else {
			printNotMoney();
		}

		/* Devuelve al menu inicial */
		returnToPrincipalMenu();
	}

	/**
	 * Solicitará al usuario introducir una cantidad de fichas para comprar hasta
	 * que sea posible realizar la compra. De lo contrario imprimirá un error y
	 * volverá a solicitar respuesta.
	 */
	private void requestValidAmount() {
		boolean validInsert = Boolean.FALSE;
		int amount = 0;

		do {
			System.out.print(" • »» Respuesta: ");
			@SuppressWarnings("resource")
			Scanner sc4 = new Scanner(System.in);

			/* Comprueba si la cantidad introducida es un valor númerico */
			try {
				amount = sc4.nextInt();

				/* Si al jugador le alcanza el dinero para comprar la cantidad indicada... */
				validInsert = purchaseVerification(amount);

			} catch (Exception e) {
				printNoValidAnswer();
			}
		} while (!validInsert);

		/* Actualiza stats del jugador */
		player.updateInvestedMoney(amount);
	}

	/**
	 * Comprueba si el usuario tiene fichas suficientes para iniciar un juego nuevo.
	 * De ser así, continuará con el procedimiento de inicio. De lo contrario
	 * imprimirá un mensaje de error.
	 */
	private void executeOptionA() {
		/* Comprueba si el jugador tiene tokens */
		if (player.haveTokens()) {
			/* Ejecuta todos los procedimientos para iniciar un juego */
			printIndicateBet();
			requestValidBet();
			executeCoinSound();
			printTypeGameMenu();
			requestAnswerGameMode();
			/* Si el jugador no tiene tokens */
		} else {
			printNoTokens();
		}

		/*
		 * Devuelve al menu inicial luego de jugar o luego de imprimir un error
		 */
		returnToPrincipalMenu();
	}

	/**
	 * Solicitará al jugador introducir una respuesta hasta que esta sea valida del
	 * menú de dificultad. Cuando la respuesta sea valida iniciará el juego
	 */
	private void requestAnswerGameMode() {
		String gameModeAnswer;
		do {
			System.out.print(" • »» Respuesta: ");
			@SuppressWarnings("resource")
			Scanner sc3 = new Scanner(System.in);

			gameModeAnswer = sc3.next().toUpperCase();

			/* Ejecuta sonido */
			sound.ejecutarSonido("assets/opcionMenu2.wav");

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			/* Intentará iniciar un juego con la dificultad indicada */
			prepareToLaunchGame(gameModeAnswer);

		} while (!gameModeAnswer.equals("E") && !gameModeAnswer.equals("M") && !gameModeAnswer.equals("H")
				&& !gameModeAnswer.equals("L"));
	}

	/**
	 * Ejecuta un sonido de moneda y deja 3 segundos a que termine de reproducirse
	 * antes de continuar.
	 */
	private void executeCoinSound() {
		try {
			sound.ejecutarSonido("assets/coinInserted.wav");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Solicitará al jugador introducir una apuesta hasta que sea valida.
	 */
	private void requestValidBet() {
		boolean betValid = Boolean.FALSE;

		do {
			System.out.print(" • »» Respuesta: ");
			@SuppressWarnings("resource")
			Scanner sc2 = new Scanner(System.in);

			/* Comprueba si la cantidad introducida es un valor númerico */
			try {
				this.bet = sc2.nextInt();

				/* Si al jugador tiene esa cantidad de Tokens para apostar... */
				betValid = player.betVerification(this.bet);

			} catch (Exception e) {
				printNoValidAnswer();
			}
		} while (!betValid);
	}

	/**
	 * Deja pasar 5 segundos y devuelve al jugador al menú principal. A su vez
	 * ejecutará un sonido
	 */
	private void returnToPrincipalMenu() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/* Ejecuta un sonido */
		sound.ejecutarSonido("assets/opcionMenu.wav");
		printLauncherMenu();
	}

	/**
	 * Intenrará iniciará un juego y lo configurará según el modo de juego indicado por
	 * parametro. Si la respuesta no es valida, imprimirá un error.
	 * 
	 * @param s La letra que representa cada modo de juego
	 */
	private void prepareToLaunchGame(String s) {
		switch (s) {

		/* Configura un juego en modo EASY */
		case "E":
			printEasyGameMode();
			addBallsToArray(5);
			this.multiplier = 2;
			lunchGame();
			break;

		/* Configura un juego en modo MEDIUM */
		case "M":
			printMediumGameMode();
			addBallsToArray(4);
			this.multiplier = 3;
			lunchGame();
			break;

		/* Configura un juego en modo HARD */
		case "H":
			printHardGameMode();
			addBallsToArray(3);
			this.multiplier = 4;
			lunchGame();
			break;

		/* Configura un juego en modo LENGENDARY */
		case "L":
			printLegendaryGameMode();
			addBallsToArray(1);
			this.multiplier = 5;
			lunchGame();
			break;

		default:
			/* Imprime un mensaje de error */
			printErrorMenus();
		}
	}

	private void lunchGame() {
		/* Quita los tokens apostados al usuario */
		player.removeTokens(this.bet);

		/* Pausa de 15 segundos antes de comenzar el juego */
		try {
			Thread.sleep(15000);
			clip = ejecutarSonidoLoop("assets/partida1.wav");
			Thread.sleep(3500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gameDevelopment(balls);
	}

	/**
	 * Lanzará las bolas e irá reproduciendo eventos hasta que se hayan acabado las
	 * bolas.
	 * 
	 * @param b Una lista con las bolas disponibles
	 */
	private void gameDevelopment(List<Ball> b) {
		ArrayList<Ball> balls = (ArrayList<Ball>) b;
		System.out.println("»»»» Una NUEVA bola se ha LANZADO ««««");

		do {
			/* Lanza un evento cada 1 segundo */
			try {
				executeGameEvents(balls);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (!balls.isEmpty());

		try {
			/*
			 * Detiene el sonido del juego y imprime los paneles correspondientes indicando
			 * que el juego ha acabado
			 */
			clip.close();
			Thread.sleep(3000);
			printGameOver();
			Thread.sleep(3000);
			checkWinner();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generará distintos eventos aleatorios que pueden ocurrir a una bola. 80% de
	 * probabilidad de obtener un evento positivo. 10% de probabilidad de obtener un
	 * evento negativo. 10% de probabilidad de perder la bola
	 * 
	 * @param balls El array de bolas disponibles para jugar
	 */
	private void executeGameEvents(ArrayList<Ball> balls) {
		Random r = new Random();
		int event = r.nextInt(100 - 0) + 1;

		/* 80% de que ocurre un evento bueno */
		if (event >= 1 && event <= 80) {
			printGoodEvent();

			/* 10% de caer en la carcel */
		} else if (event >= 81 && event <= 90) {
			System.out.println("• La bola ha caido en la CARCEL       | Pierdes: 50 puntos");
			this.score -= 50;

			/* 10% de perder la bola */
		} else {
			printLostBall(balls);
			balls.remove(0);

			/* Si luego de perder una bola, aun quedan más, se lanzará otra bola */
			if (!balls.isEmpty())
				System.out.println("»»»» Una NUEVA bola se ha LANZADO ««««");
		}
	}

	/**
	 * Ejecutará un sonido de manera infinita hasta que se le indique que pare.
	 * 
	 * @param url Url donde se encuentra alojado el sonido
	 * @return El sonido ejecutado en forma de Clip
	 */
	private Clip ejecutarSonidoLoop(String url) {
		try {
			Clip sonido = AudioSystem.getClip();

			File f = new File(url);
			sonido.open(AudioSystem.getAudioInputStream(f));
			sonido.start();
			sonido.loop(Clip.LOOP_CONTINUOUSLY);
			return sonido;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Comprueba si el jugador ha ganado o no, según si ha superado el objetivo de
	 * 500 puntos e imprimirá un panel indicando el resultado. Además según
	 * corresponda pagará al usuario o no y actualizará sus estadisticas personales.
	 */
	private void checkWinner() {
		StringBuffer sb = new StringBuffer();

		/* Si el puntaje del jugador cumple con el objetivo */
		if (this.score >= 500) {

			this.sound.ejecutarSonido("assets/victory.wav");

			sb.append("#-------------------------------------#\n");
			sb.append("|     ~~~~~ WE HAVE A WINNER ~~~~~    |\n");
			sb.append("#-------------------------------------#\n");
			sb.append("|       ¡Felicidades! Has ganado      |\n");
			sb.append("|                                     |\n");
			sb.append("  - Duplicador de premio: x");
			sb.append(this.multiplier);
			sb.append("\n  - Total Apostado: ");
			sb.append(this.bet);
			sb.append(" fichas\n  - Total Ganancia: ");
			sb.append(this.bet * this.multiplier);
			sb.append(" fichas\n|                                     |\n");
			sb.append("#-------------------------------------#\n");

			/* Paga al jugador lo ganado y actualiza sus stats */
			player.addTokens(this.bet * this.multiplier);
			player.updateEarnedTokens(this.bet);

			/* Si el jugador NO cumple el objetivo */
		} else {

			this.sound.ejecutarSonido("assets/lose.wav");

			sb.append("#-------------------------------------#\n");
			sb.append("|        ~~~~~ OH YOU LOSE ~~~~~      |\n");
			sb.append("#-------------------------------------#\n");
			sb.append("|         Ohh... has perdido :(       |\n");
			sb.append("|       Pero no te desanimes, que     |\n");
			sb.append("|  siempre puedes volver a intentarlo |\n");
			sb.append("#-------------------------------------#\n");

			/* Actualiza stats del jugador */
			player.updateLostTokens(this.bet);
		}

		/* Actualiza las stats generales */
		updateStats();

		System.out.println(sb.toString());
	}


	/**
	 * Actualiza las estadisticas del jugador
	 * y reinicia el puntaje y la apuesta a 0
	 */
	private void updateStats() {
		player.newBestScore(this.score);
		player.newMaxBet(this.bet);
		player.updateSpentTokens(this.bet);
		player.updatePlayedMatches();

		this.score = 0;
		this.bet = 0;
	}

	/**
	 * De manera aleatoria imprimirá un evento positivo y sumará los puntos
	 * correspondientes
	 */
	private void printGoodEvent() {
		Random r = new Random();
		int event = r.nextInt(4 - 0) + 1;
		StringBuffer sb = new StringBuffer();

		switch (event) {

		case 1:
			sb.append("• La bola ha dado en una CAMPANA      | Sumas: 10 puntos");
			this.score += 10;
			break;
		case 2:
			sb.append("• La bola ha caido en BONUS           | Sumas: 15 puntos");
			this.score += 15;
			break;
		case 3:
			sb.append("• La bola ha pasado por un CARRUSEL   | Sumas: 20 puntos");
			this.score += 20;
			break;
		case 4:
			sb.append("• La bola ha golpeado un ENEMIGO      | Sumas: 25 puntos");
			this.score += 25;
			break;
		}

		System.out.println(sb.toString());
	}

	/**
	 * Añade la cantidad de bolas indicada por parametro a la lista de bolas con las
	 * que se jugará
	 * 
	 * @param amount Cantidad de bolas a añadir.
	 */
	private void addBallsToArray(int amount) {
		for (int i = 0; i < amount; i++) {
			balls.add(new Ball(i));
		}
	}

	public String printExit() {
		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|          ~~~~~ SALIDA ~~~~~         |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|    Ohh lamentamos verte partir...   |\n");
		sb.append("|   ¡Esperamos lo hayas disfrutado!   |\n");
		sb.append("|                                     |\n");
		sb.append("|  Hasta la próxima, vuelva pronto :D |\n");
		sb.append("#-------------------------------------#\n");

		return sb.toString();
	}


	/**
	 * Imprime el panel del Cajero indicando el precio
	 * de las fichas y pedirá indicar la cantidad
	 * que se desea comprar.
	 */
	private void printCashier() {
		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|          ~~~~~ CAJERO ~~~~~         |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|       A continuación indica la      |\n");
		sb.append("|    cantidad de fichas que deseas    |\n");
		sb.append("|      comprar. Ten en cuenta que     |\n");
		sb.append("|          cada ficha vale 5$         |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje de el jugador no tiene dinero suficiente
	 * para comprar más fichas
	 */
	private static void printNotMoney() {
		StringBuffer sb = new StringBuffer();

		sound.ejecutarSonido("assets/error.wav");

		sb.append("\n#-------------------------------------#\n");
		sb.append("|  ¡Ups! ~~~~~ NOT MONEY ~~~~~ ¡Ups!  |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|   Parece que no tienes suficiente   |\n");
		sb.append("|      dinero para comprar fichas     |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje de que no tienes tokens suficientes para iniciar una nueva
	 * partida. Primero compra más.
	 */
	private static void printNoTokens() {
		StringBuffer sb = new StringBuffer();

		sound.ejecutarSonido("assets/error.wav");
		
		sb.append("\n#-------------------------------------#\n");
		sb.append("|        ~~~~~ SIN FICHAS ~~~~~       |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|   Parece que no tienes suficientes  |\n");
		sb.append("|    fichas para empezar la partida   |\n");
		sb.append("|  Compra fichas y vuelve a intentar  |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje de que algo salio mal, debido
	 * a que se introduce un valor no valido como respuesta
	 */
	private static void printNoValidAnswer() {
		StringBuffer sb = new StringBuffer();

		sound.ejecutarSonido("assets/error.wav");

		sb.append("\n#-------------------------------------#\n");
		sb.append("|       ~~~~ ALGO SALIO MAL ~~~~~     |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      Parece que has introducido     |\n");
		sb.append("|          un valor no valido         |\n");
		sb.append("|        Vuelve a intentarlo...       |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Comprueba si es posible comprar la cantidad de tokens indicada por parametro.
	 * De ser posible realizará la compra, ejecutará un sonido e imprimirá un
	 * mensaje de compra realizada con éxito. De lo contrario imprimirá un error
	 * 
	 * @param amount Cantidad de tokens a comprar
	 * @return True si se ha podido comprar | False en caso contrario
	 */
	private boolean purchaseVerification(int amount) {
		boolean validInsert = Boolean.FALSE;
		if (purchaseValid(amount)) {
			player.makePurchase(amount);
			sound.ejecutarSonido("assets/buying.wav");
			printPurchaseSuccessful();
			validInsert = Boolean.TRUE;
		} else {
			printPurchaseFailed();
		}
		return validInsert;
	}

	/**
	 * Comprueba si la cantidad de tokens solicitados para comprar, son un número
	 * positivo y si el jugador posee dinero suficiente para su compra
	 * 
	 * @param amountTokens La cantidad de tokens a comprar
	 * @return True en caso de ser posible comprar esa cantidad | False en caso
	 *         contrario
	 */
	private boolean purchaseValid(int amountTokens) {
		boolean validated = Boolean.FALSE;
		if (amountTokens >= 0 && (amountTokens * 5) <= player.getMoney())
			validated = Boolean.TRUE;
		return validated;
	}

	/**
	 * Imprime un mensaje de que la bola lanzada se ha perdido y indica cuantas
	 * bolas quedan restantes
	 * 
	 * @param balls Lista de las bolas del jugador
	 */
	private void printLostBall(List<Ball> balls) {
		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|   X X X ~~~ BOLA PERDIDA ~~~ X X X  |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|   La bola se ha ido por el centro   |\n");
		sb.append("|          BOLAS RESTANTES: ");
		sb.append(balls.size() - 1);
		sb.append("         |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje de Game Over junto a la ejecución de un sonido
	 * representante del mismo
	 */
	private void printGameOver() {
		StringBuffer sb = new StringBuffer();

		this.sound.ejecutarSonido("assets/gameOver.wav");

		sb.append("#-------------------------------------#\n");
		sb.append("|         ~~~~~ GAME OVER ~~~~~       |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|       Te has quedado sin bolas.     |\n");
		sb.append("|                                     |\n");
		sb.append("|            PUNTUACIÓN: ");
		sb.append(this.score);
		sb.append("\n#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje de compra realizada con éxito
	 */
	private static void printPurchaseSuccessful() {

		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|     ~~~~~ COMPRA REALIZADA ~~~~~    |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|       Tu compra se ha realizado     |\n");
		sb.append("|  con éxito. Disfruta de tus fichas  |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje de compra fallida
	 */
	private static void printPurchaseFailed() {
		StringBuffer sb = new StringBuffer();

		sound.ejecutarSonido("assets/error.wav");
		
		sb.append("\n#-------------------------------------#\n");
		sb.append("|     ~~~~~ FALLÓ LA COMPRA ~~~~~     |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|   Parece que no tienes suficiente   |\n");
		sb.append("|       dinero para comprar la        |\n");
		sb.append("|    cantidad de fichas indicadas     |\n");
		sb.append("|   o has hecho una compra negativa   |\n");
		sb.append("|        Vuelve a intentarlo...       |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje indicando el modo de juego seleccionado, la cantidad de
	 * bolas con las que se jugará, el como se desarrollará el juego y el objetivo a
	 * cumplir para poder ganar
	 */
	private void printEasyGameMode() {
		StringBuffer sb = new StringBuffer();

		sb.append("#-------------------------------------#\n");
		sb.append("|    ~~~~~ JUEGO - MODO EASY ~~~~~    |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      ¡Excelente! Tienes 5 Bolas     |\n");
		sb.append("|     Se iran lanzando una por una    |\n");
		sb.append("|Cada una irá rebotando por el tablero|\n");
		sb.append("|     Según donde caiga o rebote,     |\n");
		sb.append("|    sumará o restará tu puntuación   |\n");
		sb.append("| Para ganar debe cumplir el Objetivo |\n");
		sb.append("|                                     |\n");
		sb.append("|      OBJETIVO: Hacer 500 puntos     |\n");
		sb.append("|                                     |\n");
		sb.append("|     ¡Buena suerte y diviertete!     |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje indicando el modo de juego seleccionado, la cantidad de
	 * bolas con las que se jugará, el como se desarrollará el juego y el objetivo a
	 * cumplir para poder ganar
	 */
	private void printMediumGameMode() {
		StringBuffer sb = new StringBuffer();

		sb.append("#-------------------------------------#\n");
		sb.append("|   ~~~~~ JUEGO - MODO MEDIUM ~~~~~   |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      ¡Excelente! Tienes 4 Bolas     |\n");
		sb.append("|     Se iran lanzando una por una    |\n");
		sb.append("|Cada una irá rebotando por el tablero|\n");
		sb.append("|     Según donde caiga o rebote,     |\n");
		sb.append("|    sumará o restará tu puntuación   |\n");
		sb.append("| Para ganar debe cumplir el Objetivo |\n");
		sb.append("|                                     |\n");
		sb.append("|      OBJETIVO: Hacer 500 puntos     |\n");
		sb.append("|                                     |\n");
		sb.append("|     ¡Buena suerte y diviertete!     |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje indicando el modo de juego seleccionado, la cantidad de
	 * bolas con las que se jugará, el como se desarrollará el juego y el objetivo a
	 * cumplir para poder ganar
	 */
	private void printHardGameMode() {
		StringBuffer sb = new StringBuffer();

		sb.append("#-------------------------------------#\n");
		sb.append("|    ~~~~~ JUEGO - MODO HARD ~~~~~    |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      ¡Excelente! Tienes 3 Bolas     |\n");
		sb.append("|     Se iran lanzando una por una    |\n");
		sb.append("|Cada una irá rebotando por el tablero|\n");
		sb.append("|     Según donde caiga o rebote,     |\n");
		sb.append("|    sumará o restará tu puntuación   |\n");
		sb.append("| Para ganar debe cumplir el Objetivo |\n");
		sb.append("|                                     |\n");
		sb.append("|      OBJETIVO: Hacer 500 puntos     |\n");
		sb.append("|                                     |\n");
		sb.append("|     ¡Buena suerte y diviertete!     |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje indicando el modo de juego seleccionado, la cantidad de
	 * bolas con las que se jugará, el como se desarrollará el juego y el objetivo a
	 * cumplir para poder ganar
	 */
	private void printLegendaryGameMode() {
		StringBuffer sb = new StringBuffer();

		sb.append("#-------------------------------------#\n");
		sb.append("|  ~~~~~ JUEGO - MODO LEGENDARY ~~~~~ |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      ¡Excelente! Tienes 1 Bola      |\n");
		sb.append("|      Se lanzará una única vez y     |\n");
		sb.append("|  esta irá rebotando por el tablero  |\n");
		sb.append("|     Según donde caiga o rebote,     |\n");
		sb.append("|    sumará o restará tu puntuación   |\n");
		sb.append("| Para ganar debe cumplir el Objetivo |\n");
		sb.append("|                                     |\n");
		sb.append("|      OBJETIVO: Hacer 500 puntos     |\n");
		sb.append("|                                     |\n");
		sb.append("|     ¡Buena suerte y diviertete!     |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime el panel inicial del juego con las posibles acciones a realizar.
	 */
	private void printLauncherMenu() {
		StringBuffer sb = new StringBuffer();

		sb.append("#-------------------------------------#\n");
		sb.append("|    PINBALL NTTData - MENÚ INICIAL   |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|        A continuación, indica       |\n");
		sb.append("|         una opción del menú         |\n");
		sb.append("|                                     |\n");
		sb.append("| [A] - Iniciar juego                 |\n");
		sb.append("| [B] - Cómo jugar                    |\n");
		sb.append("| [C] - Tu información                |\n");
		sb.append("| [D] - Comprar fichas                |\n");
		sb.append("|                                     |\n");
		sb.append("| [X] - Salir                         |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un error por responder mal en algún menú
	 * 
	 */
	private static void printErrorMenus() {
		StringBuffer sb = new StringBuffer();

		sound.ejecutarSonido("assets/error.wav");
		
		sb.append("\n#-------------------------------------#\n");
		sb.append("| ERR0R! - ERR0R! --- ERR0R! - ERR0R! |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      Su eleccion no se encuentra    |\n");
		sb.append("|   dentro de las posibles opciones   |\n");
		sb.append("|        Vuelve a intentarlo...       |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un mensaje breve y resumido de como jugar
	 */
	private void printHowToPlay() {
		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|       ~~~~~ ¿CÓMO JUGAR? ~~~~~      |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("| 1- Apuesta fichas                   |\n");
		sb.append("| 2- Tira las bolas                   |\n");
		sb.append("| 3- Gana puntos                      |\n");
		sb.append("| 4- Alcanza el objetivo y gana       |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un panel para seleccionar el modo de juego
	 */
	public void printTypeGameMenu() {
		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|      ~~~~~ MENÚ DEL JUEGO ~~~~~     |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|        A continuación, indica       |\n");
		sb.append("|     el modo de juego que deseas     |\n");
		sb.append("|                                     |\n");
		sb.append("|     -    MODO   |  BOLAS  |  PREMIO |\n");
		sb.append("| [E] - Easy      |    5    |    x2   |\n");
		sb.append("| [M] - Medium    |    4    |    x3   |\n");
		sb.append("| [H] - Hard      |    3    |    x4   |\n");
		sb.append("| [L] - Legendary |    1    |    x5   |\n");
		sb.append("|                                     |\n");
		sb.append("|     El premio es la cantidad de     |\n");
		sb.append("|           fichas apostadas          |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime un menú de apuesta, donde se solicitará al jugador indicar cuantos
	 * tokens quiere apostar
	 */
	private void printIndicateBet() {
		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|      ~~~~~ MENÚ DE APUESTA ~~~~~    |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|       A continuación, indica la     |\n");
		sb.append("|        cantidad de fichas que       |\n");
		sb.append("|            deseas apostar           |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

	/**
	 * Imprime la información del jugador
	 */
	private void printPlayerInfo() {
		StringBuffer sb = new StringBuffer();

		sb.append("\n#-------------------------------------#\n");
		sb.append("|      ~~~~~ TU INFORMACIÓN ~~~~~     |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|                                     |\n");
		sb.append("  - Tu mejor puntuación es de: ");
		sb.append(player.getBestScore());
		sb.append(" Pts\n");
		sb.append("  - Tu mayor apuesta ha sido de: ");
		sb.append(player.getMaxBet());
		sb.append("\n  - Total dinero restante: ");
		sb.append(player.getMoney());
		sb.append("$\n  - Total dinero invertido: ");
		sb.append(player.getMoneyInvested());
		sb.append("$\n  - Total fichas actuales: ");
		sb.append(player.getTokens());
		sb.append("\n  - Total fichas ganadas: ");
		sb.append(player.getEarnedTokens());
		sb.append("\n  - Total fichas perdidas: ");
		sb.append(player.getLostTokens());
		sb.append("\n  - Total fichas invertidas: ");
		sb.append(player.getSpentTokens());
		sb.append("\n  - Total partidas jugadas: ");
		sb.append(player.getPlayedMatches());
		sb.append("\n|                                     |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}

}
