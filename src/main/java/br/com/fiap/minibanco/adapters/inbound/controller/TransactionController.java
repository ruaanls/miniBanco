package br.com.fiap.minibanco.adapters.inbound.controller;

import br.com.fiap.minibanco.application.usecases.TransactionUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banco")
public class TransactionController
{

    @Autowired
    private TransactionUsecases transactionService;

    @PostMapping("/pix")
    public ResponseEntity<TransactionResponseDTO> transacao (@RequestBody @Valid TransactionRequestDTO request)
    {
        TransactionResponseDTO response = transactionService.transferir(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
