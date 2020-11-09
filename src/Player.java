import java.util.ArrayList;
import java.util.Collections;

public class Player
{
	private ArrayList<Card> hand;
	
	public Player()
	{
		hand = new ArrayList<>();
	}
	
	public void add(Card c)
	{
		hand.add(c);
		Collections.sort(hand);
	}
	public Card playCard(int index)
	{
		return hand.remove(index);
	}
	public boolean moveAvailable(Card top)
	{
		for (Card item : hand)
			if (item.getColor() == 4 || item.getColor() == top.getColor() || item.getID() == top.getID())
				return true;
		return false;
	}
	public boolean colorAvailable(Card top)
	{
		for (Card item : hand)
			if (item.getColor() == top.getColor())
				return true;
		return false;
	}
	public ArrayList<Card> getHand()
	{
		return hand;
	}
	
	public String toString()
	{
		return hand.toString();
	}
}
