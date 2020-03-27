import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.SearchCriteria;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;


public class SmokeTest {

    //BrowserStack config
    private static final String BS_NAME = "matko5";
    private static final String BS_PASSWORD = "*****";
    private static final String BS_APP_ID = "bs://*****";

    //Mailosaur config
    public static final String MAILOSAUR_KEY = "*****";
    public static final String MAILOSAUR_SERVER_ID = "****";

    @Test
    void itShouldDisplayIdentificationNumber() throws MailosaurException, IOException {
        var email = buildEmail("furbinator");
        $(byXpath("//android.widget.Button")).click();
        $(byXpath("//android.widget.EditText")).val(email);
        $(byXpath("//android.widget.CheckBox")).click();
        $(byXpath("//*[@text='Pokračovať']")).click();
        //get code from email
        var code = getCodeFromEmail(email);
        $(byXpath("//android.widget.EditText")).val(code);
        $(byXpath("//*[@text='Overiť']")).click();
        $(byXpath("//*[@text='Mam podozrenie']")).click();
        $(byXpath("//*[@resource-id='ion-sel-0-lbl']")).click();
        $(byXpath("//*[@text='Bratislava']")).click();

        $(byXpath("//*[@text='Získať identifikačné čislo pre výsledky vzoriek.']")).click();
        $(byXpath("//*[@text='Odobrali mi vzorky']")).shouldBe(Condition.visible);
    }

    @BeforeAll
    static void config() {
        Configuration.timeout = 10000;
    }

    @BeforeEach
    void startDriver() throws MalformedURLException {
        var driver = new AndroidDriver(buildUrl(), getCaps());
        setWebDriver(driver);
    }

    @AfterEach
    void tearDown() {
        getWebDriver().quit();
    }

    private String getCodeFromEmail(String email) throws IOException, MailosaurException {
        var client = buildMailosaurClient();
        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(email);
        Message message = client.messages().get(MAILOSAUR_SERVER_ID, criteria, 20000);

        Pattern p = Pattern.compile(getVerificationCodeRegex());
        Matcher regexMatcher = p.matcher(message.text().body());
        String code = null;
        while (regexMatcher.find()) {
            code = regexMatcher.group(2);
        }
        return code;
    }

    private MailosaurClient buildMailosaurClient() {
        return new MailosaurClient(MAILOSAUR_KEY);
    }

    private String getVerificationCodeRegex() {
        return "(Váš overovací kód je:)\\s(.{5}).";
    }

    private static String buildEmail(String namePrefix) {
        return namePrefix
                .concat(".")
                .concat(MAILOSAUR_SERVER_ID)
                .concat("@mailosaur.io");
    }

    private static DesiredCapabilities getCaps() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device", "Google Pixel 3");
        capabilities.setCapability("os_version", "9.0");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Moj krasny android");
        capabilities.setCapability("appPackage", "sk.marekgogol.zostanzdravy");
        capabilities.setCapability("appActivity", "sk.marekgogol.zostanzdravy.MainActivity");
        capabilities.setCapability("autoGrantPermissions", "true");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("autoAcceptAlerts", "true");
        capabilities.setCapability("app", BS_APP_ID);
        return capabilities;
    }

    private URL buildUrl() throws MalformedURLException {
        return new URL("https://" + BS_NAME + ":" + BS_PASSWORD + "@hub-cloud.browserstack.com/wd/hub");
    }

}
