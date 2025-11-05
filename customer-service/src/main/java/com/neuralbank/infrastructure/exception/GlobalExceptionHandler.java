package com.neuralbank.infrastructure.exception;

import com.neuralbank.infrastructure.rest.dto.response.ErrorResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    
    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);
    
    @Override
    public Response toResponse(Exception exception) {
        
        // LOG EL ERROR REAL (evitar ruido para 404 y otros errores esperados)
        if (exception instanceof NotFoundException) {
            LOG.debug("404 Not Found: recurso no encontrado");
        } else if (exception instanceof WebApplicationException webEx) {
            int s = webEx.getResponse() != null ? webEx.getResponse().getStatus() : 500;
            if (s >= 500) {
                LOG.error("WebApplicationException status=" + s, exception);
            } else {
                LOG.warn("WebApplicationException status=" + s + ": " + exception.getMessage());
            }
        } else {
            LOG.error("Error capturado en GlobalExceptionHandler", exception);
        }
        
        if (exception instanceof CustomerNotFoundException) {
            ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                Response.Status.NOT_FOUND.getStatusCode()
            );
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }

        if (exception instanceof NotFoundException) {
            ErrorResponse error = new ErrorResponse(
                "Recurso no encontrado",
                Response.Status.NOT_FOUND.getStatusCode()
            );
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        
        if (exception instanceof DuplicateCustomerException) {
            ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                Response.Status.CONFLICT.getStatusCode()
            );
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        
        if (exception instanceof InvalidCustomerDataException) {
            ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                Response.Status.BAD_REQUEST.getStatusCode()
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (exception instanceof WebApplicationException webEx) {
            int status = webEx.getResponse() != null ? webEx.getResponse().getStatus() : Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                status
            );
            return Response.status(status).entity(error).build();
        }
        
        // Error genérico
        ErrorResponse error = new ErrorResponse(
            "Error interno del servidor: " + exception.getMessage(),
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}