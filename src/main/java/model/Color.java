package model;

public enum Color {
    DEFAULT {
        @Override
        public String toString() {
            return "\u001B[0m";
        }
    },
    BLUE_BG {
        @Override
        public String toString() {
            return "\033[48;2;0;25;120m";
        }
    },
    BLUE_FG {
        @Override
        public String toString() {
            return "\033[38;2;0;25;120m";
        }
    },
    GREEN_BG {
        @Override
        public String toString() {
            return "\033[48;2;0;102;40m";
        }
    },
    GREEN_FG {
        @Override
        public String toString() {
            return "\033[38;2;0;102;40m";
        }
    },
    PURPLE_BG {
        @Override
        public String toString() {
            return "\033[48;2;80;15;200m";
        }
    },
    PURPLE_FG {
        @Override
        public String toString() {
            return "\033[38;2;80;15;200m";
        }
    },
    YELLOW_BG {
        @Override
        public String toString() {
            return "\033[48;2;128;85;0m";
        }
    },
    YELLOW_FG {
        @Override
        public String toString() {
            return "\033[38;2;128;85;0m";
        }
    },
    ORANGE_FG {
        @Override
        public String toString() {
            return "\u001B[36m";
        }
    },
    AQUA_GREEN_BG {
        @Override
        public String toString() {
            return "\033[48;5;23m";
        }
    },
    HEADER {
        @Override
        public String toString() {
            return "\u001B[32;1m";
        }
    }
}
