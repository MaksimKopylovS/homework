package packageClient;

import io.netty.channel.ChannelHandlerContext;
import javafx.application.Platform;
import packageMessage.FileMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class NetworkMessageHandler {
    private static String msg;

    public static String getMsg(){
        return msg;
    }

    public NetworkMessageHandler(ChannelHandlerContext ctx, Object msg) throws IOException {

        if(msg instanceof String){
            this.msg = (String)msg;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Main.getControllerWindowStorage().getListView().getItems().add(getMsg());
                }
            });

        }

        if(msg instanceof FileMessage){
            FileMessage fileMessage = (FileMessage)msg;
            Files.write(Paths.get("Client/" + fileMessage.getFileName()), fileMessage.getData(), StandardOpenOption.CREATE);


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Main.getControllerWindowStorage().getListView().getItems().add("Client/"+fileMessage.getFileName() + "сохранен");
                }
            });
        }
    }

}
