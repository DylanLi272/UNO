
public class Board
{	
	//make the piles for the deal deck and the discard pile
	private int direction, playing;
	private Deck deck = new Deck();
	private Player[] players = new Player[4];
	public Board()
	{
		//make the 4 players and draw 7 cards each
		for (int i = 0; i < 4; i++)
			players[i] = new Player();
		for (int c = 0; c < 7; c++)
			for (int i = 0; i < 4; i++)
				players[i].add(deck.drawCard());
		
		//puts the starting card
		Card c = deck.drawCard();
		while (c.getID() > 9 || c.getColor() > 3)
		{
			deck.playCard(c);
			c = deck.drawCard();
		}
		deck.playCard(c);
		
		
		//start the game
		playing = (int) (Math.random() * 4); //index of the player currently in play
		direction = 1; //1 - clockwise, -1 - counterclockwise
	}
	public int win()
	{
		for (int i = 0; i < players.length; i++)
			if (players[i].getHand().size() == 0)
				return i;
		return 4;
	}
	public Card getTopCard()
	{
		return deck.getTopCard();
	}
	public void changeTop(Card card)
	{
		deck.changeTop(card);
	}
	public void next()
	{
		playing += 4 + direction;
		playing %= 4;
	}
	public Card peekNextCard()
	{
		return deck.peekNextCard();
	}
	public int getDirection()
	{
		return direction;
	}
	public int getPlaying()
	{
		return playing;
	}
	public Player getPlayer()
	{
		return players[playing];
	}
	public Player getPlayer(int x)
	{
		return players[(playing + x) % 4];
	}
	public Card secTopCard()
	{
		return deck.secTopCard();
	}
	public boolean drawCard()
	{
		Card card = deck.drawCard();
		if (deck.playable(card))
		{
			int id = card.getID();
			if (id == 10)
				direction *= -1;
			else if (id == 11)
			{
				playing += 4 + direction;
				playing %= 4;
			}
			else if (id == 12)
			{
				playing += 4 + direction;
				playing %= 4;
				for (int i = 0; i < 2; i++)
					players[playing].add(deck.drawCard());
			}
			deck.playCard(card);
		}
		else
		{
			players[playing].add(card);
			playing += 4 + direction;
			playing %= 4;
			return false;
		}
		playing += 4 + direction;
		playing %= 4;
		return true;
	}
	public boolean playCard(int index)
	{
		Card card = players[playing].playCard(index);
		if (card.getColor() == deck.getTopCard().getColor() || card.getID() == deck.getTopCard().getID())
		{
			int id = card.getID();
			if (id == 10)
				direction *= -1;
			else if (id == 11)
			{
				playing += 4 + direction;
				playing %= 4;
			}
			else if (id == 12)
			{
				playing += 4 + direction;
				playing %= 4;
				for (int i = 0; i < 2; i++)
					players[playing].add(deck.drawCard());
			}
		}
		else
		{
			players[playing].add(card);
			return false;
		}
			
		deck.playCard(card);
		playing += 4 + direction;
		playing %= 4;
		return true;
	}
	public boolean playWild(int index)
	{
		if (index == -1)
		{
			deck.playCard(deck.drawCard());
			return true;
		}
		if (index == -2)
		{
			deck.playCard(deck.drawCard());
			for (int i = 0; i < 4; i++)
				players[(playing+4+direction) % 4].add(deck.drawCard());
			return true;
		}
		if (players[playing].colorAvailable(deck.getTopCard()))
			if (players[playing].getHand().get(index).getID() == 14)
				return false;
		deck.playCard(players[playing].playCard(index));
		if (deck.getTopCard().getID() == 14)
			for (int i = 0; i < 4; i++)
				players[(playing+4+direction) % 4].add(deck.drawCard());
		return true;
	}
}
