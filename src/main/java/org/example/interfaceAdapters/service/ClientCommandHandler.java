package org.example.interfaceAdapters.service;

/** Represents input port for
 * */
public interface ClientCommandHandler {
    void handleCommand(Long Id,String command);
}