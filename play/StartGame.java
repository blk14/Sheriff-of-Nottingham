package play;
import main.Marfa;
import main.GameInput;
import main.Strategy;
import main.Player;
import java.util.ArrayList;


public final class StartGame {

    public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	Atribuire round;
	private int numberOfRounds;
	private int numberOfPlayers;
	private int index;
	private ArrayList<Integer> hot = new ArrayList<Integer>();
	public ArrayList<Marfa> buffy = new ArrayList<Marfa>();

	public StartGame(GameInput load) {
		round = new Atribuire(load);
		numberOfRounds = round.getInput().getNumberOfRounds();
		numberOfPlayers = round.getInput().getNumberOfPlayers();
	}
	// in clasa GameInput am facut 2 gettere pt determinarea nr
	// de runde si nr de playeri
	public  void start() {
		for (int currentRound = 0; currentRound < numberOfRounds; currentRound++) {
			if (currentRound == 0) {
				round.firstRound();
			} else {
				round.run();
			}
			// parcurg numarul de runde, iar in caz ca este prima runda se va parcurge
	 // o serie de metode care pe langa atribuirea cartilor o sa si aloce playerii
			if (currentRound > numberOfPlayers - 1) {
				index = currentRound - numberOfPlayers;
			} else {
				index = currentRound;
			}
			for (int i = 0; i < numberOfPlayers; i++) {
				if (i != index) {
					round.players[i].setBag(currentRound, numberOfPlayers);
				}
			}
			// stabilesc jucatorul ce urmeaza sa fie sheriff (variabila index)
			// si pun bunuri in sac pt ceilalti

			round.players[index].setSheriff(true);
// in functie de strategia sheriff-ului inspectia sacilor se face:
//					case BASE: ii verifica pe toti
			if (round.players[index].getStrategy() == Strategy.BASE) {
				for (int i = 0; i < numberOfPlayers; i++) {
					if (round.players[i].getStrategy() != Strategy.BASE) {
						round.players[index].checkBag(round.players[i]);
						for (Marfa it : round.players[i].assetsInBag) {
							round.players[i].assetsOnStand.add(it);
							// adauga bunurile din sac pe taraba
								}
							}
						}
				}
//					case GREEDY: daca are mita se face "tranzactia", fara
			// a verifica sacul. In caz ca mita este acceptata se stabileste
			// urmatoarea runda in care se vor intalni cele 2 strategii ( caz
			// special pt jucatorul cu strategia wizard). Daca nu are mita
			// sacul este verificat
			if (round.players[index].getStrategy() == Strategy.GREEDY) {
				for (int i = 0; i < numberOfPlayers; i++) {
					if (round.players[i].getStrategy() != Strategy.GREEDY) {
						if ((round.players[i].getMitaDau())
							&& (round.players[index].getMitaIau())
							&& (round.players[i].getAmMita())) {
							round.players[i].
							setCoins(-round.players[i].getMitaValue());
							round.players[index].
							setCoins(round.players[i].getMitaValue());
							round.players[i].
							setMitaRound(currentRound, numberOfPlayers);
							for (Marfa it
								: round.players[i].assetsInBag) {
								round.players[i].
								assetsOnStand.add(it);
						// adauga bunurile din sac pe taraba
								}
						}	else {
							round.players[index].
							checkBag(round.players[i]);
							for (Marfa it
								: round.players[i].assetsInBag) {
								round.players[i].
								assetsOnStand.add(it);
				// adauga bunurile din sac pe taraba
								}
							}
					}
				}
			}
//					case BRIBE: se stabilesc jucatorii din stanga si din
			// dreaptata, se verifica amandoi (pt joc cu 2 jucatori left
			// si right o sa se refere la acelasi jucator). Pt un joc in 4
			// un jucator o sa scape necontrolat, iar acesta stabileste
			// urmatoarea runda in care se vor intalni (caz special pt
			// wizard intr-un joc de 4)
				if (round.players[index].getStrategy() == Strategy.BRIBE) {
					int right = 0;
					int left = 0;
					if (index == 0) {
						right = index + 1;
						left = numberOfPlayers - 1;
						}
					if (index == numberOfPlayers - 1) {
						right = 0;
						left = index - 1;
						}
					if (index > 0 && index < numberOfPlayers - 1) {
						right = index + 1;
						left = index - 1;
						}

					if (left == right) {
						round.players[index].checkBag(round.players[right]);

					} else {
						round.players[index].checkBag(round.players[left]);
						round.players[index].checkBag(round.players[right]);
						}
					for (int i = 0; i < numberOfPlayers; i++) {
						if (i != left && i != right && i != index) {
							round.players[i].
							setUncheckedRound(
							currentRound + numberOfPlayers);
						}
					}
					for (int i = 0; i < numberOfPlayers; i++) {
						for (Marfa it:round.players[i].assetsInBag) {
							round.players[i].assetsOnStand.add(it);
							}
						}
				}
//					case WIZARD: wizard ii verifica pe jucatori
// daca este prima data cand se intalnesc pt a "vedea" cum joaca
// sau daca au mai mult de 2 bunuri in mana. Daca dupa prima
// "intalnire" are deficit la coins, atunci adauga jucatorul
// respectiv pe o lista de hoti pt a ii verifica si la rundele
// urmatoare
				if (round.players[index].getStrategy()
				== Strategy.WIZARD) {
					for (int i = 0; i < numberOfPlayers; i++) {
						if (round.players[i].getStrategy()
								!= Strategy.WIZARD) {
							if (currentRound < numberOfPlayers
							    || round.players[i].
							    assetsInBag.size() > 2) {
								int coinsBuff
								= round.players[index].getCoins();
								round.players[index].
								checkBag(round.players[i]);
								if (coinsBuff
										< round.
										players[index].
										getCoins()) {
									 hot.add(i);
								}
							} else {
								for (int j = 0; j < hot.size();
										j++) {
									if (i == hot.get(j)) {
									round.players[index].
										checkBag(round
										 .players[i]);
									}
								}
							}
								for (Marfa it : round.players[i].
										assetsInBag) {
									round.players[i].
									assetsOnStand.add(it);
//				 adauga bunurile din sac pe taraba
									}
							}
						}
				}
				// golesc sacii
				for (int i = 0; i < numberOfPlayers; i++) {
					round.players[i].assetsInBag.clear();
				}
				round.players[index].setSheriff(false);
		}
	}
	// prin aceasta metoda adaug pe taraba si bunurile bonus
	// aduse de bunurile ilegale
	public  void ilegalBonus() {
		for (int i = 0; i < numberOfPlayers; i++) {
			for (Marfa it : round.players[i].assetsOnStand) {
				if (!it.getType()) {
					if (it.getId() == TWO * FIVE) {
						Marfa buff = new Marfa(1);
						buffy.add(buff);
						buffy.add(buff);
						buffy.add(buff);

					}
					if (it.getId() == TWO * FIVE + 1) {
						Marfa buff = new Marfa(THREE);
						buffy.add(buff);
						buffy.add(buff);
					}
					if (it.getId() == THREE * FOUR) {
						Marfa buff = new Marfa(TWO);
						buffy.add(buff);
						buffy.add(buff);
					}
				}
			}
			for (Marfa it : buffy) {
				round.players[i].assetsOnStand.add(it);
			}
			buffy.clear();
		}
	}
	// metoda pt a adauga bonusurile de king si queen
	public void bonus() {
		int appleKing = 0;
		int appleQueen = 0;
		int cheeseKing = 0;
		int cheeseQueen = 0;
		int breadKing = 0;
		int breadQueen = 0;
		int chickenKing = 0;
		int chickenQueen = 0;

		// fiecare jucator are un vector de frecventa a bunurilor
		// legale
		for (int i = 0; i < numberOfPlayers; i++) {
			for (Marfa it : round.players[i].assetsOnStand) {
				if (it.getId() < FOUR) {
					round.players[i].bonusFrec[it.getId()]++;
				}
			}
		}
		// stabilesc valoare pe care artrebui sa o aiba "king-ul"
		// pt fiecare bun
		for (int i = 0; i < numberOfPlayers; i++) {
			if (round.players[i].bonusFrec[0] >= appleKing) {
				appleKing = round.players[i].bonusFrec[0];
			}
			if (round.players[i].bonusFrec[1] >= cheeseKing) {
				cheeseKing = round.players[i].bonusFrec[1];
			}
			if (round.players[i].bonusFrec[TWO] >= breadKing) {
				breadKing = round.players[i].bonusFrec[TWO];
			}
			if (round.players[i].bonusFrec[THREE] >= chickenKing) {
				chickenKing = round.players[i].bonusFrec[THREE];
			}
		}
		// stabilesc valoare pe care artrebui sa o aiba "queen-ul"
		// pt fiecare bun
		for (int i = 0; i < numberOfPlayers; i++) {
			if (round.players[i].bonusFrec[0] >= appleQueen
					&& round.players[i].bonusFrec[0] < appleKing) {
				appleQueen = round.players[i].bonusFrec[0];
			}
			if (round.players[i].bonusFrec[1] >= cheeseQueen
					&& round.players[i].bonusFrec[1] < cheeseKing) {
				cheeseQueen = round.players[i].bonusFrec[1];
			}
			if (round.players[i].bonusFrec[TWO] >= breadQueen
					&& round.players[i].bonusFrec[TWO] < breadKing) {
				breadQueen = round.players[i].bonusFrec[TWO];
			}
			if (round.players[i].bonusFrec[THREE] >= chickenQueen
					&& round.players[i].bonusFrec[THREE] < chickenKing) {
				chickenQueen = round.players[i].bonusFrec[THREE];
			}
		}
		// adaug banii castigati de pe urma bonusurilor
		for (int i = 0; i < numberOfPlayers; i++) {
			if (round.players[i].bonusFrec[0] == appleKing) {
				round.players[i].setCoins(FOUR * FIVE);
			}
			if (round.players[i].bonusFrec[0] == appleQueen
					&& appleQueen < appleKing) {
				round.players[i].setCoins(TWO * FIVE);
			}
			if (round.players[i].bonusFrec[1] == cheeseKing) {
				round.players[i].setCoins(THREE * FIVE);
			}
			if (round.players[i].bonusFrec[1] == cheeseQueen
					&& cheeseQueen < cheeseKing) {
				round.players[i].setCoins(TWO * FIVE);
			}
			if (round.players[i].bonusFrec[TWO] == breadKing) {
				round.players[i].setCoins(THREE * FIVE);
			}
			if (round.players[i].bonusFrec[TWO] == breadQueen
					&& breadQueen < breadKing) {
				round.players[i].setCoins(TWO * FIVE);
			}
			if (round.players[i].bonusFrec[THREE] == chickenKing) {
				round.players[i].setCoins(TWO * FIVE);
			}
			if (round.players[i].bonusFrec[THREE] == chickenQueen
					&& chickenQueen < chickenKing) {
				round.players[i].setCoins(FIVE);
			}
		}
	}
	// metoda pt afisarea rezultatelor
	public void results() {
		// adaug profitul pt fiecare bun de pe taraba pt fiecare jucator
		for (int i = 0; i < numberOfPlayers; i++) {
			for (Marfa it : round.players[i].assetsOnStand) {
				round.players[i].setCoins(it.getProfit());
			}
		}
		// sortez jucatorii in functie de coins
		Player aux;
		for (int i = 0; i < numberOfPlayers - 1; i++) {
			for (int j = i + 1; j < numberOfPlayers; j++) {
				if (round.players[j].getCoins() > round.players[i].getCoins()) {
					aux = round.players[j];
					round.players[j] = round.players[i];
					round.players[i] = aux;
				}
			}
		}
		// afisez
		for (int i = 0; i < numberOfPlayers; i++) {
			System.out.println(round.players[i].getName()
					+ ": " + round.players[i].getCoins());
		}
	}
}
