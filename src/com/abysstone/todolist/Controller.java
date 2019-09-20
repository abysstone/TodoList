package com.abysstone.todolist;

import com.abysstone.todolist.datamodel.TodoData;
import com.abysstone.todolist.datamodel.TodoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {

    private List<TodoItem> todoItems;
    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea itemDetailsTextArea;
    @FXML
    private Label deadLineLabel;

    @FXML
    private BorderPane mainBorderPane;

    public void initialize() {

        //        TodoItem item1 = new TodoItem("Munna's birthday card", "Buy a 27th birthday card for Munna",
//                LocalDate.of(2019, Month.SEPTEMBER, 21));
//        TodoItem item2 = new TodoItem("Doctors appointment", "See Dr. Soorma on the Subhash Market Clinic",
//                LocalDate.of(2019, Month.OCTOBER, 1));
//        TodoItem item3 = new TodoItem("Finish design proposal for Kpmg", "Promise made to Raj to be filled by today",
//                LocalDate.of(2019, Month.OCTOBER, 17));
//        TodoItem item4 = new TodoItem("PickUP bro from Railways", "Bro arriving Bangalore by train at 5 in evening",
//                LocalDate.of(2019, Month.OCTOBER, 27));
//        TodoItem item5 = new TodoItem("Gandhi Jayanti celebration", "Take family for a trip to Vaishno Devi",
//                LocalDate.of(2019, Month.NOVEMBER, 2));
//
//        todoItems = new ArrayList<TodoItem>();
//        todoItems.add(item1);
//        todoItems.add(item2);
//        todoItems.add(item3);
//        todoItems.add(item4);
//        todoItems.add(item5);
//
//        TodoData.getInstance().setTodoItems(todoItems);

        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if(newValue!= null){
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    deadLineLabel.setText(df.format(item.getDeadline()));

                }
            }
        });


       // todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
        todoListView.setItems(TodoData.getInstance().getTodoItems());
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();







        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                //return null;
                ListCell<TodoItem> cell = new ListCell<TodoItem>(){
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        }else {
                            setText(item.getShortDescription());
                            if(item.getDeadline().equals(LocalDate.now())){
                                setTextFill(Color.RED);
                            }else if(item.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BROWN);
                            }else if(item.getDeadline().isBefore(LocalDate.now())){
                                setTextFill(Color.DARKGREY);
                            }
                        }
                    }
                };
                return cell;
            }
        });






    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Todo Item");
        dialog.setHeaderText("Use this dialog to create a new Todo Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try {

//            Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));
//            dialog.getDialogPane().setContent(root);
              dialog.getDialogPane().setContent(fxmlLoader.load());


        }catch (IOException e){

            System.out.println("Could not load the dialog file");
            e.printStackTrace();
            return;

        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
 //           System.out.println("OK pressed");

            DialogController controller = fxmlLoader.getController(); //dialog controller ok button event assignment reissue coupling code
            TodoItem newItem =  controller.processResults();
//            todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());      //Now handled using dataBinding
            todoListView.getSelectionModel().select(newItem);
        }//else {
//            System.out.println("Cancle pressed");
//        }

    }

    @FXML
    public void handleClickListView(){
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
//        System.out.println("The selected item is : "+ item);                //print
//        itemDetailsTextArea.setText(item.getDetails());                     //print
//
//        StringBuilder sb = new StringBuilder(item.getDetails());            //first method
//        sb.append("\n\n\n\n\n");
//        sb.append("Due: ");
//        sb.append(item.getDeadline().toString());
//        itemDetailsTextArea.setText(sb.toString());

        itemDetailsTextArea.setText(item.getDetails());                     //second method
        deadLineLabel.setText(item.getDeadline().toString());

    }
}
