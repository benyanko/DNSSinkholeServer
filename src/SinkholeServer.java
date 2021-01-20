import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SinkholeServer
{
    public static int DNSPort = 53;
    public static int SinkholePort = 5300;
    public static ArrayList<String> DNSRootServersDomains = new ArrayList<String>() {{
        add("a.root-servers.net");
        add("b.root-servers.net");
        add("c.root-servers.net");
        add("d.root-servers.net");
        add("e.root-servers.net");
        add("f.root-servers.net");
        add("g.root-servers.net");
        add("h.root-servers.net");
        add("i.root-servers.net");
        add("j.root-servers.net");
        add("k.root-servers.net");
        add("l.root-servers.net");
        add("m.root-servers.net");
    }};
    public static String BlockListPath = "";
    public static HashMap<Integer, String> BlockListMap;


    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            BlockListPath = args[0];
        }

        DNSSinkholeServer();
    }

    public static void DNSSinkholeServer()
    {
        UDPServer server = null;
        UDPClient client = null;
        Message currentMessage = null;
        Message query;
        String currentDnsServer;
        int round;

        try
        {
            if (BlockListPath.length() > 0)
            {
                BlockListMap = BlocklistMap(BlockListPath);
            } else
            {
                BlockListMap = null;
            }
        }
        catch (Exception e)
        {
            System.out.println("error getting blocklist");
        }

        try
        {
            server = new UDPServer(SinkholePort, 1024);

            while (true)
            {
                try
                {

                    query = new Message(server.receiveFrom());

                    if (BlockListMap != null && isInBlockList(query))
                    {
                        currentMessage = query;

                        currentMessage.getHeaders().setRCODE(3);
                        currentMessage.getHeaders().setZToZero();
                    }
                    else
                    {

                        query.getHeaders().setRD(false);
                        query.getHeaders().setRA(false);

                        client = new UDPClient(1024);
                        currentDnsServer = getRandomServer();

                        round = 0;
                        while (round < 16)
                        {
                            round++;
                            byte[] message = client.request(query.getContent(), query.getEnd(), currentDnsServer, DNSPort);
                            currentMessage = new Message(message);
                            if (
                                    currentMessage.getHeaders().getNSCOUNT() == 0 ||
                                            currentMessage.getHeaders().getANCOUNT() != 0 ||
                                            (currentMessage.getHeaders().getRCODE() != 0 && currentMessage.getHeaders().getRCODE() != 3) ||
                                            (currentMessage.getHeaders().isAA() && currentMessage.getHeaders().getRCODE() == 3)
                            )
                            {
                                break;
                            }
                            currentDnsServer = currentMessage.getAuthorityList().get(0).getRData();
                        }
                    }

                    currentMessage.getHeaders().setAA(false);
                    currentMessage.getHeaders().setRD(true);
                    currentMessage.getHeaders().setQR(true);
                    currentMessage.getHeaders().setRA(true);
                    server.sendTo(currentMessage.getContent(), currentMessage.getEnd());
                }
                catch (SocketException e)
                {
                    System.err.printf("Client Socket unavailable: " + e.getMessage());
                    System.exit(1);
                }
                finally
                {
                    if (client != null)
                    {
                        client.close();
                    }
                }
            }
        }
        catch (SocketException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        finally
        {
            if (server != null)
            {
                server.close();
            }

        }
    }

    public static boolean isInBlockList(Message i_Message)
    {
        boolean answer = false;
        for (Question question: i_Message.getQuestionsList())
        {
            String name = question.getName();
            if(BlockListMap.containsValue(name))
            {
                answer = true;
                break;
            }
        }
        return answer;
    }

    public static HashMap<Integer, String> BlocklistMap(String i_FilePath) throws IOException
    {
        int index = 1;
        HashMap<Integer, String> BlocklistMap = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(i_FilePath)));
        String line;
        while((line = reader.readLine()) != null)
        {
            BlocklistMap.put(index, line);
            index++;
        }

        return BlocklistMap;
    }

    public static String getRandomServer()
    {
        int index = new Random().nextInt(DNSRootServersDomains.size());
        String server =  DNSRootServersDomains.get(index);
        return server;

    }
}
