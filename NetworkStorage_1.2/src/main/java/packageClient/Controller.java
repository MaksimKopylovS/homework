package packageClient;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.List;

public class Controller implements Initializable {

    private Network network;
    private Desktop desktop = Desktop.getDesktop();

    @FXML
    TextField textFieldMessage;

    @FXML
    ListView listView;

    public ListView getListView() {
        return listView;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network();
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setEditable(true);


    }


    public void menuItemActionSendFile() {
        //Отправка файла маленького размера
        File file = new FileChooser().showOpenDialog(Main.getStageClient());
        if (file != null) {
            List<File> listFile = Arrays.asList(file);
            System.out.println(file);
            network.sendFile(file.getPath());
        }
    }

    public void sendMessageAction(ActionEvent event) throws IOException {
        //Отправка текстового сообщения
        if(textFieldMessage.getText().equals("/clear")){
            listView.getItems().clear();
        }else {
            network.sendMessage(textFieldMessage.getText());
        }
//        network.sendMessage(textFieldMessage.getText());
//        textFieldMessage.clear();
//        textFieldMessage.requestFocus();
    }

    public void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
