package cartoland.commands;

import cartoland.mini_games.ConnectFourGame;
import cartoland.mini_games.DragonGame;
import cartoland.mini_games.MiniGame;
import cartoland.utilities.CommandBlocksHandle;
import cartoland.utilities.CommonFunctions;
import cartoland.utilities.JsonHandle;
import cartoland.utilities.ObjectAndString;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class DragonCommand extends HasSubcommands {
    public static final String BET = "bet";
    public static final String DRAW = "draw";
    public static final String COMPARE = "compare";
    public static final String GIVE_UP = "give_up";

    	public DragonCommand(MiniGame.MiniGameMap games)
	{
		super(4);

		subcommands.put(BET, new BetSubCommand());

		//不要再想著用按鈕了 不能超過5個按鈕
		subcommands.put(DRAW, new DrawSubCommand());

		subcommands.put(COMPARE, event ->
		{
			long userID = event.getUser().getIdLong();
			MiniGame playing = games.get(userID);

			if (playing == null) //沒有在玩遊戲 但還是使用了/connect_four board
			{
				event.reply(JsonHandle.getString(userID, "mini_game.not_playing", "</connect_four start:1123462079546937485>"))
						.setEphemeral(true)
						.queue();
				return;
			}

			//已經有在玩遊戲
			event.reply(playing instanceof ConnectFourGame connectFour ? //是在玩四子棋
							connectFour.getBoard() :
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
			if (!(playing instanceof DragonGame dragon))
			{
				event.reply(JsonHandle.getString(userID, "mini_game.playing_another_game", JsonHandle.getString(userID, playing.gameName() + ".name")))
						.setEphemeral(true)
						.queue();
				return;
			}
			games.remove(userID);
			event.reply(JsonHandle.getString(userID, "dragon.gave_up") + DragonGame.giveUp()).queue();
		});
	}

    private static class BetSubCommand implements ICommand
	{
		@Override
		public void commandProcess(SlashCommandInteractionEvent event)
		{
			long userID = event.getUser().getIdLong();
			CommandBlocksHandle.LotteryData lotteryData = CommandBlocksHandle.getLotteryData(userID);
			long nowHave = lotteryData.getBlocks();

			ObjectAndString validBet = createValidBet(event.getOption("bet", "", CommonFunctions.getAsString), userID, nowHave);
			String errorMessage = validBet.string();
			if (!errorMessage.isEmpty()) //有錯誤訊息
			{
				event.reply(errorMessage).setEphemeral(true).queue();
				return;
			}
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

			StringBuilder replyBuilder = new StringBuilder(JsonHandle.getString(userID, "lottery.bet.result", bet, result, afterBet));
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

