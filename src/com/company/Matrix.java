package com.company;

import java.util.ArrayList;
/*
 * для случайного расставления кораблей постороить массив из 100 чисел Случайное генерировать только для этого массива, из массива лишние удалять
 * */
class Matrix {
    protected int [][] matrix = new int[10][10];
    private int [][] hitsMatrix = new int[10][10];
    private int [][] objIndexMatrix = new int[10][10];
    //public MShip [] objArray = new MShip[10];
    protected ArrayList <MShip> objArray = new ArrayList<MShip>();
    protected int killedShipsCountByMe = 0;
    protected int killedShipsCountByEnemy = 0;
    private int [] _fillCoord = new int[2];
    protected boolean enemyCanShoot = true;
    public  Matrix() {
        this.Fill();
        this.hitsMatrix = this.matrix;
    }
    public int checkEnemyHit (int x, int y)//enemyHit
    {
        if (!this.enemyCanShoot)
            return 0;
        if (x < 0 || x > 9 || y < 0 || y > 9)
            return 0;
        Error.printStr("checkEnemyHit x="+x+",y="+y);

        if (this.hitsMatrix[x][y] != 1 && this.hitsMatrix[x][y] != 3) {
            this.enemyCanShoot = false;
            return 0;
        }
        if (this.objIndexMatrix[x][y] == 0) {
            this.enemyCanShoot = false;
            return 0;
        }

        MShip ship = this.objArray.get(this.objIndexMatrix[x][y]-1);
        if (this.hitsMatrix[x][y] != 3) {
            this.hitsMatrix[x][y] = 3;
            ship.hitted++;
            if (ship.hitted == ship.lng) {
                ship.killed = true;
                this.killedShipsCountByEnemy++;
            }
            this.objArray.set(this.objIndexMatrix[x][y]-1, ship);
        }
        if (ship.killed)
            return 3;
        return 1;
        /*
        this._botShootShipType = 0;
        if (this._checkUp(x, y) && this._checkRight(x, y) && this._checkDown(x, y) && this._checkLeft(x, y))
            return 3; // ubit
        return 1;
         */
    }

    public MShip getShip (int x, int y) {
        if (!this.existShip(x,y))
            return null;
        MShip ship = this.objArray.get(this.objIndexMatrix[x][y]-1);
        Error.printStr("getShip x="+ship.x + ", y=" +ship.y + ", killed="+(ship.killed ?1:0));
        if (!ship.killed)
            return null;
        return ship;
    }
    public boolean existShip (int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 9) {
            Error.errMsg("!existShip1 " + x +"x" +y);
            return false;
        }
        if (this.objIndexMatrix[x][y] == 0) {
            Error.errMsg("!existShip2 " + x +"x" +y);
            return false;
        }
        if (this.objIndexMatrix[x][y] > this.objArray.size()) {
            Error.errMsg("!existShip3 " + x +"x" +y+" i="+this.objIndexMatrix[x][y]);
            return false;
        }
        return true;
    }
    public boolean delShip(int x, int y, int lng, boolean horizon) {
        if (!this.existShip(x,y))
            return false;

        int ind = this.objIndexMatrix[x][y]-1;
        Error.printMatrix(this.objIndexMatrix,"objIndexMatrix before delShip ind="+ind);
        this.objArray.remove(ind);

        for (int i =0; i < 10; i++){
            for (int j =0; j < 10; j++){
                if ((this.objIndexMatrix[i][j]-1) > ind)
                    this.objIndexMatrix[i][j]--;
            }
        }

        if (horizon) {
            for (int i = 0; i < lng; i++) {
                this.matrix[x][y + i] = 0;
                this.objIndexMatrix[x][y + i] = 0;
            }
        } else {
            for (int i = 0; i < lng; i++) {
                this.matrix[x + i][y] = 0;
                this.objIndexMatrix[x + i][y] = 0;
            }
        }
        this._delFromMatrix2RoundShips();
        this.addToMatrix2RoundShips();
        Error.printMatrix(this.objIndexMatrix,"objIndexMatrix after delShip ind="+ind);
        Error.printMatrix(this.matrix,"matrix after delShip");
        return true;
    }
    public boolean addShip(int x, int y, int lng, boolean horizon) {
        if (!this._canAddShip( x,  y,  lng,  horizon))
            return false;
        this._shipCreate( x,  y,  lng,  horizon);
        addToMatrix2RoundOneShip( x,  y,  lng,  horizon);
        Error.printMatrix(this.matrix,"matrix after addShip");
        return true;
    }
    private boolean _canAddShip(int x, int y, int lng, boolean horizon) {
        if (x < 0 || x > 9 || y < 0 || y > 9) {
            Error.errMsg("!_canAddShip " + x +"x" +y);
            return false;
        }
        if (horizon) {
            if (y + lng - 1 > 9)
                return false;
            if (y > 0 && this.matrix[x][y -1] == 1)
                return false;
            if (y+ lng - 1 < 9 && this.matrix[x][y + lng] == 1)
                return false;
            for (int i = 0; i < lng; i++) {
                if (this.matrix[x][y + i] == 1)
                    return false;
            }
        } else {
            if (x + lng - 1 > 9)
                return false;
            if (x > 0 && this.matrix[x-1][y] == 1)
                return false;
            if (x+ lng - 1 < 9 && this.matrix[x+lng][y] == 1)
                return false;
            for (int i = 0; i < lng; i++) {
                if (this.matrix[x + i][y + i] == 1)
                    return false;
            }
        }
        return true;
    }
    private void _delFromMatrix2RoundShips() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (this.matrix[row][col] == 2 || this.matrix[row][col] == 4)
                    this.matrix[row][col] = 0;
            }
        }
    }
    public void addToMatrix2RoundShips() {
        for (int i = 0; i < this.objArray.size(); i++) {
            this.addToMatrix2RoundOneShip(this.objArray.get(i).x, this.objArray.get(i).y, this.objArray.get(i).lng, this.objArray.get(i).horizon);
        }
    }
    private void addToMatrix2RoundOneShip(int x, int y, int lng, boolean horizon) {
        if (horizon) {
            if (y > 0) {
                this.matrix[x][y - 1] = this._getVal2or4(this.matrix[x][y - 1]);
                if (x > 0)
                    this.matrix[x - 1][y - 1] = this._getVal2or4(this.matrix[x - 1][y - 1]);
                if (x < 9)
                    this.matrix[x + 1][y - 1] = this._getVal2or4(this.matrix[x + 1][y - 1]);
            }
            for (int j = 0; j < lng; j++) {
                if (x > 0)
                    this.matrix[x - 1][y + j] = this._getVal2or4(this.matrix[x - 1][y + j]);
                if (x < 9)
                    this.matrix[x + 1][y + j] = this._getVal2or4(this.matrix[x + 1][y + j]);
                //this.matrix[x][y + j] = 1;
            }
            if (y + lng < 10) {
                this.matrix[x][y + lng] = this._getVal2or4(this.matrix[x][y + lng]);
                if (x > 0)
                    this.matrix[x - 1][y + lng] = this._getVal2or4(this.matrix[x - 1][y + lng]);
                if (x < 9)
                    this.matrix[x + 1][y + lng] = this._getVal2or4(this.matrix[x + 1][y + lng]);
            }
        } else {
            if (x > 0) {
                this.matrix[x - 1][y] = this._getVal2or4(this.matrix[x - 1][y]);
                if (y > 0)
                    this.matrix[x - 1][y - 1] = this._getVal2or4(this.matrix[x - 1][y - 1]);
                if (y < 9)
                    this.matrix[x - 1][y + 1] = this._getVal2or4(this.matrix[x - 1][y + 1]);
            }
            for (int j = 0; j < lng; j++) {
                if (y > 0)
                    this.matrix[x + j][y - 1] = this._getVal2or4(this.matrix[x + j][y - 1]);
                if (y < 9)
                    this.matrix[x + j][y + 1] = this._getVal2or4(this.matrix[x + j][y + 1]);
                //this.matrix[x + j][y] = 1;
            }
            if (x + lng < 10) {
                this.matrix[x + lng][y] = this._getVal2or4(this.matrix[x + lng][y]);
                if (y > 0)
                    this.matrix[x + lng][y - 1] = this._getVal2or4(this.matrix[x + lng][y - 1]);
                if (y < 9)
                    this.matrix[x + lng][y + 1] = this._getVal2or4(this.matrix[x + lng][y + 1]);
            }
        }
    }
    private int _getVal2or4(int i)
    {
        return (i ==2 ? 4 : 2);
    }
    public boolean allMyShipsKilled () {
        return (this.killedShipsCountByEnemy > 9);
    }
    private void  Fill() {
        int type = (int) (Math.random() * 4);
        int side = (int) (Math.random() * 3);
        int cnt2 = 0;
        int cnt3 = 0;
        Error.printStr("type="+type);
        switch (type) {
            case 0://parallelno s protiv storon
            case 1://parallelno s odnoi storony v 2 ryada
                int row1 = (side == 0 || side == 3) ? 0 : 9;
                int row2 = (side == 0 || side == 3) ? 9 : 0;
                boolean horizon = (side == 0 || side == 2) ? true: false;
                while (!_setShip4x3ByEmptySide(side, 4)) {}
                if (_setShip3x2BySide(side, 3, this._fillCoord, 4))
                    cnt3++;
                if (type==0) {//parallelno s protiv storon
                    side = (side > 1 ? side - 2 : side + 2);
                    if (_setShip4x3ByEmptySide(side, 3))
                        cnt3++;
                    if (cnt3 < 2 && _setShip3x2BySide(side, 3, this._fillCoord, 4))
                        cnt3++;
                } else {//parallelno s odnoi storony v 2 ryada
                    row2 = (side == 0 || side == 3) ? 2 : 7;
                    if (_setShipByRow(row2, horizon, 3))
                        cnt3++;
                    if (cnt3 < 2 && _setShipByRow(row2, horizon ? true: false, 3))
                        cnt3++;
                }
                if (_setShipByRow(row1, horizon ? true: false, 2)) {
                    cnt2++;
                    if (_setShipByRow(row1, horizon ? true: false, 2))
                        cnt2++;
                }
                if (_setShipByRow(row2, horizon ? true: false, 2)) {
                    cnt2++;
                    if (_setShipByRow(row2, horizon ? true: false, 2))
                        cnt2++;
                }
                while (cnt3 < 2) {
                    if (_setShip3ByCenter())
                        cnt3++;
                }
                break;
            case 2:
                while (!_setShip4x3ByEmptySide(side, 4)) {}
                while (!_setShip3x2BySide(side, 2,this._fillCoord, 4)) {}
                cnt2 = 0;
                while (cnt2 < 2) {
                    if (_setShip3ByCenter())
                        cnt2++;
                }
                cnt2 = 1;
                break;
            default:
                while (!_setShip4x3ByEmptySide(side, 4)) {}
                while (!_setShip3x2BySide(side, 2,this._fillCoord, 4)) {}
                cnt2 = 1;
                for (int k = 0; k < 2; k++) {
                    side++;
                    side = (side > 3) ? 0 : side;
                    while (!_setShip4x3ByEmptySide(side,3)) {}
                    if (_setShip3x2BySide(side, 2, this._fillCoord, 3))//2x
                        cnt2++;
                }
                break;
        }
        while (cnt2 < 3) {
            if (_setShip2ByCenter())
                cnt2++;
        }
        cnt3 = 0;
        cnt2 = 0;
        while (cnt2 < 4 && cnt3 < 30) {
            if (_setShip1ByCenter())
                cnt2++;
            cnt3++;
        }
        if (cnt2 < 4)
            Error.errMsg("cnt1<4");

    }
    private boolean _setShipByRow (int row, boolean horizon, int shipLng)//universal
    {
        int x2 = 0;
        int y2 = 0;
        boolean go2 = false;
        int min = 0;
        int max = 0;
        boolean minSearch = true;
        if (horizon) {
            x2 = row;
            for (int i = 0; i < 10; i++) {
                if (minSearch) {
                    if (this.matrix[x2][i] == 1 || x2 > 0 && this.matrix[x2-1][i]==1  || x2 < 9 && this.matrix[x2+1][i]==1 )
                        min = (i+2>9?9:i+2);
                    else if (this.matrix[x2][min] != 1 && (x2 < 1 || this.matrix[x2-1][min] != 1)  && (x2 > 8 || this.matrix[x2+1][min]!=1))
                        minSearch = false;
                } else {
                    if (this.matrix[x2][i] != 1 && (x2 < 1 || this.matrix[x2 - 1][i] != 1) && (x2 > 8 || this.matrix[x2 + 1][i] != 1)){
                        max = (i == 9 ? i : i-1);
                    } else if ((min + shipLng -1) <= max) {
                        i = 10;
                    } else {
                        min = i + 2;
                        minSearch = true;
                    }
                }
                //Error.printStr("horizon row=" + row +", i="+i +", "+min +" "+max);
            }
        } else {
            y2 = row;
            for (int i = 0; i < 10; i++) {
                if (minSearch) {
                    if (this.matrix[i][y2] == 1 || y2 > 0 && this.matrix[i][y2-1]==1  || y2 < 9 && this.matrix[i][y2+1]==1 )
                        min = (i+2>9?9:i+2);
                    else if (this.matrix[min][y2] != 1 && (y2 < 1 || this.matrix[min][y2-1] != 1)  && (y2 > 8 || this.matrix[min][y2+1]!=1))
                        minSearch = false;
                } else {
                    if (this.matrix[i][y2] != 1 && (y2 < 1 || this.matrix[i][y2 - 1] != 1) && (y2 > 8 || this.matrix[i][y2 + 1] != 1)){
                        max = (i == 9 ? i : i-1);
                    } else if ((min + shipLng -1) <= max) {
                        i = 10;
                    } else {
                        min = i + 2;
                        minSearch = true;
                    }
                }
            }
        }
        max = max-shipLng+1;
        int xy = 0;
        if (max == min) {
            xy = min;
            go2 = true;
        } else if (max > min){
            xy = (int) (Math.random() * (max - min + 1)) + min;
            go2 = true;
        }
        if (go2) {
            //Error.printStr("_setShipByRow " + x2 +" "+y2);
            if (horizon)
                this._shipCreate(x2, xy, shipLng, horizon);
            else
                this._shipCreate(xy, y2, shipLng, horizon);
        }
        if (go2)
            return true;
        return false;
    }
    private void _shipCreate(int x, int y, int shipLng, boolean horizon) {
        MShip ship = new MShip(x,y,shipLng,horizon);
        this.objArray.add(ship);
        int xy = (horizon ? y : x);
        for (int i = 0; i < shipLng; i++) {
            //System.out.println(x2+" "+y2);
            if (horizon) {
                this.matrix[x][xy] = 1;
                this.objIndexMatrix[x][xy] = this.objArray.size();
            } else {
                this.matrix[xy][y] = 1;
                this.objIndexMatrix[xy][y] = this.objArray.size();
            }
            xy++;
        }
    }
    private boolean _setShip4x3ByEmptySide(int side, int shipLng)//row must be empty
    {
        int x = 0;
        int y = 0;
        int max = 10-shipLng;
        int min = 0;
        switch (side) {
            case 1:
                y = 9;
                break;
            case 2:
                x = 9;
                break;
        }
        switch (side) {
            case 0://hor
            case 2:
                if (this.matrix[x][0] == 1 || this.matrix[(x==0?1:8)][0]==1 )
                    min +=2;
                if (this.matrix[x][9]==1 ||  this.matrix[(x==0?1:8)][9]==1 )
                    max -=2;
                y = (int) (Math.random()*(max - min + 1)) + min;
                this._shipCreate(x, y, shipLng, true);
                break;
            default: //vert
                if (this.matrix[0][y]==1 || this.matrix[0][(y==0?1:8)]==1 )
                    min +=2;
                if (this.matrix[9][0]==1 || this.matrix[9][(y==0?1:8)]==1 )
                    max -=2;
                x = (int) (Math.random()*(max - min + 1)) + min;
                this._shipCreate(x, y, shipLng, false);
                break;
        }
        this._fillCoord[0] = x;
        this._fillCoord[1] = y;
        return true;
    }
    private boolean _setShip3x2BySide(int side, int shipLng, int[] prevShipCord, int prevShipLng)//need prevShipCord
    {
        int x2 = 0;
        int y2 = 0;
        boolean go2 = false;
        switch (side) {
            //side
            case 1:
                y2 = 9;
                break;
            case 2:
                x2 = 9;
                break;
        }
        int min = 0;
        int max = 10-shipLng;
        switch (side) {
            //side
            case 0:
            case 2:
                if (this.matrix[x2][0] == 1 || this.matrix[(x2==0?1:8)][0]==1 )
                    min += 2; //zanita pervaia kletka
                max =prevShipCord[1] - (shipLng+1);
                if (max == min) {
                    y2 = min;
                    go2 = true;
                } else if (max > min){
                    y2 = (int) (Math.random() * (max - min + 1)) + min;//-3 mesto na 2x korabl, -first mesto na pervyu zanyatu kletku
                    go2 = true;
                } else {
                    min = (prevShipCord[1] + prevShipLng + 1);
                    max = 10-shipLng;
                    if (this.matrix[x2][9] == 1 || this.matrix[(x2==0?1:8)][9]==1 )
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
                    this._shipCreate(x2, y2, shipLng, true);
                }
                break;
            case 1:
            case 3:
                if (this.matrix[0][y2] == 1 || this.matrix[0][(y2==0?1:8)]==1)
                    min += 2; //zanita pervaia kletka
                max = prevShipCord[0] - (shipLng+1);
                if (min == max) {
                    x2 = min;
                    go2 = true;
                } else if (max > min) {
                    x2 = (int) (Math.random() * (max - min + 1)) + min;//-3 mesto na 2x korabl, -first mesto na pervyu zanyatu kletku
                    go2 = true;
                } else {
                    min = prevShipCord[0] + prevShipLng + 1;
                    max = 10-shipLng; //last
                    if (this.matrix[9][y2] == 1 || this.matrix[9][(y2==0?1:8)] == 1)
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
                    this._shipCreate(x2, y2, shipLng, false);
                }
                break;
        }
        if (go2)
            return true;
        return false;
    }
    private boolean _setShip1ByCenter()//universal
    {
        int x = (int) (Math.random()*(9 - 0 + 1)) + 0;
        int y = (int) (Math.random()*(9 - 0 + 1)) + 0;
        //Error.printStr("_setShip1ByCenter " + x + ","+y);
        if (this.matrix[x][y]!=0 || y > 0 && this.matrix[x][y-1]!=0 || y < 9 && this.matrix[x][y+1]!=0
                || x > 0 && this.matrix[x-1][y]!=0 || x > 0 && y > 0 && this.matrix[x-1][y-1]!=0 || x > 0 && y < 9 && this.matrix[x-1][y+1]!=0
                || x < 9 && this.matrix[x+1][y]!=0 || x < 9 && y > 0 && this.matrix[x+1][y-1]!=0 || x < 9 && y < 9 && this.matrix[x+1][y+1]!=0)
            return false;
        this._shipCreate(x, y, 1, true);
        return true;
    }
    private boolean _setShip2ByCenter()//universal
    {
        int x = (int) (Math.random()*(7 - 2 + 1)) + 2;
        int y = (int) (Math.random()*(7 - 2 + 1)) + 2;
        Error.printStr("_setShip2ByCenter " + x + ","+y);
        if (this.matrix[x][y]!=0 || this.matrix[x][y-1]!=0 || this.matrix[x][y+1]!=0
                || this.matrix[x-1][y]!=0 || this.matrix[x-1][y-1]!=0 || this.matrix[x-1][y+1]!=0
                || this.matrix[x+1][y]!=0 || this.matrix[x+1][y-1]!=0 || this.matrix[x+1][y+1]!=0)
            return false;
        if (this.matrix[x+2][y-1]==0 && this.matrix[x+2][y]==0 && this.matrix[x+2][y+1]==0) {
            this._shipCreate(x, y, 2, false);
            return true;
        }
        if (this.matrix[x][y+2]==0 && this.matrix[x-1][y+2]==0 && this.matrix[x+1][y+2]==0) {
            this._shipCreate(x, y, 2, true);
            return true;
        }
        if (this.matrix[x-2][y-1]==0 && this.matrix[x-2][y]==0 && this.matrix[x-2][y+1]==0 ) {
            this._shipCreate(x-1, y, 2, false);
            return true;
        }
        if (this.matrix[x][y-2]==0 && this.matrix[x-1][y-2]==0 && this.matrix[x+1][y-2]==0) {
            this._shipCreate(x, y-1, 2, true);
            return true;
        }
        Error.printStr("_setShip2ByCenter Falls" + x + ","+y);
        return false;
    }
    private boolean _setShip3ByCenter()//universal
    {
        int x = (int) (Math.random()*(9 - 0 + 1)) + 0;
        int y = (int) (Math.random()*(9 - 0 + 1)) + 0;
        Error.printStr("_setShip3ByCenter "+x+","+y);
        if (this.matrix[x][y]!=0 || y > 0 && this.matrix[x][y-1]!=0 || y < 9 && this.matrix[x][y+1]!=0 || x > 0 && this.matrix[x-1][y]!=0
                || x > 0 && y > 0 && this.matrix[x-1][y-1]!=0 || x > 0 && y < 9 && this.matrix[x-1][y+1]!=0 || x < 9 && this.matrix[x+1][y]!=0
                || x < 9 && y > 0 && this.matrix[x+1][y-1]!=0 || x < 9 && y < 9 &&this.matrix[x+1][y+1]!=0)
            return false;
        if (x > 2 && y > 0 && y < 9
                && this.matrix[x-2][y-1]==0 && this.matrix[x-2][y]==0 && this.matrix[x-2][y+1]==0
                && this.matrix[x-3][y-1]==0 && this.matrix[x-3][y]==0 && this.matrix[x-3][y+1]==0 ) {
            this._shipCreate(x-2, y, 3, false);
            return true;
        }
        if (x < 7  && y > 0 && y < 9
                && this.matrix[x+2][y-1]==0 && this.matrix[x+2][y]==0 && this.matrix[x+2][y+1]==0
                && this.matrix[x+3][y-1]==0 && this.matrix[x+3][y]==0 && this.matrix[x+3][y+1]==0) {
            this._shipCreate(x, y, 3, false);
            return true;
        }
        if (y > 2 && x > 0 && x < 9
                && this.matrix[x][y-2]==0 && this.matrix[x-1][y-2]==0 && this.matrix[x+1][y-2]==0
                && this.matrix[x][y-3]==0 && this.matrix[x-1][y-3]==0 && this.matrix[x+1][y-3]==0) {
            this._shipCreate(x, y-2, 3, true);
            return true;
        }
        if (y < 7 && x > 0 && x < 9
                && this.matrix[x][y+2]==0 && this.matrix[x-1][y+2]==0 && this.matrix[x+1][y+2]==0
                && this.matrix[x][y+3]==0 && this.matrix[x-1][y+3]==0 && this.matrix[x+1][y+3]==0) {
            this._shipCreate(x, y, 3, true);
            return true;
        }
        return false;
    }
//---------------
/*
    public boolean __checkUp (int row, int col)
    {
      if (row == 0)
            return true;
        if (this.hitsMatrix[row-1][col] == 0 )
            return true;
        if (this.hitsMatrix[row-1][col] == 1 || this.hitsMatrix[row-1][col] == 3)
            this._botShootShipType = 1;//vert
        if (this.hitsMatrix[row-1][col] == 1)
            return false;
        if (this.hitsMatrix[row-1][col] == 3)
            return this.__checkUp(row-1, col);
        Error.errMsg("_checkUp");
        return false;
    }
    public boolean __checkRight (int row, int col)
    {
        if (this._botShootShipType == 1)
            return true;
        if (col == 9)
            return true;
        if (this.hitsMatrix[row][col+1] == 0 )
            return true;
        if (this.hitsMatrix[row][col+1] == 1 || this.hitsMatrix[row][col+1] == 3)
            this._botShootShipType = 2;//hor
        if (this.hitsMatrix[row][col+1] == 1)
            return false;
        if (this.hitsMatrix[row][col+1] == 3)
            return this.__checkRight(row, col+1);
        Error.errMsg("_checkUp");
        return false;
    }
    public boolean __checkDown (int row, int col)
    {
        if (this._botShootShipType == 2)
            return true;
        if (row == 9)
            return true;
        if (this.hitsMatrix[row+1][col] == 0 )
            return true;
        if (this.hitsMatrix[row+1][col] == 1 || this.hitsMatrix[row+1][col] == 3)
            this._botShootShipType = 1;//vert
        if (this.hitsMatrix[row+1][col] == 1)
            return false;
        if (this.hitsMatrix[row+1][col] == 3)
            return this.__checkDown(row+1, col);
        Error.errMsg("_checkUp");
        return false;
    }
    public boolean __checkLeft (int row, int col)
    {
        if (this._botShootShipType == 1)
            return true;
        if (col == 0)
            return true;
        if (this.hitsMatrix[row][col-1] == 0 )
            return true;
        if (this.hitsMatrix[row][col-1] == 1 || this.hitsMatrix[row][col-1] == 3)
            this._botShootShipType = 2;//hor
        if (this.hitsMatrix[row][col-1] == 1)
            return false;
        if (this.hitsMatrix[row][col-1] == 3)
            return this.__checkLeft(row, col-1);
        Error.errMsg("_checkUp");
        return false;
    }
*/
}