public class Question
{
    private int m_Start;
    private int m_End;
    private String m_Name;
    private DomainParser m_DomainParser;

    public Question(byte[] i_Content, int i_Start)
    {
        this.m_Start = i_Start;
        this.m_DomainParser = new DomainParser(i_Content, i_Start);
        this.m_Name = this.m_DomainParser.getLabels().toString();
        this.m_End = this.m_DomainParser.getEnd() + 4;
    }

    public int getStart()
    {
        return this.m_Start;
    }

    public int getEnd()
    {
        return this.m_End;
    }

    public String getName()
    {
        return this.m_Name;
    }

    public DomainParser getDomainParser()
    {
        return this.m_DomainParser;
    }

}
