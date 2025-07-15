package virtualpets.models;


public enum PetType {
    MOLE("Mole", "mole.svg"),
    MAGPIE("Magpie", "magpie.svg"),
    TOAD("Toad", "toad.svg");

    private final String displayName;
    private final String svgFileName;

    PetType(String displayName, String svgFileName) {
        this.displayName = displayName;
        this.svgFileName = svgFileName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSvgFileName() {
        return svgFileName;
    }
}
