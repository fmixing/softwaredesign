package todo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import todo.db.TodoListRepository;
import todo.db.TodoRepository;
import todo.model.TodoListModel;
import todo.model.TodoModel;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Primary
@Component
public class DBTodosDao implements TodosDao {

    private final TodoListRepository todoListRepository;

    private final TodoRepository todoRepository;


    @Autowired
    public DBTodosDao(TodoListRepository todoListRepository, TodoRepository todoRepository) {
        this.todoListRepository = todoListRepository;
        this.todoRepository = todoRepository;
    }


    @Override
    public void addTodo(String listName, String todo) {
        TodoListModel todoList = getTodoList(listName);

        if (todoList == null) {
            throw new IllegalStateException("Todo list should be present");
        }

        boolean exists = todoList.getTodos().stream().anyMatch(todoModel -> Objects.equals(todoModel.getTodoname(), todo));
        if (exists) {
            return;
        }

        TodoModel todoModel = new TodoModel();
        todoModel.setTodoname(todo);
        todoModel.setListname(listName);
        todoList.getTodos().add(todoModel);

        todoRepository.save(todoModel);
        todoListRepository.save(todoList);
    }

    @Override
    public Map<String, TodoListModel> getAllTodoLists() {
        List<TodoListModel> all = todoListRepository.findAll();
        return all.stream().collect(Collectors.toMap(TodoListModel::getName, Function.identity()));
    }

    @Override
    public void deleteTodoList(String listName) {
        TodoListModel todoList = getTodoList(listName);
        if (todoList == null) {
            throw new IllegalStateException("Todo list should be present");
        }
        todoListRepository.delete(todoList);
    }

    @Override
    public void addTodoList(String listName) {
        TodoListModel todoList = getTodoList(listName);
        if (todoList != null) {
            return;
        }

        TodoListModel todoListModel = new TodoListModel();
        todoListModel.setName(listName);
        todoListModel.setTodos(new HashSet<>());

        todoListRepository.save(todoListModel);
    }

    @Override
    public void deleteTodo(String listName, String todo) {
        TodoListModel todoList = getTodoList(listName);
        Set<TodoModel> todos = todoList.getTodos();
        todos.removeIf(next -> next.getTodoname().equals(todo));

        TodoModel todoModel = getTodo(listName, todo);
        todoRepository.delete(todoModel);

        todoListRepository.save(todoList);
    }

    private TodoModel getTodo(String listName, String todo) {
        List<TodoModel> todoModels = todoRepository.find(listName, todo);
        if (todoModels.size() != 1) {
            throw new IllegalStateException("Todo should be present");
        }
        return todoModels.get(0);
    }


    private TodoListModel getTodoList(String listName) {
        TodoListModel todoListModel = new TodoListModel();
        todoListModel.setName(listName);
        return todoListRepository.findOne(Example.of(todoListModel)).orElse(null);
    }
}
