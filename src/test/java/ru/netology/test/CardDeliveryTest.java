package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;
import ru.netology.data.User;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;


public class CardDeliveryTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    User info = DataGenerator.generateInfo("ru");


    @Test
    public void shouldFormDeliveryCard() {
        $("[data-test-id=city] input").setValue(info.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String planningDate = DataGenerator.generateDate(3);
        $("[data-test-id='date'] .input__control").setValue(planningDate);
        $("[data-test-id=name] input").setValue(info.getName());
        $("[data-test-id=phone] input").setValue(info.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").shouldHave(Condition.text("Запланировать")).click();

        $("[data-test-id=success-notification]")
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + planningDate),
                        Duration.ofSeconds(15));

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        String newPlanningDate = DataGenerator.generateDate(6);
        $("[data-test-id='date'] .input__control").setValue(newPlanningDate);

        $(".button").shouldHave(Condition.text("Запланировать")).click();

        $("[data-test-id=replan-notification]")
                .shouldHave(Condition.text("Необходимо подтверждение" +
                                " У вас уже запланирована встреча на другую дату. Перепланировать?"),
                        Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] .button").shouldHave(Condition.text("Перепланировать")).click();

        $("[data-test-id=success-notification]").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + newPlanningDate),
                        Duration.ofSeconds(15));
    }
}

