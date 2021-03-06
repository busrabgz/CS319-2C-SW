package SevenWonders.GameLogic;

import SevenWonders.GameLogic.Deck.Card.Card;
import SevenWonders.GameLogic.Enums.CARD_COLOR_TYPE;
import SevenWonders.GameLogic.Enums.CARD_EFFECT_TYPE;
import SevenWonders.GameLogic.Enums.HERO_EFFECT_TYPE;
import SevenWonders.GameLogic.Enums.WONDER_EFFECT_TYPE;
import SevenWonders.GameLogic.Game.GameModel;
import SevenWonders.GameLogic.Player.PlayerModel;
import SevenWonders.GameLogic.Wonder.GodsAndHeroes.Hero;
import SevenWonders.GameLogic.Wonder.Wonder;

import java.util.Random;

public class ScoreController {
    
    // TODO: Optimize
    public static int calculateScore(int playerID, GameModel model) {
        PlayerModel myPlayer = model.getPlayerList()[playerID];
        PlayerModel left = model.getPlayerList()[(playerID + 6) % 7];
        PlayerModel right = model.getPlayerList()[(playerID + 1) % 7];

        int score = 0;

        score += calculateMilitaryConflicts(myPlayer);
        score += calculateTreasuryContents(myPlayer);
        score += calculateWonders(myPlayer);
        score += calculateCivilianStructures(myPlayer);
        score += calculateCommercialStructures(myPlayer);
        score += calculateScientificStructures(myPlayer);
        score += calculateGuilds(myPlayer, left, right);
        score += calculateGods(myPlayer);

        return score;
    }

    public static int calculateMilitaryConflicts(PlayerModel playerModel) {
        return playerModel.getWarPoints();
    }

    public static int calculateTreasuryContents(PlayerModel playerModel) {
        return playerModel.getGold() / 3;
    }

    public static int calculateWonders(PlayerModel playerModel) {
        Wonder wonder = playerModel.getWonder();
        int score = 0;
        for (int i = 0 ; i < wonder.getCurrentStageIndex() ; i++) {
            if (wonder.getStages()[i].getWonderEffect().getEffectType() == WONDER_EFFECT_TYPE.GIVE_VICTORY_POINTS) {
                score += wonder.getStages()[i].getWonderEffect().getVictoryPoints();
            }
        }
        return score;
    }

    public static int calculateCivilianStructures(PlayerModel playerModel) {
        int score = 0;
        for (Card card : playerModel.getConstructionZone().getConstructedCards()) {
            if (card.getColor() == CARD_COLOR_TYPE.BLUE) {
                score += card.getCardEffect().getVictoryPoints();
            }
        }

        for (Hero hero: playerModel.getHeroes())
        {
            if(hero.getHeroEffect() == HERO_EFFECT_TYPE.GRANT_THREE_VP)
                score = score + 3;
        }
        return score;
    }

    public static int calculateScientificStructures(PlayerModel playerModel) {
        int drawings = 0, mechanics = 0, writings = 0;
        int tradersGuild = 0, wonder = 0;

        for (Card card : playerModel.getConstructionZone().getConstructedCards()) {
            if (card.getColor() == CARD_COLOR_TYPE.GREEN) {
                switch (card.getCardEffect().getEffectType()) {
                    case SCIENCE_DRAWINGS:
                        drawings++;
                        break;
                    case SCIENCE_MECHANICS:
                        mechanics++;
                        break;
                    case SCIENCE_WRITINGS:
                        writings++;
                        break;
                }
            } else if (card.getCardEffect().getEffectType() == CARD_EFFECT_TYPE.SCIENTISTS_GUILD) {
                tradersGuild = 1;
            }
        }

        for(Hero hero: playerModel.getHeroes()){
            if (hero.getHeroEffect() == HERO_EFFECT_TYPE.GRANT_RANDOM_SCIENCE)
            {
                Random rand = new Random();

                int random = rand.nextInt(3);
                if(random == 0)
                    drawings++;
                else if(random == 1)
                    mechanics++;
                else if(random == 2)
                    writings++;
            }
        }
        if(playerModel.getWonder().getCurrentStageIndex() >= 2 && playerModel.getWonder().getStages()[1].getWonderEffect().getEffectType() == WONDER_EFFECT_TYPE.CHOOSE_ONE_SCIENCE )
            wonder = 1;

        int result =  recursive(tradersGuild, wonder, drawings, mechanics, writings);
        return result;
    }

    public static int calculateCommercialStructures(PlayerModel playerModel) {
        int score = 0;
        for (Card card : playerModel.getConstructionZone().getConstructedCards()) {
            if (card.getColor() == CARD_COLOR_TYPE.YELLOW) {
                switch (card.getCardEffect().getEffectType()) {
                    case GET_MONEY_AND_VP_PER_BROWN:
                        score += countColor(playerModel, CARD_COLOR_TYPE.BROWN) * card.getCardEffect().getVictoryPoints();
                        break;
                    case GET_MONEY_AND_VP_PER_YELLOW:
                        score += countColor(playerModel, CARD_COLOR_TYPE.YELLOW) * card.getCardEffect().getVictoryPoints();
                        break;
                    case GET_MONEY_AND_VP_PER_GRAY:
                        score += countColor(playerModel, CARD_COLOR_TYPE.GRAY) * card.getCardEffect().getVictoryPoints();
                        break;
                    case GET_MONEY_AND_VP_PER_WONDER:
                        score += playerModel.getWonder().getCurrentStageIndex() * card.getCardEffect().getVictoryPoints();
                        break;
                }
            }
        }
        return score;
    }

    public static int calculateGuilds(PlayerModel playerModel, PlayerModel left, PlayerModel right) {
        int score = 0;
        for (Card card : playerModel.getConstructionZone().getConstructedCards()) {
            if (card.getColor() == CARD_COLOR_TYPE.PURPLE) {
                switch (card.getCardEffect().getEffectType()) {
                    case WORKERS_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (countColor(left, CARD_COLOR_TYPE.BROWN) +
                                countColor(right, CARD_COLOR_TYPE.BROWN));
                        break;
                    case CRAFTSMENS_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (countColor(left, CARD_COLOR_TYPE.GRAY) +
                                countColor(right, CARD_COLOR_TYPE.GRAY));
                        break;
                    case TRADERS_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (countColor(left, CARD_COLOR_TYPE.YELLOW) +
                                 countColor(right, CARD_COLOR_TYPE.YELLOW));
                        break;
                    case PHILOSOPHERS_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (countColor(left, CARD_COLOR_TYPE.GREEN) +
                                countColor(right, CARD_COLOR_TYPE.GREEN));
                        break;
                    case SPIES_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (countColor(left, CARD_COLOR_TYPE.RED) +
                                countColor(right, CARD_COLOR_TYPE.RED));
                        break;
                    case STRATEGISTS_GUILD:
                        // TODO: Change implementation of war points
                        break;
                    case SHIPOWNERS_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (countColor(playerModel, CARD_COLOR_TYPE.BROWN) +
                                        countColor(playerModel, CARD_COLOR_TYPE.GRAY) +
                                        countColor(playerModel, CARD_COLOR_TYPE.PURPLE));
                        break;
                    case MAGISTRATES_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (countColor(left, CARD_COLOR_TYPE.BLUE) +
                                 countColor(right, CARD_COLOR_TYPE.BLUE));
                        break;
                    case BUILDERS_GUILD:
                        score += card.getCardEffect().getVictoryPoints() *
                                (playerModel.getWonder().getCurrentStageIndex()+
                                left.getWonder().getCurrentStageIndex()+
                                right.getWonder().getCurrentStageIndex());
                        break;
                }
            }
        }
        return score;
    }

    private static int calculateGods(PlayerModel playerModel){
        return playerModel.getWonder().getGod().getVpEachTurn();
    }

    public static int countColor(PlayerModel playerModel, CARD_COLOR_TYPE color) {
        int count = 0;
        for (Card card : playerModel.getConstructionZone().getConstructedCards()) {
            if (card.getColor() == color) {
                count++;
            }
        }
        return count;
    }

    private static int recursive(int tradersGuild, int wonder, int drawings, int mechanics, int writings){

        if( tradersGuild == 0 && wonder == 0){
            return drawings*drawings + mechanics*mechanics + writings*writings + Math.min(drawings, Math.min(mechanics, writings))*7;}

        if ( (tradersGuild == 1 && wonder == 0) || (tradersGuild == 0 && wonder == 1)){
            return Math.max( Math.max(recursive(0,0,drawings + 1, mechanics, writings),
                                      recursive(0,0,drawings,mechanics + 1,writings)),
                                      recursive(0,0,drawings,mechanics, writings + 1));
        }
        else if ( tradersGuild == 1 && wonder == 1){
            return Math.max(Math.max(Math.max( Math.max(Math.max( recursive(0,0, drawings + 2, mechanics, writings),
                                      recursive(0, 0, drawings, mechanics + 2, writings)),
                                      recursive(0,0,drawings,mechanics,writings + 2)),
                                      recursive(0,0, drawings + 1, mechanics + 1, writings)),
                                      recursive(0,0,drawings + 1, mechanics, writings + 1)),
                                      recursive(0,0,drawings,mechanics + 1, writings + 1));
        }

        return 0;
    }
}
