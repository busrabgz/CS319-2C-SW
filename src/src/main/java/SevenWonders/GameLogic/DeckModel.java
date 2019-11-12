package SevenWonders.GameLogic;



public class DeckModel {

    private Card[][] cards;
    final int NUMBER_OF_AGES = 3;
    final int CARDS_PER_AGE = 49;

    public DeckModel()
    {
        cards = new Card[NUMBER_OF_AGES][CARDS_PER_AGE];
    }

    private void setCards(Card[][] cards)
    {
        this.cards = cards;
    }

    public Card[][] getCards()
    {
        return cards;
    }
}
