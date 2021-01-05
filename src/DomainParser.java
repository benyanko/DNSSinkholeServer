import java.util.Arrays;

public class DomainParser
{
    private int m_Start;
    private int m_End;
    private byte[] m_Content;
    private StringBuilder m_Labels;

    public DomainParser(byte[] i_Content, int i_Start)
    {
        this.m_Content = i_Content;
        this.m_Start = i_Start;
        this.m_Labels = new StringBuilder();
        this.m_End = this.ReadDomain();

        if(this.m_Labels.length() > 0)
        {
            this.m_Labels.deleteCharAt(this.m_Labels.length() - 1);
        }
    }

    private int ReadDomain()
    {
        int LastPosition = this.m_Start;
        int CurrentStart = this.m_Start;
        boolean isInPointer = false;

        while (this.m_Content[CurrentStart] != 0)
        {
            if(this.IsLabel(CurrentStart))
            {
                CurrentStart = this.ReadLabel(CurrentStart);
                if(!isInPointer)
                {
                    LastPosition = CurrentStart;
                }
            }
            else
            {
                CurrentStart = this.ReadPointer(CurrentStart);
                if(!isInPointer)
                {
                    LastPosition += 2;
                }
                isInPointer = true;
            }
        }

        return LastPosition;
    }

    private boolean IsLabel(int i_Start)
    {
        byte Mask = 0b1100000;
        if((this.m_Content[i_Start] & Mask) == 0)
        {
            return true;
        }
        return false;
    }

    private int ReadLabel(int i_Start)
    {
        int NumberOfOctets = this.m_Content[i_Start];
        i_Start++;
        this.m_Labels.append(new String(Arrays.copyOfRange(this.m_Content, i_Start, i_Start + NumberOfOctets)));
        this.m_Labels.append(".");

        return i_Start + NumberOfOctets;
    }

    private int ReadPointer(int i_Start)
    {
        int Offset;
        byte Mask = 0b00111111;

        Offset = ((this.m_Content[i_Start] & Mask) << 8);
        Offset = (Offset | this.m_Content[i_Start + 1]);

        return Offset;
    }

    public int getStart() {
        return this.m_Start;
    }

    public int getEnd() {
        return this.m_End;
    }

    public StringBuilder getLabels() {
        return this.m_Labels;
    }


}
