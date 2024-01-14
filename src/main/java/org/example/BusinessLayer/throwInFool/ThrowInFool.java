package org.example.BusinessLayer.throwInFool;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.BusinessLayer.controller.DeckController;
import org.example.BusinessLayer.controller.PlayerController;
import org.example.BusinessLayer.controller.TableController;
import org.example.EntityLayer.Player;
import org.example.BusinessLayer.move.Attack;
import org.example.BusinessLayer.move.Defence;
import org.example.BusinessLayer.move.Throw;
import org.example.ServiseLayer.services.MessageService;
import org.example.ServiseLayer.services.PlayerInputValidator;
import org.example.BusinessLayer.states.State;

import java.util.List;
import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ThrowInFool implements State {
    UUID gameID;
    PlayerController playerController;
    DeckController deckController;
    TableController tableController;
    MessageService messageService;

    public ThrowInFool(MessageService messageService, UUID gameID, List<Player> players) {
        this.gameID = gameID;
        this.messageService = messageService;
        this.playerController = new PlayerController(players);
        this.deckController = new DeckController(this.gameID);
        this.tableController = new TableController(deckController.getDeck().getTrump());
    }

    public void play() {
        dealCards();
        playerController.setPlayersTurn();
        PlayerInputValidator playerInputValidator = new PlayerInputValidator(messageService);
        Attack attack = new Attack(messageService, playerInputValidator);
        Defence defence = new Defence(messageService, playerInputValidator);
        Throw throwMove = new Throw(messageService, playerInputValidator);
        boolean isDeckIsEmptyMessageNotSent = true;

        while (!playerController.isGameOver()) {
            attack.init(playerController, tableController);
            attack.move(playerController, tableController);
            if (playerController.isPlayerWinner(playerController.getAttacker(), deckController.getDeck())) break;
            defence.init(playerController);
            defence.move(playerController, tableController);
            if (playerController.isPlayerWinner(playerController.getDefender(), deckController.getDeck())) break;
            for (Player thrower : playerController.getThrowQueue()) {
                boolean throwIsPossible = throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender());
                while (throwIsPossible) {
                    boolean isDefenceNeeded = throwMove.move(thrower, playerController, tableController, deckController);
                    if (isDefenceNeeded) {
                        defence.init(playerController);
                        defence.move(playerController, tableController);
                    }
                    throwIsPossible = throwMove.isThrowPossible(tableController.getAll(), thrower, playerController.getDefender());
                }
            }
            if (playerController.getDefender().getRole().equals("binder")) {
                playerController.setBinder(playerController.getDefender());
                messageService.sendMessageToAll(playerController.getPlayers(), playerController.getBinder().getName() + " забирает карты " + tableController.getAll().toString().substring(1, tableController.getAll().toString().length() - 1));
                playerController.getBinder().getPlayerHand().addAll(tableController.getAll());
            }
            tableController.clear();
            if (deckController.getDeck().isEmpty()) {
                if (isDeckIsEmptyMessageNotSent) {
                    messageService.sendMessageToAll(playerController.getPlayers(), "\uD83D\uDE45 <b>Колода пуста!</b> \uD83D\uDE45");
                    isDeckIsEmptyMessageNotSent = false;
                }
            }
            deckController.fillUpTheHands(playerController.getThrowQueue(), playerController.getDefender());
            playerController.changeTurn();
        }
        messageService.sendMessageToAll(playerController.getPlayers(), "\uD83C\uDFC6 Победил " + playerController.getWinner().getName() + "! \uD83C\uDFC6");
    }

    private void dealCards() {
        for (Player player : playerController.getPlayers()) {
            deckController.fillUpThePlayersHand(player);
            //if player gets cards - add 1 game
            player.setGames(player.getGames() + 1);
        }
    }
}