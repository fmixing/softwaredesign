package todo.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import todo.model.TodoModel;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoModel, Integer> {
    @Query("select t from TodoModel t where t.listname = :name and t.todoname = :todo")
    List<TodoModel> find(@Param("name") String name, @Param("todo") String todo);
}
