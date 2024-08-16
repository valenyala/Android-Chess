package com.valenyala.chess.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.valenyala.chess.domain.Board
import com.valenyala.chess.domain.Piece
import com.valenyala.chess.domain.Position
import com.valenyala.chess.domain.enums.Color
import com.valenyala.chess.domain.enums.PieceType
import com.valenyala.chess.domain.enums.Result

class ChessboardViewModel : ViewModel() {
    private val _chessBoard: Board = Board()

    private var _turn: Color by mutableStateOf(_chessBoard.turn)

    val turn: Color
        get() = _turn

    private var _showPromotionCard by mutableStateOf(false)

    val showPromotionCard: Boolean
        get() = _showPromotionCard

    private lateinit var _startPosition: Position
    private lateinit var _endPosition: Position

    private var _pieceToPromote: PieceType? = null

    private var _gameResult: Result? by mutableStateOf(_chessBoard.gameResult)

    val gameResult: Result?
        get() = _gameResult

    private var _possibleMoves = emptyList<Position>().toMutableStateList()

    val possibleMoves: List<Position>
        get() = _possibleMoves

    private var _checkedKingPosition: Position? by mutableStateOf(null)

    val checkedKingPosition: Position?
        get() = _checkedKingPosition

    private var _boardState =
        _chessBoard.board.toMutableStateList().let { it.map { list -> list.toMutableStateList() } }

    val boardState: List<List<Piece?>>
        get() = _boardState

    private var _selectedPosition: Position? by mutableStateOf(null)

    val selectedPosition
        get() = _selectedPosition

    fun selectPieceToPromote(pieceType: PieceType) {
        _pieceToPromote = pieceType
        _showPromotionCard = false

        movePiece(
            _startPosition,
            _endPosition
        )
    }

    fun showPromotionCard() {
        _showPromotionCard = true
    }

    fun hasSelectedPosition(): Boolean {
        return _selectedPosition != null
    }

    fun selectPosition(position: Position) {
        val pieceToSelect = _chessBoard.board[position.x][position.y]
        if (pieceToSelect == null || pieceToSelect.color != _chessBoard.turn) {
            _selectedPosition = null
            cleanPossibleMoves()
            return
        }

        _selectedPosition = position
        updatePossibleMoves(position)
    }

    fun saveMove(startPosition: Position, endPosition: Position) {
        _startPosition = startPosition
        _endPosition = endPosition
    }

    fun movePiece(
        startPosition: Position,
        endPosition: Position,
    ): Boolean {
        if (showPromotionCard) return false
        val moved = _chessBoard.movePiece(startPosition, endPosition, _pieceToPromote)
        if (moved) {
            updateBoard()
            updateCheckHint()
            checkFinished()
        }
        _pieceToPromote = null
        cleanPossibleMoves()
        return moved
    }

    private fun updateBoard() {
        for (row in 0..7) {
            for (col in 0..7) {
                val boardPiece = _chessBoard.board[row][col]
                _boardState[row][col] = boardPiece
            }
        }

        _turn = _chessBoard.turn
    }

    private fun cleanPossibleMoves() {
        _selectedPosition = null
        _possibleMoves.clear()
    }

    private fun updatePossibleMoves(position: Position) {
        val possibleMoves = _chessBoard.getPossibleMoves(position)

        _possibleMoves.clear()
        _possibleMoves.addAll(possibleMoves)
    }

    private fun updateCheckHint() {
        val turn = _chessBoard.turn

        _checkedKingPosition = if (_chessBoard.isKingInCheck(turn)) {
            _chessBoard.findKingPosition(turn)
        } else {
            null
        }
    }

    private fun checkFinished() {
        _gameResult = _chessBoard.gameResult
    }

    fun resetGame() {
        _chessBoard.setupBoard()
        _gameResult = null
        _possibleMoves = emptyList<Position>().toMutableStateList()
        _checkedKingPosition = null
        updateBoard()
    }
}