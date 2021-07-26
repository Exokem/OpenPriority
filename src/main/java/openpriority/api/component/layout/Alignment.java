package openpriority.api.component.layout;

public enum Alignment
{
    HORIZONTAL(1, 0), VERTICAL(0, 1)
    ;

    public final int colMod, rowMod;

    Alignment(int cm, int rm)
    {
        this.colMod = cm;
        this.rowMod = rm;
    }

    public int column(int next)
    {
        return colMod * next;
    }

    public int row(int next)
    {
        return rowMod * next;
    }
}
