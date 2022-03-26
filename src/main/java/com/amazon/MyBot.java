package com.amazon;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

import static java.lang.Math.toIntExact;

public class MyBot extends TelegramLongPollingBot {
    Map<String, Integer> name = new HashMap<>();
    Map<String, Integer> phone = new HashMap<>();
    Map<String, Integer> phone2 = new HashMap<>();
    Map<String, Integer> age = new HashMap<>();
    Map<String, Integer> gender = new HashMap<>();
    Map<String, User> userMap = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "@ishbozoribot";
    }

    @Override
    public String getBotToken() {
        return "5149487636:AAGLuco8Etp9xlsedkHin8WFIjo4uTsbCIU";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Resources resources = new Resources();
        if (update.hasMessage()) {
            String message = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            if (message.equals("/start")) {
                sendMessage.setReplyMarkup(startingBot());
                sendMessage.setText("Assalomu alaykum, xush kelibsiz!");
            } else if (message.equals("Bot qanday ishlaydi")) {
                sendMessage.setText("Assalomu alaykum siz bu bot orqali o'zingizga mos ish topishingiz yoki " +
                        "ish bilan taminlashingiz mumkin, Umumiy qoida siz barcha kategoriyalarni to'g'ri " +
                        "to'ldirishingiz lozim, Biz sizga yordam berishda davom etamiz!");
            } else if (message.equals("Boshlash")) {
                sendMessage.setText("Viloyatingizni tanlang \uD83C\uDFE0:  ");
                sendMessage.setReplyMarkup(getRegions(resources));
            } else if (name.get(chatId) == 1) {
                User user = userMap.get(chatId);
                user.setName(message);
                userMap.put(chatId, user);
                sendMessage.setText("Telefon raqamingizni kiriting:" +
                        " namuna: 90 123 4567 \uD83D\uDCDE ");
                name.put(chatId, 0);
                phone.put(chatId, 1);
            } else if (phone.get(chatId) == 1) {
                StringBuilder phoneNumber = new StringBuilder();
                for (int i = 0; i < message.length(); i++) {
                    if (message.charAt(i) != ' ')
                        phoneNumber.append(message.charAt(i));
                }
                if (phoneNumber.length() == 13 || phoneNumber.length() == 9) {
                    int count = 0;
                    for (int i = 0; i < phoneNumber.length(); i++) {
                        if (phoneNumber.charAt(i) == '0'
                                || phoneNumber.charAt(i) == '1'
                                || phoneNumber.charAt(i) == '2'
                                || phoneNumber.charAt(i) == '3'
                                || phoneNumber.charAt(i) == '4'
                                || phoneNumber.charAt(i) == '5'
                                || phoneNumber.charAt(i) == '6'
                                || phoneNumber.charAt(i) == '7'
                                || phoneNumber.charAt(i) == '8'
                                || phoneNumber.charAt(i) == '9'
                                || phoneNumber.charAt(i) == '+'
                        ) {
                            count++;
                        }
                    }
                    if (count == phoneNumber.length()) {
                        User user = userMap.get(chatId);
                        user.setPhoneNumber(message);
                        userMap.put(chatId, user);
                        sendMessage.setText("Holatingizni tanlang: \uD83D\uDD0D");
                        sendMessage.setReplyMarkup(chooseTypeOfUser(resources));
                        phone.put(chatId, 0);
                    } else {
                        sendMessage.setText("Noto'g'ri formatdagi raqam kiritdingiz, iltimos qaytadan kiriting: ");
                    }
                } else {
                    sendMessage.setText("Noto'g'ri formatdagi raqam kiritdingiz, iltimos qaytadan kiriting: ");
                }
            }
            execute(sendMessage);
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText new_message = new EditMessageText();
            new_message.setChatId(String.valueOf(chat_id));
            new_message.setMessageId(toIntExact(message_id));
            if (resources.list.contains(call_data)) {
                User user = new User();
                user.setRegion(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText(call_data + "dagi tumaningizni tanlang:");
                new_message.setReplyMarkup(getDistricts(resources, call_data));
            } else if (call_data.equals("Ortga")) {
                new_message.setText("Viloyatni tanlang:");
                new_message.setReplyMarkup(getRegions(resources));
            } else if (call_data.equals("Ish izlayapman")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setStatus(false);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("O'zingizga mos ishni tanlang: ");
                new_message.setReplyMarkup(getWorkTypes(resources));
            } else if (call_data.equals("Ish beraman")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setStatus(true);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("Ish bermqchi bo'lgan sohani tanlang: ");
                new_message.setReplyMarkup(getWorkTypes(resources));
            } else if (call_data.equals("Doimiy")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setWorkingType(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("malumotni tanlang:\uD83C\uDF93");
                new_message.setReplyMarkup(chooseLevelOfWorker(resources));
            } else if (call_data.equals("18-30") || call_data.equals("30-45")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setAge(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("jinsni tanlang: \uD83D\uDC6B");
                new_message.setReplyMarkup(chooseGender(resources));
            } else if (call_data.equals("Vaqtincha")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setWorkingType(call_data);
                user.setEducationLevel("o'rta");
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("Yosh oraligini tanlang: \uD83D\uDD0D");
                new_message.setReplyMarkup(chooseAge(resources));
                age.put(String.valueOf(chat_id), 1);
            } else if (call_data.equals("oliy") || call_data.equals("o'rta")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setEducationLevel(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("Yosh oraligini tanlang: \uD83D\uDD0D");
                new_message.setReplyMarkup(chooseAge(resources));
                age.put(String.valueOf(chat_id), 1);
            } else if (call_data.equals("Erkak") || call_data.equals("Ayol")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setGender(call_data);
                userMap.put(String.valueOf(chat_id), user);
                DatabaseConnection connection = new DatabaseConnection();
                User savedUser = userMap.get(String.valueOf(chat_id));
                if (savedUser.isStatus()) {
                    StringBuilder builder = new StringBuilder();
                    List<User> user1 = connection.getUser(savedUser);
                    user1.forEach(user2 -> builder.append(user2).append("\n\n"));
                    new_message.setText(builder.toString());
                } else {
                    connection.saveUser(savedUser);
                    new_message.setText("Ro'yxatdan o'tganingiz bilan tabriklaymiz, Sizga ish beruvchilar qisqa muddatda aloqaga chiqishadi! ✅✅✅✅");
                }
                gender.put(String.valueOf(chat_id), 0);
                userMap.put(String.valueOf(chat_id), null);
            } else if (resources.workList.contains(call_data)) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setProfession(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("O'zingizga mos ish holatini tanlang: ");
                new_message.setReplyMarkup(chooseTypeOfWorkCondition(resources));
            } else {
                User user = userMap.get(String.valueOf(chat_id));
                user.setDistrict(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("Ismingizni kiriting: \uD83D\uDD8A");
                name.put(String.valueOf(chat_id), 1);
            }
            execute(new_message);
        }
    }

    public ReplyKeyboardMarkup startingBot() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Boshlash");
        keyboardFirstRow.add("Bot qanday ishlaydi");
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup getRegions(Resources resources) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < resources.list.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
            keyboardButton.setText(resources.list.get(i));
            keyboardButton.setCallbackData(resources.list.get(i));
            keyboardButtons.add(keyboardButton);
            if ((i + 1) % 2 == 0) {
                inlineKeyboardButtons.add(keyboardButtons);
                keyboardButtons = new ArrayList<>();
            }
        }
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getDistricts(Resources resources, String region) {
        List<String> districts = new ArrayList<>();
        switch (region) {
            case "Andijon":
                districts.addAll(resources.andijonList);
                break;
            case "Buxoro":
                districts.addAll(resources.buxoroList);
                break;
            case "Fargona":
                districts.addAll(resources.fargonaList);
                break;
            case "Jizzax":
                districts.addAll(resources.jizzaxList);
                break;
            case "Namangan":
                districts.addAll(resources.namanganList);
                break;
            case "Navoi":
                districts.addAll(resources.navoiList);
                break;
            case "Qashqadaryo":
                districts.addAll(resources.qashqadaryoList);
                break;
            case "Samarqand":
                districts.addAll(resources.samarqandList);
                break;
            case "Sirdaryo":
                districts.addAll(resources.sirdaryoList);
                break;
            case "Surxondaryo":
                districts.addAll(resources.surxondaryoList);
                break;
            case "Toshkent shahri":
                districts.addAll(resources.toshkentshahriList);
                break;
            case "Toshkent viloyati":
                districts.addAll(resources.toshkentviloyatiList);
                break;
            case "Xorazm":
                districts.addAll(resources.xorazmList);
                break;
            case "Qoraqolpogiston Respublikasi":
                districts.addAll(resources.qaraqolpoqList);
                break;
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < districts.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
            keyboardButton.setText(districts.get(i));
            keyboardButton.setCallbackData(districts.get(i));
            keyboardButtons.add(keyboardButton);
            if ((i + 1) % 2 == 0) {
                inlineKeyboardButtons.add(keyboardButtons);
                keyboardButtons = new ArrayList<>();
            }
        }
        inlineKeyboardButtons.add(keyboardButtons);
        keyboardButtons = new ArrayList<>();
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("◀️Ortga");
        keyboardButton1.setCallbackData("Ortga");
        keyboardButtons.add(keyboardButton1);
        inlineKeyboardButtons.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup chooseTypeOfUser(Resources resources) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("Ish izlayapman");
        keyboardButton.setCallbackData("Ish izlayapman");

        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("Ish beraman");
        keyboardButton1.setCallbackData("Ish beraman");

        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);

        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup chooseAge(Resources resources) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("18-30");
        keyboardButton.setCallbackData("18-30");

        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("30-45");
        keyboardButton1.setCallbackData("30-45");

        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);

        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup chooseTypeOfWorkCondition(Resources resources) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("Vaqtincha");
        keyboardButton.setCallbackData("Vaqtincha");

        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("Doimiy");
        keyboardButton1.setCallbackData("Doimiy");

        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);

        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup chooseLevelOfWorker(Resources resources) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("oliy");
        keyboardButton.setCallbackData("oliy");

        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("o'rta");
        keyboardButton1.setCallbackData("o'rta");

        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);

        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup chooseGender(Resources resources) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("Erkak");
        keyboardButton.setCallbackData("Erkak");

        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("Ayol");
        keyboardButton1.setCallbackData("Ayol");

        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);

        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getWorkTypes(Resources resources) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < resources.workList.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
            switch (resources.workList.get(i)){
                case "Oshpaz":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF73");
                    break;
                case "IT muhandisi":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83D\uDC68\uD83C\uDFFB\u200D\uD83D\uDCBB");
                    break;
                case "Tarjimon":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83D\uDC69\uD83C\uDFFC\u200D\uD83C\uDFEB");
                    break;
                case "Quruvchi":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83D\uDC77\uD83C\uDFFC");
                    break;
                case "Texnikalar ustasi":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83D\uDEE0");
                    break;
                case "Uy ishlari":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83E\uDDF9");
                    break;
                case "Sartarosh":
                    keyboardButton.setText(resources.workList.get(i)+" ✂️");
                    break;
                case "Shifokor":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83E\uDE7A");
                    break;
                case "Farmasevt":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83E\uDDD1\uD83C\uDFFC\u200D\uD83D\uDD2C");
                    break;
                case "Ofitsant":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83C\uDF7D");
                    break;
                case "Tikuvchi":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83D\uDC88");
                    break;
                case "Qoravul":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83D\uDC6E\uD83C\uDFFB\u200D♂️");
                    break;
                case "Sotuvchi":
                    keyboardButton.setText(resources.workList.get(i)+"  ⚖️");
                    break;
                case "Buxgalter":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83D\uDCB5");
                    break;
                case "Dala ishlari":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83C\uDF3D");
                    break;
                case "Menejer":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83D\uDCBC");
                    break;
                case "Arxitektor":
                    keyboardButton.setText(resources.workList.get(i)+" \uD83D\uDCC9");
                    break;
                case "Mardikor":
                    keyboardButton.setText(resources.workList.get(i)+"  Ⓜ️");
                    break;
                case "Agranom":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83D\uDCBE");
                    break;
                case "Va boshqalar":
                    keyboardButton.setText(resources.workList.get(i)+"  \uD83D\uDDE3");
                    break;
                default:
                    keyboardButton.setText(resources.workList.get(i));
                    break;
            }
            keyboardButton.setCallbackData(resources.workList.get(i));
            keyboardButtons.add(keyboardButton);
            if ((i + 1) % 2 == 0) {
                inlineKeyboardButtons.add(keyboardButtons);
                keyboardButtons = new ArrayList<>();
            }
        }
        inlineKeyboardButtons.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }
}
