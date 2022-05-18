package nttdata.javat1.game;

/**
 * Pinball
 * 
 * Clase Player
 * 
 * @author Franco Mancinelli
 *
 */
public class Player {
	
	/* ~~ ATRIBUTOS */
	private String nick;
	private int money;
	private int tokens;
	private int bestScore;
	private int maxBet;
	private int spentTokens;
	private int earnedTokens;
	private int investedMoney;
	private int lostTokens;
	private int playedMatechs;
	
	/* ~~ CONSTRUCTOR */
	public Player(String name) {
		super();
		this.nick = name;
		this.money = 100;
		this.bestScore = 0;
		this.maxBet = 0;
		this.tokens = 0;
		this.spentTokens = 0;
		this.investedMoney = 0;
		this.lostTokens = 0;
		this.playedMatechs = 0;
	}

	/* ~~ GETTERS && SETTERS */
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getTokens() {
		return tokens;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}

	public int getMaxBet() {
		return maxBet;
	}

	public void setMaxBet(int maxBet) {
		this.maxBet = maxBet;
	}

	public int getSpentTokens() {
		return spentTokens;
	}

	public void setSpentTokens(int spentTokens) {
		this.spentTokens = spentTokens;
	}

	public int getEarnedTokens() {
		return earnedTokens;
	}

	public void setEarnedTokens(int earnedTokens) {
		this.earnedTokens = earnedTokens;
	}

	public int getMoneyInvested() {
		return investedMoney;
	}

	public void setMoneyInvested(int moneyInvested) {
		this.investedMoney = moneyInvested;
	}

	public int getLostTokens() {
		return lostTokens;
	}

	public void setLostTokens(int lostTokens) {
		this.lostTokens = lostTokens;
	}
	
	public int getPlayedMatches() {
		return playedMatechs;
	}

	public void setPlayedMatches(int matches) {
		this.playedMatechs = matches;
	}

	/* ~~ MÉTODOS */
	
	/**
	 * Suma la cantidad de tokens pasada por parametro
	 * al total de tokens que ya posee el jugador
	 * @param tokens Cantidad de tokens a sumar
	 */
	public void addTokens(int tokens) {
		this.tokens += tokens;
	}
	
	/**
	 * Resta la cantidad de tokens pasada por parametro
	 * al total de tokens que ya posee el jugador
	 * @param tokens Cantidad de tokens a restar
	 */
	public void removeTokens(int tokens) {
		this.tokens -= tokens;
	}
	
	/**
	 * Realiza una compra de fichas. Restará el dinero
	 * del jugador según la cantidad de fichas compradas
	 * @param amount Cantidad de fichas compradas
	 */
	public void makePurchase(int amount) {
		int tokenValue = 5;
		setMoney(getMoney() - (amount * tokenValue));
		addTokens(amount);
	}
	
	/**
	 * Comprueba si el jugador posee más de 0 tokens
	 * @return True si posee tokens | False en caso contrario
	 */
	public boolean haveTokens() {
		boolean have = Boolean.FALSE;
		if(getTokens() > 0)
			have = Boolean.TRUE;
		return have;
	}
	
	/**
	 * Comprueba si el jugador posee suficiente dinero
	 * como para realizar al menos una compra
	 * @return True si posee | False en caso contrario
	 */
	public boolean haveEnoughMoney() {
		boolean have = Boolean.FALSE;
		if(getMoney() >= 5)
			have = Boolean.TRUE;
		return have;
	}
	
	/**
	 * Comprueba que la apuesta realizada sea un valor positivo
	 * y que el jugador realmente posea esa cantidad de tokens
	 * para poder apostarla.
	 * @param bet Las fichas apostadas
	 * @return True en caso de ser una apuesta valida | Falso en caso contrario
	 */
	public boolean betVerification(int bet) {
		boolean verify = Boolean.FALSE;
		if(bet > 0 && getTokens() >= bet) {
			verify = Boolean.TRUE;
		} else {
			printBetNotValidated();
		}
		return verify;
	}
	
	/**
	 * Imprime un mensaje de ERROR por intentar hacer
	 * una apuesta invalida
	 */
	public static void printBetNotValidated() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("\n#-------------------------------------#\n");
		sb.append("|    ~~~~~ ERROR AL APOSTAR ~~~~~     |\n");
		sb.append("#-------------------------------------#\n");
		sb.append("|      Parece que no tienes esa       |\n");
		sb.append("|  cantidad de fichas para apostar    |\n");
		sb.append("|        Vuelve a intentarlo...       |\n");
		sb.append("#-------------------------------------#\n");

		System.out.println(sb.toString());
	}
	
	/**
	 * Comprueba si el puntaje introducido por parametro
	 * es mayor que la mejor puntuacion del jugador. De ser así
	 * actualizará la nueva mejor puntuación.
	 * @param score Último puntaje obtenido por el jugador
	 */
	public void newBestScore(int score) {
		if(score > this.bestScore)
			this.bestScore = score;
	}
	
	/**
	 * Comprueba si la apuesta introducida por parametro
	 * es mayor que la mayor apuesta realizada por el jugador.
	 * De ser así actualizará la nueva mayor apuesta.
	 * @param bet Última apuesta realizada por el jugador
	 */
	public void newMaxBet(int bet) {
		if(bet > this.maxBet)
			this.maxBet = bet;
	}
	
	/**
	 * Actualiza el record historico del total de tokens
	 * gastados hasta el momento. Sumará al total la
	 * cantidad indicada por parametro.
	 * @param tokens Última cantidad de tokens apostados
	 */
	public void updateSpentTokens(int tokens) {
		this.spentTokens += tokens;
	}
	
	/**
	 * Actualiza el record historico del total de tokens
	 * ganados hasta el momento. Sumará al total la cantidad 
	 * indicada por parametro.
	 * @param tokens Última cantidad de tokens ganados
	 */
	public void updateEarnedTokens(int tokens) {
		this.earnedTokens += tokens;
	}

	/**
	 * Actualiza el total de dinero gastado en fichas. 
	 * Sumará al total la cantidad indicada por parametro.
	 * @param money Última cantidad de dinero invertida
	 */
	public void updateInvestedMoney(int money) {
		this.investedMoney += money*5;
	}
	
	/**
	 * Actualiza el record historico del total de tokens
	 * perdidos hasta el momento. Sumará al total la cantidad
	 * indicada por parametro.
	 * @param tokens Última cantidad de tokens perdidos
	 */
	public void updateLostTokens(int tokens) {
		this.lostTokens += tokens;
	}
	
	/**
	 * Actualiza el total de partidas jugadas hasta el 
	 * momento. Sumará 1 partida al total.
	 */
	public void updatePlayedMatches() {
		this.playedMatechs++;
	}


	
}
