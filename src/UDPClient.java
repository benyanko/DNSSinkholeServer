import java.io.IOException;
import java.util.Arrays;
import java.net.*;

public class UDPClient
{
    private DatagramSocket m_ClientSocket;
    private  byte[] m_ReceiveData;

    public UDPClient(int i_BuffSize) throws SocketException
    {
        this.m_ClientSocket = new DatagramSocket();
        this.m_ReceiveData = new byte[i_BuffSize];
    }

    public byte[] request(byte[] i_Content, int i_Length, String i_Address, int i_Port) throws IOException
    {
        InetAddress IPAddress = InetAddress.getByName(i_Address);
        DatagramPacket sendPacket = new DatagramPacket(i_Content, i_Length, IPAddress, i_Port);
        this.m_ClientSocket.send(sendPacket);

        return receive();
    }

    private byte[] receive() throws IOException
    {
        DatagramPacket receivePacket = new DatagramPacket(this.m_ReceiveData, this.m_ReceiveData.length);
        this.m_ClientSocket.receive(receivePacket);
        return Arrays.copyOf(this.m_ReceiveData, this.m_ReceiveData.length);
    }

    public void close()
    {
        this.m_ClientSocket.close();
    }

}
