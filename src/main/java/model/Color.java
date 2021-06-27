package model;

/**
 * This enum contains all the colors used when printing
 */
public enum Color {
    /**
     * The default colors (white text, transparent background)
     */
    RESET {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\u001B[0m";
        }
    },
    /**
     * The acqua green background color
     */
    AQUA_GREEN_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;5;23m";
        }
    },
    /**
     * The acqua green text color
     */
    AQUA_GREEN_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;5;23m";
        }
    },
    /**
     * The blue background color
     */
    BLUE_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;0;25;120m";
        }
    },
    /**
     * The blue text color
     */
    BLUE_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;0;25;120m";
        }
    },
    /**
     * The green background color
     */
    GREEN_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;0;102;40m";
        }
    },
    /**
     * The green text color
     */
    GREEN_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;0;102;40m";
        }
    },
    /**
     * The dark gray background color
     */
    GREY_DARK_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;51;51;51m";
        }
    },
    /**
     * The dark gray text color
     */
    GREY_DARK_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;51;51;51m";
        }
    },
    /**
     * The light gray background color
     */
    GREY_LIGHT_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;102;102;102m";
        }
    },
    /**
     * The light gray text color
     */
    GREY_LIGHT_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;102;102;102m";
        }
    },
    /**
     * The light blue background color
     */
    LIGHT_BLUE_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;0;122;204m";
        }
    },
    /**
     * The light blue text color
     */
    LIGHT_BLUE_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;0;122;204m";
        }
    },
    /**
     * The purple background color
     */
    PURPLE_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;80;15;200m";
        }
    },
    /**
     * The purple text color
     */
    PURPLE_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;80;15;200m";
        }
    },
    /**
     * The dark red background color
     */
    RED_DARK_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;102;0;0m";
        }
    },
    /**
     * The dark red text color
     */
    RED_DARK_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;102;0;0m";
        }
    },
    /**
     * The light red background color
     */
    RED_LIGHT_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;180;0;0m";
        }
    },
    /**
     * The light red text color
     */
    RED_LIGHT_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;180;0;0m";
        }
    },
    /**
     * The dark yellow background color
     */
    YELLOW_DARK_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;128;85;0m";
        }
    },
    /**
     * The dark yellow text color
     */
    YELLOW_DARK_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;128;85;0m";
        }
    },
    /**
     * The light yellow background color
     */
    YELLOW_LIGHT_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;210;210;0m";
        }
    },
    /**
     * The light yellow text color
     */
    YELLOW_LIGHT_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;210;210;0m";
        }
    },
    /**
     * The light orange background color
     */
    ORANGE_LIGHT_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;210;110;0m";
        }
    },
    /**
     * The light orange text color
     */
    ORANGE_LIGHT_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;210;110;0m";
        }
    },
    /**
     * The white background color
     */
    WHITE_BG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[48;2;217;217;217m";
        }
    },
    /**
     * The white text color
     */
    WHITE_FG {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\033[38;2;217;217;217m";
        }
    },
    /**
     * The text color used for resource names
     */
    RESOURCE_STD {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\u001B[36m";
        }
    },
    /**
     * The text color used for CLI headers
     */
    HEADER {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\u001B[32;1m";
        }
    },
    /**
     * The text color used for the CLI header
     */
    VIEW {
        /**
         * Returns a String containing the color's setting character
         *
         * @return the color's setting character
         */
        @Override
        public String toString() {
            return "\u001B[34;1m";
        }
    }
}
