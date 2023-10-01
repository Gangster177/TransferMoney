package ru.diplomatransfermoney.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplomatransfermoney.model.reqest.ConfirmRequest;
import ru.diplomatransfermoney.model.reqest.TransferRequest;
import ru.diplomatransfermoney.model.response.TransferAndConfirmResponse;
import ru.diplomatransfermoney.service.TransferMoneyService;

@RestController
@CrossOrigin(origins = "${cross.origin.host.name}")
public class TransferMoneyController {

    private final TransferMoneyService transferMoneyService;

    @Autowired
    public TransferMoneyController(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferAndConfirmResponse> postTransfer(@RequestBody TransferRequest request){
        return  ResponseEntity.ok(transferMoneyService.transfer(request));
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<TransferAndConfirmResponse> postConfirmOperation(@RequestBody ConfirmRequest request){
        return ResponseEntity.ok(transferMoneyService.confirmOperation(request));
    }
}
