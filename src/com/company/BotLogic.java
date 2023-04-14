package com.company;
import java.lang.Math;
import java.util.ArrayList;

class BotLogic extends Matrix {
    private int [][] shootsMatrix = new int[10][10];
    private boolean shipHitted = false;
    private int hittedShipType = 0;//1-vert; 2-hor
    private int [] hittedShipPoint = new int[2];
    private int [] shootedShipPoint = new int[2];
    //private int killedShipsCountByMe = 0;
    private boolean continueShooting = false;
    private ArrayList <int []> pointsForShoot4x4 = new ArrayList <int []>();
    private ArrayList <int []> pointsForShoot3x3_2x2 = new ArrayList <int []>();
    private ArrayList <int []> pointsForShoot1x1 = new ArrayList <int []>();
    public PlayerLogic Player;
    //private Matrix botShips;
    public BotLogic() {
        super();
        //this.botShips = new Matrix();
        this._pointsForShootCreate();
//        Error.printMatrix(this.botShips.matrix,"_botShipsMatrix");
    }
    public int getKilledShipsCount(){
        return this.killedShipsCountByEnemy;
    }
    public void setPlayer(PlayerLogic player) {
        this.Player = player;
    }
    private void _pointsForShootCreate()
    {
        this.pointsForShoot4x4 = this._pointsForShootXxXCreate(3, 4);
        this.pointsForShoot3x3_2x2 = this._pointsForShootXxXCreate(1, 4);
        this.pointsForShoot1x1 = this._pointsForShootXxXCreate(0, 2);
    }
    private ArrayList <int []> _pointsForShootXxXCreate(int start, int step)
    {
        //int [][] tmp = new int[10][10];
        ArrayList <int []> alist = new ArrayList <int []>();
        for (int col = 0; col < 10; col++) {
            int row = start;
            while (row < 10) {
                alist.add(new int[]{row,col});
                //tmp[row][col] = 1;
                row += step;//!!!
            }
            start --;
            start = (start < 0) ? (step-1) : start;//!!!
        }
        //Error.printMatrix(tmp);
        return alist;
    }
    public int [] botShoot()
    {
        int[] res = {0,0,0,0};
        if (this.killedShipsCountByMe > 9) {
            Error.printStr("_botKilledShips > 9");
            return res;
        }
        if (this.shipHitted) {
            if (!this._botShootAfterHitting())
                return this.botShoot();
        } else if (!this._botShootBy4x4() && !this._botShootBy3x3_2x2()) {
            Error.printStr("!this._botShootBy4x4() && !this._botShootBy3x3_2x2()");
            this._botShootBy1x1();
        }
        res[1] = this.shootedShipPoint[0];
        res[2] = this.shootedShipPoint[1];
        res[3] = this.killedShipsCountByMe;
        if (!this.continueShooting)
            return res;
        if (!this.shipHitted)
            res[0]=3;
        else
            res[0]=1;
        return res;

//        while (this.continueShooting) {
//            Error.printStr("this._botContinueShooting == true");
//            this.botShoot();
//        }
//        Error.printMatrix(this.shootsMatrix, "shootsMatrix");
    }
    public boolean _botShootAfterHitting()
    {
        Error.printStr("_botShootAfterHitting x=" + this.hittedShipPoint[0] + ", y="+this.hittedShipPoint[1]);
        if (this.shipHitted) {
            if (this._shootUp(this.hittedShipPoint[0], this.hittedShipPoint[1]))
                return true;
            Error.errMsg("_botShootAfterHitting");
        }
        return false;
    }
    private boolean _botShootBy4x4()
    {
        Error.printStr("_botShootBy4x4");
        int lengthArrayList = this.pointsForShoot4x4.size();
        if (lengthArrayList < 1)
            return false;
        lengthArrayList--;
        int indexArrayList = (int) (Math.random()*(lengthArrayList - 0 + 1)) + 0;
        int [] cord = this.pointsForShoot4x4.get(indexArrayList);
        this.pointsForShoot4x4.remove(indexArrayList);
        if (this.shootsMatrix[cord[0]][cord[1]] == 0) {
            return this._shootByPoint(cord[0], cord[1]);
            //System.out.println(cord[0]+","+cord[1]);
        }
        return this._botShootBy4x4();
    }
    private boolean _botShootBy3x3_2x2()
    {
        Error.printStr("_botShootBy3x3_2x2");
        int lengthArrayList = this.pointsForShoot3x3_2x2.size();
        if (lengthArrayList < 1)
            return false;
        lengthArrayList--;
        int indexArrayList = (int) (Math.random()*(lengthArrayList - 0 + 1)) + 0;
        int [] cord = this.pointsForShoot3x3_2x2.get(indexArrayList);
        this.pointsForShoot3x3_2x2.remove(indexArrayList);
        if (this.shootsMatrix[cord[0]][cord[1]] == 0) {
            return this._shootByPoint(cord[0], cord[1]);
            //System.out.println(cord[0]+","+cord[1]);
        }
        return this._botShootBy3x3_2x2();
    }
    private boolean _botShootBy1x1()
    {
        Error.printStr("_botShootBy1x1");
        int lengthArrayList = this.pointsForShoot1x1.size();
        if (lengthArrayList < 1)
            return false;
        lengthArrayList--;
        int indexArrayList = (int) (Math.random()*(lengthArrayList - 0 + 1)) + 0;
        int [] cord = this.pointsForShoot1x1.get(indexArrayList);
        this.pointsForShoot1x1.remove(indexArrayList);
        if (this.shootsMatrix[cord[0]][cord[1]] == 0) {
            return this._shootByPoint(cord[0], cord[1]);
            //System.out.println(cord[0]+","+cord[1]);
        }
        return this._botShootBy1x1();
    }
    private boolean _shootByPoint( int x, int y)
    {
        Error.printStr("_shootByPoint x=" + x + ", y="+y);
        this.shootedShipPoint[0] = x;
        this.shootedShipPoint[1] = y;
        //Error.printStr("_shootByPoint "+x+","+y);
        if (this.shootsMatrix[x][y] != 0) {
            Error.errMsg("_shootByPoint");
            return false;
        }
        int res = this.Player.checkHit(x,y);
        Error.printStr("bot Hit by "+x+"x"+y+"="+res);
        if (res == 3) {//ubit
            this.shootsMatrix[x][y] = 1;//popal
            Error.printMatrix(this.shootsMatrix,"shootsMatrix before");
            MShip ship = this.Player.getShip(x,y);
            this._markShipBorder(ship.x, ship.y, ship.lng, ship.horizon);
            Error.printMatrix(this.shootsMatrix,"shootsMatrix after");

            this.shipHitted = false;
            this.continueShooting = true;//prodolzhaem streliat
            this.hittedShipType = 0;
            this.hittedShipPoint[0] = 0;
            this.hittedShipPoint[1] = 0;
            this.killedShipsCountByMe++;
            Error.printStr("killedShipsCountByMe =" + killedShipsCountByMe);
        } else if (res == 1) {//ranil
            this.shootsMatrix[x][y] = 1;//popal
            this.shipHitted = true;
            this.continueShooting = true;//prodolzhaem streliat
            if (this.hittedShipType == 0) {
                Error.printArray(this.shootedShipPoint);
                Error.printArray(this.hittedShipPoint);
                if (this.shootedShipPoint[0] == this.hittedShipPoint[0] && Math.abs(this.shootedShipPoint[1] - this.hittedShipPoint[1]) == 1)
                    this.hittedShipType = 2;//hor
                else if (this.shootedShipPoint[1] == this.hittedShipPoint[1] && Math.abs(this.shootedShipPoint[0] - this.hittedShipPoint[0]) == 1)
                    this.hittedShipType = 1;//vert
            }
            this.hittedShipPoint[0] = x;
            this.hittedShipPoint[1] = y;

            //Error.printArray(this.hittedShipPoint);
        } else {
            this.shootsMatrix[x][y] = 2;//ne popal
            this.continueShooting = false;
        }
        return true;
    }
    private void _markShipBorder (int row, int col, int lng, boolean horizon)
    {
        if (horizon) {
            int col1 = col > 0 ? col - 1 : col;
            int col2 = col + lng;
            col = col1;
            col2 = col2 > 9 ? 9 : col2;
            if (row > 0) {
                while (col1 <= col2) {
                    if (this.shootsMatrix[row][col1] != 1)
                        this.shootsMatrix[row][col1] = 2;
                    this.shootsMatrix[row-1][col1] = 2;
                    col1++;
                }
            }
            if (row < 9) {
                col1 = col;
                while (col1 <= col2) {
                    if (this.shootsMatrix[row][col1] != 1)
                        this.shootsMatrix[row][col1] = 2;
                    this.shootsMatrix[row+1][col1] = 2;
                    col1++;
                }
            }
        } else {
            int row1 = row > 0 ? row - 1 : row;
            int row2 = row + lng;
            row = row1;
            row2 = row2 > 9 ? 9 : row2;
            if (col > 0) {
                while (row1 <= row2) {
                    if (this.shootsMatrix[row1][col] != 1)
                        this.shootsMatrix[row1][col] = 2;
                    this.shootsMatrix[row1][col - 1] = 2;
                    row1++;
                }
            }
            if (col < 9) {
                row1 = row;
                while (row1 <= row2) {
                    if (this.shootsMatrix[row1][col] != 1)
                        this.shootsMatrix[row1][col] = 2;
                    this.shootsMatrix[row1][col + 1] = 2;
                    row1++;
                }
            }
        }
    }
    private void _markShipBorderVert(int row, int col, boolean up) {
        if (col > 0) {
            this.shootsMatrix[row][col-1] = 2;
            this.shootsMatrix[(up ? row-1 : row+1)][col-1] = 2;
        }
        if (col < 9) {
            this.shootsMatrix[row][col+1] = 2;
            this.shootsMatrix[(up ? row-1 : row+1)][col+1] = 2;
        }
    }
    private void _markShipBorderHor(int row, int col, boolean right) {
        if (row > 0) {
            this.shootsMatrix[row-1][col] = 2;
            this.shootsMatrix[row-1][(right ? col+1 : col-1)] = 2;
        }
        if (row < 9) {
            this.shootsMatrix[row+1][col] = 2;
            this.shootsMatrix[row+1][(right ? col+1 : col-1)] = 2;
        }
    }
    public boolean _shootUp(int x, int y)
    {
        Error.printStr("_shootUp x=" + x + ", y="+y+", type="+this.hittedShipType);
        if (this.hittedShipType == 2 || x == 0)
            return this._shootRight(x,y);//idti vpravo
        if (this.shootsMatrix[x-1][y] == 1) {
            this.hittedShipType = 1;//vert
            this._markShipBorderVert(x, y, true);
            x--;
            return this._shootUp(x,y);// idti vverh || srazu vniz
        }
        if (this.shootsMatrix[x -1][y] == 2) //tam net korabla
            return this._shootRight(x,y);//idem vpravo
        return this._shootByPoint(x-1, y);//streliaem
    }
    public boolean _shootRight(int x, int y)
    {
        Error.printStr("_shootRight x=" + x + ", y="+y+", type="+this.hittedShipType);
        if (this.hittedShipType == 1 || y == 9) //this._botShipHittedType = 1 - vertic
            return this._shootDown(x,y);//idti down
        if (this.shootsMatrix[x][y+1] == 1) {
            this.hittedShipType = 2;//hor
            this._markShipBorderHor(x, y, true);
            y++;
            return this._shootRight(x,y);//ili vlevo
        }
        if (this.shootsMatrix[x][y+1] == 2)//tam net korabla
            return this._shootDown(x,y);//idem vniz
        return this._shootByPoint(x, y+1);//streliaem
    }
    public boolean _shootDown(int x, int y)
    {
        Error.printStr("_shootDown x=" + x + ", y="+y+", type="+this.hittedShipType);
        if (this.hittedShipType == 2 || x == 9) //this._botShipHittedType = 2 - horiz
            return this._shootLeft(x,y);
        if (this.shootsMatrix[x+1][y] == 1) {
            this.hittedShipType = 1;//vert
            this._markShipBorderVert(x, y, false);
            x++;
            return this._shootDown(x,y);
        }
        if (this.shootsMatrix[x+1][y] == 2)
            return this._shootLeft(x,y);//left
        return this._shootByPoint(x+1, y);//streliaem
    }
    public boolean _shootLeft(int x, int y)
    {
        Error.printStr("_shootLeft x=" + x + ", y="+y+", type="+this.hittedShipType);
        if (this.hittedShipType == 1 || y == 0)
            return this._shootUp(x,y);
        if (this.shootsMatrix[x][y-1] == 1) {
            this.hittedShipType = 2;
            this._markShipBorderHor(x, y, false);
            y--;
            return this._shootLeft(x,y);//ili vlevo
        }
        if (this.shootsMatrix[x][y-1] == 2)
            return this._shootUp(x,y);
        return this._shootByPoint(x, y-1);//streliaem
    }
}

