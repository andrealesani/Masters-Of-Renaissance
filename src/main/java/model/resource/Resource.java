package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public abstract class Resource {
    private final ResourceType type;

    public void addResourceFromMarket(PlayerBoard playerBoard) {}

    public void addResourceFromProduction(PlayerBoard playerBoard) {}

    public Resource() {
        this.type = null;
    }

    public ResourceType getType(){
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Resource){
            if(((Resource) obj).type == type)
                return true;
        }
        return false;
    }
}
