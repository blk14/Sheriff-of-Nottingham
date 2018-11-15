package main;
import java.util.ArrayList;

import play.Atribuire;

public final class Player extends GameInput {
    public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public ArrayList<Marfa> assetsInHand;
	private Strategy strategy;
	public ArrayList<Marfa> assetsInBag;
	public ArrayList<Marfa> assetsOnStand;
	private ArrayList<Marfa> buffer;
	private int coins;
	private boolean sheriff = false;
	private int declaration;
	private int currentRoundNumber = 0;
	private boolean mitaDau;
	private int mitaValue;
	private String name;
	private boolean mitaIau;
	private boolean amMita;
	private int mitaRound = -1;
	private int uncheckedRound = -1;
	public int[] bonusFrec = new int[FOUR];

	// in constructor aloc si initializez elementele cele mai folosite
	// precum assetsInBag (bunurile din sac), assetsInHand (bunurile
	// din mana), coins, strategy, mitaIau (true pt greedy, false pt
	// restul), vecotrul de frecventa pt bonusul de king si queen
	public Player(final ArrayList<Marfa> buff, final Strategy strategy,
				  final boolean mita, final String name,
				  final boolean mitaIau) {
		assetsInHand = new ArrayList<Marfa>();
		assetsInHand = buff;
		coins = TWO * FIVE * FIVE;
		this.mitaDau = mita;
		mitaValue = 0;
		this.name = name;
		this.strategy = strategy;
		assetsInBag = new ArrayList<Marfa>();
		assetsOnStand = new ArrayList<Marfa>();
		buffer = new ArrayList<Marfa>();
		this.mitaIau = mitaIau;
		for (int i = 0; i < FOUR; i++) {
			bonusFrec[i] = 0;
		}
	}
	public int getCoins() {
		return this.coins;
	}
	public void setCoins(final int val) {
		this.coins += val;
	}
	public boolean getMitaDau() {
		return this.mitaDau;
	}
	public boolean getMitaIau() {
		return this.mitaIau;
	}
	public boolean getAmMita() {
		return this.amMita;
	}
	public int getDeclaration() {
		return this.declaration;
	}
	public void setSheriff(final boolean val) {
		this.sheriff = val;
	}
	public Strategy getStrategy() {
		return this.strategy;
	}
	public int getMitaValue() {
		return this.mitaValue;
	}
	public String getName() {
		return this.name;
	}
	// setter ce stabileste runda in care jucatorul wizard
	// poate da mit
	public void setMitaRound(final int curRound, final int nr) {
		this.mitaRound = curRound + nr;
	}
	// setter ce stabileste runda in care jucatorul wizard
	// nu este verificat
	public void setUncheckedRound(final int round) {
		this.uncheckedRound = round;
	}

// metoda setBag pune bunurile in saci pt jucator in functie de strategia
// pe care o are
	public void setBag(final int wizardRoundNumber, final int nrOfPlayers) {
//		case BASE: initial stabilesc frecventa de aparitie pt fiecare bun
		if (strategy == Strategy.BASE) {
			int[] vectorFrecventa = new int[THREE * FOUR + 1];
			for (int i = 0; i < THREE * FOUR; i++) {
				vectorFrecventa[i] = 0;
			}
			for (Marfa it:assetsInHand) {
				vectorFrecventa[it.getId()]++;
			}
// apoi aleg bunul legal cu frecventa maxima si ii retin pozitia
// verificand cazul special dintre bread si chicken
			int maxim = -1;
			int poz = 0;
			for (int i = 0; i < FOUR; i++) {
				if (vectorFrecventa[i] >= maxim) {
					if (i != THREE) {
					maxim = vectorFrecventa[i];
					poz = i;
					}
					if (i == THREE && vectorFrecventa[2]
							< vectorFrecventa[THREE]) {
						maxim = vectorFrecventa[i];
						poz = i;
					}
				}
			}
			this.declaration = poz;
// setez elementul declarat
// verific daca nu cumva nu am avut bunuri legale si in cazul asta aleg
// un bun ilegal cu profit maxim si decla apple
			if (maxim == -1) {
				for (int i = THREE * FOUR + 1; i > THREE * THREE; i--) {
					if (vectorFrecventa[i] > 0) {
						maxim = 1;
						poz = i;
					}
				}
				this.declaration = 0;
			}
// adaug bunurile ce au frecventa maxima, iar in caz ca sunt 6 cartie de
// acelasi tip in mana se aduaga maxim "times" = 5 carti;
			int times = 0;
			for (Marfa it:assetsInHand) {
					if (it.getId() == poz) {
						assetsInBag.add(it);
						times++;
						buffer.add(it);
						maxim--;
						if (maxim == 0 || times == FOUR + 1) {
							break;
						}
					}
				}
			for (Marfa it:buffer) {
				assetsInHand.remove(it);
			}
			buffer.clear();
		}
//		case GREEDY: apelez metoda greedyMethod
		if (strategy == Strategy.GREEDY) {
			greedyMethod();
		}
//		case BRIBE: initial verific daca jucatorul are banii
		// necesari pt a pune bunuri ilegale si setez si
		// target-ul pe bunuri ilegale, acesta fiind Silk
		// avand cel mai mare profit. Totodata in functie
		// de cati bani are setez si "contor" ce reprezinta
		// nr de bunuri ilegale pe care le poate pune
		if (strategy == Strategy.BRIBE) {
			this.declaration = 0;
			int contor = FOUR + 1;
			int target = TWO * FIVE;
			if (coins < TWO * FIVE && coins > FOUR) {
				contor = 2;
				this.amMita = true;
			}
			if (coins > THREE * THREE) {
				this.amMita = true;
			}
			// daca nu are bani pt niciun bun ilegal,
			// aplic strategia de baza
			if (coins < FIVE) {
				this.amMita = false;
				contor = 0;
				baseMethod();
			}
			int ok = 0;
			// pun bunurile ilegale in sac in functie
			// de profit
				while (target < THREE * FOUR + 1) {
					if (contor == 0) {
						break;
					}
					for (Marfa it : assetsInHand) {
						if (it.getId() == target) {
							ok = 1;
							assetsInBag.add(it);
							buffer.add(it);
							contor--;
							if (contor == 0) {
								break;
							}
						}
					}
					for (Marfa it:buffer) {
						assetsInHand.remove(it);
					}
					buffer.clear();

					target++;
				}
				// daca nu are bun ilegal aplica base
				if (ok == 0) {
					this.amMita = false;
					baseMethod();
				}
				// setez valoare mitei
				if (assetsInBag.size() < THREE) {
					mitaValue = FIVE;
				}
				if (assetsInBag.size() > TWO) {
					mitaValue = TWO * FIVE;
				}
		}
//		case Wizard: este o strategie ce se bazeaza pe sacrficarea
// primelor runde pt a cunoaste adversarii. La prima intalnire cu
// un sheriff nou adaug un bun ilegal si mita pt a il testa. In
// caz ca accepta o sa fie retinuta runda in care urmeaza sa se
// mai intalneasca cei 2 pt a profita la maxim de ea. Duoa ce e
// adaugat un bun ilegal se aplica strategia de baza
		if (strategy == Strategy.WIZARD) {
			int ok2 = 0;
			int ok = 0;
			// verific daca e prima data cand intalnesc sheriff-ul
			if (wizardRoundNumber < nrOfPlayers) {
				// adaug un bun ilegal si setez mita
				this.amMita = false;
				this.mitaDau = false;
				for (int i = THREE * THREE + 1;
						i < THREE * FOUR + 1; i++) {
					for (Marfa it : assetsInHand) {
						if (it.getId() == i) {
							assetsInBag.add(it);
							this.amMita = true;
							this.mitaDau = true;
							this.mitaValue = FIVE;
							ok = 1;
							break;
						}
					}
					if (ok == 1) {
						break;
					}
				}
			} else {
				// verific daca in runda curenta pot da mita sau
				// nu sunt verificat
				if (wizardRoundNumber == this.mitaRound
						|| wizardRoundNumber == this.uncheckedRound) {
					// adaug bunuri ilegale
					for (int i = THREE * THREE;
							i < THREE * FOUR + 1; i++) {
						for (Marfa it : assetsInHand) {
							if (it.getId() == i) {
								if (assetsInBag.size() < FIVE) {
									assetsInBag.add(it);
								}
									this.amMita = true;
									this.mitaDau = true;
							}
						}
					}
					// daca nu am avut destule ilegale
					// completez cu legale
					for (int i = THREE; i >= 0; i--) {
						for (Marfa it : assetsInHand) {
							if (assetsInBag.size() == FIVE) {
								break;
							} else {
								assetsInBag.add(it);
							}
						}
						if (assetsInBag.size() == FIVE) {
							break;
						}
					}
					// setez valoarea mitei
					if (assetsInBag.size() > 2) {
						this.mitaValue = TWO * FIVE;
					} else {
						this.mitaValue = FIVE;
					}
				} else {
					// daca nu este runda in care dau mita
					this.amMita = false;
					this.mitaDau = false;
					if (wizardRoundNumber < nrOfPlayers - 1) {
						this.amMita = true;
						this.mitaDau = true;
					}
					// aplic strategia de baza prin metoda
					// baseMethod cu parametru
					baseMethod(assetsInBag.size());
					ok2 = 1;
				}
			}
			// daca runda curenta este printre primele
			// aplez metoda de baza pt a completa sacul
			// ce are deja un bun ilegal
			if (ok2 == 0) {
				baseMethod(assetsInBag.size());
			}
		}
	}
	// aceasta metoda aplica initial strategia base, insa daca
	// runda curenta este para si sacul nu este plin adauga un
	// bun ilegal
	public void greedyMethod() {
		this.currentRoundNumber++;
		int aux = 0;
		int[] vectorFrecventa = new int[THREE * FOUR + 1];

			for (int i = 0; i < THREE * FOUR + 1; i++) {
				vectorFrecventa[i] = 0;
			}
			for (Marfa it:assetsInHand) {
				vectorFrecventa[it.getId()]++;
			}
			int times = 0;
			int maxim = 0;
			int pozition = 0;
			for (int i = 0; i < FOUR; i++) {
				if (vectorFrecventa[i] >= maxim) {
					maxim = vectorFrecventa[i];
					pozition = i;
					this.declaration = pozition;
				}
			}
			if (maxim == 0 && times == 0) {
				for (int i = THREE * FOUR; i > THREE * THREE; i--) {
					if (vectorFrecventa[i] > 0) {
						maxim = 1;
						pozition = i;
					}
				}
				this.declaration = 0;
			}

			for (Marfa it : assetsInHand) {
					if (it.getId() == pozition) {
						assetsInBag.add(it);
						times++;
						buffer.add(it);
						maxim--;
						if (maxim == 0 || times == FOUR + 1) {
							break;
						}
					}
				}

			for (Marfa it:buffer) {
				assetsInHand.remove(it);
			}
			buffer.clear();

			if (currentRoundNumber % 2 == 0 && assetsInBag.size() < FOUR + 1) {
				for (int i = THREE * THREE; i < THREE * FOUR + 1; i++) {
					if (vectorFrecventa[i] > 0) {
						aux = i;
						break;
					}
				}
				if (aux > 0) {
					for (Marfa it : assetsInHand) {
						if (it.getId() == aux) {
							assetsInBag.add(it);
							times++;
							buffer.add(it);
							break;
						}
					}
					for (Marfa it:buffer) {
						assetsInHand.remove(it);
					}
					buffer.clear();
				}
			}
	}
	// metoda supraincarcata speciala pt strategia wizard
	public void baseMethod(final int size) {
		int[] vectorFrecventa = new int[THREE * FOUR + 1];
		for (int i = 0; i < THREE * FOUR; i++) {
			vectorFrecventa[i] = 0;
		}
		for (Marfa it:assetsInHand) {
			vectorFrecventa[it.getId()]++;
		}
		int maxim = -1;
		int poz = 0;
		for (int i = 0; i < FOUR; i++) {
			if (vectorFrecventa[i] >= maxim) {
				maxim = vectorFrecventa[i];
				poz = i;
			}
		}
		this.declaration = poz;
		if (maxim == -1) {
			for (int i = THREE * FOUR + 1; i > THREE * THREE; i--) {
				if (vectorFrecventa[i] > 0) {
					maxim = 1;
					poz = i;
				}
			}
			this.declaration = 0;
		}
		int times = size;
		for (Marfa it:assetsInHand) {
				if (it.getId() == poz) {
					assetsInBag.add(it);
					times++;
//					assetsInHand.remove(it);
					buffer.add(it);
					maxim--;
					if (maxim == 0 || times == FOUR + 1) {
						break;
					}
				}
			}
		// daca am mai multe bunuri legale nu merita riscul
		// sa dau penalty, cand adversarul ar da mai  mult
		// asa ca scot bunul ilegal din sac
		if (assetsInBag.size() > 1) {
			int i = 0;
			for (Marfa it:assetsInBag) {
				if (it.getId() > TWO * FOUR) {
					this.mitaDau = false;
					this.amMita = false;
					assetsInBag.remove(i);
					break;
				}
				i++;
			}
		}
		for (Marfa it:buffer) {
			assetsInHand.remove(it);
		}
		buffer.clear();
	}
	public void baseMethod() {
		int[] vectorFrecventa = new int[THREE * FOUR + 1];
		for (int i = 0; i < THREE * FOUR + 1; i++) {
			vectorFrecventa[i] = 0;
		}
		for (Marfa it:assetsInHand) {
			vectorFrecventa[it.getId()]++;
		}
		int maxim = 0;
		int poz = 0;
		for (int i = 0; i < FOUR; i++) {
			if (vectorFrecventa[i] >= maxim) {
				maxim = vectorFrecventa[i];
				poz = i;
			}
		}
		this.declaration = poz;
		if (maxim == 0) {
			for (int i = THREE * FOUR + 1; i > THREE * THREE; i--) {
				if (vectorFrecventa[i] > 0) {
					maxim = 1;
					poz = i;
				}
			}
			this.declaration = 0;
		}
		int times = 0;
		for (Marfa it:assetsInHand) {
				if (it.getId() == poz) {
					assetsInBag.add(it);
					times++;
//					assetsInHand.remove(it);
					buffer.add(it);
					maxim--;
					if (maxim == 0 || times == FIVE) {
						break;
					}
				}
			}
		for (Marfa it : buffer) {
			assetsInHand.remove(it);
		}
		buffer.clear();
	}

	// aceasta metoda inspecteaza sacul jucatorului
	// dat ca parametru comparand elementele din
	// sac cu bunul declarat si face tranzactia pt
	// penalty daca e nevoie
	public void checkBag(final Player player) {
		int ok = 1;
		int penalty = 0;
		for (Marfa it:player.assetsInBag) {
			penalty = it.getPenalty();
			if (it.getId() != player.declaration) {
				ok = 0;
				player.coins -= it.getPenalty();
				this.coins += it.getPenalty();
				player.assetsInHand.remove(it);
				buffer.add(it);
	                     //pun la sf pachetului
				Atribuire.assets.add(it.getId());
			}
		}
		for (Marfa it:buffer) {
			player.assetsInBag.remove(it);
		}
		buffer.clear();
		// actualizez penalty pentru serif daca a verificat gresit
		if (ok == 1) {
			player.coins += (player.assetsInBag.size() * penalty);
			this.coins -= (player.assetsInBag.size() * penalty);

		}
	}

}