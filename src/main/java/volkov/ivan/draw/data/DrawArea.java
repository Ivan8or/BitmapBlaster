package volkov.ivan.draw.data;

public record DrawArea(int x1, int y1, int x2, int y2) {
    public int smallX() { return Math.min(x1, x2); }
    public int largeX() { return Math.max(x1, x2); }
    public int smallY() { return Math.min(y1, y2); }
    public int largeY() { return Math.max(y1, y2); }
    public int width() { return largeX() - smallX(); }
    public int height() { return largeY() - smallY(); }

    public String toString() {
        return smallX()+","+smallY()+","+largeX()+","+largeY()+" w="+width()+" h="+height();
    }
}
