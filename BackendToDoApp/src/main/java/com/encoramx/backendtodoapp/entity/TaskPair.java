package com.encoramx.backendtodoapp.entity;

public class TaskPair<L, R> {

    private final L left;
    private final R right;

    public TaskPair (L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
