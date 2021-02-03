package moe.exusiai;

import moe.exusiai.timer.Timer;
import moe.exusiai.utils.Config;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;

public class Rosmontis {
    public static MessageChainBuilder latestdynamic;

    public static void main(String[] args) {
        rosmontis();
    }

    public static void rosmontis() {
        Config.loadConfig();
        Long accountid = Config.accountid;
        String accountpassword = Config.accountpassword;
        Bot bot = BotFactory.INSTANCE.newBot(accountid, accountpassword, new BotConfiguration() {
            {
                fileBasedDeviceInfo();
                setProtocol(MiraiProtocol.ANDROID_PAD);
            }
        });
        bot.login();
        Timer.StartTimer();

//        Contact contact = bot.getFriend(Long.parseLong("2824034155"));
//        Image image = contact.uploadImage(ExternalResource.Companion.create(new File("./image.jpg")));
//        contact.sendMessage(new PlainText("image").plus(image));
        bot.join();
    }

    public static void updateDynamic(String dynamiccontent, String dynamicid) {
        Bot bot = Bot.Companion.getInstance(Config.accountid).getBot();
        Contact contact = bot.getGroup(Config.group);
        File folder = new File("./" + dynamicid + "/");
        if (!folder.exists()) {
            contact.sendMessage(dynamiccontent);
            return;
        }
        File[] filelist = folder.listFiles();
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        messageChainBuilder.append(new PlainText(dynamiccontent));
        for (File image : filelist) {
            ExternalResource externalResource = ExternalResource.Companion.create(image);
            messageChainBuilder.append(contact.uploadImage(externalResource));
            try {
                externalResource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        contact.sendMessage(messageChainBuilder.build());
        latestdynamic = (new MessageChainBuilder()).append(new PlainText(dynamiccontent));
        for (File image : filelist) {
            image.delete();
        }
        folder.delete();
        return;
    }

    public static void updateLive(String liveurl) {
        Bot bot = Bot.Companion.getInstance(Config.accountid).getBot();
        Contact contact = bot.getGroup(Config.group);
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        messageChainBuilder.append(new PlainText(latestdynamic.toString()+"\n直播地址: ")).append(liveurl);
        contact.sendMessage(messageChainBuilder.build());
    }
}
