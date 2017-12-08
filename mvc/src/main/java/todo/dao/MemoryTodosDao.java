package todo.dao;

import todo.model.TodoListModel;
import todo.model.TodoModel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryTodosDao implements TodosDao {

    private Map<String, TodoListModel> todoLists = new ConcurrentHashMap<>();

    private AtomicInteger todoIdSeq = new AtomicInteger();


    @Override
    public void addTodo(String listName, String todo) {
        TodoListModel todoListModel = todoLists.get(listName);

        TodoModel todoModel = new TodoModel();
        todoModel.setTodoname(todo);
        todoModel.setTodoid(todoIdSeq.incrementAndGet());

        todoListModel.addTodo(todoModel);
    }


    @Override
    public Map<String, TodoListModel> getAllTodoLists() {
        return Collections.unmodifiableMap(todoLists);
    }


    @Override
    public void deleteTodoList(String listName) {
        todoLists.remove(listName);
    }


    @Override
    public void addTodoList(String listName) {
        TodoListModel todoListModel = new TodoListModel();
        todoListModel.setName(listName);
        todoListModel.setTodos(new HashSet<>());

        todoLists.putIfAbsent(listName, todoListModel);
    }


    @Override
    public void deleteTodo(String listName, String todo) {
        TodoModel todoModel = new TodoModel();
        todoModel.setTodoname(todo);
        todoModel.setListname(listName);
        Set<TodoModel> todos = todoLists.get(listName).getTodos();
        todos.removeIf(next -> next.getTodoname().equals(todo));
    }
}
