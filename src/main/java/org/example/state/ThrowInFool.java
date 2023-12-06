package org.example.state;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.controller.PlayerInputValidator;
import org.example.controller.move.Move;
import org.example.monitor.GameMonitor;
import org.example.monitor.PlayerMonitor;
import org.example.network.TelegramBot;

import lombok.experimental.FieldDefaults;
import org.example.controller.DeckController;
import org.example.controller.PlayerController;
import org.example.controller.TableController;
import org.example.model.Player;
import org.example.model.Table;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.example.controller.moveValidator.ThrowValidator.isThrowPossible;

@Component
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThrowInFool implements Move, PlayerInputValidator, GameMonitor, PlayerMonitor {
    UUID gameID;
    boolean isGameOver = false;
    TelegramBot bot;
    List<Player> players;

    PlayerController playerController;
    DeckController deckController;
    TableController tableController;

    Table table;


    public ThrowInFool(TelegramBot bot) {
        this.bot = bot;
        this.gameID = UUID.randomUUID();

        this.playerController = new PlayerController(getThrowInFoolWaiterList());
        this.deckController = new DeckController(this.gameID);
        this.tableController = new TableController(deckController.getDeck().getTrump());

        this.table = tableController.getTable();
        addThrowInFoolToGameMonitor(gameID, playerController.getPlayers());
    }

    public void execute() {
        dealCards();
        playerController.setPlayersTurn();

        gameloop:
        while (!playerController.isGameOver()) {
            Player attacker = playerController.getAttacker();
            Player defender = playerController.getDefender();

            //attackInit attackMove
            attackInit(bot, playerController, tableController);
            attackMove(bot, playerController.getAttacker(),  tableController);
            if (playerController.isPlayerWinner(attacker, deckController.getDeck())) break;

            //defence Move
            if (!playerController.isGameOver()) {
                defenceInit(bot, playerController, tableController);
                defenceMove(bot, playerController.getDefender(), playerController.getPlayers(), tableController);
                if (defender.getRole().equals("binder")) playerController.setBinder(defender);
                if (playerController.isPlayerWinner(defender, deckController.getDeck())) break;
            }

            //throw attackMove
            //If the Game doesn't finnish...
            if (!playerController.isGameOver()) {
                ///...for each thrower....
                for (Player thrower : playerController.getPlayersQueue()) {
                    //...check if thrower can throw it.
                    while (isThrowPossible(tableController.getAll(), thrower.getPlayerHand()) && !defender.getPlayerHand().isEmpty()) {
                        int numberOfUnbeatenCards = table.getUnbeatenCards().size();
                        // If thrower can throw send initial notification to all waitingPlayers...
                        sendMessageToAll(bot, playerController.getPlayers(), "------------------------------\n" +
                                thrower.getName() + " может подкинуть" +
                                "\n" + tableController.getTable() +
                                "\n------------------------------");
                        ///...and make a throw attackMove.
                        throwMove(bot, thrower, playerController.getPlayers(), tableController);
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
                                //...make a defence attackMove.
                                defenceInit(bot, playerController, tableController);
                                defenceMove(bot, playerController.getDefender(), playerController.getPlayers(), tableController);
                                if (playerController.isPlayerWinner(defender, deckController.getDeck()))
                                    break gameloop;
                            }
                        }
                    }
                }
            }
            if (defender.getRole().equals("binder")) {
                playerController.setBinder(defender);
                sendMessageToAll(bot, playerController.getPlayers(), playerController.getBinder().getName() + " забирает карты " + tableController.getAll());
                playerController.getBinder().getPlayerHand().addAll(tableController.getAll());
            }

            tableController.clear();
            if (deckController.getDeck().isEmpty()) {
                sendMessageToAll(bot, playerController.getPlayers(), "Колода пуста!");
            } else
                deckController.fillUpTheHands(playerController.getPlayersQueue(), defender, deckController.getDeck());
            playerController.changeTurn();
        }
        sendMessageToAll(bot, playerController.getPlayers(), "Победил " + playerController.getWinner().getName() + "!");
    }

    private void dealCards() {
        for (Player player : playerController.getPlayers()) {
            deckController.fillUpThePlayersHand(player, deckController.getDeck());
        }
    }
}