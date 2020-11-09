
public class Card implements Comparable<Card>
{
	public static final String[] COLOR = {"Blue", "Green", "Red", "Yellow", "Black"};
	public static final String[] ID = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Reverse", "Skip", "Plus 2", "Wild", "Plus 4", "Back"};
	private int color, id;
	
	public Card(int c, int i)
	{
		color = c; //0-blue, 1-green, 2-red, 3-yellow, 4-black
		id = i; //0-9:0-9, 10-reverse, 11-skip, 12-plus2, 13-wild, 14-plus4, 15-back
	}
	
	public boolean isNum()
	{
		return id < 10;
	}
	public void setColor(int c)
	{
		color = c;
	}
	public int getColor()
	{
		return color;
	}
	public int getID()
	{
		return id;
	}
	
	public int compareTo(Card c)
	{
		if (color < c.color)
			return -1;
		if (color > c.color)
			return 1;
		if (id < c.id)
			return -1;
		if (id > c.id)
			return 1;
		return 0;
	}
	public boolean equals(Object obj)
	{
		Card c = (Card) obj;
		if (color != c.color)
			return false;
		if (id != c.id)
			return false;
		return true;
	}
	public String toString()
	{
		return COLOR[color] + " " + ID[id];
	}
}
