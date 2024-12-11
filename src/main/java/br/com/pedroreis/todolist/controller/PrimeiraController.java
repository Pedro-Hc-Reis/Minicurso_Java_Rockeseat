package br.com.pedroreis.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ( "/primeiraRota" )
public class PrimeiraController {

    @GetMapping ( "/primeiroMetodo" )
    public String primeiraMensagem ( ) {
        return "Funcionou";
    }
}
