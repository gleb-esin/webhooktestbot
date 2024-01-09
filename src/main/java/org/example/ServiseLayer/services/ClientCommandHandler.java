package org.example.ServiseLayer.services;

/**
 * ClientCommandHandler handles messages and commands from the user.
 */
public interface ClientCommandHandler {
    /**
     * Handle a messages and commands from the user.
     *
     * @param id      the ID of the user
     * @param command the command to be handled
     */
    void handleCommand(Long id, String command);
}