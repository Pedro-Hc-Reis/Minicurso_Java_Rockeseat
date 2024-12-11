package br.com.pedroreis.todolist.task;

import br.com.pedroreis.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping ( "/" )
    public ResponseEntity<?> list ( HttpServletRequest request ) {
        var idUser = ( UUID ) request.getAttribute ( "idUser" );
        return ResponseEntity.ok ( this.taskRepository.findByIdUser ( idUser ) );
    }

    @PutMapping ( "/{id}" )
    public ResponseEntity<?> update ( @RequestBody TaskModel taskModel , HttpServletRequest request , @PathVariable UUID id ) {

        var currentTask = this.taskRepository.findById ( id ).orElse ( null );

        if ( currentTask == null ) {
            return ResponseEntity.badRequest ( ).body ( "Task não encontrada com este id" );
        }

        Utils.copyNonNullProperties ( taskModel , currentTask );

        return ResponseEntity.ok ( this.taskRepository.save ( currentTask ) );
    }
}
