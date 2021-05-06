package pl.krakow.up;

public class Main {

    public static void main(String[] args) {
	// write your code here
        // Run a simple server
        SimpleServer s = new SimpleServer();
        s.start(450);
        s.stop();
        // Run a multicast server
//        MultiServer ms = new MultiServer();
//        ms.start(450);

        // Run Client
//        Client c = new Client();
//        c.startConnect("192.168.21.128", 450);
//        c.startConnect("pmazurek.ddns.net", 450);
//        c.seendMessage();
//        c.stopConnection();

    }
}
