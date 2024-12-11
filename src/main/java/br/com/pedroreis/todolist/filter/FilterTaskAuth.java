package br.com.pedroreis.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.pedroreis.todolist.user.IUserRepository;
import br.com.pedroreis.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal ( HttpServletRequest request , HttpServletResponse response , FilterChain filterChain ) throws ServletException, IOException {

        var servletPath = request.getServletPath ( );

        if ( servletPath.equals ( "/tasks/" ) ) {

            var authorization = request.getHeader ( "Authorization" );

            var authEncoded = authorization.substring ( "Basic".length ( ) ).trim ( );

            byte[] authDecode = Base64.getDecoder ( ).decode ( authEncoded );

            var authString = new String ( authDecode );

            String[] credentials = authString.split ( ":" );

            String username = credentials[0];
            String password = credentials[1];

            UserModel userModel = userRepository.findByUsername ( username );

            if ( userModel == null ) {
                response.sendError ( 401 );
            } else {
                var passwordVerify = BCrypt.verifyer ( ).verify ( password.toCharArray ( ) , userModel.getPassword ( ) );

                if ( passwordVerify.verified ) {
                    request.setAttribute ( "idUser" , userModel.getId ( ) );
                    filterChain.doFilter ( request , response );
                } else {
                    response.sendError ( 401 );
                }
            }
        } else {
            filterChain.doFilter ( request , response );
        }
    }
}
