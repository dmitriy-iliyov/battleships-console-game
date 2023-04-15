package com.company;

import javafx.application.Application;

import java.util.ArrayList;

public class PlayerLogic extends Matrix {
    //public Matrix Ships;

    public PlayerLogic()
    {
        super();
    }
    public int playerShoot(int x, int y, BotLogic Bot)
    {
        int res = Bot.checkEnemyHit(x,y);
        if (res == 0)
            this.enemyCanShoot = true;
        return res;
    }
    public ArrayList <MShip> getShips()
    {
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
    public boolean canTurnShipOn(int x, int y, int lng, boolean horizon)
    {
        Error.printStr("canTurnShipOn "+x+"x"+y+" hor=" +horizon);
        Error.printMatrix(this.matrix);
        if (horizon) {
            if (this.matrix[x + 1][y] != 2)
                return false;
            for (int i = 2; i < lng; i++) {
                if (this.matrix[x + i][y] != 0)
                    return false;
            }
        } else {
            if (this.matrix[x][y + 1] != 2)
                return false;
            for (int i = 2; i < lng; i++) {
                if (this.matrix[x][y + i] != 0)
                    return false;
            }
        }
        return true;
    }

}