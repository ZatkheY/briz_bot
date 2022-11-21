package com.zatkhey.briz_bot.sevice;



import com.zatkhey.briz_bot.configuration.BotConfig;
import com.zatkhey.briz_bot.cowocers.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.*;
@Component
public class Bot extends TelegramLongPollingBot {
    final private int UTP = 100;
    final private int GPON = 150;
    final private int GPON_CHS = 550;
    final private int GPON_MAF = 300;
    final private int CONNECT_GPON = 200;
    final BotConfig botConfig;

    public Bot(BotConfig botConfig){
        this.botConfig = botConfig;
    }

    private Zatkhey zatkhey = new Zatkhey();
    private Evstratov evstratov = new Evstratov();
    private Sherbakov sherbakov = new Sherbakov();
    private Chernomirdin chernomirdin = new Chernomirdin();
    private Person[] people = new Person[]{zatkhey, sherbakov};


    public String getBotUsername() {
        return botConfig.getBotName();
    }

    public String getBotToken() {
        return botConfig.getToken();
    }

    //                    sendMsg(chatId, "Извени " + firstName + " нет такой команды");
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            String messageText = update.getMessage().getText();
            int moneyByDate = setChatId(chatId).printMoneyByDate(setDate());
            int otkatByDate = setChatId(chatId).printOtkatByDate(setDate());
            int allMoney = setChatId(chatId).printAllMoney();
            int allOtkat = setChatId(chatId).printAllOtkat();
            String date = "21:45:00";
            String allMembersMoney = ": Проведенно = " + allMoney + "грн." +
                    " Откатов = " + allOtkat + "грн." +
                    " Сдать = " + (allMoney - allOtkat) + "грн.";

            if ("/start".equals(messageText) && chatId == setChatId(chatId).getChatId()) {
                sendMsg(chatId, setDate());
                sendMsg(chatId, firstName + " Введи сумму проведеных карточек");

            } else if ("/print".equals(messageText) && chatId == setChatId(chatId).getChatId()) {
                sendMsg(chatId, "За " + setDate().substring(0, 10) +
                        " Проведенно = " + moneyByDate + "грн." +
                        " Откатов = " + otkatByDate + "грн." +
                        " Сдать = " + (moneyByDate - otkatByDate) + "грн.");
            } else if ("/print_all".equals(messageText) && chatId == setChatId(chatId).getChatId()) {
                sendMsg(chatId, firstName + allMembersMoney);
            } else if ("/print_all_members".equals(messageText) && chatId == setChatId(chatId).getChatId()) {
                sendMsg(chatId, zatkhey.getName() +
                        ": " + zatkhey.printAllMoney() + "/" + zatkhey.printAllOtkat() + "/" +
                        (zatkhey.printAllMoney() - zatkhey.printAllOtkat()) + "\n" +
                        chernomirdin.getName() +
                        ": " + chernomirdin.printAllMoney() + "/" + chernomirdin.printAllOtkat() + "/" +
                        (chernomirdin.printAllMoney() - chernomirdin.printAllOtkat()) + "\n" +
                        evstratov.getName() +
                        ": " + evstratov.printAllMoney() + "/" + evstratov.printAllOtkat() + "/" +
                        (evstratov.printAllMoney() - evstratov.printAllOtkat()) + "\n" +
                        sherbakov.getName() +
                        ": " + sherbakov.printAllMoney() + "/" + sherbakov.printAllOtkat() + "/" +
                        (sherbakov.printAllMoney() - sherbakov.printAllOtkat()) + "\n" +
                        "Общая = " + countMoneyFromAllMembers() + "/" + countOtkatFromAllMembers() +
                        "/" + (countMoneyFromAllMembers() - countOtkatFromAllMembers()));
            } else if ("/clear".equals(messageText) && chatId == 402388586) {
                clearList();
                sendMsg(chatId, "Списки очищены");
            } else if ("/send".equals(messageText) && chatId == 402388586) {
               sendMessageAll(firstName+ " Незабудь вписать проведенные карточки!!!");
            } else if (chatId == setChatId(chatId).getChatId()) {
                setChatId(chatId).getMap().put(setDate(), Integer.parseInt(messageText)); //сетаем введеную сумму в мапу
                sendInlineKeyBoardMessage(chatId); // вызываем inlineKeyboard
            } else {
                sendMsg(chatId, "Твой Чат ИД = " + update.getMessage().getChatId().toString() + "\n" +
                        "Отправь его сюда ==>> https://t.me/fLaIt");
            }

        } else if (update.hasCallbackQuery()) {
            String typeOfConnect = update.getCallbackQuery().getData();
            long callBackChatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            if ("40 метров 1грн.".equals(typeOfConnect)) {
                setChatId(callBackChatId).setOtkat(setDate(), UTP);
                editMsg(callBackChatId, "Оплата внесена,откат = 100грн.", messageId);
            } else if ("GPON 1грн.".equals(typeOfConnect)) {
                setChatId(callBackChatId).setOtkat(setDate(), GPON);
                editMsg(callBackChatId, "Оплата внесена,откат = 150грн.", messageId);
            } else if ("GPON Ч.С (999грн)".equals(typeOfConnect)) {
                setChatId(callBackChatId).setOtkat(setDate(), GPON_CHS);
                editMsg(callBackChatId, "Оплата внесена,откат = 550грн.", messageId);
            } else if ("GPON MAF (999грн)".equals(typeOfConnect)) {
                setChatId(callBackChatId).setOtkat(setDate(), GPON_MAF);
                editMsg(callBackChatId, "Оплата внесена,откат = 300грн.", messageId);
            } else if ("Переход на GPON (200грн)".equals(typeOfConnect)) {
                setChatId(callBackChatId).setOtkat(setDate(), CONNECT_GPON);
                editMsg(callBackChatId, "Оплата внесена,откат = 200грн.", messageId);
            } else if ("Оплата Без откатов".equals(typeOfConnect)) {
                editMsg(callBackChatId, "Оплата внесена", messageId);
            } else if ("Срочное Подключение".equals(typeOfConnect)) {
                setChatId(callBackChatId).setOtkat(setDate(), CONNECT_GPON);
                editMsg(callBackChatId, "Оплата внесена,откат = 200грн."+"\n"+
                        "Выбери Тип подключения:", messageId);
                secondInlineKeyBoardMessage(callBackChatId);
            }
        }
    }


    //Кнопки выбора типа подключения
    private void sendInlineKeyBoardMessage(long chatId) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("40 метров 1грн.").callbackData("40 метров 1грн.").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("GPON 1грн.").callbackData("GPON 1грн.").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("GPON Ч.С (999грн)").callbackData("GPON Ч.С (999грн)").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("GPON MAF (999грн)").callbackData("GPON MAF (999грн)").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("Переход на GPON").callbackData("Переход на GPON (200грн)").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("Оплата Без откатов").callbackData("Оплата Без откатов").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("Срочное Подключение").callbackData("Срочное Подключение").build()));
        try {
            execute(SendMessage.builder().text("Тип подключения")
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(rowList).build())
                    .chatId(chatId).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void secondInlineKeyBoardMessage(long chatId) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("40 метров 1грн.").callbackData("40 метров 1грн.").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("GPON 1грн.").callbackData("GPON 1грн.").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("GPON MAF (999грн)").callbackData("GPON MAF (999грн)").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("Переход на GPON").callbackData("Переход на GPON (200грн)").build()));
        rowList.add(Collections.singletonList(InlineKeyboardButton.builder()
                .text("Оплата Без откатов").callbackData("Оплата Без откатов").build()));
        try {
            execute(SendMessage.builder().text("Тип подключения")
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(rowList).build())
                    .chatId(chatId).build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editMsg(long catId, String textToSend, int messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(catId);
        editMessageText.setText(textToSend);
        editMessageText.setMessageId(messageId);
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Person setChatId(long chatId) {
        int index = 0;
        for (int i = 0; i < people.length; i++) {
            if (people[i].getChatId() == chatId) {
                index = i;
            }
        }
        return people[index];
    }

    private String setDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy/ HH:mm:ss");
        calendar.setTime(date);
        return simpleDateFormat.format(calendar.getTime());
    }

    private int countMoneyFromAllMembers() {
        int moneyForAllMembers = 0;
        for (Person person : people) {
            moneyForAllMembers += person.printAllMoney();
        }
        return moneyForAllMembers;
    }

    private int countOtkatFromAllMembers() {
        int otkatForAllMembers = 0;
        for (Person person : people) {
            otkatForAllMembers += person.printAllOtkat();
        }
        return otkatForAllMembers;
    }

    private void clearList() {
        for (Person value : people) {
            value.clear();
        }
    }
//    @Scheduled(fixedRateString = "${pingtask.period}")
    public void sendMessageAll(String text){
        for (Person person : people) {
            sendMsg(person.getChatId(), text);
        }
    }

}

