package com.valenyala.chess.domain

import com.valenyala.chess.domain.enums.Color
import com.valenyala.chess.domain.enums.PieceType
import com.valenyala.chess.domain.enums.Result
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Board {
    lateinit var board: List<MutableList<Piece?>>

    lateinit var turn: Color

    var gameResult: Result? = null
    private var enPassantTarget: Position? = null

    init {
        setupBoard()
    }

    private fun changeTurn() {
        turn = when(turn) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
    }

    fun setupBoard() {
        turn = Color.WHITE
        gameResult = null
        enPassantTarget = null
        board =List(8) {
            MutableList(8) { null }
        }
// Pawns
        for (i in 0 until 8) {
            board[1][i] = Piece(PieceType.PAWN, Color.WHITE)
            board[6][i] = Piece(PieceType.PAWN, Color.BLACK)
        }

        // Rooks
        board[0][0] = Piece(PieceType.ROOK, Color.WHITE)
        board[0][7] = Piece(PieceType.ROOK, Color.WHITE)
        board[7][0] = Piece(PieceType.ROOK, Color.BLACK)
        board[7][7] = Piece(PieceType.ROOK, Color.BLACK)

        // Knights
        board[0][1] = Piece(PieceType.KNIGHT, Color.WHITE)
        board[0][6] = Piece(PieceType.KNIGHT, Color.WHITE)
        board[7][1] = Piece(PieceType.KNIGHT, Color.BLACK)
        board[7][6] = Piece(PieceType.KNIGHT, Color.BLACK)

        // Bishops
        board[0][2] = Piece(PieceType.BISHOP, Color.WHITE)
        board[0][5] = Piece(PieceType.BISHOP, Color.WHITE)
        board[7][2] = Piece(PieceType.BISHOP, Color.BLACK)
        board[7][5] = Piece(PieceType.BISHOP, Color.BLACK)

        // Queens
        board[0][3] = Piece(PieceType.QUEEN, Color.WHITE)
        board[7][3] = Piece(PieceType.QUEEN, Color.BLACK)

        // Kings
        board[0][4] = Piece(PieceType.KING, Color.WHITE)
        board[7][4] = Piece(PieceType.KING, Color.BLACK)
    }

    private fun isWithinBounds(position: Position): Boolean {
        return position.x in 0..7 && position.y in 0..7
    }

    private fun canMove(startPosition: Position, endPosition: Position): Boolean {
        if (!isWithinBounds(startPosition) || !isWithinBounds(endPosition)) {
            return false
        }

        val piece = board[startPosition.x][startPosition.y] ?: return false
        val target = board[endPosition.x][endPosition.y]

        if (piece.color != turn) return false

        if (!isValidMove(piece, startPosition, endPosition)) {
            return false
        }

        if (target != null && target.color == piece.color) {
            return false
        }

        if (isKingInCheckAfterMove(startPosition, endPosition, piece.color)) {
            println("Move puts king in check")
            return false
        }

        return true
    }

    fun movePiece(startPosition: Position, endPosition: Position, promotionChoice: PieceType? = null): Boolean {
        val canMove = canMove(startPosition, endPosition)

        if (!canMove) return false
        executeMove(startPosition, endPosition, promotionChoice)
        changeTurn()
        // check if the next player has possible moves, if not, declare the winner or it's stalemate (draw)
        checkFinishCondition()

        return true
    }

    private fun checkFinishCondition() {
        var hasPossibleMoves = false
        outerloop@ for (row in 0..7) {
            for (column in 0..7) {
                if (board[row][column]?.color == turn) {
                    hasPossibleMoves = checkPieceHasPossibleMoves(Position(row, column))
                    if (hasPossibleMoves) break@outerloop
                }
            }
        }

        if (!hasPossibleMoves) {
            gameResult = if (isKingInCheck(turn)) {
                when (turn) {
                    Color.WHITE -> Result.BLACK
                    Color.BLACK -> Result.WHITE
                }
            } else {
                Result.DRAW
            }
        }
    }

    private fun checkPieceHasPossibleMoves(startPosition: Position): Boolean {
        for (row in 0..7) {
            for (column in 0..7) {
                val targetPosition = Position(row, column)
                if (canMove(startPosition, targetPosition)) {
                    return true
                }
            }
        }
        return false
    }

    //if i got here then i'm sure i'm moving
    private fun executeMove(startPosition: Position, endPosition: Position, promotionChoice: PieceType? = null) {
        val (startX, startY) = startPosition
        val (endX, endY) = endPosition
        var piece = board[startX][startY]!!
        // Handle en passant capture
        if (piece.type == PieceType.PAWN && enPassantTarget == endPosition) {
            board[startX][endY] = null
        }

        // Handle castling
        if (piece.type == PieceType.KING && abs(startY - endY) == 2) {
            if (endY == 6) { // Kingside castling
                board[endX][5] = board[endX][7]
                board[endX][7] = null
            } else if (endY == 2) { // Queenside castling
                board[endX][3] = board[endX][0]
                board[endX][0] = null
            }
        }

        // Update en passant target
        enPassantTarget = if (piece.type == PieceType.PAWN && abs(startX - endX) == 2) {
            Position((startX + endX) / 2, endY)
        } else {
            null
        }

        // Check for promotion
        if (piece.type == PieceType.PAWN && (endX == 0 || endX == 7)) {
            piece = promotePawn(turn, promotionChoice!!)
        }

        // Execute the move
        board[endX][endY] = piece.apply { hasMoved = true }
        board[startX][startY] = null
    }

    private fun promotePawn(color: Color, promotionChoice: PieceType): Piece{
        // Promote to the chosen piece, default to Queen if null
        return Piece(promotionChoice, color)
    }

    private fun isValidMove(piece: Piece, startPosition: Position, endPosition: Position): Boolean {
        return when (piece.type) {
            PieceType.PAWN -> isValidPawnMove(piece, startPosition, endPosition)
            PieceType.ROOK -> isValidRookMove(startPosition, endPosition)
            PieceType.KNIGHT -> isValidKnightMove(startPosition, endPosition)
            PieceType.BISHOP -> isValidBishopMove(startPosition, endPosition)
            PieceType.QUEEN -> isValidQueenMove(startPosition, endPosition)
            PieceType.KING -> isValidKingMove(piece, startPosition, endPosition)
        }
    }

    private fun isValidPawnMove(
        piece: Piece,
        startPosition: Position,
        endPosition: Position
    ): Boolean {
        val (startX, startY) = startPosition
        val (endX, endY) = endPosition
        val direction = if (piece.color == Color.WHITE) 1 else -1
        val startRow = if (piece.color == Color.WHITE) 1 else 6

        if (endX == startX + direction && endY == startY && board[endX][endY] == null) {
            return true
        }

        if (endX == startX + 2 * direction && startX == startRow && endY == startY && board[endX][endY] == null && board[startX + direction][endY] == null) {
            return true
        }

        if (endX == startX + direction && (endY == startY + 1 || endY == startY - 1)) {
            if (board[endX][endY] != null && board[endX][endY]?.color != piece.color) {
                return true
            }
            if (enPassantTarget == Position(endX, endY)) {
                return true
            }
        }

        return false
    }

    private fun isValidRookMove(startPosition: Position, endPosition: Position): Boolean {
        val (startX, startY) = startPosition
        val (endX, endY) = endPosition
        if (startX != endX && startY != endY) return false
        if (startX == endX) {
            for (y in min(startY, endY) + 1 until max(startY, endY)) {
                if (board[startX][y] != null) return false
            }
        } else {
            for (x in min(startX, endX) + 1 until max(startX, endX)) {
                if (board[x][startY] != null) return false
            }
        }
        return true
    }

    private fun isValidKnightMove(startPosition: Position, endPosition: Position): Boolean {
        val dx = abs(startPosition.x - endPosition.x)
        val dy = abs(startPosition.y - endPosition.y)
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2)
    }

    private fun isValidBishopMove(startPosition: Position, endPosition: Position): Boolean {
        val (startX, startY) = startPosition
        val (endX, endY) = endPosition
        if (abs(startX - endX) != abs(startY - endY)) return false
        val dx = if (startX < endX) 1 else -1
        val dy = if (startY < endY) 1 else -1
        var x = startX + dx
        var y = startY + dy
        while (x != endX && y != endY) {
            val isWithinBounds = isWithinBounds(Position(x, y))
            if (!isWithinBounds || board[x][y] != null) return false

            x += dx
            y += dy
        }
        return true
    }

    private fun isValidQueenMove(startPosition: Position, endPosition: Position): Boolean {
        return isValidRookMove(startPosition, endPosition) || isValidBishopMove(startPosition, endPosition)
    }

    private fun isValidKingMove(
        piece: Piece,
        startPosition: Position,
        endPosition: Position
    ): Boolean {
        val (startX, startY) = startPosition
        val (endX, endY) = endPosition
        val dx = abs(startX - endX)
        val dy = abs(startY - endY)

        // Castling
        if (dx == 0 && dy == 2 && !piece.hasMoved) {
            val rookY = if (endY == 6) 7 else 0
            val rook = board[startX][rookY]
            if (rook?.type == PieceType.ROOK && !rook.hasMoved) {
                val pathClear = if (endY == 6) {
                    board[startX][5] == null && board[startX][6] == null
                } else {
                    board[startX][1] == null && board[startX][2] == null && board[startX][3] == null
                }
                if (pathClear && !isKingInCheck(piece.color)) {
                    return true
                }
            }
        }

        return dx <= 1 && dy <= 1
    }

    fun isKingInCheck(color: Color): Boolean {
        val (kingX, kingY) = findKingPosition(color)
        for (x in 0 until 8) {
            for (y in 0 until 8) {
                val piece = board[x][y]
                if (piece != null && piece.color != color) {
                    if (isValidMove(piece, Position(x, y), Position(kingX, kingY))) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun isKingInCheckAfterMove(
        startPosition: Position,
        endPosition: Position,
        color: Color
    ): Boolean {
        val (startX, startY) = startPosition
        val (endX, endY) = endPosition
        val tempPiece = board[endX][endY]
        board[endX][endY] = board[startX][startY]
        board[startX][startY] = null
        val inCheck = isKingInCheck(color)
        board[startX][startY] = board[endX][endY]
        board[endX][endY] = tempPiece
        return inCheck
    }

    fun findKingPosition(color: Color): Position {
        for (x in 0 until 8) {
            for (y in 0 until 8) {
                val piece = board[x][y]
                if (piece != null && piece.type == PieceType.KING && piece.color == color) {
                    return Position(x, y)
                }
            }
        }
        throw IllegalStateException("King not found on the board")
    }

    fun getPossibleMoves(position: Position): List<Position> {
        val possibleMoves = mutableListOf<Position>()

        for (row in 0..7) {
            for (column in 0..7) {
                val target = Position(row, column)
                if (canMove(position, target)) {
                    possibleMoves.add(target)
                }
            }
        }

        return possibleMoves
    }
}