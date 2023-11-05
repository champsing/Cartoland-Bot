package cartoland.events;

import cartoland.Cartoland;
import cartoland.commands.*;
import cartoland.commands.TicTacToeCommand;
import cartoland.mini_games.IMiniGame;
import cartoland.utilities.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

import static cartoland.commands.ICommand.*;

/**
 * {@code CommandUsage} is a listener that triggers when a user uses slash command. This class was registered in
 * {@link cartoland.Cartoland#main}, with the build of JDA.
 *
 * @since 1.0
 * @author Alex Cai
 */
public class CommandUsage extends ListenerAdapter
{
	/**
	 * The key of this map is the n of a command, and the value is the execution.
	 */
	private final Map<String, ICommand> commands = new HashMap<>();
	/**
	 * The key of this map is the n of a game, and the value is the actual game.
	 */
	private final Map<Long, IMiniGame> games = new HashMap<>();
	public Map<Long, IMiniGame> getGames()
	{
		return games;
	}

	/**
	 * 403 images about Megumin.
	 */
	private static final long[] MEGUMIN_IMAGES = {712966825663795202L,888014816266211329L,895656010974502912L,914382762978680832L,925014913285734402L,942065366179627008L,1038481866993500163L,1068471996369915906L,1099923406571528193L,1105799480396279809L,1113333584654266368L,1120180446392807424L,1123238336359886849L,1123913549934473216L,1123935283995713543L,1127271844384395264L,1128289624126738432L,1129119914638233600L,1129334190695100416L,1129402831587274752L,1130947782209024000L,1132349932705214465L,1172969592458600448L,1184557949714219009L,1211311585571917825L,1228335403221966849L,1256453504299757569L,1260533099361787905L,1267778380994236416L,1271368885682319360L,1277202674916589570L,1331916310100336645L,1332594611156500487L,1333072335717289986L,1333391702132867073L,1333706453182341120L,1333748893239046144L,1334414009840226305L,1334513374726643715L,1334513603865686018L,1334513715660644355L,1334516599760490498L,1334542548497387521L,1334575930908397568L,1334743269582938112L,1334837150714527745L,1334837425667969024L,1334864422934757376L,1334957384381288448L,1334992143715201025L,1335131654415818753L,1336584872471584768L,1336638090828341250L,1336868237141704705L,1338114471143739392L,1338774453711355904L,1340946345096450049L,1342076712587206658L,1342110371662888960L,1342130873785745411L,1342283498783002626L,1342795265724436481L,1343169561680519169L,1343423407442948101L,1344206596310761472L,1344635807965974528L,1345199258220126216L,1345234835741188105L,1345279854040649728L,1345392705837309952L,1345655525795512321L,1345658481076887553L,1346091304124497920L,1346389393750204416L,1346410525853245445L,1347026153098211331L,1348193043229278208L,1348240561505853442L,1348538564900917248L,1348915439527620608L,1349470312648290307L,1350413371363848196L,1352167601951002624L,1352830077696962560L,1352926702398185473L,1353356379889700864L,1353673446719250433L,1353732864177623040L,1354433463043022849L,1355069629840855042L,1355473834326061062L,1355848490383855617L,1356574457360310273L,1356814844792504321L,1357357057213538309L,1358023126894825472L,1358677301731368964L,1359838927914373130L,1360559485664677889L,1360906615008886784L,1360906994673016833L,1360925354773241857L,1362948691376619524L,1363396372309901313L,1363775871711014919L,1363817144018866178L,1363822716260782080L,1364578421406199822L,1365634168839790601L,1365995755194880003L,1367020232334663683L,1367024008223354880L,1367079770870046720L,1367304489468796932L,1369256816794771456L,1370182918258184192L,1370264344903045120L,1370370105696161794L,1372881405705744384L,1373256680763396106L,1373556726469775363L,1373572772983574537L,1374333181684375556L,1375088851446755332L,1377561442694488064L,1378299101071085569L,1378316365128331264L,1378351221283594241L,1378395239845482499L,1378677005764292611L,1379289441450139654L,1379418416046477312L,1380122050883293189L,1380295158676090880L,1380491541450424320L,1380758914401099777L,1380808129676333064L,1380913346589822978L,1381215456741138434L,1382302645726027782L,1382662692729618434L,1382673881903689730L,1383254021201297414L,1383359465366323209L,1383375535967903749L,1384431678454521860L,1385345798372741120L,1385928402499108866L,1387723681833447427L,1388147012881707009L,1388463621164859395L,1388805801859452928L,1389692031606267904L,1390275707347951621L,1390290873951285248L,1391319694859137024L,1391411800420151310L,1393257386287144963L,1393868573106327563L,1394177004967108608L,1394877336516907010L,1394971615864270850L,1395598782675357698L,1396492351346860038L,1398405163677671425L,1399677759698337797L,1400129807925628933L,1400192682480136194L,1400214535080878081L,1400465949963145226L,1401107716651638786L,1401149547724820482L,1401554662130556928L,1401606597584068617L,1401870084285493252L,1403371578327404544L,1403669632108564485L,1406206671571984384L,1406218302515384322L,1408089488358723585L,1408435172224114691L,1412336326108610563L,1413824587935010820L,1416330421525041152L,1416334656371269634L,1416720114066415617L,1416757349046382592L,1416979268928634880L,1417047602130993154L,1417105811650727936L,1418163778487652354L,1418673457491943428L,1418859623973589000L,1418913957067194368L,1420731295156477954L,1421495091001135106L,1421734667326132225L,1422169100533460995L,1422834901066207237L,1422886552280072195L,1422891821772906500L,1423468322893357056L,1423601843368730625L,1423605503146426376L,1424305682589708293L,1424335734085853193L,1424383682442268683L,1426514140554698753L,1427601524688003072L,1428436939015741440L,1429373932159660032L,1429742364428431367L,1432310655512506386L,1434109889056428034L,1435205520265121793L,1435533706161102852L,1436616211828129798L,1437002711405268993L,1438652486806425603L,1441056569236463637L,1443910414455107588L,1444998923739824131L,1445719564185075722L,1446105335723298817L,1448264639385509892L,1449003566610325504L,1449698306620227589L,1451479334636572676L,1452032402742669313L,1454041990329352192L,1454782046337781770L,1455005915992584196L,1455867948883529732L,1455918876990996489L,1456979191736377344L,1458399016023060481L,1458708798562918400L,1459384335799562241L,1459477324437458946L,1460578197477552133L,1460934239822696456L,1461324636524728323L,1461695108055658496L,1463416144765751300L,1463503094977687554L,1463709023455399936L,1464202279897104391L,1464586977114607616L,1465279326392176641L,1466361693324210183L,1467024737448886274L,1467394004971323393L,1468201641723965447L,1468855768367898625L,1471803818048634884L,1474349388307005448L,1474425440760897536L,1474737540821561345L,1475441506437988352L,1476897920071114759L,1478490774585376768L,1480872741784125441L,1482306755447050241L,1484269503563264000L,1485297876947566594L,1487062233343426561L,1487746415602577409L,1487748613908615182L,1487825453427863553L,1488530273373683713L,1493194450688159746L,1493204800414322689L,1495134351272529926L,1496087004211453960L,1496121346824232966L,1497902031813890058L,1502885083614973952L,1504421763479920640L,1504494884765396994L,1505947094192095235L,1506027874825895938L,1507644448079253508L,1507656088103297026L,1514219512388026373L,1521823893857931264L,1523274342465175553L,1523465539678785536L,1524090497249075206L,1526173684355870720L,1526404539121037312L,1527310236247560193L,1528012740656345088L,1528329828012785664L,1529055947767050240L,1530582872184483840L,1532330229880205312L,1533389156755603456L,1533765708513148928L,1535957094251507712L,1538112903555215360L,1538592731962179587L,1540621318374162433L,1541753284070965249L,1544305092333129728L,1544612178556121088L,1545769743151353856L,1546102725196550144L,1547189145818525696L,1553379890128306177L,1553715061713039360L,1555916614138150912L,1557015010668609536L,1557726596202606595L,1558054431153082368L,1558410423837523970L,1559492213809496064L,1559510936616321024L,1565325467850518529L,1565673703215886337L,1569990932568891392L,1571040020219428865L,1573212323644575744L,1573673618172624896L,1573801227376214017L,1576769547117293572L,1576950800261095424L,1577266429568901124L,1577615125720485888L,1583789431949230081L,1586696036126711809L,1588833560546676737L,1593937896784166912L,1593967339036450820L,1594666279511945216L,1595465406420221952L,1596347482916950018L,1596505160175747072L,1598270723747098624L,1599057455702765568L,1599059007498772480L,1599205425127501824L,1599242577865478145L,1600838962406625280L,1602273539247476738L,1605171507097145344L,1606620722864214017L,1606632160756396032L,1606956350596317185L,1606981310349967366L,1606986710520565761L,1607322393818849281L,1609197162726055936L,1610423761794379776L,1613869444387983362L,1614200785335623682L,1618230537969364996L,1622554337330499585L,1625462523523919872L,1625463570090852352L,1626944636525903873L,1628346593803141120L,1630805213530685441L,1631265798554808322L,1631987566663843840L,1633030821388099585L,1638859837034610692L,1639303735951384576L,1639598009716707329L,1639615200885366787L,1641738701482110976L,1642908738276458503L,1643237268717699078L,1643578446768132097L,1643586361029705728L,1643912324561137664L,1644037256431423488L,1644283492635930624L,1644717774214959106L,1652145692498972673L,1652202562052059136L,1652654046753480704L,1654095640509702144L,1654300719732891654L,1657309710658461696L,1658807591819153408L,1659114097823105025L,1659121877288366080L,1660083826889207811L,1660971335517106176L,1661658367314771968L,1663893703096950786L,1665927714610761729L,1666097657549565952L,1668961956404002816L,1671852223087902720L,1672808564459728899L,1677691852861079555L,1690695854523600897L,1691420944919891968L,1693628863522218470L,1697855211526816122L,1699083257919521253L,1705808948287463457L};

	/**
	 * Put every command and their execution into {@link #commands}.
	 */
	public CommandUsage()
	{
		ICommand alias;

		//初始化map 放入所有指令
		//invite
		commands.put(INVITE, event -> event.reply("https://discord.gg/UMYxwHyRNE").queue());

		//help
		commands.put(HELP, event -> event.reply(minecraftCommandRelated("help", event)).queue());

		//cmd
		alias = event -> event.reply(minecraftCommandRelated("cmd", event)).queue();
		commands.put(CMD, alias);
		commands.put(MCC, alias);
		commands.put(COMMAND, alias);

		//faq
		alias = event -> event.reply(minecraftCommandRelated("faq", event)).queue();
		commands.put(FAQ, alias);
		commands.put(QUESTION, alias);

		//dtp
		alias = event -> event.reply(minecraftCommandRelated("dtp", event)).queue();
		commands.put(DTP, alias);
		commands.put(DATAPACK, alias);

		//jira
		alias = new JiraCommand();
		commands.put(JIRA, alias);
		commands.put(BUG, alias);

		//tool
		commands.put(TOOL, new ToolCommand());

		//lang
		alias = event -> event.reply(minecraftCommandRelated("lang", event)).queue();
		commands.put(LANG, alias);
		commands.put(LANGUAGE, alias);

		//quote
		commands.put(QUOTE, new QuoteCommand());

		//youtuber
		commands.put(YOUTUBER, event -> event.reply("https://www.youtube.com/" + event.getOption("youtuber_name", CommonFunctions.getAsString)).queue());

		//introduce
		commands.put(INTRODUCE, new IntroduceCommand());

		//birthday
		commands.put(BIRTHDAY, new BirthdayCommand());

		//megumin
		commands.put(MEGUMIN, event -> event.reply("https://vxtwitter.com/i/status/" + Algorithm.randomElement(MEGUMIN_IMAGES)).queue()); //隨機一張惠惠

		//shutdown
		commands.put(SHUTDOWN, event ->
		{
			if (event.getUser().getIdLong() != IDs.AC_ID) //不是我
			{
				event.reply("You can't do that.").queue();
				return;
			}

			event.reply("Shutting down...").complete(); //先送訊息 再下線

			JDA jda = Cartoland.getJDA();
			Guild cartoland = jda.getGuildById(IDs.CARTOLAND_SERVER_ID); //定位創聯
			if (cartoland == null) //如果找不到創聯
			{
				jda.shutdown(); //直接結束 不傳訊息了
				return;
			}

			TextChannel botChannel = cartoland.getTextChannelById(IDs.BOT_CHANNEL_ID); //創聯的機器人頻道
			if (botChannel != null) //找到頻道了
				botChannel.sendMessage("Cartoland Bot 已下線。\nCartoland Bot is now offline.").complete();
			jda.shutdown(); //關機下線
		});

		//reload
		commands.put(RELOAD, event ->
		{
			if (event.getUser().getIdLong() != IDs.AC_ID) //不是我
			{
				event.reply("You can't do that.").queue();
				return;
			}

			event.reply("Reloading...").queue();
			JsonHandle.reloadLanguageFiles();
		});

		//admin
		commands.put(ADMIN, new AdminCommand());

		//one_a_two_b
		commands.put(ONE_A_TWO_B, new OneATwoBCommand(this));

		//lottery
		commands.put(LOTTERY, new LotteryCommand());

		//transfer
		commands.put(TRANSFER, new TransferCommand());

		//tic_tac_toe
		commands.put(TIC_TAC_TOE, new TicTacToeCommand(this));

		//connect_four
		commands.put(CONNECT_FOUR, new ConnectFourCommand(this));

		//light_out
		commands.put(LIGHT_OUT, new LightOutCommand(this));
	}

	/**
	 * The method that inherited from {@link ListenerAdapter}, triggers when a user uses slash command.
	 *
	 * @param event The event that carries information of the user and the command.
	 * @since 1.0
	 * @author Alex Cai
	 */
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
	{
		String commandName = event.getName();
		ICommand commandExecution = commands.get(commandName);
		if (commandExecution != null)
			commandExecution.commandProcess(event);
		else
			event.reply("You can't use this!").queue();
		User user = event.getUser();
		FileHandle.log(user.getEffectiveName() + "(" + user.getIdLong() + ") used /" + commandName); //IO放最後 避免超過3秒限制
	}

	/**
	 * When it comes to /help, /cmd, /faq, /dtp and /lang that needs to use lang/*.json files, those lambda
	 * expressions will call this method.
	 *
	 * @param commandName the command n, only "help", "cmd", "faq", "dtp" and "lang" are allowed.
	 * @param event The event that carries information of the user and the command.
	 * @return The content that the bot is going to reply the user.
	 * @since 1.0
	 * @author Alex Cai
	 */
	private String minecraftCommandRelated(String commandName, SlashCommandInteractionEvent event)
	{
		String argument = event.getOption(commandName + "_name", CommonFunctions.getAsString); //獲得參數
		if (argument == null) //沒有參數
			return JsonHandle.command(event.getUser().getIdLong(), commandName); //儘管/lang的參數是必須的 但為了方便還是讓他用這個方法處理
		return JsonHandle.command(event.getUser().getIdLong(), commandName, argument);
	}
}