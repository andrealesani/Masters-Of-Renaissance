package model;

/**
 * This enum contains all possible phases for the players' turn
 */
public enum TurnPhase {
    LEADERCHOICE {
        @Override
        public String toString() {
            return "\u001B[36mLEADERCHOICE\u001B[0m";
        }
    },
    ACTIONSELECTION {
        @Override
        public String toString() {
            return "\u001B[36mACTIONSELECTION\u001B[0m";
        }
    },
    MARKETDISTRIBUTION {
        @Override
        public String toString() {
            return "\u001B[36mMARKETDISTRIBUTION\u001B[0m";
        }
    },
    CARDPAYMENT {
        @Override
        public String toString() {
            return "\u001B[36mCARDPAYMENT\u001B[0m";
        }
    },
    PRODUCTIONPAYMENT {
        @Override
        public String toString() {
            return "\u001B[36mPRODUCTIONPAYMENT\u001B[0m";
        }
    };

    public String vanillaToString() {
        return super.toString();
    }
}
