package packageServer;

import io.netty.channel.*;
import packageMessage.FileMessage;
import packageMessage.MyMessage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final List<Channel>  channels = new ArrayList<Channel>();
    private String clientName;
    private static int newClientIndex = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент " +newClientIndex+ " подключился");
        channels.add(ctx.channel());
        clientName = "Клиента # " + newClientIndex;
        newClientIndex++;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        new MessageHandler(ctx, msg);


//        System.out.println(msg.getClass().getName());
//        if(msg instanceof MyMessage){
//            String comand = (String)msg;
//            System.out.println("Client text message " + ((MyMessage)msg).getText());
//            ctx.writeAndFlush("Hellow Client!");
//        }else{
//            System.out.printf("Server recived wrong object");
//        }
//
//        if(msg instanceof FileMessage){
//            FileMessage fm = (FileMessage)msg;
//            Files.write(Paths.get("Server/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
//            System.out.println("Client send File name: " + ((FileMessage)msg).getFileName());
//        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                cause.printStackTrace();
                ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client "+clientName+" disconnected");
    }


}
