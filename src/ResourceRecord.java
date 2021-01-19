public class ResourceRecord
{

    private eType m_Type;
    private int m_Start;
    private int m_End;
    private String m_Name;
    private String m_RData;
    private int m_RDataLength;
    private byte[] m_Content;
    private DomainParser m_DomainParser;


    public ResourceRecord(eType i_Type, byte[] i_Content, int i_Start)
    {
        this.m_Type = i_Type;
        this.m_Start = i_Start;
        this.m_RData = "";
        this.m_Content = i_Content;
        this.m_DomainParser = new DomainParser(this.m_Content, this.m_Start);
        this.m_Name = this.m_DomainParser.getLabels().toString();

        if(this.m_Type == eType.Answer)
        {
            this.m_End = this.ReadAnswer();
        }
        else if(this.m_Type == eType.Authority)
        {
            this.m_End = this.ReadAuthority();
        }
        else
        {
            this.m_End = 0;
        }
    }

    private int ReadAnswer()
    {
        int CurrentStart = this.m_DomainParser.getEnd() + 8;
        int RDLength = (this.m_Content[CurrentStart] << 8);
        RDLength = (RDLength | this.m_Content[CurrentStart + 1]);
        CurrentStart += (2 + RDLength);

        return CurrentStart;
    }

    private int ReadAuthority()
    {
        int CurrentStart = this.m_DomainParser.getEnd() + 8;

        this.m_RDataLength =  (this.m_Content[CurrentStart] << 8 | this.m_Content[CurrentStart + 1]) & 0x0000ffff;
        CurrentStart += 2;

        this.m_DomainParser = new DomainParser(this.m_Content, CurrentStart);
        this.m_RData = this.m_DomainParser.getLabels().toString();

        return CurrentStart + this.m_RDataLength;
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

    public String getRData()
    {
        return this.m_RData;
    }


}
