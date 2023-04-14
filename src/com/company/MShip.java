package com.company;

class MShip {
    public int x;
    public int y;
    public int lng;
    public boolean horizon;
    public int hitted = 0;
    public boolean killed;
    public MShip (int x, int y, int lng, boolean horizon) {
        this.x = x;
        this.y = y;
        this.lng = lng;
        this.horizon = horizon;
        this.hitted = 0;
        this.killed =false;
    }
}
