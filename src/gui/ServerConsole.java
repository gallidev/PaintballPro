package gui;

/**
 * Created by jack on 03/03/2017.
 */
public class ServerConsole implements ServerView {
    @Override
    public void addMessage(String message) {
        System.out.println(message);
    }
}
