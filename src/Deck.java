import java.util.ArrayList;
import java.util.Stack;

public class Deck
{
	private Stack<Card> deck;
	private ArrayList<Card> discard;
	
	public Deck()
	{
		deck = new Stack<>();
		discard = new ArrayList<>();
		for (int c = 0; c < 4; c++)
			discard.add(new Card(c, 0));
		for (int c = 0; c < 4; c++)
			for (int i = 0; i < 24; i++)
				discard.add(new Card(c, i%12+1));
		for (int i = 0; i < 8; i++)
			discard.add(new Card(4, i%2+13));
		
		while (discard.size() != 0)
			deck.push(discard.remove((int) (Math.random() * discard.size())));
	}
	
	public Card drawCard()
	{
		if (deck.empty())
			reset();
		return deck.pop();
	}
	public void playCard(Card c)
	{
		discard.add(0, c);
	}
	public boolean playable(Card c)
	{
		return c.getColor() == 4 || c.getColor() == discard.get(0).getColor() || c.getID() == discard.get(0).getID();
	}
	public void reset()
	{
		Card c = discard.remove(0);
		for (Card item : discard)
			if (item.getID() == 13 || item.getID() == 14)
				item.setColor(4);
		while (discard.size() != 0)
			deck.push(discard.remove((int) (Math.random() * discard.size())));
		discard.add(0, c);
	}
	public Card peekNextCard()
	{
		return deck.peek();
	}
	public void changeTop(Card card)
	{
		discard.set(0, card);
	}
	public Card getTopCard()
	{
		return discard.get(0);
	}
	public Card secTopCard()
	{
		return discard.get(1);
	}
	
	public String toString()
	{
		return "Deck : " + deck + "\nDiscard : " + discard;
	}
}
