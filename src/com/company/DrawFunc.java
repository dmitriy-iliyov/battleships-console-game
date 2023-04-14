package com.company;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;

public class DrawFunc extends Application{
    private final int fildsCount = 10;
    private final int size = 30;
    private final int [] ships = {0, 4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    private TextArea debug;
    private Rectangle [][] botCells = new Rectangle[10][10];
    private Rectangle [][] playerCells = new Rectangle[10][10];
    private Ship [][] playerShips = new Ship[10][10];
    private PlayerLogic player;
    private BotLogic bot;
    private Group PlayerCellsGroup;
    private boolean Fight = false;


    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void init() throws Exception {
        super.init();
        this.player = new PlayerLogic();
        this.player.addToMatrix2RoundShips();
        this.bot = new BotLogic();
        bot.setPlayer(player);
    }


    @Override
    public void start(Stage stage) {
        Group playerField = this.buildField();
        Group enemyField = this.buildEnemyField();

        HBox center = new HBox();
        center.setPadding(new Insets(15, 12, 15, 12));
        center.setSpacing(10.0);
        center.setAlignment(Pos.BASELINE_CENTER);
        center.getChildren().add(playerField);
        center.getChildren().add(enemyField);

        this.debug = new TextArea();
        this.debug.setPrefWidth(800);
        this.debug.setEditable(false);
        //this.debug.setDisable(true);
        this.debug.setPrefHeight(20);
        this.debug.setWrapText(true);
        this.debug.setScrollLeft(Double.MAX_VALUE);


        //VBox vbox = new VBox(10);
        //VBox.setVgrow(this.debug, Priority.ALWAYS);
        //this.debug.setMaxHeight(20);
        //this.debug.setMaxWidth(400);
        //VBox.getChildren().addAll(this.debug);
        //this.debug.setX(0);
        //this.debug.setY(0);

        HBox control = new HBox();
        control.setPrefHeight(40F);
        control.setMaxWidth(800);
        control.setAlignment(Pos.BASELINE_CENTER);
        control.setPadding(new Insets(-5,5,0,5));
        control.getChildren().add(this.debug);


        BorderPane border = new BorderPane();
        border.setCenter(center);
        border.setBottom(control);

        stage.setScene(new Scene(border, 700, 400));
        stage.setTitle("Battle Ships Menu");
        stage.setResizable(false);
        stage.show();

        Button _start = new Button("Start");
        control.getChildren().addAll(_start);
        border.setBottom(control);

        //this.BotShips = new Matrix();
        //Error.printMatrix(this.BotShips.matrix);
        this.buildPlayerFlotilla(playerField);
    }


    private Group buildField() {
        String[] numbers = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] letters = {" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        int defult = this.size / 3;
        Group field1 = new Group();
        this.PlayerCellsGroup = new Group();
        this.PlayerCellsGroup.setLayoutX(this.size);
        this.PlayerCellsGroup.setLayoutY(this.size);
        for (int x = 0; x < this.fildsCount; x++) {
            field1.getChildren().add(new Text(defult, (x+1) * this.size + 2 * defult, numbers[x+1]));
            field1.getChildren().add(new Text((x+1) * this.size + defult, 2 * defult, letters[x+1]));
            for (int y = 0; y < this.fildsCount; y++) {
                this.PlayerCellsGroup.getChildren().add(makeCell(x, y));
            }
        }
        field1.getChildren().add(this.PlayerCellsGroup);
        return field1;
    }


    private Rectangle makeCell(int x, int y) {
        Rectangle cell = new Rectangle();
        this.playerCells[y][x] = cell;
        cell.setX(x * this.size);
        cell.setY(y * this.size);
        cell.setWidth(this.size);
        cell.setHeight(this.size);
        cell.setFill(Color.WHITE);
        cell.setStroke(Color.BLACK);
        return cell;
    }


    private Group buildEnemyField() {
        String [] numbers = {" ","1","2","3","4","5","6","7","8","9","10"};
        String [] letters = {" ","A","B","C","D","E","F","G","H","I","J"};
        int defult = this.size/3;
        Group field1 = new Group();
        Group field2 = new Group();
        field2.setLayoutX(this.size);
        field2.setLayoutY(this.size);
        for(int x = 0; x < this.fildsCount; x++) {
            field1.getChildren().add(new Text(defult,(x+1)*this.size + 2*defult, numbers[x+1]));
            field1.getChildren().add(new Text((x+1)*this.size + defult, 2*defult, letters[x+1]));
            for(int y = 0; y < this.fildsCount; y++) {
                field2.getChildren().add(makeEnemyCell(x,y,field2));
            }
        }
        field1.getChildren().add(field2);
        return field1;
    }


    private Rectangle makeEnemyCell(int x, int y, Group field) {
        Rectangle cell = new Rectangle();
        this.botCells[y][x] = cell;
        cell.setX(x*this.size);
        cell.setY(y*this.size);
        cell.setWidth(this.size);
        cell.setHeight(this.size);
        cell.setFill(Color.WHITE);
        cell.setStroke(Color.BLACK);
        cell.setOnMousePressed(
                event -> {
                    this.Fight=true;
                    int a = this.bot.checkHit(y,x);
                    switch (a) {
                        case 0:
                            field.getChildren().add(makeMissShoot(cell));
                            this.botShoot();
                            break;
                        case 1:
                            cell.setFill(Color.web(String.valueOf(Color.YELLOW), 0.1));
                            field.getChildren().add(makeHitedShip(cell.getX(), cell.getY(), this.size));
                            break;
                        case 3:
                            cell.setFill(Color.web(String.valueOf(Color.YELLOW), 0.1));
                            field.getChildren().add(makeHitedShip(cell.getX(), cell.getY(), this.size));
                            MShip ship = this.bot.getShip(y,x);//objArray.get(this.BotShips.objIndexMatrix[y][x]);
                            if (ship.killed) {
                                Rectangle cell1;
                                for (int i = 0; i < ship.lng; i++) {
                                    if (ship.horizon) {
                                        cell1 = this.botCells[ship.x][ship.y + i];
                                    } else {
                                        cell1 = this.botCells[ship.x + i][ship.y];
                                    }
                                    cell1.setFill(Color.web(String.valueOf(Color.WHITE), 0.1));
                                    cell1.setStroke(Color.RED);
                                    cell1.setStrokeWidth(1.5);
                                    cell1.toFront();
                                    this.pushToDebug("KILLED " + this.bot.getKilledShipsCount() + " SHIPS!!!");
                                }
                                if (this.bot.allShipsKilled()) {
                                    Alert alert = new Alert(AlertType.INFORMATION);
                                    alert.setTitle("Player killed all ships");
                                    alert.setContentText("Player killed all ships");
                                    alert.showAndWait();
                                    this.pushToDebug("KILLED 10 SHIPS!!!");
                                }

                            }
                            break;
                    }
                }
        );
        return cell;
    }


    private Group makeHitedShip(double x, double y, int size) {
        Group group = new Group();
        int shift = this.size - 1;
        Line line1 = new Line(x + shift, y + shift, x + size - shift, y + size - shift);
        line1.setStroke(Color.RED);
        line1.setStrokeWidth(1.5);
        Line line2 = new Line(x + size - shift, y + shift, x + shift, y + size - shift);
        line2.setStroke(Color.RED);
        line2.setStrokeWidth(1.5);
        group.getChildren().addAll(line1,line2);
        return group;
    }


    private Group makeMissShoot(Rectangle cell) {
        Group group = new Group();
        cell.setFill(Color.web(String.valueOf(Color.GRAY), 0.1));
        group.getChildren().add(cell);
        group.getChildren().add(pointInCell(cell));
        return group;
    }


    private Circle pointInCell(Rectangle cell) {
        double centerX = cell.getX() + (cell.getWidth() / 2);
        double centerY = cell.getY() + (cell.getHeight() / 2);
        Circle point = new Circle();
        point.setCenterX(centerX);
        point.setCenterY(centerY);
        point.setRadius(2);
        return point;
    }


    private Rectangle makeKilledShip(double x, double y) {
        Rectangle cell = new Rectangle();
        cell.setX(x);
        cell.setY(y);
        cell.setStroke(Color.RED);
        cell.setStrokeWidth(1.5);
        cell.toFront();
        return cell;
    }


    private Group makeAreaAroundKilledShip(Rectangle cell) {
        Group group = new Group();
        double x = cell.getX();
        double y = cell.getY();

        cell.setFill(Color.web(String.valueOf(Color.GRAY), 0.1));
        group.getChildren().add(pointInCell(cell));
        return group;
    }


    private void botShoot(){
        int[] res = this.bot.botShoot();
        this.pushToDebug("botShoot res=" + res[0]+ ", x="+res[1]+", y="+res[2] + ", killedShips=" + res[3]);
        switch (res[0]) {
            case 0:
                //this.pushToDebug(" case0");
                this.playerCells[res[1]][res[2]].setFill(Color.web(String.valueOf(Color.DARKGRAY), 0.1));
                this.PlayerCellsGroup.getChildren().add(pointInCell(this.playerCells[res[1]][res[2]]));

//               this.playerCells[res[1]][res[2]].setFill(Color.web(String.valueOf(Color.DARKGRAY)));

                break;
            case 1:
                //this.pushToDebug(" case1");
//                this.playerCells[res[1]][res[2]].setFill(Color.web(String.valueOf(Color.LIGHTCORAL)));
                this.playerCells[res[1]][res[2]].setFill(Color.web(String.valueOf(Color.YELLOW), 0.1));
                this.PlayerCellsGroup.getChildren().add(makeHitedShip(this.playerCells[res[1]][res[2]].getX(),this.playerCells[res[1]][res[2]].getY(), this.size));
                //this.playerShips[res[1]][res[2]].setFill(Color.web(String.valueOf(Color.DARKGRAY)));
                this.botShoot();
                break;
            case 3:
                this.playerCells[res[1]][res[2]].setFill(Color.web(String.valueOf(Color.YELLOW), 0.1));
                this.PlayerCellsGroup.getChildren().add(makeHitedShip(this.playerCells[res[1]][res[2]].getX(),this.playerCells[res[1]][res[2]].getY(), this.size));
                MShip ship = this.player.getShip(res[1],res[2]);
                for (int i = 0; i < ship.lng; i++) {
                    if (ship.horizon){
                        this.playerCells[ship.x][ship.y + i].setFill(Color.web(String.valueOf(Color.WHITE), 0.1));
                        this.playerCells[ship.x][ship.y + i].setStroke(Color.RED);
                        this.playerCells[ship.x][ship.y + i].setStrokeWidth(1.5);
                        this.playerCells[ship.x][ship.y + i].toFront();
//                        this.playerCells[ship.x][ship.y + i].setFill(Color.web(String.valueOf(Color.RED)));
                    }
                    else{
                        this.playerCells[ship.x + i][ship.y].setFill(Color.web(String.valueOf(Color.WHITE), 0.1));
                        this.playerCells[ship.x + i][ship.y].setStroke(Color.RED);
                        this.playerCells[ship.x + i][ship.y].setStrokeWidth(1.5);
                        this.playerCells[ship.x + i][ship.y].toFront();
//                        this.playerCells[ship.x + i][ship.y].setFill(Color.web(String.valueOf(Color.RED)));
                    }
                }
                if (res[3] > 9) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Bot killed all ships");
                    //alert.setHeaderText("Bot killed all ships");
                    alert.setContentText("Bot killed all ships");
                    alert.showAndWait();
                }
                //this.pushToDebug(" case2");
                //this.playerShips[res[1]][res[2]].setFill(Color.web(String.valueOf(Color.RED)));
                this.botShoot();
                break;
        }
    }


    private void buildPlayerFlotilla(Group flotilla) {
        ArrayList <MShip>  Ships = this.player.getShips();
        for (int i = 0; i < Ships.size(); i++) {
            flotilla.getChildren().add(this.buildPlayerShip(Ships.get(i).lng, (Ships.get(i).y + 1) * this.size,
                    (Ships.get(i).x + 1) * this.size, Ships.get(i).horizon, Ships.get(i).x, Ships.get(i).y));
        }
    }


    private Rectangle buildPlayerShip(int deck, int startX, int startY, boolean horizon, int x, int y) {
        Ship ship = new Ship(this.size, deck, startX, startY, horizon, x, y);
        this.playerShips[x][y] = ship;
        ship.setOnMouseDragged(
                event -> {
                    if (!this.Fight) {
                        double x1 = Math.round((event.getX() - ship.shiftX) / this.size) * this.size;
                        if (x1 < this.size)
                            x1 = this.size;
                        else if (x1 + this.size * (ship.horizon ? ship.deck : 1) > this.size * 11)
                            x1 = this.size * 11 - this.size * (ship.horizon ? ship.deck : 1);
                        double y1 = Math.round((event.getY() - ship.shiftY) / this.size) * this.size;
                        if (y1 < this.size)
                            y1 = this.size;
                        else if (y1 + this.size * (ship.horizon ? 1 : ship.deck) > this.size * 11)
                            y1 = this.size * 11 - this.size * (ship.horizon ? 1 : ship.deck);
                        if (this.canPut ((int)x1, (int)y1, ship.deck, ship.horizon)) {
                            ship.setX(x1);
                            ship.setY(y1);
                            ship.setStroke(Color.GREEN);
                            ship.setFill(Color.web(String.valueOf(Color.GREEN), 0.1));
                        } else {
                            if (((event.getX() - ship.shiftX) + this.size * (ship.horizon ? ship.deck : 1)) < this.size * 11
                                    && ((event.getX() - ship.shiftX)) > this.size)
                                ship.setX(event.getX() - ship.shiftX);
                            if (((event.getY() - ship.shiftY) + this.size * (ship.horizon ? 1 : ship.deck)) < this.size * 11 && ((event.getY() - ship.shiftY)) > this.size)
                                ship.setY(event.getY() - ship.shiftY);
                            ship.setFill(Color.web(String.valueOf(Color.BLUE), 0.1));
                            ship.setStroke(Color.BLUE);
                        }
                    }
                }
        );
        ship.setOnMouseReleased(
                event -> {
                    if (!this.Fight) {
                        double x1 = Math.round((event.getX() - ship.shiftX) / this.size) * this.size;
                        if (x1 < this.size)
                            x1 = this.size;
                        else if (x1 + this.size * (ship.horizon ? ship.deck : 1) > this.size * 11)
                            x1 = this.size * 11 - this.size * (ship.horizon ? ship.deck : 1);
                        double y1 = Math.round((event.getY() - ship.shiftY) / this.size) * this.size;
                        if (y1 < this.size)
                            y1 = this.size;
                        else if (y1 + this.size * (ship.horizon ? 1 : ship.deck) > this.size * 11)
                            y1 = this.size * 11 - this.size * (ship.horizon ? 1 : ship.deck);
                        ship.setX(x1);
                        ship.setY(y1);
                        ship.setFill(Color.web(String.valueOf(Color.BLUE), 0.1));
                        ship.setStroke(Color.BLUE);
                        //ship.setY(((int)(event.getY() / this.size)) * this.size);

                        double x2 = y1 / this.size - 1;
                        double y2 = x1 / this.size - 1;
                        ship.x = (int) x2;
                        ship.y = (int) y2;
                        this.addToMatrixShip(ship.x, ship.y, ship.deck, ship.horizon);
                        //Error.printMatrix(this.BotShips.matrix);
                    }
                }
        );
        ship.setOnMouseClicked(
                event -> {
                    if (!this.Fight) {
                        //if (event.getButton() != MouseButton.PRIMARY) {
                        Rectangle momento = new Rectangle(ship.getX(), ship.getY(), ship.getWidth(), ship.getHeight());
                        ship.setWidth(momento.getHeight());
                        ship.setHeight(momento.getWidth());
                    }
                }
        );
        ship.setOnMousePressed(
                event -> {
                    if (!this.Fight) {
                        ship.shiftX = event.getX() - ship.getX();
                        ship.shiftY = event.getY() - ship.getY();
                        ship.setStroke(Color.GREEN);
                        ship.setFill(Color.web(String.valueOf(Color.GREEN), 0.1));
                        this.delFromMatrixShip(ship.x, ship.y, ship.deck, ship.horizon);
                        //this.addToMatrix2ForOneShip(ship.x, ship.y, ship.deck, ship.horizon, 0);
                    }
                }
        );
        return ship;
    }


    private boolean canPut(int x, int y, int lng, boolean horizon) {
        //Error.printStr("x="+(y/this.size-1)+", y="+(x/this.size-1));
        int x1 = y / this.size - 1;
        int y1 = x / this.size - 1;
        return this.player.canPutShipOn(x1, y1, lng, horizon);
    }


    private void addToMatrixShip(int x, int y, int lng, boolean horizon) {
        this.player.addShip(x, y, lng, horizon);
    }


    private void delFromMatrixShip(int x, int y, int lng, boolean horizon) {
        this.player.delShip( x,  y,  lng,  horizon);
    }


    private void pushToDebug(String txt){
        this.debug.setText(this.debug.getText() + "\n" + txt);
        this.debug.setScrollTop(Double.MAX_VALUE);//
    }


    private void setDebug(String txt){
        this.debug.setText(txt);
        this.debug.setScrollTop(Double.MAX_VALUE);
    }
}
