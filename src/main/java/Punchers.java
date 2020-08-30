public class Punchers {

    private int punchPage;
    private String punchName;

    public Punchers(int punchPage, String punchName){
        this.punchName = punchName;
        this.punchPage = punchPage;
    }

    public int getPunchPage() {
        return punchPage;
    }

    public void setPunchPage(int punchPage) {
        this.punchPage = punchPage;
    }

    public String getPunchName() {
        return punchName;
    }

    public void setPunchName(String punchName) {
        this.punchName = punchName;
    }

    @Override
    public String toString() {
        return "Punchers{" +
                "punchPage=" + punchPage +
                ", punchName='" + punchName + '\'' +
                '}';
    }
}
