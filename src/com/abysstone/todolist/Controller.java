package com.abysstone.todolist;

import com.abysstone.todolist.datamodel.TodoData;
import com.abysstone.todolist.datamodel.TodoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

//import java.awt.event.KeyEvent;

public class Controller {

    private ObservableList<TodoItem> FIRSTENTRY;
    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private TextArea itemDetailsTextArea;
    @FXML
    private Label deadLineLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private ToggleButton filterToggleButton;

    private FilteredList<TodoItem> filteredList;

    private Predicate<TodoItem> wantAllItems;
    private Predicate<TodoItem> wantTodaysItems;

    public void initialize() {

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        listContextMenu.getItems().add(deleteMenuItem);

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




//        TodoItem hardCoded = new TodoItem("Welcome To Manas TodoList", "Add as many number of notes as you wish." +
//                "Keep in mind the notes you add gets srynchonized with every new update and so new ones will appear towards the bootom of the List" +
//                " while old ones stacking near the top. Also look after for the items turning red as they are due on todays date. Thanks and wish" +
//                " you all the best with your goals.!", LocalDate.of(1970, Month.JANUARY, 1));
//
//        FIRSTENTRY = new ObservableList<TodoItem>() {
//        @Override
//        public void add ( int index, TodoItem element){
//
//        }
//    };
//
//FIRSTENTRY.add(hardCoded); //issue observable list is abstract(so binding is the only way is the real reason behind data binding implementation for safe control synch).







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
        }); //calling full initialization probably coz sorted is also working after adding a new entry after creating sorted list.



//        filteredList = new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(),
//                new Predicate<TodoItem>() {
//                    @Override
//                    public boolean test(TodoItem item) {
//                        return true;
//                    }
//                });

//        SortedList<TodoItem> sortedList = new SortedList<TodoItem>(TodoData.getInstance().getTodoItems(),
//                new Comparator<TodoItem>() {
//                    @Override
//                    public int compare(TodoItem o1, TodoItem o2) {
//                        return o1.getDeadline().compareTo(o2.getDeadline());
//                    }
//                });

        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return true;
            }
        };

        wantTodaysItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return (item.getDeadline().equals(LocalDate.now()));
            }
        };

        filteredList = new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(),wantAllItems);

        SortedList<TodoItem> sortedList = new SortedList<TodoItem>(filteredList,
                new Comparator<TodoItem>() {
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        return o1.getDeadline().compareTo(o2.getDeadline());
                    }
                });



       // todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
        //todoListView.setItems(TodoData.getInstance().getTodoItems());                               //BINDING and observable population
        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();





        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                //return null;
                ListCell<TodoItem> cell = new ListCell<TodoItem>() {
                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if (item.getDeadline().isBefore(LocalDate.now())) {
                                setTextFill(Color.DARKORANGE);
                            } else if (item.getDeadline().equals(LocalDate.now())) {
                                setTextFill(Color.RED);
                            } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.BROWN);
                            }else if (item.getDeadline().isAfter(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };



                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(listContextMenu);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        });
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
//            System.out.println("Cancel pressed");
//        }

    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            }
        }
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

    public void deleteItem(TodoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item : "+ item.getShortDescription());
        alert.setContentText("Are you sure? Press Ok to confirm or Cancel to back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }


//    public void handleFilterButton(){
//        if(filterToggleButton.isSelected()){
//            filteredList.setPredicate(new Predicate<TodoItem>() {
//                @Override
//                public boolean test(TodoItem item) {
//                    return (item.getDeadline().equals(LocalDate.now()));
//                }
//            });
//        }else {
//            filteredList.setPredicate(new Predicate<TodoItem>() {
//                @Override
//                public boolean test(TodoItem item) {
//                    return true;
//                }
//            });
//        }
//    }
    @FXML
    public void handleFilterButton(){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if(filterToggleButton.isSelected()){
            filteredList.setPredicate(wantTodaysItems);
            if(filteredList.isEmpty()){
                itemDetailsTextArea.clear();
                deadLineLabel.setText("");
            }else if(filteredList.contains(selectedItem)){
                todoListView.getSelectionModel().select(selectedItem);
            }else {
                todoListView.getSelectionModel().selectFirst();
            }
            }else{
                filteredList.setPredicate(wantAllItems);
                todoListView.getSelectionModel().select(selectedItem);
        }
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }



        }


