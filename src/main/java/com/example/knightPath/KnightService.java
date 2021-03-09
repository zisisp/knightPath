package com.example.knightPath;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

@Service
public class KnightService {

  public Map<String, Integer> mapLetters =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<>("a", 1),
          new AbstractMap.SimpleEntry<>("b", 2),
          new AbstractMap.SimpleEntry<>("c", 3),
          new AbstractMap.SimpleEntry<>("d", 4),
          new AbstractMap.SimpleEntry<>("e", 5),
          new AbstractMap.SimpleEntry<>("f", 6),
          new AbstractMap.SimpleEntry<>("g", 7),
          new AbstractMap.SimpleEntry<>("h", 8)
      );

  public Map<Integer, String> mapNumbers =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<>(1, "a"),
          new AbstractMap.SimpleEntry<>(2, "b"),
          new AbstractMap.SimpleEntry<>(3, "c"),
          new AbstractMap.SimpleEntry<>(4, "d"),
          new AbstractMap.SimpleEntry<>(5, "e"),
          new AbstractMap.SimpleEntry<>(6, "f"),
          new AbstractMap.SimpleEntry<>(7, "g"),
          new AbstractMap.SimpleEntry<>(8, "h")
      );

  public static final String NO_VALID_PATH_HAS_BEEN_FOUND = "No valid path has been found";
  public static final int FILE_MAX_SIZE = 8;
  public static final int RANK_MAX_SIZE = 8;
  public static final String DESTINATION_REACHED_IN_LESS_THAN_3_STEPS = "Destination Reached in less than 3 steps";
  // Below arrays detail all eight possible movements
  // for a knight
  private static int[] rank = {2, 2, -2, -2, 1, 1, -1, -1};
  private static int[] file = {-1, 1, 1, -1, 2, -2, 2, -2};

  // Check if `(x, y)` is valid chessboard coordinates.
  // Note that a knight cannot go out of the chessboard
  private boolean isValid(int x, int y) {
    if (x <= 0 || y <= 0 || x > FILE_MAX_SIZE || y > RANK_MAX_SIZE) {
      return false;
    }

    return true;
  }

  /**
   * @param src
   * @param dest
   * @return
   */
  public String findTheMinimumPath(Node src, Node dest) {
    // set to check if the matrix cell is visited before or not
    Map<String, Node> visited = new HashMap<>();

    // create a queue and enqueue the first node
    Queue<Node> q = new ArrayDeque<>();
    q.add(src);

    // loop till queue is empty
    while (!q.isEmpty()) {
      // dequeue front node and process it
      Node node = q.poll();

      int x = node.x;
      int y = node.y;
      int dist = node.dist;

      // if the destination is reached, return distance
      boolean distance3 = node.dist == 3;
      boolean distanceMoreThan3 = node.dist > 3;
      if (x == dest.x && y == dest.y) {
        if (distance3) {
          return printPath(visited, node);
        } else {
          return DESTINATION_REACHED_IN_LESS_THAN_3_STEPS;
        }
      } else {
        if (distanceMoreThan3) {
          return NO_VALID_PATH_HAS_BEEN_FOUND;
        }
      }

      // skip if the location is visited before
      if (visited.get(node.toKey()) == null) {
        // mark the current node as visited
        visited.put(node.toKey(), node);

        // check for all eight possible movements for a knight
        // and enqueue each valid movement
        for (int i = 0; i < 8; i++) {
          // Get the knight's valid position from the current position on
          // the chessboard and enqueue it with +1 distance
          int x1 = x + rank[i];
          int y1 = y + file[i];

          if (isValid(x1, y1)) {
            q.add(new Node(x1, y1, dist + 1));
          }
        }
      }
    }

    // return infinity if the path is not possible
    return NO_VALID_PATH_HAS_BEEN_FOUND;
  }

  /**
   * We get the visited nodes and
   *
   * @param visited
   * @param dest
   * @return
   */
  private String printPath(Map<String, Node> visited, Node dest) {
    //we start from the end and go back to distance 0
    boolean notFinished = true;
    Node nodeChecked = dest;
    Map<Integer, Node> path = new HashMap<>();
    path.put(3, dest);
    int currentDist = dest.dist;
    while (notFinished) {
      for (int i = 0; i < 8; i++) {
        int x1 = nodeChecked.x + rank[i];
        int y1 = nodeChecked.y + file[i];

        if (isValid(x1, y1) && visited.get("" + (currentDist - 1) + x1 + y1) != null) {
          nodeChecked = visited.get("" + (currentDist - 1) + x1 + y1);
          currentDist = nodeChecked.dist;
          path.put(nodeChecked.dist, nodeChecked);
        }

      }
      if (currentDist == 0) {
        notFinished = false;
      }

    }
    return printMoves(path);
  }

  /**
   * Moves are always
   *
   * @param path
   * @return
   */
  private String printMoves(Map<Integer, Node> path) {
    StringBuilder toReturn = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      toReturn.append(getChessNodeFromNode(path.get(i)).toString()).append("->")
          .append(getChessNodeFromNode(path.get(i + 1)).toString()).append(",");
    }
    return toReturn.toString().substring(0, toReturn.toString().length() - 1);
  }

  public void main(ApplicationArguments args) {
    if (args.getSourceArgs().length != 4) {
      System.out.println(
          "Expected format: java -jar KnightServer.jar Start_File Start_Rank End_File End_Rank");
      System.exit(0);
    }

    ChessNode from0 = new ChessNode(args.getSourceArgs()[0],
        Integer.parseInt(args.getSourceArgs()[1]));
    ChessNode to0 = new ChessNode(args.getSourceArgs()[2],
        Integer.parseInt(args.getSourceArgs()[3]));
    Node src1 = getNodeFromChessNode(from0);
    // destination coordinates
    Node dest1 = getNodeFromChessNode(to0);
    System.out.println("Results for given positions = " + findTheMinimumPath(src1, dest1));

    checkResultHas3Steps();
    checkLessSteps();
    checkMoreSteps();
    ChessNode from = new ChessNode("a", 4);
    ChessNode to = new ChessNode("g", 5);
    Node src = getNodeFromChessNode(from);
    // destination coordinates
    Node dest = getNodeFromChessNode(to);
    System.out.println("Test Results for a4->g5 = " + findTheMinimumPath(src, dest));

  }

  private void checkResultHas3Steps() {
    ChessNode from = new ChessNode("a", 8);
    ChessNode to = new ChessNode("f", 4);
    String expectedText = "a8->c7,c7->d5,d5->f4";

    // source coordinates
    checkResults(from, to, expectedText);
    ChessNode from1 = new ChessNode("a", 8);
    ChessNode to1 = new ChessNode("a", 7);

    // source coordinates
    checkResults(from1, to1, "a8->b6,b6->c8,c8->a7");
  }

  private void checkLessSteps() {
    ChessNode from = new ChessNode("a", 8);
    ChessNode to = new ChessNode("c", 7);

    // source coordinates
    checkResults(from, to, DESTINATION_REACHED_IN_LESS_THAN_3_STEPS);
  }

  private void checkMoreSteps() {
    ChessNode from = new ChessNode("a", 8);
    ChessNode to = new ChessNode("g", 2);

    // source coordinates
    checkResults(from, to, NO_VALID_PATH_HAS_BEEN_FOUND);
  }

  private void checkResults(ChessNode from, ChessNode to, String expectedText) {
    Node src = getNodeFromChessNode(from);

    // destination coordinates
    Node dest = getNodeFromChessNode(to);

    String theMinimumPath = findTheMinimumPath(src, dest);
    if (!(expectedText.equals(theMinimumPath))) {
      System.out.println("Expected result was:" + expectedText + " but it was:" + theMinimumPath);
    }
  }

  private Node getNodeFromChessNode(ChessNode from) {
    return new Node(mapLetters.get(from.file), from.rank);
  }

  private ChessNode getChessNodeFromNode(Node node) {
    return new ChessNode(mapNumbers.get(node.x), node.y);
  }
}

//class Main {


class ChessNode {

  String file;
  Integer rank;

  public ChessNode(String file, Integer rank) {
    this.file = file;
    this.rank = rank;
  }

  @Override
  public String toString() {
    return "" + file + rank;
  }
}

// A queue node used in BFS
class Node {

  // `(x, y)` represents chessboard coordinates
  // `dist` represents its minimum distance from the source
  int x, y, dist;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Node(int x, int y, int dist) {
    this.x = x;
    this.y = y;
    this.dist = dist;
  }

  public String toKey() {
    return "" + dist + x + y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Node node = (Node) o;
    return x == node.x &&
        y == node.y &&
        dist == node.dist;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, dist);
  }
}