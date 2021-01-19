public class Headers
{

    private byte[] m_Content;
    private int m_ID;
    private boolean m_QR;
    private boolean m_AA;
    private boolean m_RA;
    private boolean m_RD;
    private int m_RCODE;
    private int m_QDCOUNT;
    private int m_ANCOUNT;
    private int m_NSCOUNT;
    private int m_ARCOUNT;

    public Headers(byte[] i_Content)
    {
        this.m_Content = i_Content;
        this.m_ID = ((this.m_Content[0] << 8) | this.m_Content[1]) & 0x0000ffff;
        this.m_QR = this.isBitSet(this.m_Content[2] & 0b10000000);
        this.m_AA = this.isBitSet(this.m_Content[2] & 0b00000100);
        this.m_RA = this.isBitSet(this.m_Content[3] & 0b10000000);
        this.m_RD = this.isBitSet(this.m_Content[2] & 0b00000001);
        this.m_RCODE = (this.m_Content[4] & 0b00001111);
        this.m_QDCOUNT = ((this.m_Content[4] << 8) | this.m_Content[5]);
        this.m_ANCOUNT = ((this.m_Content[6] << 8) | this.m_Content[7]);
        this.m_NSCOUNT = ((this.m_Content[8] << 8) | this.m_Content[9]);
        this.m_ARCOUNT = ((this.m_Content[10] << 8) | this.m_Content[11]);

    }

    private boolean isBitSet(int i_Bit)
    {
        if (i_Bit == 0)
        {
            return false;
        }
        return true;
    }

    private static void SetBit(byte[] i_Content, int i_ByteIndex, int i_BitIndex, boolean i_SetOn)
    {
        if(i_SetOn)
        {
            i_Content[i_ByteIndex] = (byte) (i_Content[i_ByteIndex] | (1 << i_BitIndex));
        }
        else
        {
            i_Content[i_ByteIndex] =  (byte) (i_Content[i_ByteIndex] & ~(1 << i_BitIndex));
        }
    }

    public int getID()
    {
        return this.m_ID;
    }

    public boolean isQR()
    {
        return this.m_QR;
    }

    public boolean isAA()
    {
        return this.m_AA;
    }

    public boolean isRA() { return this.m_RA; }

    public boolean isRD() { return m_RD; }

    public int getQDCOUNT()
    {
        return this.m_QDCOUNT;
    }

    public int getANCOUNT()
    {
        return this.m_ANCOUNT;
    }

    public int getNSCOUNT()
    {
        return this.m_NSCOUNT;
    }

    public int getARCOUNT()
    {
        return this.m_ARCOUNT;
    }

    public int getRCODE() { return this.m_RCODE; }


    public void setQR(boolean m_QR)
    {
        this.m_QR = m_QR;
        SetBit(this.m_Content, 2, 7, this.m_QR);
    }

    public void setAA(boolean m_AA)
    {
        this.m_AA = m_AA;
        SetBit(this.m_Content, 2, 2, this.m_AA);

    }

    public void setRA(boolean m_RA)
    {
        this.m_RA = m_RA;
        SetBit(this.m_Content, 3, 7, this.m_RA);

    }

    public void setRD(boolean m_RD)
    {
        this.m_RD = m_RD;
        SetBit(this.m_Content, 2, 0, this.m_RD);
    }

    public void setRCODE(int m_RCODE)
    {
        if ( 0 > m_RCODE || m_RCODE > 5)
        {
            throw new IllegalArgumentException("RCODE must be 0 to 5");
        }

        this.m_Content[3] = (byte) ((this.m_Content[3] & 0xf0) | (byte) m_RCODE);
    }

    public void setZToZero(){

        this.m_Content[3] = (byte) (this.m_Content[3] & 0b10001111);
    }



}
