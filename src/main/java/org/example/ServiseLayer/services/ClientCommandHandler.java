package org.example.ServiseLayer.services;

/** Represents input port for
 * */
public interface ClientCommandHandler {
    void handleCommand(Long Id,String command);
}