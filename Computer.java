import java.util.*;

public class Computer 
{
	/**
	 * Finds the best move using a Minimax algorithm
	 * @param moves A 2D ArrayList with all possible moves
	 * @param chess The board in play
	 * @param white Is the bot white
	 * @param depth Layers to search (Must be odd number)
	 * @return An array, [x1,y1,x2,y2], with the best move
	 */
	public static int[] getMinimaxMove(Board chess, boolean white, int depth)
	{	
		int clr;
		if (white)
			clr = 1;
		else
			clr = 2;
		
		Map<Integer, int[]> scoredMoves = new TreeMap<>();
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chess.occupation(i, j) == clr) {
					
					ArrayList<ArrayList<Integer>> moves = chess.getPiece(i, j).getPossibleMoves(chess);
					ArrayList<Integer> scores = new ArrayList<>();
					int score = Integer.MIN_VALUE;

					for (int k = 0; k < moves.get(0).size(); k++)
					{
						Board c = chess.copy();
						c.movePiece(i, j, moves.get(0).get(k), moves.get(1).get(k), true);
						int newScore = calculateScore(c, white);
						scores.add(newScore);
						if (newScore > score)
							score = newScore;
					}
					for (int k = 0; k < moves.get(0).size(); k++)
					{
						if (scores.get(k) == score)
						{
							Board c = chess.copy();
							c.movePiece(i, j, moves.get(0).get(k), moves.get(1).get(k), true);
							score = minimaxLayer(c, !white, depth - 1);
							int[] m = {i, j, moves.get(0).get(k), moves.get(1).get(k)};
							scoredMoves.put(score, m);
						}
					}
				}
		int max = Integer.MIN_VALUE;
		for (int x : scoredMoves.keySet())
			if (x > max)
				max = x;
		
		return scoredMoves.get(max);
	}
	
	
	public static int minimaxLayer(Board chess, boolean white, int depth)
	{
		int score = Integer.MIN_VALUE;
		
		int clr;
		if (white)
			clr = 1;
		else
			clr = 2;
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chess.occupation(i, j) == clr) {
					
					ArrayList<ArrayList<Integer>> moves = chess.getPiece(i, j).getPossibleMoves(chess);
					ArrayList<Integer> scores = new ArrayList<>();
					score = Integer.MIN_VALUE;

					for (int k = 0; k < moves.get(0).size(); k++)
					{
						Board c = chess.copy();
						c.movePiece(i, j, moves.get(0).get(k), moves.get(1).get(k), true);
						int newScore = calculateScore(c, white);
						scores.add(newScore);
						if (newScore > score)
							score = newScore;
					}
					if (depth > 1)
						for (int k = 0; k < moves.get(0).size(); k++)
						{
							if (scores.get(k) == score)
							{
								Board c = chess.copy();
								c.movePiece(i, j, moves.get(0).get(k), moves.get(1).get(k), true);
								score = minimaxLayer(c, !white, depth - 1);
							}
						}
					else
					{
						return score;
					}
						
				}
		
		return score;
	}
	
	public static int[] getMinimaxMoveLinear(Board chess, boolean white, int depth)
	{
		int clr;
		if (white)
			clr = 1;
		else
			clr = 2;
		
		int[] move = {0, 0, 0, 0};	// [xCoord,yCoord,xMove,yMove]
		int score = Integer.MIN_VALUE;
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chess.occupation(i, j) == clr) {
					ArrayList<ArrayList<Integer>> moves = chess.getPiece(i, j).getPossibleMoves(chess);
					for (int k = 0; k < moves.get(0).size(); k++)
					{
						Board c = chess.copy();
						c.movePiece(i, j, moves.get(0).get(k), moves.get(1).get(k), true);
						int newScore = calculateScore(c, white);
						if (newScore > score || (newScore == score && Math.random() > .5))
						{
							move[0] = i;
							move[1] = j;
							move[2] = moves.get(0).get(k);
							move[3] = moves.get(1).get(k);
							score = newScore;
						}
					}
				}
		
		return move;
	}
	
	private static int calculateScore(Board chess, boolean white)
	{
		int sum = 0;
		
		int clr;
		if (white)
			clr = 1;
		else
			clr = 2;
				
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chess.occupation(i, j) == clr)
					sum += chess.getPiece(i,j).getScore();
		
		if (clr == 1)
			clr = 2;
		else
			clr = 1;
		
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (chess.occupation(i, j) == clr)
					sum -= chess.getPiece(i,j).getScore();
		
		return sum;
	}
}
