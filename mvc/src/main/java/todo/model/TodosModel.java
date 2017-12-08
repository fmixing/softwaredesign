package todo.model;

import java.util.List;

public class TodosModel {

    private List<TodoListModel> todoLists;


    public TodosModel(List<TodoListModel> todoLists) {
        this.todoLists = todoLists;
    }


    public List<TodoListModel> getTodoLists() {
        return todoLists;
    }


    public void setTodoLists(List<TodoListModel> todoLists) {
        this.todoLists = todoLists;
    }
}
