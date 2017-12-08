package todo.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "todolist")
public class TodoListModel {

    @Id
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "listname")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<TodoModel> todos;


    public void addTodo(TodoModel todo) {
        todos.add(todo);
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Set<TodoModel> getTodos() {
        return todos;
    }


    public void setTodos(Set<TodoModel> todos) {
        this.todos = todos;
    }
}
