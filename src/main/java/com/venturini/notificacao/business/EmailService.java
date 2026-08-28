package com.venturini.notificacao.business;

import com.venturini.notificacao.business.dto.TarefaDTO;
import com.venturini.notificacao.infrastructure.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    // FROM -> DE
    @Value("${envio.email.remetente}")
    private String remetente;

    // IDENTIFICAÇÃO DE REMETENTE
    @Value("${envio.email.nomeRemetente}")
    private String nomeRemetente;

    public void enviaEmail(TarefaDTO tarefaDTO) {
        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(mensagem, true, StandardCharsets.UTF_8.name());

            // Envio -> Remetente
            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));

            // Para - Destinatário
            mimeMessageHelper.setTo(InternetAddress.parse(tarefaDTO.getEmailUsuario()));

            // Assunto do Email
            mimeMessageHelper.setSubject("Notificação da Tarefa");

            Context context = new Context();
            context.setVariable("nomeTarefa", tarefaDTO.getNomeTarefa());
            context.setVariable("dataEvento", tarefaDTO.getDataEvento());
            context.setVariable("descricao", tarefaDTO.getDescricao());
            String template = templateEngine.process("notificacao", context);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mensagem);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailException("Erro ao enviar o email " + e.getCause());
        }
    }
}
