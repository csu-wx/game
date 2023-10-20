package com.huoxin4415.bwai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessBoard {
    private int width;
    private int height;
    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private int[][] board;
    private int freeSize;

    private LinkedList<Point> trace;    // 落子轨迹

    public ChessBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[width][height];
        this.board[width / 2 - 1][height / 2 - 1] = 1;
        this.board[width / 2][height / 2 - 1] = -1;
        this.board[width / 2][height / 2] = 1;
        this.board[width / 2 - 1][height / 2] = -1;
        this.freeSize = width * height - 4;
        this.minX = width / 2 - 1;
        this.maxX = width / 2;
        this.minY = height / 2 - 1;
        this.maxY = height / 2;

        this.trace = new LinkedList<>();
        this.trace.addFirst(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE)); // 占位对象
        this.trace.addFirst(new Point(Integer.MIN_VALUE, Integer.MIN_VALUE)); // 占位对象
    }

    public ChessBoard(ChessBoard cb) {
        this.width = cb.width;
        this.height = cb.height;
        this.board = new int[width][height];
        this.freeSize = cb.freeSize;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.board[i][j] = cb.board[i][j];
            }
        }
        this.minX = cb.minX;
        this.maxX = cb.maxX;
        this.minY = cb.minY;
        this.maxY = cb.maxY;
    }

    public int fall(int x, int y, int piece) {
        if (x >= width || y >= height) {
            return 0;
        }

        if (board[x][y] == 0) {
            if (trans(x, y, piece) == 0) { // 未翻转任何棋子
                return 0; // 落子失败
            }

            board[x][y] = piece;
            if (x < minX) {
                minX = x;
            } else if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            } else if (y > maxY) {
                maxY = y;
            }
            this.freeSize--;

            if (this.trace != null) {
                this.trace.addLast(new Point(x, y));
                this.trace.removeFirst();
            }

            return piece;
        } else {
            return 0;
        }
    }

    public int trans(int x, int y, int piece) {
        int result = 0;
        // left
        result += trans(x, y, piece, -1, 0, x);

        // right
        result += trans(x, y, piece, 1, 0, width - x - 1);

        // top
        result += trans(x, y, piece, 0, -1, y);

        // bottom
        result += trans(x, y, piece, 0, 1, height - y - 1);

        // left-top
        result += trans(x, y, piece, -1, -1, Math.min(x, y));

        // right-top
        result += trans(x, y, piece, 1, -1, Math.min(width - x - 1, y));

        // left-bottom
        result += trans(x, y, piece, -1, 1, Math.min(x, height - y - 1));

        // right-bottom
        result += trans(x, y, piece, 1, 1, Math.min(width - x - 1, height - y - 1));
        return result;
    }

    private int trans(int x, int y, int piece, int xIncr, int yIncr, int length) {
        int result = 0;
        for (int i = 1; i <= length; i++) {
            if (board[x + xIncr * i][y + yIncr * i] == piece) {
                for (int j = 1; j < i; j++) {
                    board[x + xIncr * j][y + yIncr * j] = piece;
                    result++;
                }
                break;
            } else if (board[x + xIncr * i][y + yIncr * i] == 0) {
                break;
            }
        }
        return result;
    }

    public boolean hasChoice(Piece piece) {
        for (int x = Math.max(this.getMinX() - 1, 0); x < Math.min(this.getMaxX() + 2, this.getWidth()); x++) {
            for (int y = Math.max(this.getMinY() - 1, 0); y < Math.min(this.getMaxY() + 2, this.getHeight()); y++) {
                if (this.getBoard()[x][y] == 0) {
                    ChessBoard childCb = new ChessBoard(this);
                    if (childCb.fall(x, y, piece.val()) != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int result() {
        Integer black = 0;
        Integer white = 0;
        int[][] board = this.getBoard();
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == 1) {
                    black++;
                } else if (board[x][y] == -1) {
                    white++;
                }
            }
        }
        return black.compareTo(white);
    }

    public LinkedList<Point> getTrace() {
        return trace;
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getFreeSize() {
        return this.freeSize;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public List<Point> getSteps(Boolean gameTurn) {

//        ArrayList<Point> points = new ArrayList<>(10);
//        ArrayList<Point> a = new ArrayList<>();
//        ArrayList<Point> b = new ArrayList<>();
//        ArrayList<Point> c = new ArrayList<>();
//        ArrayList<Point> d = new ArrayList<>();
//
//        //获取横竖斜条路径上的所有棋子
//
//        for (Point point : points) {
//            //竖
//            for (int i = 0; i < point.getX(); i++) {
//                a.add(new Point(i, point.getY()));
//            }
//
//            //横
//            for (int i = 0; i < point.getY(); i++) {
//                b.add(new Point(point.getX(), i));
//            }
//
//            //斜1
//            for (int i = 0; ; i++) {
//                if (point.getX() + i >= width || point.getY() + i >= height) {
//                    break;
//                }
//                c.add(new Point(point.getX() + i, point.getY() + i));
//            }
//
//            //斜1
//            for (int i = 0; ; i++) {
//                int nx = point.getX() + i;
//                int ny = point.getY() + i;
//                if (nx >= width || ny >= height) {
//                    break;
//                }
//                c.add(new Point(nx ,ny));
//            }
//            for (int i = 0; ; i++) {
//                int nx = point.getX() - i;
//                int ny = point.getY() - i;
//                if (nx <= 0 || ny <= 0) {
//                    break;
//                }
//                c.add(new Point(nx, ny));
//            }
//
//            //斜2
//            for (int i = 0; ; i++) {
//                int nx = point.getX() + i;
//                int ny = point.getY() - i;
//                if (nx >= width || nx < 0 || ny >= height || ny < 0) {
//                    break;
//                }
//                d.add(new Point(nx ,ny));
//            }
//            for (int i = 0; ; i++) {
//                int nx = point.getX() - i;
//                int ny = point.getY() + i;
//                if (nx >= width || nx < 0 || ny >= height || ny < 0) {
//                    break;
//                }
//                d.add(new Point(nx, ny));
//            }
//        }

        int player = Boolean.TRUE.equals(gameTurn) ? 1 : -1;

        List<Point> nextSteps = getNextSteps(this, player);

        return nextSteps.stream().filter(
                s -> {
                    ChessBoard childCb = new ChessBoard(this);
                    return childCb.fall(s.getX(), s.getY(), player) == 1 ? Boolean.TRUE : Boolean.FALSE;
                }
        ).collect(Collectors.toList());


    }

    private List<Point> getNextSteps(ChessBoard cb, int player) {
        List<Point> steps = new ArrayList<>();
        int[][] board = cb.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != player && board[i][j] != 0) {
                    //不是左右边界
                    if (i != 0 && i != board[0].length -1) {
                        //搜左右
                        if (board[i - 1][j] != -1 && board[i - 1][j] != 1) {
                            Point point = new Point(i - 1, j);
                            steps.add(point);
                        }
                        if (board[i + 1][j] != -1 && board[i + 1][j] != 1) {
                            Point point = new Point(i + 1, j);
                            steps.add(point);
                        }

                    } else {
                        if (i == 0) {
                            if (board[i + 1][j] != -1 && board[i + 1][j] != 1) {
                                Point point = new Point(i + 1, j);
                                steps.add(point);
                            }
                        } else {
                            if (board[i - 1][j] != -1 && board[i - 1][j] != 1) {
                                Point point = new Point(i - 1, j);
                                steps.add(point);
                            }
                        }

                    }
                    //不是上下边界
                    if (j != 0 && j != board.length-1) {
                        //搜上下
                        if (board[i][j - 1] != -1 && board[i][j - 1] != 1) {
                            Point point = new Point(i, j - 1);
                            steps.add(point);
                        }
                        if (board[i][j + 1] != -1 && board[i][j + 1] != 1) {
                            Point point = new Point(i, j + 1);
                            steps.add(point);
                        }
                    } else {
                        if (j == 0) {
                            if (board[i][j + 1] != -1 && board[i][j + 1] != 1) {
                                Point point = new Point(i, j + 1);
                                steps.add(point);
                            }
                        } else {
                            if (board[i][j - 1] != -1 && board[i][j - 1] != 1) {
                                Point point = new Point(i, j - 1);
                                steps.add(point);
                            }
                        }
                    }
                }
            }
        }
        return steps;
    }


    public boolean isWin() {
        int cntWhite = 0;
        int cntBlack = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (board[i][j]) {
                    case -1:
                        cntWhite++;
                        break;
                    case 1:
                        cntBlack++;
                        break;
                    default:
                        break;
                }
            }
        }
        return cntBlack > cntWhite;
    }

    public boolean isOver() {
        return !hasChoice(Piece.WHITE) && !hasChoice(Piece.BLACK);
    }
}