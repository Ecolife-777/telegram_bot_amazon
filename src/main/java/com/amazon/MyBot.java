package com.amazon;

import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Math.toIntExact;

public class MyBot extends TelegramLongPollingBot {
    Map<String, Integer> name = new HashMap<>();
    Map<String, Integer> phone = new HashMap<>();
    Map<String, Admin> adminList = new HashMap<>();
    Map<String, Integer> age = new HashMap<>();
    Map<String, Integer> ageStatus = new HashMap<>();
    Map<String, Integer> gender = new HashMap<>();
    Map<String, User> userMap = new HashMap<>();
    Map<String, Integer> adDescription = new HashMap<>();
    Map<String, Integer> adName = new HashMap<>();
    Map<String, Ads> adPhoto = new HashMap<>();
    Map<String, Integer> deleteAd = new HashMap<>();
    Map<String, Integer> addProfession = new HashMap<>();
    Map<String, Integer> deleteProfession = new HashMap<>();
    Map<String, String> keepProfession = new HashMap<>();
    Map<String, Integer> adminState = new HashMap<>();
    Map<String, Admin> admin = new HashMap<>();
    Map<String, Integer> deleteAdmin = new HashMap<>();

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
        DatabaseConnection connection = new DatabaseConnection();
        if (update.hasMessage()) {
            String chatId = update.getMessage().getChatId().toString();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            if (update.getMessage().hasText()) {
                String message = update.getMessage().getText();
                if (message.equals("/start")) {
                    sendMessage.setReplyMarkup(startingBot());
                    sendMessage.setText("Assalomu alaykum, Ish bozori botiga xush kelibsiz! \uD83D\uDE4B\u200D♂️\n\n" +
                            "\uD83E\uDDFE Botni boshlashdan oldin bot bilan tanishib chiqishingizni so'raymiz. " +
                            "Siz ish bozori botga muvaffaqiyatli a'zo bo'ldingiz. " +
                            "Mazkur bot orqali siz O'zbekiston bo'ylab ish berish yoki ish izlash imkoniyatiga ega bo'lasiz.  " +
                            "O'ylaymizki sizning ezgu maqsadlaringiz uchun mazkur bot samarali yordamchingizga aylanadi. ✅️" +
                            "\n\nBotdan foydalanishdan oldin quyidagilar bilib oling. ☝️" +
                            "\n\n\uD83D\uDDE3 Diqqat: ️Bot orqali ish beruvchi va ish oluvchilar o'rtasida  muammolar " +
                            "va ziddiyatlar kelib chiqsa biz javobgarlikni o'z zimmamizga olmaymiz. ❗️❗️❗️ \n" +
                            "Quyidagi vaziyatlar sodir bo'lsa ish beruvchi va ish oluvchi o'zaro kelishib olishingiz shart. \uD83E\uDD1D " +
                            "\n\nBarcha talablarga rozi bo'lsangiz botdan foydalanishingiz mumkin. ✅ " +
                            " \n\n https://t.me/ishbozori_kanali");
                }
                else if (message.equals("Menu")) {
                    sendMessage.setReplyMarkup(adminMenu());
                    sendMessage.setText("Quyidagilardan birini tanlang!");
                }
                else if (message.equals("Bosh menu")) {
                    sendMessage.setReplyMarkup(startingBot());
                    sendMessage.setText("Assalomu alaykum, xush kelibsiz!");
                }
                else if (message.equals("Ishchilar royhati")) {
                    SendDocument sendDocument = allWorkers(chatId);
                    execute(sendDocument);
                    sendMessage.setText("Barcha ishchilar!");
                }
                else if (message.equals("Ish beruvchilar royhati")) {
                    SendDocument sendDocument = allWEmployees(chatId);
                    execute(sendDocument);
                    sendMessage.setText("Barcha ish beruvchilar!");
                }
                else if (message.equals("barchaga reklama jonatish")) {
                    sendMessage.setText("jo'xnatmoqchi bolgan reklamangizni tanlang");
                    InlineKeyboardMarkup ads = getAds();
                    sendMessage.setReplyMarkup(ads);
                }
                else if (message.equals("Adminni tahrirlash")) {
                    sendMessage.setText("Tanlang:");
                    sendMessage.setReplyMarkup(adminEditMenu());
                }
                else if (message.equals("Roziman va boshlayman")) {
                    sendMessage.setText("Viloyatingizni tanlang \uD83C\uDFE0:  ");
                    sendMessage.setReplyMarkup(getRegions(resources));
                }
                else if (name.size() != 0 && name.get(chatId) == 1) {
                    User user = userMap.get(chatId);
                    user.setName(message);
                    userMap.put(chatId, user);
                    sendMessage.setText("Telefon raqamingizni ulashing \uD83D\uDCDE ");
                    name.put(chatId, 0);
                    sendMessage.setReplyMarkup(shareContact(Long.valueOf(chatId)));
                }
                else if (adName.size() != 0 && adName.get(chatId) == 1) {
                    Ads ads = new Ads();
                    ads.setName(message);
                    adPhoto.put(chatId, ads);
                    sendMessage.setText("Reklama haqida malumot kiriting");
                    adDescription.put(chatId, 1);
                    adName.put(chatId, 0);
                }
                else if (adDescription.size() != 0 && adDescription.get(chatId) == 1) {
                    Ads ads = adPhoto.get(chatId);
                    ads.setDescription(message);
                    adPhoto.put(chatId, ads);
                    sendMessage.setText("Reklamaning rasmi bolsa jonating");
                    adDescription.put(chatId, 0);
                }
                else if (adminList.size() != 0 && adminList.get(chatId).getPhoneNumber() != null) {
                    if (adminList.get(chatId).getPassword().equals(message)) {
                        sendMessage.setText("Welcome Admin");
                        sendMessage.setReplyMarkup(adminPanel());
                        adminList.put(chatId, new Admin());
                    } else {
                        sendMessage.setText("Parolni hato kiritdingiz, iltimos qaytadan kiriting: ");
                    }
                }
                else if (deleteAd.size() != 0 && deleteAd.get(chatId) == 1) {
                    Ads ads = connection.checkAd(Long.valueOf(message));
                    if (ads.getId() != null) {
                        connection.deleteAd(Long.valueOf(message));
                        sendMessage.setText("muvaffaqiyatli ochirildi.");
                        deleteAd.put(chatId, 0);
                    } else {
                        sendMessage.setText("bunday id mavjud emas, iltimos qaytadan urinib koring");
                    }
                }
                else if (addProfession.size() != 0 && addProfession.get(chatId) == 1) {
                    Profession profession = connection.checkProfession(message);
                    if (profession.getName() != null) {
                        sendMessage.setText("Bunday kasb mavjud, iltimos qaytdadan urunib koring");
                    } else {
                        connection.saveProfession(message);
                        sendMessage.setText("muvaffaqiyatli qoshildi");
                        addProfession.put(chatId, 0);
                    }
                }
                else if (adminState.size() != 0 && adminState.get(chatId) == 1) {
                    Admin admin1 = new Admin();
                    admin1.setName(message);
                    admin.put(chatId, admin1);
                    sendMessage.setText("Enter phone number: ");
                    adminState.remove(chatId);
                }
                else if (admin.size() != 0
                        && admin.get(chatId).getName() != null
                        && admin.get(chatId).getPhoneNumber() == null
                        && admin.get(chatId).getPassword() == null) {
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
                            Admin admin1 = connection.checkAdmin(message);
                            if (admin1.getPhoneNumber() == null) {
                                Admin admin = this.admin.get(chatId);
                                admin.setPhoneNumber(String.valueOf(phoneNumber));
                                sendMessage.setText("Parolingizni kiriting: \uD83D\uDD0D");
                            } else {
                                sendMessage.setText("Bunday telefon nomer mavjud: ");
                            }
                        } else {
                            sendMessage.setText("Noto'g'ri formatdagi raqam kiritdingiz, iltimos qaytadan kiriting: ");
                        }
                    } else {
                        sendMessage.setText("Noto'g'ri formatdagi raqam kiritdingiz, iltimos qaytadan kiriting: ");
                    }
                }
                else if (admin.size() != 0
                        && admin.get(chatId).getName() != null
                        && admin.get(chatId).getPhoneNumber() != null
                        && admin.get(chatId).getPassword() == null) {
                    Admin admin1 = this.admin.get(chatId);
                    admin1.setPassword(message);
                    connection.saveAdmin(admin1);
                    sendMessage.setText("Admin saved.");
                }
                else if (deleteAdmin.size() != 0 && deleteAdmin.get(chatId) == 1) {
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
                            Admin admin1 = connection.checkAdmin(String.valueOf(phoneNumber));
                            if (admin1.getPhoneNumber() != null) {
                                connection.deleteAdmin(String.valueOf(phoneNumber));
                                sendMessage.setText("deleted: \uD83D\uDD0D");
                            } else {
                                sendMessage.setText("Bunday telefon nomer mavjud emas: ");
                            }
                        } else {
                            sendMessage.setText("Noto'g'ri formatdagi raqam kiritdingiz, iltimos qaytadan kiriting: ");
                        }
                    } else {
                        sendMessage.setText("Noto'g'ri formatdagi raqam kiritdingiz, iltimos qaytadan kiriting: ");
                    }
                }
                else if (age.size() != 0 && age.get(chatId) == 1){
                    User user = userMap.get(chatId);
                    user.setAge(message);
                    User put = userMap.put(chatId, user);
                    sendMessage.setText("jinsni tanlang: \uD83D\uDC6B");
                    sendMessage.setReplyMarkup(chooseGender(put));
                }
            }
            else if (update.getMessage().hasContact()){
                String phoneNumber = update.getMessage().getContact().getPhoneNumber();
                boolean contains = phoneNumber.contains("+");
                if (!contains){
                    phoneNumber = "+" + phoneNumber;
                }
                Admin admin = connection.checkAdmin(phoneNumber);
                if (admin.getPhoneNumber() == null) {
                    User user = userMap.get(chatId);
                    user.setPhoneNumber(phoneNumber);
                    userMap.put(chatId, user);
                    sendMessage.setText("Holatingizni tanlang: \uD83D\uDD0D");
                    sendMessage.setReplyMarkup(chooseTypeOfUser(resources));
                } else {
                    adminList.put(chatId, admin);
                    sendMessage.setText("Parolni kiriting: ");
                }
            }
            else {
                if (adPhoto.size() != 0 && adPhoto.get(chatId).getDescription() != null) {
                    Ads ads = adPhoto.get(chatId);
                    ads.setFileId(update.getMessage().getPhoto().get(0).getFileId());
                    connection.savePhoto(ads);
                    sendMessage.setText("reklamangiz muvoffaqiyatli joylandi");
                    adPhoto.put(chatId, new Ads());
                } else {
                    sendMessage.setText("Notogri turdagi malumot kiritdingiz, qaytadan urunib koring!");
                }
            }
            execute(sendMessage);
        }
        else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText new_message = new EditMessageText();
            new_message.setChatId(String.valueOf(chat_id));
            new_message.setMessageId(toIntExact(message_id));
            if (resources.list.contains(call_data)) {
                User user = new User();
                user.setChatId(String.valueOf(chat_id));
                user.setRegion(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText(call_data + "dagi tumaningizni tanlang:");
                new_message.setReplyMarkup(getDistricts(resources, call_data));
            }
            else if (call_data.equals("Ortga")) {
                new_message.setText("Viloyatni tanlang:");
                new_message.setReplyMarkup(getRegions(resources));
            }
            else if (call_data.equals("Orqaga")) {
                new_message.setText("O'zingizga mos ishni tanlang: ");
                new_message.setReplyMarkup(getWorkTypes(resources));
            }
            else if (call_data.equals("Ish izlayapman")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setStatus(false);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("O'zingizga mos ishni tanlang: ");
                new_message.setReplyMarkup(getWorkTypes(resources));
            }
            else if (call_data.equals("Ish beraman")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setStatus(true);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("Ish bermqchi bo'lgan sohani tanlang: ");
                new_message.setReplyMarkup(getWorkTypes(resources));
            }
            else if (call_data.equals("Doimiy")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setWorkingType(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("malumotni tanlang:\uD83C\uDF93");
                new_message.setReplyMarkup(chooseLevelOfWorker(resources));
            }
            else if (call_data.equals("18-30") || call_data.equals("30-45")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setAge(call_data);
                User put = userMap.put(String.valueOf(chat_id), user);
                new_message.setText("jinsni tanlang: \uD83D\uDC6B");
                new_message.setReplyMarkup(chooseGender(put));
            }
            else if (call_data.equals("Vaqtincha")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setWorkingType(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("malumotni tanlang:\uD83C\uDF93");
                new_message.setReplyMarkup(chooseLevelOfWorker(resources));
            }
            else if (call_data.equals("oliy") || call_data.equals("o'rta")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setEducationLevel(call_data);
                userMap.put(String.valueOf(chat_id), user);
                if (userMap.get(String.valueOf(chat_id)).isStatus()){
                    User user1 = userMap.get(String.valueOf(chat_id));
                    new_message.setText("jinsni tanlang: \uD83D\uDD0D");
                    new_message.setReplyMarkup(chooseGender(user1));
                } else {
                    new_message.setText("Ish tajribangizni tanlang: \uD83D\uDD0D");
                    new_message.setReplyMarkup(chooseWorkExperience());
                }
            }
            else if (call_data.equals("0")
                    || call_data.equals("1")
                    || call_data.equals("2")
                    || call_data.equals("3")
                    || call_data.equals("4")
                    || call_data.equals("5+")){
                User user = userMap.get(String.valueOf(chat_id));
                user.setExperience(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("Yoshingini kiriting: \uD83D\uDD0D");
//                new_message.setReplyMarkup(chooseAge(resources));
                age.put(String.valueOf(chat_id), 1);
            }
            else if (call_data.equals("Erkak") || call_data.equals("Ayol")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setGender(call_data);
                userMap.put(String.valueOf(chat_id), user);
                User savedUser = userMap.get(String.valueOf(chat_id));
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chat_id));
                if (savedUser.isStatus()) {
                    StringBuilder builder = new StringBuilder();
                    List<User> user1 = connection.getUser(savedUser);
                    if (user1.size() == 0) {
                        builder.append("Bu hududda siz izlagan kasbga ega shaxs hozirda registratsiyadan o'tmadi, keyinroq yana urinib ko'ring:");
                    } else {
                        user1.forEach(user2 -> {
                            if (user2.getExperience() == null){
                                user2.setExperience("0");
                            }
                            builder.append(user2).append("\n\n");
                        });
                    }
                    new_message.setText("Natijalar:");
                    sendMessage.setText(builder.toString());
                } else {
                    new_message.setText("Siz muvaffaqiyatli ro'yxatdan o'tdingiz, ");
                    sendMessage.setText("Sizga ish beruvchilar aloqaga chiqishadi! ✅✅✅✅");
                }
                sendMessage.setReplyMarkup(startingBotMenu());
                execute(sendMessage);
                connection.saveUser(savedUser);
                gender.put(String.valueOf(chat_id), 0);
                userMap.put(String.valueOf(chat_id), null);
                ads(chat_id, 0);
            }
            else if (call_data.equals("Muhim emas")) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setGender(call_data);
                userMap.put(String.valueOf(chat_id), user);
                User savedUser = userMap.get(String.valueOf(chat_id));
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(chat_id));
                StringBuilder builder = new StringBuilder();
                List<User> user1 = connection.getAllUser(savedUser);
                if (user1.size() == 0) {
                        builder.append("Bu hududda siz izlagan kasbga ega shaxs hozirda registratsiyadan o'tmadi, keyinroq yana urinib ko'ring:");
                } else {
                        user1.forEach(user2 -> {
                            if (user2.getExperience() == null){
                                user2.setExperience("0");
                            }
                            builder.append(user2).append("\n\n");
                        });
                }
                new_message.setText("Natijalar:");
                sendMessage.setText(builder.toString());
                sendMessage.setReplyMarkup(startingBotMenu());
                execute(sendMessage);
                connection.saveUser(savedUser);
                gender.put(String.valueOf(chat_id), 0);
                userMap.put(String.valueOf(chat_id), null);
                ads(chat_id, 0);
            }
            else if (deleteProfession.size() != 0
                    && deleteProfession.get(String.valueOf(chat_id)) == 1
                    && connection.getProfessions().contains(call_data)) {
                new_message.setText("Tanlang: ");
                new_message.setReplyMarkup(checkDeleteStatus());
                keepProfession.put(String.valueOf(chat_id), call_data);
                deleteProfession.remove(String.valueOf(chat_id));
            }
            else if (connection.getProfessions().contains(call_data)) {
                User user = userMap.get(String.valueOf(chat_id));
                user.setProfession(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("O'zingizga mos ish holatini tanlang: ");
                new_message.setReplyMarkup(chooseTypeOfWorkCondition(resources));
            }
            else if (call_data.equals("✅")) {
                if (keepProfession.size() != 0) {
                    connection.deleteProfession(keepProfession.get(String.valueOf(chat_id)));
                    new_message.setText("muvaffaqiyatli ochirildi");
                } else {
                    new_message.setText("qaytadan urunib koring");
                }
            }
            else if (call_data.equals("❌")) {
                if (keepProfession.size() != 0) {
                    new_message.setReplyMarkup(getWorkTypes(resources));
                    new_message.setText("O'chirmoqchi bolgan kasbingizni tanlang");
                } else {
                    new_message.setText("qaytadan urunib koring");
                }
            }
            else if (call_data.equals("Reklama qoshish")) {
                new_message.setText("Reklamaning nomini kiriting");
                adName.put(String.valueOf(chat_id), 1);
            }
            else if (call_data.equals("Reklama ochirish")) {
                ads(chat_id,1);
                new_message.setText("Reklama id sini kiriting");
                deleteAd.put(String.valueOf(chat_id), 1);
            }
            else if (call_data.equals("Kasb qoshish")) {
                new_message.setText("Kasb nomini va unga mos sticker kiriting");
                addProfession.put(String.valueOf(chat_id), 1);
            }
            else if (call_data.equals("Kasb ochirish")) {
                new_message.setText("O'chirmoqchi bolgan kasbingizni tanlang");
                new_message.setReplyMarkup(getWorkTypes(resources));
                deleteProfession.put(String.valueOf(chat_id), 1);
            }
            else if (call_data.equals("Admin qoshish")) {
                new_message.setText("Ism kiriting: ");
                adminState.put(String.valueOf(chat_id), 1);
            }
            else if (call_data.equals("Admin ochirish")) {
                new_message.setText("Telefon raqam kiriting: ");
                deleteAdmin.put(String.valueOf(chat_id), 1);
            }
            else if (adsNames().contains(call_data)) {
                List<String> allUsersChatId = connection.getAllUsersChatId();
                sendAds(allUsersChatId, call_data);
                new_message.setText("Bajarildi✅");
            }
            else {
                User user = userMap.get(String.valueOf(chat_id));
                user.setDistrict(call_data);
                userMap.put(String.valueOf(chat_id), user);
                new_message.setText("Ism va familiya kiriting: \uD83D\uDD8A");
                name.put(String.valueOf(chat_id), 1);
            }
            execute(new_message);
        }
    }

    public List<String> adsNames() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<Ads> ads = connection.getAds();
        List<String> names = new ArrayList<>();
        for (Ads ad : ads) {
            names.add(ad.getName());
        }
        return names;
    }

    public ReplyKeyboardMarkup adminPanel() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Menu");
        keyboardFirstRow.add("Adminni tahrirlash");
        KeyboardRow keyboardFirstRow2 = new KeyboardRow();
        keyboardFirstRow2.add("Ish beruvchilar royhati");
        keyboardFirstRow2.add("Ishchilar royhati");
        KeyboardRow keyboardFirstRow3 = new KeyboardRow();
        keyboardFirstRow3.add("barchaga reklama jonatish");
        keyboardFirstRow3.add("Bosh menu");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardFirstRow2);
        keyboard.add(keyboardFirstRow3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup adminMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons1 = new ArrayList<>();
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("Reklama qoshish");
        keyboardButton.setCallbackData("Reklama qoshish");
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("Reklama ochirish");
        keyboardButton1.setCallbackData("Reklama ochirish");
        InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton();
        keyboardButton2.setText("Kasb qoshish");
        keyboardButton2.setCallbackData("Kasb qoshish");
        InlineKeyboardButton keyboardButton3 = new InlineKeyboardButton();
        keyboardButton3.setText("Kasb ochirish");
        keyboardButton3.setCallbackData("Kasb ochirish");
        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);
        keyboardButtons1.add(keyboardButton2);
        keyboardButtons1.add(keyboardButton3);
        inlineKeyboardButtons.add(keyboardButtons);
        inlineKeyboardButtons.add(keyboardButtons1);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup adminEditMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("Admin qoshish");
        keyboardButton.setCallbackData("Admin qoshish");
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("Admin ochirish");
        keyboardButton1.setCallbackData("Admin ochirish");
        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);
        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup checkDeleteStatus() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("❌");
        keyboardButton.setCallbackData("❌");
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("✅");
        keyboardButton1.setCallbackData("✅");
        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);
        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public void ads(Long chatId, Integer number) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<Ads> ads = connection.getAds();
        if (number == 1) {
            for (int i = 0; i < ads.size(); i++) {
                SendPhoto message = new SendPhoto();
                message.setChatId(String.valueOf(chatId));
                message.setCaption("Id: " + ads.get(i).getId() + "\n" + ads.get(i).getDescription() + "\nhttps://t.me/ishbozori_kanali");
                message.setPhoto(new InputFile(ads.get(i).getFileId()));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            for (int i = 0; i < ads.size(); i++) {
                SendPhoto message = new SendPhoto();
                message.setChatId(String.valueOf(chatId));
                message.setCaption(ads.get(i).getDescription() + "\nhttps://t.me/ishbozori_kanali");
                message.setPhoto(new InputFile(ads.get(i).getFileId()));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public InlineKeyboardMarkup getAds() throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<Ads> ads = connection.getAds();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < ads.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
            keyboardButton.setText(ads.get(i).getName());
            keyboardButton.setCallbackData(ads.get(i).getName());
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

    public void sendAds(List<String> users, String adName) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<Ads> ads = connection.getAds();
        Ads ads1 = new Ads();
        for (Ads ad : ads) {
            if (ad.getName().equals(adName)){
                ads1.setId(ad.getId());
                ads1.setName(ad.getName());
                ads1.setDescription(ad.getDescription());
                ads1.setFileId(ad.getFileId());
                break;
            }
        }
        if (ads1.getName() != null) {
            SendPhoto message = new SendPhoto();
            message.setCaption(ads1.getDescription() + "\nhttps://t.me/ishbozori_kanali");
            message.setPhoto(new InputFile(ads1.getFileId()));
            for (String user : users) {
                message.setChatId(String.valueOf(user));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void users(Long chatId) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<User> allUser = connection.getAllWorkers();
        for (int i = 0; i < allUser.size(); i++) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(allUser.get(i).toString());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public ReplyKeyboardMarkup startingBot() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Roziman va boshlayman");
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup startingBotMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Bosh menu");
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
        List<InlineKeyboardButton> keyboardButtons1 = new ArrayList<>();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("Vaqtincha");
        keyboardButton.setCallbackData("Vaqtincha");

        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("Doimiy");
        keyboardButton1.setCallbackData("Doimiy");

        InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton();
        keyboardButton2.setText("Orqaga");
        keyboardButton2.setCallbackData("Orqaga");

        keyboardButtons1.add(keyboardButton2);

        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);

        inlineKeyboardButtons.add(keyboardButtons);
        inlineKeyboardButtons.add(keyboardButtons1);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup chooseWorkExperience() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("0");
        keyboardButton.setCallbackData("0");

        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton();
        keyboardButton1.setText("1");
        keyboardButton1.setCallbackData("1");

        InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton();
        keyboardButton2.setText("2");
        keyboardButton2.setCallbackData("2");

        InlineKeyboardButton keyboardButton3 = new InlineKeyboardButton();
        keyboardButton3.setText("3");
        keyboardButton3.setCallbackData("3");

        InlineKeyboardButton keyboardButton4 = new InlineKeyboardButton();
        keyboardButton4.setText("4");
        keyboardButton4.setCallbackData("4");

        InlineKeyboardButton keyboardButton5 = new InlineKeyboardButton();
        keyboardButton5.setText("5+");
        keyboardButton5.setCallbackData("5+");

        keyboardButtons.add(keyboardButton);
        keyboardButtons.add(keyboardButton1);
        keyboardButtons.add(keyboardButton2);
        keyboardButtons.add(keyboardButton3);
        keyboardButtons.add(keyboardButton4);
        keyboardButtons.add(keyboardButton5);

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

    public InlineKeyboardMarkup chooseGender(User user) {
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

        if (user.isStatus()) {
            InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton();
            keyboardButton2.setText("Muhim emas");
            keyboardButton2.setCallbackData("Muhim emas");
            keyboardButtons.add(keyboardButton2);
        }

        inlineKeyboardButtons.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getWorkTypes(Resources resources) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<String> professions = connection.getProfessions();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < professions.size(); i++) {
            InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
            keyboardButton.setText(professions.get(i));
            keyboardButton.setCallbackData(professions.get(i));
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

    public SendDocument allWorkers(String chatId) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<User> allUser = connection.getAllWorkers();

        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Worker List");

        int rowNumber = 1;
        for (User user : allUser) {
            XSSFRow row = sheet.createRow(rowNumber);

            XSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(user.getName());
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(user.getPhoneNumber());
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(user.getRegion());
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(user.getDistrict());
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(user.getAge());
            XSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(user.getGender());
            XSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(user.getProfession());
            XSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(user.getEducationLevel());
            XSSFCell cell9 = row.createCell(9);
            cell9.setCellValue(user.getWorkingType());

            rowNumber++;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Elitebook\\Desktop\\telegram_bot_amazon\\src\\main\\resources\\users.xlsx");
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendDocument.setDocument(new InputFile(new File("C:\\Users\\Elitebook\\Desktop\\telegram_bot_amazon\\src\\main\\resources\\users.xlsx")));
        return sendDocument;
    }

    public SendDocument allWEmployees(String chatId) throws SQLException {
        DatabaseConnection connection = new DatabaseConnection();
        List<User> allUser = connection.getAllJobOwners();

        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Employee List");

        int rowNumber = 1;
        for (User user : allUser) {
            XSSFRow row = sheet.createRow(rowNumber);

            XSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(user.getName());
            XSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(user.getPhoneNumber());
            XSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(user.getRegion());
            XSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(user.getDistrict());
            XSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(user.getAge());
            XSSFCell cell6 = row.createCell(6);
            cell6.setCellValue(user.getGender());
            XSSFCell cell7 = row.createCell(7);
            cell7.setCellValue(user.getProfession());
            XSSFCell cell8 = row.createCell(8);
            cell8.setCellValue(user.getEducationLevel());
            XSSFCell cell9 = row.createCell(9);
            cell9.setCellValue(user.getWorkingType());

            rowNumber++;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Elitebook\\Desktop\\telegram_bot_amazon\\src\\main\\resources\\employees.xlsx");
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendDocument.setDocument(new InputFile(new File("C:\\Users\\Elitebook\\Desktop\\telegram_bot_amazon\\src\\main\\resources\\employees.xlsx")));
        return sendDocument;
    }

    public ReplyKeyboardMarkup shareContact(Long chat_id){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // new list
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first keyboard line
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Telefon raqam ulashish");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);

        // add array to list
        keyboard.add(keyboardFirstRow);

        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
