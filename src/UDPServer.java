import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDPServer {

    private byte[] m_ReceiveData;
    private DatagramSocket m_ServerSocket;
    private int m_Port;
    private int m_OutPort;
    private InetAddress m_IPAddress = null;

    public UDPServer(int i_Port, int i_BuffSize) throws SocketException
    {
        this.m_Port = i_Port;
        this.m_ReceiveData = new byte[i_BuffSize];
        this.m_ServerSocket = new DatagramSocket(this.m_Port);
    }

    public byte[] receive() throws IOException
    {
        DatagramPacket receivePacket = new DatagramPacket(m_ReceiveData, m_ReceiveData.length);
        this.m_ServerSocket.receive(receivePacket);
        this.m_IPAddress = receivePacket.getAddress();
        this.m_OutPort = receivePacket.getPort();

        return Arrays.copyOf(this.m_ReceiveData, this.m_ReceiveData.length);
    }

    public void send(byte[] i_SendData, int i_Length) throws RuntimeException, IOException
    {
        if (this.m_IPAddress == null)
        {
            throw new RuntimeException("server did not receive");
        }

        DatagramPacket sendPacket = new DatagramPacket(i_SendData, i_Length, this.m_IPAddress, this.m_OutPort);
        this.m_ServerSocket.send(sendPacket);

    }

    public void close()
    {
        this.m_ServerSocket.close();
    }

}
