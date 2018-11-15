package play;
import main.GameInput;
import main.Player;
import main.Marfa;
import main.Strategy;
import java.util.List;
import java.util.ArrayList;



public final class Atribuire {
    public static final int SIX = 6;
	private GameInput input;
	private List<String> playersOrder = new ArrayList<>();
	public static List<Integer> assets = new ArrayList<>();
    private int playerIterator = 0;
    public Player[] players;

	Atribuire(final GameInput load) {
		input = load;
		playersOrder = input.getPlayerNames();
		assets = input.getAssetIds();
		players = new Player[playersOrder.size()];
	}
	public GameInput getInput() {
		return this.input;
	}
	// metoda ce apeleaza "putAssets()" de un numar
	// egal cu nr de jucatori
	public void firstRound() {
		int buff = 0;
		while (buff < playersOrder.size()) {
			buff++;
			putAssets();
		}
	}
	// metoda run se plimba prin lista de jucatori si
	// apeleaza metoda "putAssets" cu parametru pt fiecare
	public void run() {
		for (int i = 0; i < playersOrder.size(); i++) {
			putAssets(players[i]);
		}

	}
	// metoda ce aloca o lista de 6 bunuri din pachet si
	// o da ca parametru pt metoda "putMarfaInName()"
	public void putAssets() {
		ArrayList<Marfa> marfa = new ArrayList<Marfa>();
		while (marfa.size() < SIX) {
			Marfa buff = new Marfa(assets.get(0));
			marfa.add(buff);
			assets.remove(0);
		}
		putMarfaInName(marfa);
	}
	// aceasta metoda este supraincarcata si are rolul de
	// a recompleta numarul de carti din mana pt fiecare
	// jucator
	public void putAssets(final Player p) {
		while (p.assetsInHand.size() < SIX) {
			Marfa buff = new Marfa(assets.get(0));
			p.assetsInHand.add(buff);
			assets.remove(0);
		}
	}
	// aceasta metoda primeste o lista de bunuri ca parametru si
	// o atribuie jucatorului care urmeaza din lista de jucatori
	// prin apelarea constructorului, aceasta metoda se apeleaza
	// doar la prima runda
	public void putMarfaInName(final ArrayList<Marfa> marfa) {
				String buff = playersOrder.get(playerIterator);
				if (buff.equals("greedy")) {
				players[playerIterator] = new Player(marfa,
						Strategy.GREEDY, false, "GREEDY", true);
					}
				if (buff.equals("basic")) {
					players[playerIterator] = new Player(marfa,
							Strategy.BASE, false, "BASIC", false);
					}
				if (buff.equals("wizard")) {
					players[playerIterator] = new Player(marfa,
							Strategy.WIZARD, false, "WIZARD", false);
					}
				if (buff.equals("bribed")) {
					players[playerIterator] = new Player(marfa,
							Strategy.BRIBE, true, "BRIBED", false);
					}
				playerIterator++;
	}
}
