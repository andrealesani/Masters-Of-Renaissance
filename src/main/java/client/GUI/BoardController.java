package client.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

//TODO possibilità di togliere risorse nei depot
public class BoardController {
    @FXML
    public Button addResourceFromMarketButton;
    @FXML
    public ImageView firstDepot;
    @FXML
    public ImageView firstResourceSecondDepot;
    @FXML
    public ImageView secondResourceSecondDepot;
    @FXML
    public ImageView firstResourceThirdDepot;
    @FXML
    public ImageView secondResourceThirdDepot;
    @FXML
    public ImageView thirdResourceThirdDepot;
    @FXML
    public Button coinButton;
    @FXML
    public Button servantButton;
    @FXML
    public Button stoneButton;
    @FXML
    public Button shieldButton;
    @FXML
    public Label currentPlayerLabel;
    @FXML
    public ImageView firstPopeFT;
    @FXML
    public ImageView secondPopeFT;
    @FXML
    public ImageView thirdPopeFT;
    @FXML
    public Button firstDepotButton;
    @FXML
    public Button secondDepotButton;
    @FXML
    public Button thirdDepotButton;
    @FXML
    public Button RemoveResourceFromWarehouseButton;
    @FXML
    public ImageView firsLevel1Card;
    @FXML
    public ImageView secondLevel1Card;
    @FXML
    public ImageView thirdLevel1Card;


    public void setGameTable(URL location, ResourceBundle resources) {

        coinButton.setOpacity(0);
        servantButton.setOpacity(0);
        stoneButton.setOpacity(0);
        shieldButton.setOpacity(0);
        firstDepotButton.setOpacity(0);
        secondDepotButton.setOpacity(0);
        thirdDepotButton.setOpacity(0);

    }

    public void addResourceFromMarket(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setOpacity(0);

        coinButton.setOpacity(100);
        servantButton.setOpacity(100);
        stoneButton.setOpacity(100);
        shieldButton.setOpacity(100);

    }

    public void addCoin(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/coin.png");
        firstDepot.setImage(image);

        servantButton.setOpacity(0);
        stoneButton.setOpacity(0);
        shieldButton.setOpacity(0);
        addResourceFromMarketButton.setOpacity(100);
    }

    public void addServant(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/servant.png");
        firstResourceSecondDepot.setImage(image);
        secondResourceSecondDepot.setImage(image);

        coinButton.setOpacity(0);
        stoneButton.setOpacity(0);
        shieldButton.setOpacity(0);
        addResourceFromMarketButton.setOpacity(100);
    }

    public void addStone(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        coinButton.setOpacity(0);
        servantButton.setOpacity(0);
        shieldButton.setOpacity(0);
        addResourceFromMarketButton.setOpacity(100);
    }

    public void addShield(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/shield.png");
        firstResourceThirdDepot.setImage(image);
        secondResourceThirdDepot.setImage(image);
        thirdResourceThirdDepot.setImage(image);

        coinButton.setOpacity(0);
        servantButton.setOpacity(0);
        stoneButton.setOpacity(0);
        addResourceFromMarketButton.setOpacity(100);
    }

    public void activateFirstPopeFT(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/firstPopeFT.png");
        firstPopeFT.setImage(image);
    }

    public void discardFirstPopeFT(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/quadrato giallo.png");
        firstPopeFT.setImage(image);
    }

    public void activateSecondPopeFT(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/secondPopeFT.png");
        secondPopeFT.setImage(image);
    }

    public void discardSecondPopeFT(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/quadrato arancione.png");
        secondPopeFT.setImage(image);
    }

    public void activateThirdPopeFT(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/thirdPopeFT.png");
        thirdPopeFT.setImage(image);
    }


    public void discardThirdPopeFT(ActionEvent actionEvent){
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/punchboard/quadrato rosso.png");
        thirdPopeFT.setImage(image);
    }

    public void showMarket(ActionEvent actionEvent){
    }

    public void removeResourceFromWarehouse(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        firstDepotButton.setOpacity(100);
        secondDepotButton.setOpacity(100);
        thirdDepotButton.setOpacity(100);
    }

    public void removeResourcesFD(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        secondDepotButton.setOpacity(0);
        thirdDepotButton.setOpacity(0);

        firstDepot.setOpacity(0);
        RemoveResourceFromWarehouseButton.setDisable(false);
    }

    public void removeResourcesSD(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        firstDepotButton.setOpacity(0);
        thirdDepotButton.setOpacity(0);

        firstResourceSecondDepot.setOpacity(0);
        secondResourceSecondDepot.setOpacity(0);
        RemoveResourceFromWarehouseButton.setDisable(false);
    }

    public void removeResourcesTD(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);
        firstDepotButton.setOpacity(0);
        secondDepotButton.setOpacity(0);

        firstResourceThirdDepot.setOpacity(0);
        secondResourceThirdDepot.setOpacity(0);
        thirdResourceThirdDepot.setOpacity(0);
        RemoveResourceFromWarehouseButton.setDisable(false);
    }

    public void addDevelopmentCard1(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.png");
        firsLevel1Card.setImage(image);

    }

    public void addDevelopmentCard2(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-2-1.png");
        secondLevel1Card.setImage(image);
    }

    public void addDevelopmentCard3(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        button.setDisable(true);

        Image image = new Image("/graphics/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-3-1.png");
        thirdLevel1Card.setImage(image);
    }
}

