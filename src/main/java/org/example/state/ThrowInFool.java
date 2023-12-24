package org.example.state;

import lombok.AccessLevel;
import lombok.experimental.NonFinal;

import lombok.experimental.FieldDefaults;
import org.example.controller.DeckController;
import org.example.controller.PlayerController;
import org.example.controller.TableController;
import org.example.model.Player;
import org.example.model.Table;
import org.example.move.Attack;
import org.example.move.Defence;
import org.example.move.Throw;
import org.example.network.TelegramBot;
import org.example.service.PlayerInputValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowInFool {
    UUID gameID;
    @NonFinal
    boolean isGameOver = false;
    PlayerController playerController;
    DeckController deckController;
    TableController tableController;
    TelegramBot bot;

    @Autowired
    public ThrowInFool(TelegramBot bot,UUID gameID, List<Player> players) {
        this.gameID = gameID;
        this.bot = bot;
        this.playerController = new PlayerController(players);
        this.deckController = new DeckController(this.gameID);
        this.tableController = new TableController(deckController.getDeck().getTrump());}

    public void play() {
        dealCards();
        playerController.setPlayersTurn();
        Table table = tableController.getTable();
        PlayerInputValidator playerInputValidator = new PlayerInputValidator();
        Attack attack = new Attack(bot, playerInputValidator);
        Defence defence = new Defence(bot, playerInputValidator);
        Throw throwMove = new Throw(bot);

        gameloop:
        while (!playerController.isGameOver()) {
            Player attacker = playerController.getAttacker();
            Player defender = playerController.getDefender();

            //attackInit attackMove
            attack.init(playerController, tableController);
            attack.move(playerController.getAttacker(), tableController, playerController);
            if (playerController.isPlayerWinner(attacker, deckController.getDeck())) break;

            //defence Move
            if (!playerController.isGameOver()) {
                defence.init(playerController);
                defence.move(playerController.getDefender(), playerController.getPlayers(), tableController);
                if (defender.getRole().equals("binder")) playerController.setBinder(defender);
                if (playerController.isPlayerWinner(defender, deckController.getDeck())) break;
            }

            //throw attackMove
            //If the Game doesn't finnish...
            if (!playerController.isGameOver()) {
                ///...for each thrower....
                for (Player thrower : playerController.getThrowQueue()) {
                    //...check if thrower can throw it.
                    while (throwMove.isThrowPossible(tableController.getAll(), thrower.getPlayerHand()) && !defender.getPlayerHand().isEmpty()) {
                        int numberOfUnbeatenCards = table.getUnbeatenCards().size();
                        // If thrower can throw send initial notification to all waitingPlayers...
                        bot.sendMessageToAll(playerController.getPlayers(), "⚔️ " + thrower.getName() + " может подкинуть ⚔️");
                        ///...and make a throw attackMove.
                        throwMove.throwMove(thrower, playerController.getPlayers(), tableController, defender);
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
                                defence.init(playerController);
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
                bot.sendMessageToAll(playerController.getPlayers(), playerController.getBinder().getName() + " забирает карты " + tableController.getAll().toString().substring(1, tableController.getAll().toString().length() - 1));
                playerController.getBinder().getPlayerHand().addAll(tableController.getAll());
            }

            tableController.clear();
            if (deckController.getDeck().isEmpty()) {
                bot.sendMessageToAll(playerController.getPlayers(), "Колода пуста!");
            } else
                deckController.fillUpTheHands(playerController.getThrowQueue(), defender);
            playerController.changeTurn();
        }
        bot.sendMessageToAll(playerController.getPlayers(), "\uD83C\uDFC6 Победил " + playerController.getWinner().getName() + "! \uD83C\uDFC6");
    }

    private void dealCards() {
        for (Player player : playerController.getPlayers()) {
            deckController.fillUpThePlayersHand(player);
            //if player gets cards - add 1 game
            player.setGames(player.getGames() + 1);
        }
    }
}