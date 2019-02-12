import server.Server;

public class Main {

    public static void main (String[] args) throws Exception {
        /*if (args == null || args.length != 1)
            throw new Exception("Bad parameter length");
        new Server(Integer.valueOf(args[0])).run();*/
        new Server(38608).run();
    }

}
