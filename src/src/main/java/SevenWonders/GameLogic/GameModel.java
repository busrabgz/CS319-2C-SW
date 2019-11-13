package SevenWonders.GameLogic;

public class GameModel {

    private final int NUMBER_OF_PLAYERS = 7;

    private int currentAge;
    private int currentTurn;
    private DiscardPileModel discardPile;
    private DeckModel deck;
    private PlayerModel[] playerList;

    public GameModel()
    {
        this.currentAge = 1;
        this.currentTurn = 1;
        discardPile = new DiscardPileModel();
        deck = new DeckModel();
        playerList = new PlayerModel[NUMBER_OF_PLAYERS];
    }

    public int getCurrentAge() { return currentAge; }

    public int getCurrentTurn() { return currentTurn; }

    public DiscardPileModel getDiscardPile() { return discardPile; }

    public DeckModel getDeck() { return deck; }

    public PlayerModel[] getPlayerList() { return playerList; }

    public void incrementCurrentTurn(){
        currentTurn++;
    }

    public void incrementCurrentAge() {
        if (!isGameEnded())
        {
            currentAge++;
            currentTurn = 1;
        }
    }

    public boolean isGameEnded()
    {
        return currentAge == 3 && currentTurn == 7;  //Might change according to design choices in GameController
    }
}
