package todo.dao;

import todo.model.TodoListModel;

import java.util.Map;

public interface TodosDao {

    void addTodo(String listName, String todo);

    Map<String, TodoListModel> getAllTodoLists();

    void deleteTodoList(String listName);

    void addTodoList(String listName);

    void deleteTodo(String listName, String todo);
}
