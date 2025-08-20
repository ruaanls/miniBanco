package br.com.fiap.minibanco.adapters.inbound.controller;

import br.com.fiap.minibanco.application.usecases.TransactionUsecases;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionRequestDTO;
import br.com.fiap.minibanco.core.transactionals.DTO.TransactionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banco")
public class TransactionController
{

    @Autowired
    private final TransactionUsecases transactionService;

    public TransactionController(TransactionUsecases transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/pix")
    public ResponseEntity<TransactionResponseDTO> transacao (@RequestBody @Valid TransactionRequestDTO request)
    {
        TransactionResponseDTO response = transactionService.transferir(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/extrato/{cpf}")
    public ResponseEntity<Page<TransactionResponseDTO>> extrato (@RequestParam(defaultValue = "0") Integer page, @PathVariable String cpf)
    {
        Pageable pageable = PageRequest
                .of(page, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<TransactionResponseDTO> response =  transactionService.extrato(pageable, cpf);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
