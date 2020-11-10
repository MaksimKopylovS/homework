package packageServer;

import io.netty.channel.ChannelHandlerContext;
import packageMessage.FileMessage;
import packageMessage.MyMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MessageHandler {

    private final String rootPath = "Server";
    private static String userPath = "NewFolder";

    public MessageHandler(ChannelHandlerContext ctx, Object msg) {
        //System.out.println(msg.getClass().getName());
        try {

            if (msg instanceof MyMessage) {
                String comand = ((MyMessage) msg).getText();

                if (comand.startsWith("/")) {
                    System.out.println("Cистемное от " + ":" + msg + " ");

                    //  touch (name) создать текстовый файл с именем
                    if (comand.startsWith("/touch")) {
                        System.out.println(userPath + "/" + comand.split(" ", 10)[1]);
                        Path path = Paths.get(rootPath + "/" + userPath + "/" + comand.split(" ", 10)[1]);
                        Files.createFile(path);
                        if (Files.exists(path)) {
                            ctx.writeAndFlush(path + " Создан");
                        } else {
                            ctx.writeAndFlush(path + " не создан");
                        }
                    }
                    //  mkdir (name) создать директорию
                    if (comand.startsWith("/mkdir")) {
                        Path path = Paths.get(rootPath + "/" + userPath + "/" + comand.split(" ", 10)[1]);
                        Path newDir = Files.createDirectory(path);
                        if (Files.exists(path)) {
                            ctx.writeAndFlush(path + " Создан");
                        } else {
                            ctx.writeAndFlush(path + " не создан");
                        }
                    }
                    //  cd (name) - перейти в папку
                    if (comand.startsWith("/cd")) {
                        if (comand.split(" ", 4).length >= 2) {
                            if (Files.exists(Paths.get(comand.split(" ", 4)[1]))) {
                                System.out.println(comand.split(" ", 10)[1]);
                                setUserPath(comand.split(" ", 10)[1]);
                                ctx.writeAndFlush("вы в дериктории " + Paths.get(comand.split(" ", 4)[1]));
                            } else {
                                ctx.writeAndFlush("Нет такого пути");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }
                    }
                    //  rm (name) удалить файл по имени
                    if (comand.startsWith("/rm")) {
                        if (comand.split(" ", 4).length >= 2) {
                            if (Files.exists(Paths.get(comand.split(" ", 4)[1]))) {
                                Files.delete(Paths.get(comand.split(" ", 4)[1]));
                                ctx.writeAndFlush("Файл " + Paths.get(comand.split(" ", 10)[1]) + " удалён");
                            } else {
                                ctx.writeAndFlush("Файл не существует");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }

                    }
                    //  copy (src, target) скопировать файл из одного пути в другой
                    if (comand.startsWith("/copy")) {
                        if (comand.split(" ", 4).length >= 3) {
                            if (Files.exists(Paths.get(comand.split(" ", 4)[1])) || Files.exists(Paths.get(comand.split(" ", 4)[2]))) {
                                Files.copy(Paths.get(comand.split(" ", 10)[1]),
                                        Paths.get(comand.split(" ", 10)[2]));
                                ctx.writeAndFlush("Файл " + Paths.get(comand.split(" ", 10)[1])
                                        + " скопирован" + " в "
                                        + Paths.get(comand.split(" ", 10)[2]));
                                ///copy Server/NewFolder/test.txt Server/test.txt
                            } else {
                                ctx.writeAndFlush("Деректории не существует");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }
                    }
                    //  cat (name) - вывести в консоль содержимое файла
                    if (comand.startsWith("/cat")) {
                        if (comand.split(" ", 4).length >= 2) {
                            if (Files.exists(Paths.get(comand.split(" ", 4)[1]))) {
                                List<String> list = Files.readAllLines(Paths.get(comand.split(" ", 10)[1]));
                                for (String str : list) {
                                    ctx.writeAndFlush(str + "\n");
                                }
                            }else{
                                ctx.writeAndFlush("Файла не сушествует");
                            }
                        }else{
                            ctx.writeAndFlush("Ошибка ввода");
                        }

                    }

                    if (comand.startsWith("/ls")) {
                        Object o = getFilesList();
                        ctx.writeAndFlush(getFilesList());
                    }

                    if (comand.startsWith("/info")) {
                        if (comand.split(" ", 4).length >= 2) {
                            File file = new File(comand.split(" ", 10)[1]);
                            Object o;
                            if (file.exists()) {
                                for (File item : file.listFiles()) {
                                    o = (String) item.getName();
                                    ctx.writeAndFlush(o);
                                }
                            } else {
                                ctx.writeAndFlush("Файл не найден");
                            }
                        } else {
                            ctx.writeAndFlush("Ошибка ввода");
                        }
                    }


                    if (comand.startsWith("/download")) {
                        try {
                            if (comand.split(" ", 4).length >= 2) {
                                if(Files.exists(Paths.get(comand.split(" ", 4)[1]))){
                                    Path path = Paths.get(comand.split(" ", 4)[1]);
                                    System.out.println(path.getFileName());
                                    Object o = new FileMessage(path);
                                    ctx.writeAndFlush(o);
                                }else{
                                    ctx.writeAndFlush("Файл не существует");
                                }
                            }else{
                                ctx.writeAndFlush("Ошибка ввода");
                            }

                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    if (comand.startsWith("/help")) {
                        String help =
                                "\n/touch name - создать текстовый файл с именем\n"
                                        + "/mkdir name - создать директорию\n"
                                        + "/cd name - перейти в папку\n"
                                        + "/rm name - удалить файл по имени\n"
                                        + "/copy src target скопировать файл из одного пути в другой\n"
                                        + "/cat (name) - вывести в консоль содержимое файла\n"
                                        + "/help - помошь\n"
                                        + "/download - загрузка файла, пример: /download Server/s.jpg\n"
                                        + "/clear - очистить окно\n"
                                        + "/info - что находится в дериктории пример /info Server\n";
                        Object o = help;
                        ctx.writeAndFlush(o);
                    }

                } else {
                    String out = ((MyMessage) msg).getText();
                    System.out.print(out + "\n");
                    ctx.writeAndFlush(out + "\n");
                }
            } else {
                System.out.printf("Server recived wrong object");
            }

            if (msg instanceof FileMessage) {
                FileMessage fm = (FileMessage) msg;
                Files.write(Paths.get("Server/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
                System.out.println("Client send File name: " + ((FileMessage) msg).getFileName());
                ctx.writeAndFlush("Сервер записал файл " + fm.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            ctx.writeAndFlush("Ошибка ввода");
        }

    }


    private String getFilesList() {
        return String.join(" ", new File(userPath).list());
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }
}
