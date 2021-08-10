package com.ilongross.seafight.models;

import java.util.ArrayList;

public class Ship {

    private int size;
    private boolean isVertical;
    private ArrayList<int[]> shipCells = new ArrayList<>();

    public Ship() {
        this.size = 0;
    }

    public Ship(int size, int[] startCell, boolean isVertical) {
        this.size = size;
        this.isVertical = isVertical;
        this.shipCells.add(startCell);
    }

    public void removeCell(int[] cell) {
        shipCells.remove(cell);
    }

    public void removeCell(int cellNum) {
        shipCells.remove(cellNum);
    }

    public boolean isEmpty() {
        return shipCells.isEmpty();
    }

    public void addCell(int[] cell) {
        shipCells.add(cell);
    }

    public int[] getCell(int index) {
        return shipCells.get(index);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }

    public ArrayList<int[]> getShipCells() {
        return shipCells;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int[] cell : shipCells) {
            sb.append("[");
            sb.append(cell[0]).append(",").append(cell[1]);
            sb.append("]");
        }
        return "Ship{" +
                "size=" + size +
                ", isVertical=" + isVertical +
                ", shipCells=" + sb +
                '}';
    }
}
