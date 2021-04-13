package model.resource;

import model.PlayerBoard;
import model.ResourceType;

public abstract class Resource {
    private final ResourceType type;

    public void addResourceFromMarket(PlayerBoard playerBoard) {}

    public void addResourceFromProduction(PlayerBoard playerBoard) {}

    public Resource() { type = null;}
    public Resource(ResourceType type) { this.type = type;}

    public ResourceType getType(){
        return type;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Resource){
            if(((Resource) obj).getType() == type)
                return true;
        }
        return false;
    }
}
