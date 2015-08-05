package surrealfish;

public class MappingNames {

    public static final String FORWARD = "forward";
    public static final String BACKWARD = "backward";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String[] ARRAY = {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    };

    public static int toIndex(String name) {
        for (int i = 0; i < ARRAY.length; i++) {
            if (ARRAY[i].equals(name)) {
                return i;
            }
        }

        return -1;
    }
}
