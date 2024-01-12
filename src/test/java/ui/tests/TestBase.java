package ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;
import ui.helpers.Attach;
import ui.pages.components.TarkovArenaButtons;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase implements TarkovArenaButtons {

    @BeforeAll
    static void configure() {
        Configuration.baseUrl = "https://arena.tarkov.com/";
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion","100.0");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.holdBrowserOpen = false;
        Configuration.remote = System.getProperty("selenoidAddress", "https://user1:1234@selenoid.autotests.cloud/wd/hub");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        if (!Configuration.browser.equalsIgnoreCase("firefox")){
            Attach.browserConsoleLogs();
        }
        Attach.pageSource();
        Attach.addVideo();
        closeWebDriver();
    }
}