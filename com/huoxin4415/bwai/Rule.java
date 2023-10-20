package com.huoxin4415.bwai;


import java.util.ArrayList;
import java.util.List;

/**
 * 实现游戏规则
 * 1.判断某一步是否合法
 * 2.获取所有合法走步
 * 3.走一步 --翻转敌方棋子
 * 4.统计两方棋子个数
 */public class Rule {


    /**
     * 某步落子是否在棋盘边界内
     * @param x 落子位置横坐标
     * @param y 落子位置纵坐标
     * @return 合法性
     */
    public static boolean isLegal(int x,int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    /**
     * 1.判断在某个位置落子是否合法 -是否与己方棋子在某方向上夹住敌方棋子
     * @param chessBoard 当前棋盘
     * @param position 某位置
     * @param player 玩家标识
     * @return 合法性
     */
    public static boolean isLegalMove(int[][] chessBoard, Position position, int player) {
        int x = position.getX(); //待搜索的棋子位置以x，y坐标表示
        int y = position.getY();
        //不在棋盘范围  或者该位子已经有棋子了 直接返回非法
        if (!isLegal(x,y) || chessBoard[x][y] != Constant.NULL.getValue())
            return false;
        //取当前棋子的对手棋子颜色
        int enemyPlayer = player == Constant.BLACK.getValue() ? Constant.WHITE.getValue() : Constant.BLACK.getValue();
        //遍历八个方向
        int dirx,diry;//坐标轴x,y 取值范围 -1 0 1
        for (dirx = -1; dirx <= 1 ; dirx++) {
            for (diry = -1; diry <= 1 ; diry++) {
                if (dirx == 0 && diry == 0)  //原点不搜
                    continue;
                int willGoX = x + dirx;
                int willGoY = y + diry;
                //从此方向的移动的下一步棋在棋盘上 且与当前棋子颜色相反
                if (isLegal(willGoX,willGoY) && chessBoard[willGoX][willGoY] == enemyPlayer) {
                    //在该方向继续搜索，直到找到己方棋子颜色为止
                    for (int i = willGoX + dirx,j = willGoY + diry;isLegal(i,j);i+= dirx,j+= diry) {
                        if (chessBoard[i][j] == enemyPlayer) { //是敌方棋子，继续搜
                            continue;
                        }
                        else if (chessBoard[i][j] == player) { //己方棋子，返回true
                            return true;
                        }else { //为空 则不合法 跳出对该方向的搜索
                            break;
                        }
                    }
                }
            }
        }
        //八个方向都没找到合法位置
        return false;
    }

    /**
     * 2.获取所有合法走步
     * @param chessBoard 当前棋盘
     * @param player 玩家标识
     * @return 当前玩家所有合法走步
     */
    public List<Position> getAllLegalMoves(int[][] chessBoard,int player) {
        List<Position> legalMoves = new ArrayList<>();
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (chessBoard[i][j] == Constant.NULL.getValue() && isLegalMove(chessBoard,new Position(i,j),player)){
                    legalMoves.add(new Position(i,j));
                }
            }
        }
        return legalMoves;
    }

    /**
     * 3.走一步，翻转敌方被夹住棋子
     * @param board 当前棋盘
     * @param position 将走步的位置
     * @param player 玩家标识
     * @return 翻转后的新棋盘
     */
    public int[][] move(int[][] board, Position position,int player) {
        //拷贝当前棋盘到新棋盘进行翻转
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        int x = position.getX();
        int y = position.getY();
        //搜索当前位置八个方向，找到直线上被本方夹住的棋子 翻转
        //取当前棋子的对手棋子颜色
        int enemyPlayer = player == Constant.BLACK.getValue() ? Constant.WHITE.getValue() : Constant.BLACK.getValue();
        //遍历八个方向
        int dirx,diry;//坐标轴x,y 取值范围 -1 0 1
        for (dirx = -1; dirx <= 1 ; dirx++) {
            for (diry = -1; diry <= 1 ; diry++) {
                if (dirx == 0 && diry == 0)  //原点不搜
                    continue;
                int willGoX = x + dirx;
                int willGoY = y + diry;
                //从此方向的移动的下一步棋在棋盘上 且与当前棋子颜色相反
                if (isLegal(willGoX,willGoY) && board[willGoX][willGoY] == enemyPlayer) {
                    //在该方向继续搜索，直到找到己方棋子颜色为止
                    for (int i = willGoX + dirx,j = willGoY + diry;isLegal(i,j);i+= dirx,j+= diry) {
                        if (board[i][j] == enemyPlayer) { //是敌方棋子，继续搜
                            continue;
                        }
                        else if (board[i][j] == player) { //己方棋子
                            //将从x,y 到 i,j 沿线棋子全部置为己方棋子颜色
                            setBoardColor(newBoard,x,y,i,j,player);
                            break;
                        }else { //为空 则不合法 跳出对该方向的搜索
                            break;
                        }
                    }
                }
            }
        }
        return newBoard;
    }

    /**
     * 将从x,y 到 i,j 沿线所夹敌方棋子全部置为己方棋子颜色
     * @param newBoard
     * @param x
     * @param y
     * @param i
     * @param j
     * @param player
     */
    private void setBoardColor(int[][] newBoard, int x, int y, int i, int j, int player) {
        //判断i，j 相对 x,y 的方向
        //上
        if (i == x && j > y) {
            //将沿上方向所夹对手棋子颜色置为己方棋子颜色
            for (int k = y + 1 ; k < j; k++) {
                newBoard[x][k] = player;
            }
        }
        //右上
        if (i > x && j > y) {
            for (int k = x + 1; k < i; k++) {
                for (int l = y + 1; l < j; l++) {
                    newBoard[k][l] = player;
                }
            }
        }
        //右
        if (i > x && j == y) {
            for (int k = x + 1; k < i; k++) {
                newBoard[k][y] = player;
            }
        }
        //右下
        if (i > x && j < y) {
            for (int k = x + 1; k < i; k++) {
                for (int l = y - 1; l > j ; l--) {
                    newBoard[k][l] = player;
                }
            }
        }
        //下
        if (i == x && j < y) {
            for (int k = y - 1; k > j; k--) {
                newBoard[x][k] = player;
            }
        }
        //左下
        if (i < x && j < y) {
            for (int k = x - 1; k > i; k--) {
                for (int l = y - 1; l > j ; l--) {
                    newBoard[k][l] = player;
                }
            }
        }
        //左
        if (i < x && j == y) {
            for (int k = x - 1; k > i; k--) {
                newBoard[k][y] = player;
            }
        }
        //左上
        if (i < x && j > y) {
            for (int k = x - 1; k > i ; k--) {
                for (int l = y + 1; l < y; l++) {
                    newBoard[k][l] = player;
                }
            }
        }
    }

    /**
     * 4.统计棋子个数
     * @param board 当前棋盘
     * @param player 玩家标识
     * @return 玩家棋子个数
     */
    private int countPiece(int[][] board,int player) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 黑白棋评估函数 考虑评估因素：
     * 棋子个数
     * 边角 赋边界位置权重 5 2 1
     * 行动力 棋子合法下一步合法走步数量
     * 稳定度
     * @param chessBoard
     * @param player
     * @return
     */
    public int evaluate(int[][] chessBoard,int player) {
        int playerScore = 0;
        int enemyScore = 0;
        //取对手棋子
        int enemyPlayer = player == Constant.BLACK.getValue() ? Constant.WHITE.getValue() : Constant.BLACK.getValue();
        /**
         * 棋子个数
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoard[i][j] == player) {
                    playerScore += 1;
                } else if (chessBoard[i][j] == enemyPlayer) {
                    enemyScore += 1;
                }
            }
        }
        /**
         * 边角
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i == 0 || i == 7) && (j == 0 || j == 7)) {
                    if (chessBoard[i][j] == player) {
                        playerScore += 5;
                    } else if (chessBoard[i][j] == enemyPlayer) {
                        enemyScore += 5;
                    }
                } else if (i == 0 || i == 7 || j == 0 || j == 7) {
                    if (chessBoard[i][j] == player) {
                        playerScore += 2;
                    } else if (chessBoard[i][j] == enemyPlayer) {
                        enemyScore += 2;
                    }
                } else {
                    if (chessBoard[i][j] == player) {
                        playerScore += 1;
                    } else if (chessBoard[i][j] == enemyPlayer) {
                        enemyScore += 1;
                    }
                }
            }
        }
        /**
         * 稳定度
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int weight[] = new int[] { 2, 4, 6, 10, 15 };
                if (chessBoard[i][j] == player) {
                    playerScore += weight[getStabilizationDegree(chessBoard, new Position(i, j))];
                } else if (chessBoard[i][j] == enemyPlayer) {
                    enemyScore += weight[getStabilizationDegree(chessBoard, new Position(i, j))];
                }
            }
        }
        /**
         * 行动力
         */
        playerScore += getAllLegalMoves(chessBoard, player).size();
        enemyScore += getAllLegalMoves(chessBoard, enemyPlayer).size();

        return playerScore - enemyScore;

    }

    /**
     * 计算某位置处 player棋子稳定度
     * 若player 某一方向直到不为空（包括边界）的两个位置 左-右 上-下 左上-右下 右上-左下 的某一个位置，至少有一个出界 || 两个均为敌方棋子 稳定度加一
     *
     * @param board 当前棋盘
     * @param position 当前棋子坐标
     * @return 棋子稳定度
     */
    private static int getStabilizationDegree(int[][] board, Position position) {
        int player = board[position.getY()][position.getY()];
        //敌方棋子
        int enemy = player == Constant.BLACK.getValue() ? Constant.WHITE.getValue() : Constant.BLACK.getValue();
        int drow[][], dcol[][];
        int row[] = new int[2], col[] = new int[2];
        int degree = 0;

        drow = new int[][] { { 0, 0 }, { -1, 1 }, { -1, 1 }, { 1, -1 } };
        dcol = new int[][] { { -1, 1 }, { 0, 0 }, { -1, 1 }, { -1, 1 } };

        for (int k = 0; k < 4; k++) {
            row[0] = row[1] = position.getX();
            col[0] = col[1] = position.getY();
            for (int i = 0; i < 2; i++) {
                while (Rule.isLegal(row[i] + drow[k][i], col[i] + dcol[k][i])
                        && board[row[i] + drow[k][i]][col[i] + dcol[k][i]] == player) {
                    row[i] += drow[k][i];
                    col[i] += dcol[k][i];
                }
            }
            if (!Rule.isLegal(row[0] + drow[k][0], col[0] + dcol[k][0])
                    || !Rule.isLegal(row[1] + drow[k][1], col[1] + dcol[k][1])) {
                degree += 1;
            } else if (board[row[0] + drow[k][0]][col[0] + dcol[k][0]] == enemy
                    && board[row[1] + drow[k][1]][col[1] + dcol[k][1]] == enemy) {
                degree += 1;
            }
        }
        return degree;
    }
}
