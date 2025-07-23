package com.LicentaBTA.app.chess.utils;

import com.LicentaBTA.app.chess.models.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveHistory {
    private final List<Move> history = new ArrayList<>();
    private int currentIndex = 0;

    public void recordMove(Move move) {
        // Elimina mutarile viitoare daca s-a facut o mutare noua dupa undo
        while (history.size() > currentIndex) {
            history.removeLast();
        }
        history.add(move);
        currentIndex++;
    }

    public boolean canUndo() {
        return currentIndex > 0;
    }

    public boolean canRedo() {
        return currentIndex < history.size();
    }

    public Move undoMove() {
        if (!canUndo()) return null;
        currentIndex--;
        return history.get(currentIndex);
    }

    public Move redoMove() {
        if (!canRedo()) return null;
        return history.get(currentIndex++);
    }

    public void reset() {
        history.clear();
        currentIndex = 0;
    }

    public Move getLastMove() {
        if (currentIndex == 0) return null;
        return history.get(currentIndex - 1);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public Move getMoveAt(int index) {
        if (index < 0 || index >= history.size()) return null;
        return history.get(index);
    }

    public int getSize() {
        return history.size();
    }

    public boolean hasPrevious() {
        return canUndo();
    }

    public boolean hasNext() {
        return canRedo();
    }
}
