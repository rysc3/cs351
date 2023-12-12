import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;

/**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Client Class
 */
public class Client implements Runnable {
    private final String hostname;
    private final int port;
    private final PlayerData playerData;

    private int playerNum;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean playing;


    public Client(String hostname, int port, PlayerData playerData) {
        this.hostname = hostname;
        this.port = port;
        this.playerData = playerData;

        playing = true;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public boolean isReady() { return playing; }

    public void stopClient() {
        playing = false;
    }

    public PlayerData getPlayerData(){
        return playerData;
    }

    @Override
    public void run() {
        Data inData;
        Data outData;

        try (Socket socket = new Socket(hostname, port)) {
            socket.setSoTimeout(250);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            playerNum = in.readInt();
            System.out.println("Player: " + playerNum);

            while (playing) {
                outData = playerData.getPlayerData(playerNum);
                out.writeUnshared(outData);
                out.flush();

                for (int idx = 0; idx < playerData.getNumPlayers(); idx++) {
                    try {
                        inData = (Data) in.readUnshared();

                        if (idx != playerNum) {
                            playerData.setPlayerData(idx, inData.getX(), inData.getY(), inData.getScore(), inData.xVelocity(),
                                    inData.yVelocity(), inData.getIsAlive(), inData.isJumping(), inData.isWalking(),
                                    inData.isGrounded(), inData.isClimbing(), inData.isClimbingSpecial(),
                                    inData.getDirection(), inData.isCycle(), true);

                            // System.out.println("Player " + idx + ": " + inData.getX() + ", " + inData.getY() + ", " + inData.getIsAlive() + " Walking: " + inData.isWalking());
                            //System.out.println("Player " + idx + ": " + inData.getX() + ", " + inData.getY() + ", " + inData.getIsAlive() + " Ready: " + inData.isReady());
                        }

                        // player scores 
                        

                    } catch (SocketTimeoutException e) {
                        idx = 4;
                    }
                }
            }

            System.out.println("Disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
