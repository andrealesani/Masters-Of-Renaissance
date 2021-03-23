package model.resource;

import model.PlayerBoard;

public abstract class Resource {
    public void pickedFromMarket(PlayerBoard playerBoard) {}
    public void usedInProduction(PlayerBoard playerBoard) {}
    public void gainedInProduction(PlayerBoard playerBoard) {}
    //TODO
}
