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

import java.awt.*;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class Bot extends TelegramLongPollingBot {
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
        keyboardFirstButtons.add(new KeyboardButton("/Am I late?"));
        keyboardFirstButtons.add(new KeyboardButton("/currentTime"));// создание двух кнопок на этой троке
        keyboardRowList.add(keyboardFirstButtons);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()) {
            switch (message.getText()) {
                case "/Am I late?":
                    sentMsg(message, amILate());
                    break;
                case  "/currentTime":
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    sentMsg(message, dateFormat.format(date));
                    break;
                default:
            }
        }
    }
    public String amILate() {
        LocalTime now = LocalTime.now();
        LocalTime begin = LocalTime.of(8, 30);
        LocalTime end = LocalTime.of(17, 30);
        if(now.isAfter(begin) && now.isBefore(end)) {
           int countOfHours = (now.getMinute() - begin.getMinute()) / 60;
           int countOfMinutes = (now.getMinute() - begin.getMinute()) % 60;
           double totalCf = countOfHours;
           if((countOfMinutes > 0) && (countOfMinutes <= 6)) {
               totalCf = totalCf + 0.1;
           }
           else if((countOfMinutes > 6) && (countOfMinutes <= 12)) {
                totalCf = totalCf + 0.2;
           }
           else if((countOfMinutes > 12) && (countOfMinutes <= 18)) {
               totalCf = totalCf + 0.3;
           }
           else if((countOfMinutes > 18) && (countOfMinutes <= 24)) {
               totalCf = totalCf + 0.4;
           }
           else if((countOfMinutes > 24) && (countOfMinutes <= 30)) {
               totalCf = totalCf + 0.5;
           }
           else if((countOfMinutes > 30) && (countOfMinutes <= 36)) {
               totalCf = totalCf + 0.6;
           }
           else if((countOfMinutes > 36) && (countOfMinutes <= 42)) {
               totalCf = totalCf + 0.7;
           }
           else if((countOfMinutes > 42) && (countOfMinutes <= 48)) {
               totalCf = totalCf + 0.8;
           }
           else if((countOfMinutes > 48) && (countOfMinutes <= 54)) {
               totalCf = totalCf + 0.9;
           }
           else totalCf = totalCf + 1;
           String s = "" + totalCf;
            return s;
        }
        else if(now.isBefore(begin)) return "Вы не опоздали";
        else return "Ваш рабочий день уже закончлся:)";
    }

    public String getBotUsername() {
        return "ProhodKAZbot";
    }

    public String getBotToken() {
        return "1455259153:AAG31eSyFfes9Pb4lGmy5ySjM4ik27wNIeA";
    }
}
