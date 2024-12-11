package br.com.pedroreis.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ( "/users" )
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping ( "/" )
    public ResponseEntity<?> create ( @RequestBody UserModel userModel ) {

        if ( this.userRepository.existsByUsername ( userModel.getUsername ( ) ) ) {
            return ResponseEntity.badRequest ( ).body ( "Usuário já existe" );
        }

        var passwordHashed = BCrypt.withDefaults ( ).hashToString ( 12 , userModel.getPassword ( ).toCharArray ( ) );

        userModel.setPassword ( passwordHashed );

        return ResponseEntity.status ( HttpStatus.CREATED ).body ( this.userRepository.save ( userModel ) );
    }
}
