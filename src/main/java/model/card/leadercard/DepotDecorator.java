package model.card.leadercard;

public class DepotDecorator extends LeaderCardDecorator {
    private int[] maxResource;
    private int[] numResource;
    private int[] requiredResources;

    private void activateSpecialDepot(){
        //TODO
    }

    @Override
    public void doAction(){ /* this method should either be boolean or throw an exception */
        activateSpecialDepot();
    }
}
