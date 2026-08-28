package com.venturini.notificacao.controller;

import com.venturini.notificacao.business.EmailService;
import com.venturini.notificacao.business.dto.TarefaDTO;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {


    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> enviarEmail(@RequestBody TarefaDTO tarefaDTO) {
        emailService.enviaEmail(tarefaDTO);
        return ResponseEntity.ok().build();
    }
}
