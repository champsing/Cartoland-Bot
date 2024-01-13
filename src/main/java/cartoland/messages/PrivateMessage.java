package cartoland.messages;

import cartoland.utilities.AnonymousHandle;
import cartoland.utilities.FileHandle;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.List;

/**
 * {@code PrivateMessage} is a listener that triggers when a user types anything in the direct message to the bot. This
 * class is in an array in {@link cartoland.events.MessageEvent}.
 *
 * @since 1.0
 * @author Alex Cai
 */
public class PrivateMessage implements IMessage
{
	@Override
	public boolean messageCondition(MessageReceivedEvent event)
	{
		return !event.isFromGuild();
	}

	@Override
	public void messageProcess(MessageReceivedEvent event)
	{
		Message message = event.getMessage();
		User author = event.getAuthor();

		TextChannel[] undergroundChannel = new TextChannel[1]; //checkMemberValid 會順便獲取地下頻道'
		String isValid = AnonymousHandle.checkMemberValid(author.getIdLong(), undergroundChannel);
		if (!isValid.isEmpty()) //空字串代表沒有錯誤
		{
			message.reply(isValid).mentionRepliedUser(false).queue();
			return;
		}

		MessageCreateBuilder messageBuilder = MessageCreateBuilder.fromMessage(message); //訊息本文
		List<Message.Attachment> attachments = message.getAttachments(); //訊息附件
		int attachmentsCount = attachments.size();
		if (attachmentsCount != 0)
			messageBuilder.addFiles(
				attachments.stream()
						.map(attachment -> FileUpload.fromStreamSupplier(attachment.getFileName(), () -> attachment.getProxy().download().join()))
						.toList());

		undergroundChannel[0].sendMessage(messageBuilder.build()) //私訊轉到地下聊天室
				.queue(undergroundMessage -> AnonymousHandle.addConnection(message.getIdLong(), undergroundMessage.getIdLong()));
		FileHandle.log(author.getName(), '(', author.getId(), ") dm \"", message.getContentRaw(), "\" w ", attachmentsCount);
	}
}