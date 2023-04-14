package com.company;

import javafx.application.Application;

import java.util.ArrayList;

public class PlayerLogic extends Matrix {
    //public Matrix Ships;

    public PlayerLogic() {
        super();
    }
    public ArrayList <MShip> getShips() {
        return this.objArray;
    }
    public boolean canPutShipOn(int x, int y, int lng, boolean horizon)
    {
        if (horizon)
            for (int i = 0; i < lng; i++) {
                if (this.matrix[x][y + i] != 0)
                    return false;
            }
        else
            for (int i = 0; i < lng; i++) {
                if (this.matrix[x+1][y] != 0)
                    return false;
            }
        return true;
    }

}
