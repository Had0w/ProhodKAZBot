import com.google.inject.internal.cglib.proxy.$CallbackFilter;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    private static final Map<String, String> getenv = System.getenv();
    static File file = new File("src\\main\\resources\\config.properties");
    static Properties properties = new Properties();
    static {
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sentMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>(); // список строк с кнопками

        KeyboardRow keyboardFirstButtons = new KeyboardRow(); // создание строки
        keyboardFirstButtons.add(new KeyboardButton("Вход"));
//        keyboardFirstButtons.add(new KeyboardButton("currentTime"));// создание двух кнопок на этой троке
        keyboardRowList.add(keyboardFirstButtons);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sentMsg(message, "Добро пожаловать!");
                    break;
                case "Вход":
                    sentMsg(message, amILate());
                    break;
//                case "/currentTime":
//                    Date date = new Date();
//                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
//                    sentMsg(message, dateFormat.format(date));
//                    break;
                default:
            }
        }
    }

    public String amILate() {
        LocalTime now = LocalTime.now().plusHours(3);
        LocalTime begin = LocalTime.of(8, 29);
        LocalTime startLunch = LocalTime.of(12, 0);
        LocalTime endLunch = LocalTime.of(13, 0);
        LocalTime end = LocalTime.of(17, 31);
        double min10;
        if(now.isAfter(startLunch) && now.isBefore(endLunch)) { // Если пришел в обед
            return "Ваше опоздание " + "3.5" + "(Сейчас обед)"; // Костыль! Добавить расчет времени
        }
        if (now.isAfter(begin) && now.isBefore(end)) {
            int countOfHours = (now.getHour() - begin.getHour());
            int countOfMinutes = (now.getMinute() - begin.getMinute());
            int total = countOfHours * 60 + countOfMinutes;
            int finHour = total / 60;
            int finMin = total % 60;
            min10 = finHour;
            if ((finMin > 0) && (finMin <= 6)) {
                min10 = min10 + 0.1;
            } else if ((finMin > 6) && (finMin <= 12)) {
                min10 = min10 + 0.2;
            } else if ((finMin > 12) && (finMin <= 18)) {
                min10 = min10 + 0.3;
            } else if ((finMin > 18) && (finMin <= 24)) {
                min10 = min10 + 0.4;
            } else if ((finMin > 24) && (finMin <= 30)) {
                min10 = min10 + 0.5;
            } else if ((finMin > 30) && (finMin <= 36)) {
                min10 = min10 + 0.6;
            } else if ((finMin > 36) && (finMin <= 42)) {
                min10 = min10 + 0.7;
            } else if ((finMin > 42) && (finMin <= 48)) {
                min10 = min10 + 0.8;
            } else if ((finMin > 48) && (finMin <= 54)) {
                min10 = min10 + 0.9;
            }
           if(now.isAfter(endLunch)) {
               min10--;
           }
            return "Ваше опоздание: " + min10;
        } else if (now.isBefore(begin)) return "Вы не опоздали";
        else return "Ваш рабочий день уже закончился:)";
    }

    public String getBotUsername() {
        return "ProhodKAZbot";
    }

    public String getBotToken() {
        return "1455259153:AAG31eSyFfes9Pb4lGmy5ySjM4ik27wNIeA";
    }
}
