package com.company;
import java.lang.Math;

class LogicFunc {
    private int [][] _randMatrix = new int[10][10];
    private int [][] _shootMatrix = new int[10][10];


    public LogicFunc() {
        //4x
        int i = (int) (Math.random()*3);
        int [] cord4 = _setShipBySide(i,4);
        int j = cord4[0];
        //3x
        for(int k = 0; k < 3; k++)
        {
            j++;
            j = (j > 3)?0:j;
            int [] cord3 = _setShipBySide(j,3);
            //2x
            _setShip2BySide(cord3, 3);
        }
        _setShip2BySide(cord4, 4);
        //1x
        _set1Ship();
        _printMatrix();

    }


    public void Shoot4x4()
    {
        int max = 3;
        int min = 0;
        int x1;
        for(int y1 = 0; y1 < 10; y1++)
            {
                x1 = (int) (Math.random()*(3 - 0 + 1)) + 0;
                min = y1 - 3;
                if (min < 0)
                    min = 0;
                for (int y2 = min; y2 < y1; y2++) {
                    if (this._shootMatrix[x1][y2] == 1) {
                        x1 = (int) (Math.random()*(3 - 0 + 1)) + 0;
                        y2 = min-1;
                    }
                }
                this._pushShootValue(x1,y1,1);
            }
        _printShootMatrix();
    }


    private void _pushShootValue(int x, int y, int v)
    {
        this._shootMatrix[x][y] = v;
    }


    private void _set1Ship()
    {
        int count = 0;
        while (count < 4)
        {
            int y = (int) (Math.random()*(6))+2;
            int x = (int) (Math.random()*(6))+2;
            if( this._randMatrix[x][y] == 0)
            {
                this._pushValue(x,y,1);
//                _pushValue(x-1,y+1,2);
//                _pushValue(x-1,y,2);
//                _pushValue(x-1,y-1,2);
//                _pushValue(x+1,y+1,2);
//                _pushValue(x+1,y,2);
//                _pushValue(x+1,y-1,2);
//                _pushValue(x,y-1,2);
//                _pushValue(x,y+1,2);
                count++;
            }
        }
    }


    private void _setShip2BySide(int[] cord3, int lenght)
    {
        //2x
        int x2 = 0;
        int y2 = 0;
        int cnt2 = 0;
        boolean go2 = false;
        switch (cord3[0]) {
            //side
            case 1:
                x2 = 9;
                break;
            case 2:
                y2 = 9;
                break;
        }

        int min = 0;
        int max = 8;
        switch (cord3[0]) {
            //side
            case 0:
            case 2:
                if (this._randMatrix[0][y2] == 1 || this._randMatrix[0][(y2==0?1:8)]==1 )
                    min += 2; //zanita pervaia kletka
                max =cord3[1] - 3;
                if (max == min) {
                    x2 = min;
                    go2 = true;
                } else if (max > min){
                    x2 = (int) (Math.random() * (max - min + 1)) + min;//-3 mesto na 2x korabl, -first mesto na pervyu zanyatu kletku
                    go2 = true;
                } else {
                    min = (cord3[1] + lenght + 1);
                    max = 8;
                    if (this._randMatrix[9][y2] == 1 || this._randMatrix[9][(y2==0?1:8)]==1 )
                        max -= 2; //zanita last kletka
                    if (min == max) {
                        x2 = min;
                        go2 = true;
                    } else if (min < max) {//3 konez korabla 3x, 7=9-2x
                        x2 = (int) (Math.random() * (max - min + 1)) + min;//lenght+1 konez 3x korabl s probelom, -first mesto na last zanyatu kletku
                        go2 = true;
                    }
                }
                if (go2) {
                    for (int k = 0; k < 2; k++) {
                        //System.out.println(x2+" "+y2);
                        this._pushValue(x2, y2, 1);
                        x2++;
                    }
                    cnt2++;
                }
                break;
            case 1:
            case 3:
                if (this._randMatrix[x2][0] == 1 || this._randMatrix[(x2==0?1:8)][0]==1)
                    min += 2; //zanita pervaia kletka
                max = cord3[2] - 3;
                if (min == max) {
                    y2 = min;
                    go2 = true;
                } else if (max > min) {
                    y2 = (int) (Math.random() * (max - min + 1)) + min;//-3 mesto na 2x korabl, -first mesto na pervyu zanyatu kletku
                    go2 = true;
                } else {
                    min = cord3[2] + lenght + 1;
                    max = 8; //last
                    if (this._randMatrix[x2][9] == 1 || this._randMatrix[(x2==0?1:8)][9] == 1)
                        max -= 2; //zanita last kletka
                    if (min == max) {
                        y2 = min;
                        go2 = true;
                    } else if (min < max) {//3 konez korabla 3x, 7=9-2x
                        y2 = (int) (Math.random() * (max - min + 1)) + min;//lenght+1 konez 3x korabl s probelom, -first mesto na last zanyatu kletku
                        go2 = true;
                    }
                }
                if (go2) {
                    for (int k = 0; k < 2; k++) {
                        this._pushValue(x2, y2, 1);
                        y2++;
                    }
                    cnt2++;
                }
                break;
            //???? if menshe 4 korablia ???
        }

    }


    private int[] _setShipBySide(int side, int lenght)
    {
        int x = 0;
        int y = 0;
        int max = 10-lenght;
        int min = 0;
        switch (side)
        {
            case 1:
                x = 9;
                break;
            case 2:
                y = 9;
                break;
        }
        switch (side)
        {
            case 0:
            case 2:
                if (this._randMatrix[0][y]==1 || this._randMatrix[0][(y==0?1:8)]==1 )
                    min +=2;
                if (this._randMatrix[9][y]==1 ||  this._randMatrix[9][(y==0?1:8)]==1 )
                    max -=2;
                x = (int) (Math.random()*(max - min + 1)) + min;
                for(int k = 0; k < lenght; k++)
                {
                    this._pushValue(x,y,1);
                    x++;
                }
                x -= lenght;
                break;
            case 1:
            case 3:
                if (this._randMatrix[x][0]==1 || this._randMatrix[(x==0?1:8)][0]==1 )
                    min +=2;
                if (this._randMatrix[x][9]==1 || this._randMatrix[(x==0?1:8)][9]==1 )
                    max -=2;
                y = (int) (Math.random()*(max - min + 1)) + min;
                for(int k = 0; k < lenght; k++)
                {
                    this._pushValue(x,y,1);
                    y++;
                }
                y -= lenght;
                break;
        }
        return new int [] {side, x, y};
    }


    private void _pushValue(int x, int y, int v)
    {
       this._randMatrix[x][y] = v;
    }


    private void _printMatrix()
    {
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                System.out.print(this._randMatrix[j][i] + "  ");
            }
            System.out.println();
        }
    }


    private void _printShootMatrix()
    {
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                System.out.print(this._shootMatrix[j][i] + "  ");
            }
            System.out.println();
        }
    }
}

