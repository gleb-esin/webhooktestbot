package org.example.game;

import lombok.Data;
import org.example.controller.DeckController;
import org.example.controller.PlayerController;
import org.example.controller.TableController;
import org.example.controller.move.Attack;
import org.example.controller.move.Defence;
import org.example.controller.move.Throw;
import org.example.model.Player;
import org.example.model.Table;
import org.example.monitor.GameMonitor;
import org.example.monitor.PlayerMonitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.beans.ConstructorProperties;
import java.util.UUID;

import static org.example.controller.moveValidator.ThrowValidator.isThrowPossible;
import static org.example.service.MessageHandler.sendNotificationToAll;

@Component
@Data
public class ThrowInFool implements Game {
    private UUID gameID;
    private DeckController deckController;
    private TableController tableController;
    private PlayerController playerController;

    @ConstructorProperties({"gameID"})
    public ThrowInFool(@Value("#{T(java.util.UUID).randomUUID()}") UUID gameID) {
        this.gameID = gameID;
        this.playerController = new PlayerController(PlayerMonitor.getThrowInFoolWaiterList());
        this.deckController = new DeckController(this.gameID);
        this.tableController = new TableController(deckController.getDeck().getTrump());
        GameMonitor.addThrowInFoolPlayers(gameID, playerController.getPlayers());
    }

    public void play() {
        Attack attack = new Attack();
        Defence defence = new Defence();
        Throw aThrow = new Throw();
        Table table = tableController.getTable();
        dealCards();
        playerController.setPlayersTurn();

        gameloop:
        while (!playerController.isGameOver()) {
            Player attacker = playerController.getAttacker();
            Player defender = playerController.getDefender();

            //init move
            attack.init(playerController, tableController);
            attack.move(playerController.getAttacker(), playerController.getPlayers(), tableController);
            if (playerController.isPlayerWinner(attacker, deckController.getDeck())) break;

            //defence move
            if (!playerController.isGameOver()) {
                defence.init(playerController, tableController);
                defence.move(playerController.getDefender(), playerController.getPlayers(), tableController);
                if (defender.getRole().equals("binder")) playerController.setBinder(defender);
                if (playerController.isPlayerWinner(defender, deckController.getDeck())) break;
            }

            //throw move
            //If the Game doesn't finnish...
            if (!playerController.isGameOver()) {
                ///...for each thrower....
                for (Player thrower : playerController.getPlayersQueue()) {
                    //...check if thrower can throw it.
                    while (isThrowPossible(tableController.getAll(), thrower.getPlayerHand()) && !defender.getPlayerHand().isEmpty()) {
                        int numberOfUnbeatenCards = table.getUnbeatenCards().size();
                        // If thrower can throw send initial notification to all waitingPlayers...
                        sendNotificationToAll(playerController.getPlayers(), "------------------------------\n" +
                                thrower.getName() + " может подкинуть" +
                                "\n" + tableController.getTable() +
                                "\n------------------------------");
                        ///...and make a throw move.
                        aThrow.move(thrower, playerController.getPlayers(), tableController);
                        //If thrower became the winner - break game loop
                        if (playerController.isPlayerWinner(thrower, deckController.getDeck())) break gameloop;
                        // if the thrower still didn't throw...
                        boolean throwerDidntThrow = numberOfUnbeatenCards == table.getUnbeatenCards().size();
                        //...break throw loop
                        if (throwerDidntThrow) break;
                        //If the Game still doesn't finnish and table has unbeaten cards...
                        if (!playerController.isGameOver() && !table.getUnbeatenCards().isEmpty()) {
                            //...and defender are not binder...
                            if (!defender.getRole().equals("binder")) {
                                //...make a defence move.
                                defence.init(playerController, tableController);
                                defence.move(playerController.getDefender(), playerController.getPlayers(), tableController);
                                if (playerController.isPlayerWinner(defender, deckController.getDeck()))
                                    break gameloop;
                            }
                        }
                    }
                }
            }
            if (defender.getRole().equals("binder")) {
                playerController.setBinder(defender);
                sendNotificationToAll(playerController.getPlayers(), playerController.getBinder().getName() + " забирает карты " + tableController.getAll());
                playerController.getBinder().getPlayerHand().addAll(tableController.getAll());
            }

            tableController.clear();
            if (deckController.getDeck().isEmpty()) {
                sendNotificationToAll(playerController.getPlayers(), "Колода пуста!");
            } else
                deckController.fillUpTheHands(playerController.getPlayersQueue(), defender, deckController.getDeck());
            playerController.changeTurn();
        }
        sendNotificationToAll(playerController.getPlayers(), "Победил " + playerController.getWinner().getName() + "!");
    }

    private void dealCards() {
        for (Player player : playerController.getPlayers()) {
            deckController.fillUpThePlayersHand(player, deckController.getDeck());
        }
    }
}
