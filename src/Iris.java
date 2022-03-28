public class Iris {

    private final String type;
    private final double[] params;

    public Iris(String name, double[] params) {
        this.type = name;
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public double[] getParams() {
        return params;
    }

}