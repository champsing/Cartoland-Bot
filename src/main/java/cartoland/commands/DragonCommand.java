package cartoland.commands;

import cartoland.mini_games.ConnectFourGame;
import cartoland.mini_games.DragonGame;
import cartoland.mini_games.MiniGame;
import cartoland.utilities.Algorithm;
import cartoland.utilities.CommandBlocksHandle;
import cartoland.utilities.CommonFunctions;
import cartoland.utilities.JsonHandle;
import cartoland.utilities.ObjectAndString;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Random;

/**
 * {@code DragonCommand} is an execution when a user uses /dragon command. This class implements {@link ICommand}
 * interface, which is for the commands HashMap in {@link cartoland.events.CommandUsage}. This can be seen as a frontend
 * of the Dragon game.
 * 
 * @TODO 
 * (/dragon bet command ID)
 * JSON strings: dragon.
 * gave_up
 * start
 * bet.result
 * 
 * @since 2.2
 * @author champsing
 */

public class DragonCommand extends HasSubcommands {

	private static final Random random = new Random();
	private static final long MAXIMUM = 1000000L;
	private static final byte INVALID_BET = -1;

    public static final String BET = "bet";
    public static final String DRAW = "draw";
    public static final String COMPARE = "compare";
    public static final String GIVE_UP = "give_up";

    	public DragonCommand(MiniGame.MiniGameMap games)
	{
		super(4);

		subcommands.put(BET, new BetSubCommand(games));

		subcommands.put(DRAW, new DrawSubCommand());

		subcommands.put(COMPARE, event ->
		{
			long userID = event.getUser().getIdLong();
			MiniGame playing = games.get(userID);

			if (playing == null) //沒有在玩遊戲 但還是使用了/dragon
			{
				event.reply(JsonHandle.getString(userID, "mini_game.not_playing", "</dragon bet: (/dragon bet command ID))>"))
						.setEphemeral(true)
						.queue();
				return;
			}

			//已經有在玩遊戲
			event.reply(playing instanceof DragonGame dragonGame ? //是在玩射龍門
							dragonGame.getCard() :
							JsonHandle.getString(userID, "mini_game.playing_another_game", JsonHandle.getString(userID, playing.gameName() + ".name")))
					.setEphemeral(true)
					.queue();
		});

		subcommands.put(GIVE_UP, event ->
		{
			long userID = event.getUser().getIdLong();
			MiniGame playing = games.get(userID);
			if (playing == null)
			{
				event.reply(JsonHandle.getString(userID, "mini_game.no_game_gave_up")).queue();
				return;
			}
			if (!(playing instanceof DragonGame dragonGame))
			{
				event.reply(JsonHandle.getString(userID, "mini_game.playing_another_game", JsonHandle.getString(userID, playing.gameName() + ".name")))
						.setEphemeral(true)
						.queue();
				return;
			}
			games.remove(userID);
			event.reply(JsonHandle.getString(userID, "dragon.gave_up") + dragonGame.giveUp()).queue();
		});
	}

    private static class BetSubCommand extends GameSubcommand
	{
		private BetSubCommand(MiniGame.MiniGameMap games)
		{
			super(games);
		}
		
		@Override
		public void commandProcess(SlashCommandInteractionEvent event)
		{
			// 0. 先知道來者何人
			long userID = event.getUser().getIdLong();
			
			// 1. 檢查有沒有進行中遊戲

			MiniGame playing = games.get(userID);

			if (playing != null) //已經有在玩遊戲
			{
				event.reply(JsonHandle.getString(userID, "mini_game.playing_another_game", JsonHandle.getString(userID, playing.gameName() + ".name")))
						.setEphemeral(true)
						.queue();
				return;
			}

			// 2. 檢查財務資訊，沒錢不能賭

			CommandBlocksHandle.LotteryData lotteryData = CommandBlocksHandle.getLotteryData(userID);
			long nowHave = lotteryData.getBlocks();

			ObjectAndString validBet = cartoland.commands.LotteryCommand.createValidBet(event.getOption("bet", "", CommonFunctions.getAsString), userID, nowHave);
			String errorMessage = validBet.string();
			if (!errorMessage.isEmpty()) //有錯誤訊息
			{
				event.reply(errorMessage).setEphemeral(true).queue();
				return;
			}


			// Final. 都通過才放行開始遊戲

			DragonGame newGame = new DragonGame();
			event.reply(JsonHandle.getString(userID, "dragon.start") + newGame.drawCard()).queue();
			games.put(userID, newGame);


			long bet = (Long) validBet.object(); //沒有錯誤訊息 就轉換
			long afterBet;
			String result;
			boolean win = random.nextBoolean(); //輸贏
			boolean showHand = bet == nowHave; //梭哈

			if (win) //賭贏
			{
				afterBet = Algorithm.safeAdd(nowHave, bet);
				result = JsonHandle.getString(userID, "lottery.bet.win");
			}
			else //賭輸
			{
				afterBet = nowHave - bet;
				result = JsonHandle.getString(userID, "lottery.bet.lose");
			}

			StringBuilder replyBuilder = new StringBuilder(JsonHandle.getString(userID, "dragon.bet.result", bet, result, afterBet));
			if (showHand)
			{
				if (win)
					replyBuilder.append("\nhttps://www.youtube.com/watch?v=RbMjxQEZ1IQ");
				else
					replyBuilder.append('\n').append(JsonHandle.getString(userID, "lottery.bet.play_with_your_limit"));
			}
			event.reply(replyBuilder.toString()).queue(); //盡快回覆比較好

			lotteryData.addGame(win, showHand); //紀錄勝場和是否梭哈
			lotteryData.setBlocks(afterBet); //設定方塊
		}
	}

	private static class DrawSubCommand implements ICommand {
		@Override
		public void commandProcess(SlashCommandInteractionEvent event) {

		}
	}

}

