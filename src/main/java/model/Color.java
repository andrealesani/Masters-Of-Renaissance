package model;

public enum Color {
    RESET {
        @Override
        public String toString() {
            return "\u001B[0m";
        }
    },
    AQUA_GREEN_BG {
        @Override
        public String toString() {
            return "\033[48;5;23m";
        }
    },
    AQUA_GREEN_FG {
        @Override
        public String toString() {
            return "\033[38;5;23m";
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
    GREY_DARK_BG {
        @Override
        public String toString() {
            return "\033[48;2;51;51;51m";
        }
    },
    GREY_DARK_FG {
        @Override
        public String toString() {
            return "\033[38;2;51;51;51m";
        }
    },
    GREY_LIGHT_BG {
        @Override
        public String toString() {
            return "\033[48;2;102;102;102m";
        }
    },
    GREY_LIGHT_FG {
        @Override
        public String toString() {
            return "\033[38;2;102;102;102m";
        }
    },
    LIGHT_BLUE_BG {
        @Override
        public String toString() {
            return "\033[48;2;0;122;204m";
        }
    },
    LIGHT_BLUE_FG {
        @Override
        public String toString() {
            return "\033[38;2;0;122;204m";
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
    RED_DARK_BG {
        @Override
        public String toString() {
            return "\033[48;2;102;0;0m";
        }
    },
    RED_DARK_FG {
        @Override
        public String toString() {
            return "\033[38;2;102;0;0m";
        }
    },
    RED_LIGHT_BG {
        @Override
        public String toString() {
            return "\033[48;2;180;0;0m";
        }
    },
    RED_LIGHT_FG {
        @Override
        public String toString() {
            return "\033[38;2;180;0;0m";
        }
    },
    YELLOW_DARK_BG {
        @Override
        public String toString() {
            return "\033[48;2;128;85;0m";
        }
    },
    YELLOW_DARK_FG {
        @Override
        public String toString() {
            return "\033[38;2;128;85;0m";
        }
    },
    YELLOW_LIGHT_BG {
        @Override
        public String toString() {
            return "\033[48;2;210;210;0m";
        }
    },
    YELLOW_LIGHT_FG {
        @Override
        public String toString() {
            return "\033[38;2;210;210;0m";
        }
    },
    ORANGE_LIGHT_BG {
        @Override
        public String toString() {
            return "\033[48;2;210;110;0m";
        }
    },
    ORANGE_LIGHT_FG {
        @Override
        public String toString() {
            return "\033[38;2;210;110;0m";
        }
    },
    WHITE_BG {
        @Override
        public String toString() {
            return "\033[48;2;217;217;217m";
        }
    },
    WHITE_FG {
        @Override
        public String toString() {
            return "\033[38;2;217;217;217m";
        }
    },
    RESOURCE_STD {
        @Override
        public String toString() {
            return "\u001B[36m";
        }
    },
    HEADER {
        @Override
        public String toString() {
            return "\u001B[32;1m";
        }
    },
    VIEW {
        @Override
        public String toString() {
            return "\u001B[34;1m";
        }
    }
}
