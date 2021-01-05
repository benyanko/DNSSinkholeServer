public class Test {

    public static void main(String[] args) {
        String at = "d2338000000100000006000d02656e0977696b697065646961036f72670000010001c019000200010002a3000015026430036f72670b6166696c6961732d6e7374c019c019000200010002a3000019026130036f72670b6166696c6961732d6e737404696e666f00c019000200010002a3000005026330c052c019000200010002a3000005026132c052c019000200010002a3000005026230c031c019000200010002a3000005026232c031c02e000100010002a3000004c7133901c02e001c00010002a300001020010500000f00000000000000000001c04f000100010002a3000004c7133801c04f001c00010002a300001020010500000e00000000000000000001c074000100010002a3000004c7133501c074001c00010002a300001020010500000b00000000000000000001c085000100010002a3000004c7f97001c085001c00010002a300001020010500004000000000000000000001c096000100010002a3000004c7133601c096001c00010002a300001020010500000c00000000000000000001c0a7000100010002a3000004c7f97801c0a7001c00010002a3000010200105000048000000000000000000010000291000000000000000";
        byte [] data = hexStringToByteArray(at);

//      ResourceRecord rr = new ResourceRecord(34, data);
        byte [] nd = new byte[1];
        nd[0] =  (byte) 0b11000000;
        System.out.println(toBinary(nd));
        nd = fromBinary("11000000");

        System.out.println((byte) 0b11000000);

        DomainParser domainParser = new DomainParser(data, 34);
        System.out.println(domainParser.getLabels());

    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String toBinary( byte[] bytes )
    {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public static byte[] fromBinary( String s )
    {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for( int i = 0; i < sLen; i++ )
            if( (c = s.charAt(i)) == '1' )
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            else if ( c != '0' )
                throw new IllegalArgumentException();
        return toReturn;
    }
}