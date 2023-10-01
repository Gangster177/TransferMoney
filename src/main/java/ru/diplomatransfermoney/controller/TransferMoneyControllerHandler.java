package ru.diplomatransfermoney.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.diplomatransfermoney.exception.ConfirmOperationException;
import ru.diplomatransfermoney.exception.ErrorInputDataException;
import ru.diplomatransfermoney.exception.ErrorTransferException;
import ru.diplomatransfermoney.model.response.ExceptionResponse;

@ControllerAdvice
public class TransferMoneyControllerHandler {


    @ExceptionHandler(ErrorInputDataException.class)
    public ResponseEntity<ExceptionResponse> handleErrorInputDataException(@NonNull final ErrorInputDataException exc) {
        return new ResponseEntity<>(new ExceptionResponse(exc.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorTransferException.class)
    public ResponseEntity<ExceptionResponse> handleErrorTransferException(@NonNull final ErrorTransferException exc) {
        return new ResponseEntity<>(new ExceptionResponse(exc.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConfirmOperationException.class)
    public ResponseEntity<ExceptionResponse> handleConfirmOperationException(@NonNull final ConfirmOperationException exc) {
        return new ResponseEntity<>(new ExceptionResponse(exc.getMessage(), 400), HttpStatus.BAD_REQUEST);
    }
}
