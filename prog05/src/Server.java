import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;

 /**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Server Class
 */
public class Server {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        int playerNum = 0;
        PlayerData playerData = new PlayerData();
        
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client);

                playerData.addPlayer(0, 0, 0,  true); // set to default spawn location based on player number
                
                ClientHandler handler = new ClientHandler(client, playerData, playerNum);
                Thread thread = new Thread(handler);
                thread.start();

                playerNum++;

                // Better debugging
                /*
                System.out.println();
                System.out.println("----------------------------");
                for(int i = 0; i < playerData.getNumPlayers(); i++){
                    System.out.println("---------");
                    System.out.println("Player " + i);
                    System.out.println("X: " + playerData.getPlayerData(i).getX());
                    System.out.println("Y: " + playerData.getPlayerData(i).getY());
                    System.out.println("Score: " + playerData.getPlayerData(i).getScore());
                    System.out.println("Alive: " + playerData.getPlayerData(i).getIsAlive());
                    System.out.println("---------");
                }
                System.out.println("----------------------------");
                System.out.println();
                */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
