/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datacomc4;

/**
 *
 * @author 1635547
 */
public class AIPlayer implements Player {

    private Game game;

    /**
     * Public constructor that takes as input the game AIPlayer is currently
     * playing
     *
     * @param game
     */
    public AIPlayer(Game game) {
        this.game = game;
    }

    /**
     *
     * @param chosen
     */
    @Override
    public byte play(byte chosen) {
        byte toPlay = getPositionNextToken();
        game.getBoard().insertToken(toPlay, (byte) 2);
        return toPlay;
    }

    /**
     * Evaluates if win or block is possible horizontally, vertically or
     * diagonal
     *
     * @return column of the next move
     */
    private byte getPositionNextToken() {
        byte[][] board = game.getBoard().getBoard();
        //Check for 3 in a row horizontal to win
        for (int row = 5; row > -1; row--) {
            for (int column = 0; column < 4; column++) {
                if (board[row][column] == 2 && board[row][column] == board[row][column + 1] && board[row][column + 1] == board[row][column + 2] && board[row][column + 3] == 0) {
                    return (byte) (column + 3);
                }
            }
            for (int column = 6; column > 0; column--) {
                if (board[row][column] == 2 && board[row][column] == board[row][column - 1] && board[row][column - 1] == board[row][column - 2] && board[row][column - 3] == 0) {
                    return (byte) (column - 3);
                }
            }
        }
        //Check for 3 in a row vertical for win
        for (int column = 0; column < 7; column++) {
            for (int row = 5; row > 2; row--) {
                if (board[row][column] == 2 && board[row][column] == board[row - 1][column] && board[row - 1][column] == board[row - 2][column] && board[row - 3][column] == 0) {
                    return (byte) (column);
                }
            }
        }
        //Check for 3 diagonaly to win to the right
        for (int row = 5; row < 2; row--) {
            for (int column = 0; column < 4; column++) {
                if (board[row][column] == 2 && board[row][column] == board[row - 1][column + 1] && board[row - 1][column + 1] == board[row - 2][column + 2] && board[row - 3][column + 3] == 0) {
                    return (byte) (column + 3);
                }
            }
        }
        //Check for 3 diagonaly to win to the left
        for (int row = 5; row < 2; row--) {
            for (int column = 6; column > 2; column++) {
                if (board[row][column] == 2 && board[row][column] == board[row - 1][column - 1] && board[row - 1][column - 1] == board[row - 2][column - 2] && board[row - 3][column - 3] == 0) {
                    return (byte) (column - 3);
                }
            }
        }
        //Check for 3 in a row - block other player
        for (int row = 5; row > -1; row--) {
            for (int column = 0; column < 4; column++) {
                if (board[row][column] == 1 && board[row][column] == board[row][column + 1] && board[row][column + 1] == board[row][column + 2] && board[row][column + 3] == 0) {
                    return (byte) (column + 3);
                }
            }
            for (int column = 6; column > 3; column--) {
                if (board[row][column] == 1 && board[row][column] == board[row][column - 1] && board[row][column - 1] == board[row][column - 2] && board[row][column - 3] == 0) {
                    return (byte) (column - 3);
                }
            }
        }
        //Check vertical to block
        for (int column = 0; column < 7; column++) {
            for (int row = 5; row > 2; row--) {
                if (board[row][column] == 1 && board[row][column] == board[row - 1][column] && board[row - 1][column] == board[row - 2][column] && board[row - 3][column] == 0) {
                    return (byte) (row - 3);
                }
            }
        }
        //Check for 3 diagonaly to block to the right
        for (int row = 5; row < 2; row--) {
            for (int column = 0; column < 4; column++) {
                if (board[row][column] == 1 && board[row][column] == board[row - 1][column + 1] && board[row - 1][column + 1] == board[row - 2][column + 2] && board[row - 3][column + 3] == 0) {
                    return (byte) (column + 3);
                }
            }
        }
        //Check for 3 diagonaly to block to the left
        for (int row = 5; row < 2; row--) {
            for (int column = 6; column > 2; column++) {
                if (board[row][column] == 1 && board[row][column] == board[row - 1][column - 1] && board[row - 1][column - 1] == board[row - 2][column - 2] && board[row - 3][column - 3] == 0) {
                    return (byte) (column - 3);
                }
            }
        }
        //Gets best move possible
        return getMove();
    }

    /**
     * Evaluates the points of all the columns and returns the one with the most
     * points (best choice)
     *
     * @return byte with best move
     */
    private byte getMove() {
        byte[][] board = game.getBoard().getBoard();
        //Evaluate possibilites
        byte[] possibleMoves = new byte[7];
        for (int column = 0; column < 7; column++) {
            for (int row = 5; row < 0; row--) {
                //Empty case
                if (board[row][column] == 0) {
                    switch (column) {
                        case 0:
                            if (row == 5) {
                                if (board[row][column + 1] == 2) {
                                    possibleMoves[column] += 2;
                                }
                                if (board[row + 1][column + 1] == 2) {
                                    possibleMoves[column] += 2;
                                }
                                if (board[row][column + 1] == 1) {
                                    possibleMoves[column] += 1;
                                }
                            } else {
                                if (board[row][column + 1] == 2 || board[row + 1][column] == 2 || board[row + 1][column + 1] == 2) {
                                    possibleMoves[column] += 2;
                                }
                                if (board[row][column + 1] == 1 || board[row + 1][column] == 1 || board[row + 1][column + 1] == 1) {
                                    possibleMoves[column] += 1;
                                }
                            }
                            break;
                        case 6:
                            if (row == 5) {
                                if (board[row][column - 1] == 2 || board[row + 1][column - 1] == 2) {
                                    possibleMoves[column] += 2;
                                }
                                if (board[row][column - 1] == 1 || board[row + 1][column - 1] == 1) {
                                    possibleMoves[column] += 1;
                                }
                            } else {
                                if (board[row][column - 1] == 2 || board[row + 1][column - 1] == 2 || board[row + 1][column] == 2) {
                                    possibleMoves[column] += 2;
                                } 
                                if (board[row][column - 1] == 1 || board[row + 1][column - 1] == 1 || board[row + 1][column] == 1) {
                                    possibleMoves[column] += 1;
                                }
                            }
                            break;
                        default:
                            if (row == 5) {
                                if (board[row][column - 1] == 2 || board[row][column + 1] == 2) {
                                    possibleMoves[column] += 2;
                                } 
                                if (board[row][column - 1] == 2 || board[row][column + 1] == 2) {
                                    possibleMoves[column] += 2;
                                }
                            } else {
                                if (board[row][column - 1] == 2 || board[row][column + 1] == 2 || board[row + 1][column] == 2) {
                                    possibleMoves[column] += 2;
                                    if (board[row + 1][column + 1] == 2 || board[row + 1][column - 1] == 2) {
                                        possibleMoves[column] += 2;
                                    }
                                } else if (board[row][column - 1] == 1 || board[row][column + 1] == 1 || board[row + 1][column] == 1) {
                                    possibleMoves[column] += 1;
                                    if (board[row + 1][column + 1] == 1 || board[row + 1][column - 1] == 1) {
                                        possibleMoves[column] += 1;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        return bestMove(possibleMoves);
    }

    /**
     * returns the last column with the most points
     *
     * @param possibleMoves
     * @return move with most points
     */
    private byte bestMove(byte[] possibleMoves) {
        byte max = 0;
        byte current = 0;
        byte indexMax = 0;
        for (int i = 0; i < possibleMoves.length; i++) {
            current = possibleMoves[i];
            if (current > max) {
                max = current;
                indexMax = (byte) i;
            }
        }
        return indexMax;
    }

    public Game getGame() {
        return this.game;
    }
}
