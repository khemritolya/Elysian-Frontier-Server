import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        System.out.println("Enter run mode: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        switch (input) {
            case "server":
                System.out.println("--------------- Elysian-Frontier Server ---------------");
                UdpServer.run();
                break;
            case "client":
                System.out.println("--------------- Elysian-Frontier ---------------");
                UdpClient.run();
                break;
            default:
                System.out.println("could not understand input: `" + input + "`");
                break;
        }
    }
}
