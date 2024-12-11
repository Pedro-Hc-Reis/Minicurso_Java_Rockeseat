package br.com.pedroreis.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping ( "/tasks" )
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping ( "/" )
    public ResponseEntity<?> create ( @RequestBody TaskModel taskModel , HttpServletRequest request ) {

        var idUser = ( UUID ) request.getAttribute ( "idUser" );
        taskModel.setIdUser ( idUser );

        var currentDate = LocalDateTime.now ( );

        if ( currentDate.isAfter ( taskModel.getStartAt ( ) ) || currentDate.isAfter ( taskModel.getEndAt ( ) ) ) {
            return ResponseEntity.badRequest ( ).body ( "A data de início / data de término deve ser maior do que a data atual" );
        } else if ( taskModel.getStartAt ( ).isAfter ( taskModel.getEndAt ( ) ) ) {
            return ResponseEntity.badRequest ( ).body ( "A data de início deve ser menor que a data de término" );
        }

        return ResponseEntity.ok ( this.taskRepository.save ( taskModel ) );
    }
}
