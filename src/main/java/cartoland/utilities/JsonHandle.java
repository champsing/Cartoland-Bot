package cartoland.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class JsonHandle
{
	private static final JSONObject usersFile = new JSONObject(FileHandle.buildJsonStringFromFile("users.json")); //使用者的語言設定
	private static final HashMap<String, JSONObject> languageFileMap= new HashMap<>();

	private static JSONObject file = null; //在lastUse中獲得這個ID對應的語言檔案 並在指令中使用
	private static final JSONObject englishFile = new JSONObject(FileHandle.buildJsonStringFromFile("lang/en.json"));
	private static String userIDString = null; //將ID轉換成字串

	static
	{
		languageFileMap.put(Languages.ENGLISH, englishFile);
		languageFileMap.put(Languages.TW_MANDARIN, new JSONObject(FileHandle.buildJsonStringFromFile("lang/tw.json")));
		languageFileMap.put(Languages.TAIWANESE, new JSONObject(FileHandle.buildJsonStringFromFile("lang/ta.json")));
		languageFileMap.put(Languages.CANTONESE, new JSONObject(FileHandle.buildJsonStringFromFile("lang/hk.json")));
		languageFileMap.put(Languages.CHINESE, new JSONObject(FileHandle.buildJsonStringFromFile("lang/cn.json")));
	}

	private static void lastUse(long userID)
	{
		userIDString = Long.toUnsignedString(userID);
		String userLanguage;

		if (usersFile.has(userIDString))
			userLanguage = usersFile.getString(userIDString); //獲取使用者設定的語言
		else //找不到設定的語言
			usersFile.put(userIDString, userLanguage = Languages.ENGLISH); //放英文進去

		file = languageFileMap.get(userLanguage);
	}

	static String getUsersFileString()
	{
		return usersFile.toString();
	}

	public static String command(long userID, String typeCommandName)
	{
		lastUse(userID);
		StringBuilder builder = new StringBuilder();
		builder.append(file.getString(typeCommandName + ".begin")); //開頭

		String jsonKey = typeCommandName + ".list";
		JSONObject hasKeyFile;
		if (file.has(jsonKey)) //如果有這個key
			hasKeyFile = file;
		else if (englishFile.has(jsonKey)) //預設使用英文
			hasKeyFile = englishFile;
		else
			return file.getString(typeCommandName + ".fail");

		JSONArray dotListArray = hasKeyFile.getJSONArray(jsonKey);
		int dotListArrayLength = dotListArray.length();
		for (int i = 0; i < dotListArrayLength; i++) //所有.list內的內容
			builder.append(dotListArray.getString(i)).append(' ');
		builder.append(file.getString(typeCommandName + ".end")); //結尾
		return builder.toString();
	}

	public static String command(long userID, String typeCommandName, String argument)
	{
		lastUse(userID);
		String jsonKey = typeCommandName + ".name." + argument;
		JSONObject hasKeyFile;
		if (file.has(jsonKey)) //如果有這個key
			hasKeyFile = file;
		else if (englishFile.has(jsonKey)) //預設使用英文
			hasKeyFile = englishFile;
		else
			return file.getString(typeCommandName + ".fail");

		if (typeCommandName.equals("lang"))
			usersFile.put(userIDString, argument);
		return hasKeyFile.getString(typeCommandName + ".name." + argument);
	}
}