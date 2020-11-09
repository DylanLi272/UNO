import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UNORunner extends JFrame implements MouseListener
{
	private static final long serialVersionUID = -1268319310886136966L;
	
	private final BufferedImage[][] cards = new BufferedImage[5][15];
	private final BufferedImage back = ImageIO.read(new File("Black15.png"));
	private final BufferedImage uno = ImageIO.read(new File("uno.png"));
	private final BufferedImage[] clockwise = new BufferedImage[2];
	private final BufferedImage[] counterClockwise = new BufferedImage[2];
	private JPanel contentPane;
	private Board board;
	private boolean win, isWild;
	private int winPlayer;
	private ArrayList<String> moves;
	
	public static void main(String[] args) throws IOException
	{
		@SuppressWarnings("unused")
		UNORunner game = new UNORunner();
	}
	public UNORunner() throws IOException
	{
		super("UNO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1500, 1000);
		
		for (int r = 0; r < 4; r++)
			for (int c = 0; c < 13; c++)
				cards[r][c] = ImageIO.read(new File(r+"-"+c+".png"));
		for (int r = 0; r < 4; r++)
			for (int c = 13; c < 15; c++)
				cards[r][c] = ImageIO.read(new File("black"+c+".png"));
		for (int i = 13; i < 15; i++)
			cards[4][i] = ImageIO.read(new File("black"+i+".png"));
		for (int i = 0; i < 2; i++)
		{
			clockwise[i] = ImageIO.read(new File("clockwise"+i+".png"));
			counterClockwise[i] = ImageIO.read(new File("counterClockwise"+i+".png"));
		}
		
		moves = new ArrayList<>();
		board = new Board();
		
		contentPane = new JPanel() {
			private static final long serialVersionUID = -8573999121279063348L;
			
			public void paintComponent(Graphics gr) {
				super.paintComponent(gr);
				
				BufferedImage scr = new BufferedImage(1500, 1000, BufferedImage.TYPE_INT_ARGB);
				Graphics g = scr.createGraphics();
				g.setColor(Color.GRAY);
				g.fillRect(0, 0, 1500, 1000);
				
				if (win)
				{
					g.setColor(Color.WHITE);
					g.setFont(new Font("Arial", 0, 100));
					g.drawString("Player "+winPlayer+" Wins!!!", 435, 530);
					g.dispose();
					int width = contentPane.getWidth();
					int height = contentPane.getHeight();
					if (width >= height * 3 / 2)
					{
						gr.setColor(Color.BLACK);
						gr.fillRect(0, 0, width, height);
						gr.drawImage(scr, (int)(width/2 - height*3/4.0), 0, (int)(height*3/2.0), height, null);
					}
					else
					{
						gr.setColor(Color.BLACK);
						gr.fillRect(0, 0, width, height);
						gr.drawImage(scr, 0, (int)(height/2.0 - width*2/6.0), width, (int)(width*2/3.0), null);
					}
					return;
				}
				Card topCard = board.getTopCard();
				if (isWild)
				{
					g.setColor(Color.RED);
					g.fillRect(755, 322, 60, 60);
					g.setColor(Color.GREEN);
					g.fillRect(815, 322, 60, 60);
					g.setColor(Color.BLUE);
					g.fillRect(755, 382, 60, 60);
					g.setColor(Color.YELLOW);
					g.fillRect(815, 382, 60, 60);
				}
				else
					g.drawImage(cards[topCard.getColor()][topCard.getID()], 750, 292, 130, 180, null);
				if (board.getTopCard().getID() > 12)
				{
					if (board.getTopCard().getColor() < 4)
					{
						switch (board.getTopCard().getColor())
						{
						case 0: g.setColor(Color.BLUE); break;
						case 1: g.setColor(Color.GREEN); break;
						case 2: g.setColor(Color.RED); break;
						case 3: g.setColor(Color.YELLOW); break;
						}
						g.fillRect(750, 482, 130, 15);
					}
				}
				if (board.getDirection() > 0)
				{
					g.drawImage(clockwise[0], 640, 320, 140, 140, null);
					g.drawImage(clockwise[1], 850, 320, 140, 140, null);
				}
				else
				{

					g.drawImage(counterClockwise[0], 640, 320, 140, 140, null);
					g.drawImage(counterClockwise[1], 850, 320, 140, 140, null);
				}
				//deck
				g.drawImage(back, 530, 292, 130, 180, null);
				//top
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", 0, 20));
				g.drawString("Player "+((board.getPlaying()+2)%4+1), 715, 240);
				int number = board.getPlayer(2).getHand().size();
				for (int i = 0; i < number; i++)
					g.drawImage(back, 685 + (i-number/2)*10, 35, 130, 180, null);
				if (board.getPlayer(2).getHand().size() == 1)
					g.drawImage(uno, 825, 100, 100, 70, null);
				//left
				g.drawString("Player "+((board.getPlaying()+1)%4+1), 360, 280);
				for (int i = board.getPlayer(1).getHand().size()-1; i >= 0; i--)
					g.drawImage(back, 330 - i*10, 292, 130, 180, null);
				if (board.getPlayer(1).getHand().size() == 1)
					g.drawImage(uno, 220, 355, 100, 70, null);
				//right
				g.drawString("Player "+((board.getPlaying()+3)%4+1), 1070, 280);
				for (int i = 0; i < board.getPlayer(3).getHand().size(); i++)
					g.drawImage(back, 1040 + i*10, 292, 130, 180, null);
				if (board.getPlayer(3).getHand().size() == 1)
					g.drawImage(uno, 1180, 355, 100, 70, null);
				
				if (moves.size() < 5)
					try {
						for (int i = moves.size()-1, j = 0; i > moves.size()-6; i--, j += 30)
							g.drawString(moves.get(i), 1250, 30+j);
					} catch (Exception e) {}
				else 
					for (int i = 4, j = 0; i > -1; i--, j += 30)
						g.drawString(moves.get(i), 1250, 30+j);
				
				
				g.setFont(new Font("Arial", 0, 100));
				g.drawString("Player "+(board.getPlaying()+1)+"'s", 10, 100);
				g.drawString("Turn", 80, 200);
				
				Player player = board.getPlayer();
				ArrayList<Card> hand = player.getHand();
				if (hand.size() == 1)
					g.drawImage(uno, 825, 590, 100, 70, null);
				if (hand.size() <= 10)
				{
					if (hand.size() % 2 == 0)
					{
						for (int i = hand.size()/2, x = 750; i < hand.size(); i++, x += 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
						for (int i = hand.size()/2-1, x = 610; i >= 0; i--, x -= 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
					}
					else
					{
						for (int i = hand.size()/2, x = 685; i < hand.size(); i++, x += 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
						for (int i = hand.size()/2-1, x = 545; i >= 0; i--, x -= 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
					}
				}	
				else
				{
					int size = hand.size();
					int mid = hand.size()/2;
					if (mid % 2 == 0)
					{
						for (int i = mid/2, x = 750; i < mid; i++, x += 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
						for (int i = mid/2-1, x = 610; i >= 0; i--, x -= 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
					}
					else
					{
						for (int i = mid/2, x = 685; i < mid; i++, x += 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
						for (int i = mid/2-1, x = 545; i >= 0; i--, x -= 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 528, 130, 180, null);
						}
					}
					if ((size - mid) % 2 == 0)
					{
						for (int i = (size - mid)/2+mid, x = 750; i < hand.size(); i++, x += 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 764, 130, 180, null);
						}
						for (int i = (size - mid)/2+mid-1, x = 610; i >= mid; i--, x -= 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 764, 130, 180, null);
						}
					}
					else
					{
						for (int i = (size - mid)/2+mid, x = 685; i < hand.size(); i++, x += 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 764, 130, 180, null);
						}
						for (int i = (size - mid)/2+mid-1, x = 545; i >= mid; i--, x -= 140)
						{
							Card card = hand.get(i);
							g.drawImage(cards[card.getColor()][card.getID()], x, 764, 130, 180, null);
						}
					}
				}
				
				g.dispose();
				int width = contentPane.getWidth();
				int height = contentPane.getHeight();
				if (width >= height * 3 / 2)
				{
					gr.setColor(Color.BLACK);
					gr.fillRect(0, 0, width, height);
					gr.drawImage(scr, (int)(width / 2 - height * 3 / 4.0), 0, (int)(height * 3 / 2.0), height, null);
				}
				else
				{
					gr.setColor(Color.BLACK);
					gr.fillRect(0, 0, width, height);
					gr.drawImage(scr, 0, (int)(height / 2.0 - width * 2 / 6.0), width, (int)(width * 2 / 3.0), null);
				}
			}
		};
		
		contentPane.setPreferredSize(new Dimension(1500, 1000));
		contentPane.setBackground(Color.GRAY);
		contentPane.addMouseListener(this);
		add(contentPane);
		setContentPane(contentPane);
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public void mouseReleased(MouseEvent e) {
		if (win)
			return;
		
		int x = e.getX();
		int y = e.getY();
		int width = contentPane.getWidth();
		int height = contentPane.getHeight();
		
		int playNum = board.getPlaying()+1;
		Player player = board.getPlayer();
		ArrayList<Card> hand = player.getHand();
		if (width >= height * 3 / 2)
		{
			double xFactor = height*3/2.0/1500.0;
			double yFactor = height/1000.0;
			int start = (int)(width / 2 - height * 3 / 4.0);
			x -= start;
			x /= xFactor;
			y /= yFactor;
		}
		else
		{
			double xFactor = width/1500.0;
			double yFactor = width*2/3/1000.0;
			int start = (int)(height / 2.0 - width * 2 / 6.0);
			x /= xFactor;
			y -= start;
			y /= yFactor;
		}
		if (isWild)
		{
			if (755 <= x && x < 815 && 382 <= y && y < 442)
			{
				if (board.secTopCard().getColor() == 0)
					return;
				board.changeTop(new Card(0, board.getTopCard().getID()));
				if (board.getTopCard().getID() == 14)
					moves.add(0, "Player "+(board.getPlaying()+1)+": Plus 4 - Blue");
				else
					moves.add(0, "Player "+(board.getPlaying()+1)+": Wild - Blue");
			}
			else if (815 <= x && x < 875 && 322 <= y && y < 382)
			{
				if (board.secTopCard().getColor() == 1)
					return;
				board.changeTop(new Card(1, board.getTopCard().getID()));
				if (board.getTopCard().getID() == 14)
					moves.add(0, "Player "+(board.getPlaying()+1)+": Plus 4 - Green");
				else
					moves.add(0, "Player "+(board.getPlaying()+1)+": Wild - Green");
			}
			else if (755 <= x && x < 815 && 322 <= y && y < 382)
			{
				if (board.secTopCard().getColor() == 2)
					return;
				board.changeTop(new Card(2, board.getTopCard().getID()));
				if (board.getTopCard().getID() == 14)
					moves.add(0, "Player "+(board.getPlaying()+1)+": Plus 4 - Red");
				else
					moves.add(0, "Player "+(board.getPlaying()+1)+": Wild - Red");
			}
			else if (815 <= x && x < 875 && 382 <= y && y < 442)
			{
				if (board.secTopCard().getColor() == 3)
					return;
				board.changeTop(new Card(3, board.getTopCard().getID()));
				if (board.getTopCard().getID() == 14)
					moves.add(0, "Player "+(board.getPlaying()+1)+": Plus 4 - Yellow");
				else
					moves.add(0, "Player "+(board.getPlaying()+1)+": Wild - Yellow");
			}
			else
				return;
			isWild = false;
			if (board.getTopCard().getID() == 14)
				board.next();
			board.next();
		}
		else if (530 <= x && x <= 660 && 292 <= y && y <= 472)
		{
			Card card = board.peekNextCard();
			if (board.getPlayer().moveAvailable(board.getTopCard()))
				return;
			moves.add(0, "Player "+playNum+": Draw");
			if (card.getColor() == 4)
			{
				if (card.getID() == 13)
				{
					if (board.playWild(-1))
						isWild = true;
				}
				else
					if (board.playWild(-2))
						isWild = true;
			}
			else if (board.drawCard())
				moves.add(0, "Player "+playNum+": "+card);
		}
		else if (hand.size() <= 10)
		{
			if (hand.size() % 2 == 0)
			{
				int num = x - (750 - hand.size()/2*140);
				if (0 <= num/140 && num/140 < hand.size() && num % 140 <= 130 && 528 <= y && y <= 708)
				{
					Card card = hand.get(num/140);
					if (card.getColor() == 4)
					{
						if (board.playWild(num/140))
							isWild = true;
					}
					else if (board.playCard(num/140))
						moves.add(0, "Player "+playNum+": "+card);
				}
			}
			else
			{
				int num = x - (685 - hand.size()/2*140);
				if (0 <= num/140 && num/140 < hand.size() && num % 140 <= 130 && 528 <= y && y <= 708)
				{
					Card card = hand.get(num/140);
					if (card.getColor() == 4)
					{
						if (board.playWild(num/140))
							isWild = true;
					}
					else if (board.playCard(num/140))
						moves.add(0, "Player "+playNum+": "+card);
				}
			}
		}
		else
		{
			int size = hand.size();
			int mid = hand.size()/2;
			if (528 <= y && y <= 708)
			{
				if (mid % 2 == 0)
				{
					int num = x - (750 - mid/2*140);
					if (0 <= num/140 && num/140 < mid && num % 140 <= 130)
					{
						Card card = hand.get(num/140);
						if (card.getColor() == 4)
						{
							if (board.playWild(num/140))
								isWild = true;
						}
						else if (board.playCard(num/140))
							moves.add(0, "Player "+playNum+": "+card);
					}
				}
				else
				{
					int num = x - (685 - mid/2*140);
					if (0 <= num/140 && num/140 < mid && num % 140 <= 130)
					{
						Card card = hand.get(num/140);
						if (card.getColor() == 4)
						{
							if (board.playWild(num/140))
								isWild = true;
						}
						else if (board.playCard(num/140))
							moves.add(0, "Player "+playNum+": "+card);
					}
				}
			}
			else
			{
				if (size - mid % 2 == 0)
				{
					int num = x - (750 - (size-mid)/2*140);
					if (0 <= num/140 && num/140 < size-mid && num % 140 <= 130)
					{
						Card card = hand.get(num/140+mid);
						if (card.getColor() == 4)
						{
							if (board.playWild(num/140+mid))
								isWild = true;
						}
						else if (board.playCard(num/140+mid))
							moves.add(0, "Player "+playNum+": "+card);
					}
				}
				else
				{
					int num = x - (685 - (size-mid)/2*140);
					if (0 <= num/140 && num/140 < size-mid && num % 140 <= 130)
					{
						Card card = hand.get(num/140+mid);
						if (card.getColor() == 4)
						{
							if (board.playWild(num/140+mid))
								isWild = true;
						}
						else if (board.playCard(num/140+mid))
							moves.add(0, "Player "+playNum+": "+card);
					}
				}
			}
		}
		if (board.win() < 4)
		{
			win = true;
			winPlayer = board.win()+1;
		}
		repaint();
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
}
