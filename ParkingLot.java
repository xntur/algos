/*
The question: Given a 2d array of integers where -1 represents other cars, 0 represents free space, 
1 represents your car, and 2 represents exits, find the shortest way out. 

The answer uses breadth first search: https://www.freecodecamp.org/news/exploring-the-applications-and-limits-of-breadth-first-search-to-the-shortest-paths-in-a-weighted-1e7b28b3307/

This is one implementation; I did this quickly during the meetup so it has some flaws with error handling
and some places it could be more efficient, but the basic idea is right.
*/

import java.util.concurrent.ArrayBlockingQueue;

public class ParkingLot {

  class Coord {
    int row;
    int col;
    Coord next;

    Coord(int row, int col, Coord next) {
      this.row = row;
      this.col = col;
      this.next = next;
    }

    public String toString() {
      return "Row: " + row + ", Col: " + col;
    }
  }

  private int[][] board;

  private Coord findStart() {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 1) return new Coord(i, j, null);
      }
    }
    throw new RuntimeException("oh no!");
  }

  private boolean isValid(Coord c) {
    return c.row >= 0 &&
        c.row < board.length &&
        c.col >= 0 &&
        c.col < board[0].length &&
        board[c.row][c.col] != -1;
  }

  private Coord reverse(Coord c) {
    if (c == null || c.next == null) return c;
    Coord prev = null;
    Coord curr = c;
    Coord next = c.next;
    while (next != null) {
      curr.next = prev;
      prev = curr;
      curr = next;
      next = curr.next;
    }
    curr.next = prev;
    return curr;
  }

  public ParkingLot(int[][] board) {
    this.board = board;
  }

  public Coord path() {
    Coord start = findStart();
    ArrayBlockingQueue<Coord> todo = new ArrayBlockingQueue<>(
        board.length * board[0].length);
    todo.add(start);

    while (todo.size() > 0) {
      Coord curr = todo.remove();
      if (board[curr.row][curr.col] == 2) {
        return reverse(curr);
      }
      board[curr.row][curr.col] = -1;
      Coord left = new Coord(curr.row - 1, curr.col, curr);
      if (isValid(left)) {
        todo.add(left);
      }
      Coord right = new Coord(curr.row + 1, curr.col, curr);
      if (isValid(right)) {
        todo.add(right);
      }
      Coord up = new Coord(curr.row, curr.col + 1, curr);
      if (isValid(up)) {
        todo.add(up);
      }
      Coord down = new Coord(curr.row, curr.col - 1, curr);
      if (isValid(down)) {
        todo.add(down);
      }
    }

    return null;
  }

  public static void main(String[] args) {
    int[][] board = new int[][]{
      { 2,  2,  0,  0, -1},
      {-1, -1, -1,  0,  0},
      { 1, -1,  0,  0,  0},
      { 0,  0,  0,  0,  0},
      { 0, -1, -1, -1, -1},
    };
    ParkingLot lot = new ParkingLot(board);
    Coord c = lot.path();
    while (c != null) {
      System.out.println(c);
      c = c.next;
    }
  }
}
