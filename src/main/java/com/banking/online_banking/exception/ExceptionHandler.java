package com.banking.online_banking.exception;

import com.banking.online_banking.assistance.IdealResponse;
import com.banking.online_banking.assistance.ResponseStatus;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Component
public class ExceptionHandler extends ResponseEntityExceptionHandler {

   private IdealResponse idealResponse;

   public ExceptionHandler(){
       //default constructor
   }
   @Autowired
    public ExceptionHandler(IdealResponse idealResponse) {
        this.idealResponse = new IdealResponse(ResponseStatus.FAILURE, null, null);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public IdealResponse handleAuthenticationException(AuthenticationException e) {
        idealResponse.setError(e.getMessage());
        return idealResponse;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
    public IdealResponse handleUsernameNotFoundException(UsernameNotFoundException e) {
        idealResponse.setError(e.getMessage());
        return idealResponse;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameAlreadyExistsException.class)
    public IdealResponse handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        idealResponse.setError(e.getMessage());
        return idealResponse;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        ErrorDetails errorDetails = new ErrorDetails(statusCode, errorMap);
        idealResponse.setError(errorDetails);
        return new ResponseEntity<>(idealResponse, headers, statusCode);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public IdealResponse handleConstraintViolationException(ConstraintViolationException e){
       idealResponse.setError(e.getMessage());
       return idealResponse;
    }



    /*
    In Spring, the ResponseEntityExceptionHandler class provides default exception handling methods for various common
    exceptions. These default methods are automatically triggered when the corresponding exception occurs during request
    processing. However, there is no default handler method for TypeMismatchException in ResponseEntityExceptionHandler.
    The reason for this is that TypeMismatchException is a lower-level exception that occurs during data binding or
    conversion when the framework tries to convert a request parameter or a request body into the specified Java type.
    It typically occurs during request parameter binding or deserialization of request body data. Since data binding and
    type conversion are handled before controller advice kicks in, the ResponseEntityExceptionHandler doesn't have a
    default handler method for TypeMismatchException.

    To handle TypeMismatchException, you need to explicitly define an exception handler method in your @ControllerAdvice
    class.. By adding the @ExceptionHandler(TypeMismatchException.class) annotation to the method, you are instructing
    Spring to use this method specifically for handling TypeMismatchException and provide a custom response accordingly.
     */

   @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = "Invalid data type. Please provide a valid value of type " + ex.getRequiredType().getSimpleName() + ".";
        idealResponse.setError(error);
        return handleExceptionInternal(ex,idealResponse,headers,status,request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return new ResponseEntity<>(body, headers, statusCode);
    }

    //    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public IdealResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
//        String error = "Invalid data type for " + ex.getName() + ". Please provide a valid value of type " + ex.getRequiredType().getSimpleName() + ".";
//        idealResponse.setError(error);
//        return idealResponse;
//    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatusCode statusCode,
                                                                   WebRequest request) {
        // Customize the handling of NoHandlerFoundException
        String Error = "Sorry an internal error happened. Please try again later.";
        idealResponse.setError(Error);
        return new ResponseEntity<>(idealResponse, headers, statusCode);
    }
}
